package cn.edu.jit.tianyu_paas.web.service;

import cn.edu.jit.tianyu_paas.shared.entity.PhoneVerificationCode;
import cn.edu.jit.tianyu_paas.shared.entity.User;
import cn.edu.jit.tianyu_paas.shared.entity.UserActive;
import cn.edu.jit.tianyu_paas.shared.entity.UserDynamic;
import cn.edu.jit.tianyu_paas.shared.util.*;
import cn.edu.jit.tianyu_paas.web.global.Constants;
import cn.edu.jit.tianyu_paas.web.im.FeignTianyuIm;
import cn.edu.jit.tianyu_paas.web.mapper.UserMapper;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Date;

@Service
public class UserService extends ServiceImpl<UserMapper, User> {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private RedisTemplate redisTemplate;
    private final UserActiveService userActiveService;
    private final MailUtilService mailUtilService;
    private final PhoneVerificationCodeService phoneVerificationCodeService;
    private final FeignTianyuIm feignTianyuIm;
    private final UserDynamicService userDynamicService;

    @Autowired
    public UserService(RedisTemplate redisTemplate, UserActiveService userActiveService, MailUtilService mailUtilService, PhoneVerificationCodeService phoneVerificationCodeService, FeignTianyuIm feignTianyuIm, UserDynamicService userDynamicService) {
        this.redisTemplate = redisTemplate;
        this.userActiveService = userActiveService;
        this.mailUtilService = mailUtilService;
        this.phoneVerificationCodeService = phoneVerificationCodeService;
        this.feignTianyuIm = feignTianyuIm;
        this.userDynamicService = userDynamicService;
    }

    /**
     * 判断用户信息是否合法
     *
     * @param user
     * @return
     */
    public TResult checkUser(User user) {
        if (StringUtil.isAllEmpty(user.getPhone(), user.getEmail())) {
            return TResult.failure("邮箱和手机号必填一个");
        }

        if (selectCount(new EntityWrapper<User>().eq("name", user.getName())) != 0) {
            return TResult.failure("名字已存在");
        }

        if (!StringUtil.isEmpty(user.getPhone()) && !RegexUtil.isPhoneNumber(user.getPhone())) {
            return TResult.failure("手机号不合法");
        }

        if (!StringUtil.isEmpty(user.getEmail()) && !RegexUtil.isEmail(user.getEmail())) {
            return TResult.failure("邮箱格式不合法");
        }

        //如果查询出邮箱的结果不为0
        if (selectCount(new EntityWrapper<User>().eq("email", user.getEmail())) != 0 && !StringUtil.isEmpty(user.getEmail())) {
            return TResult.failure("用户邮箱已注册");
        }

        //如果查询出的结果不为0且注册的用户手机号不为空
        if (selectCount(new EntityWrapper<User>().eq("phone", user.getPhone())) != 0 && !StringUtil.isEmpty(user.getPhone())) {
            return TResult.failure("用户手机号已注册");
        }
        return TResult.success();
    }

    /**
     * 通过邮箱注册用户，发送链接到用户邮箱，此时未激活
     *
     * @param user
     * @return
     */
    public TResult registerUserByEmailAndSendMail(User user) {
        String emailCode = MailUtil.getRandomEmailCode();
        //插入邮箱验证码
        UserActive userActive = new UserActive();
        userActive.setUserEmail(user.getEmail());
        userActive.setEmailCode(emailCode);
        userActive.setEmailCodeGtmCreate(new Date());
        if (!userActiveService.insert(userActive)) {
            return TResult.failure(TResultCode.BUSINESS_ERROR);
        }
        //发送邮箱验证
        if (!mailUtilService.sendRegisterMail(user.getUserId(), user.getEmail(), emailCode)) {
            return TResult.failure("邮箱验证发送失败，请重试：" + String.format(Constants.RE_SEND_EMAIL, user.getEmail()));
        }
        return TResult.success();
    }

    /**
     * 通过手机验证码注册并激活用户，此时已激活
     *
     * @return
     */
    public TResult registerAndActiveUserByPhone(User user, String phoneVerifyCode) {
        PhoneVerificationCode phoneVerificationCode = phoneVerificationCodeService.selectOne(new EntityWrapper<PhoneVerificationCode>()
                .eq("phone", user.getPhone()).and().eq("phone_code", phoneVerifyCode));
        if(StringUtil.isEmpty(phoneVerifyCode)) {
            return TResult.failure("请填写验证码");
        }
        if (System.currentTimeMillis() - phoneVerificationCode.getGmtCreate().getTime() > Constants.PHONE_VERIFY_CODE_MAX_VALID) {
            return TResult.failure("验证码已过期");
        }
        user.setActive(1);
        // 将用户状态改为激活状态
        if (!updateById(user)) {
            return TResult.failure("激活失败！");
        }
        return TResult.success();
    }

    @Cacheable(value = "user", key = "#id.toString()")
    @Override
    public User selectById(Serializable id) {
        return super.selectById(id);
    }

    @Override
    public boolean insert(User entity) {
        // 先通过微服务向im中进行注册
        TResult tResult = feignTianyuIm.createImUser(entity.getName(), entity.getPhone(), entity.getEmail(), entity.getPwd(), User.TYPE_COMMON_USER, entity.getHeadImg());
        if (!tResult.getCode().equals(TResultCode.SUCCESS.getCode())) {
            LOGGER.error("在im中注册用户失败！");
            return false;
        }
        if (super.insert(entity)) {
            // 同时生成dynamic表信息
            UserDynamic userDynamic = new UserDynamic();
            userDynamic.setUserId(entity.getUserId());
            userDynamicService.insert(userDynamic);

            redisTemplate.opsForValue().set("user::" + entity.getUserId().toString(), baseMapper.selectById(entity.getUserId()));
            return true;
        }
        return false;
    }

    @CacheEvict(value = "user", key = "#id.toString()")
    @Override
    public boolean deleteById(Serializable id) {
        return super.deleteById(id);
    }

    @Override
    public boolean update(User entity, Wrapper<User> wrapper) {
        if (super.update(entity, wrapper)) {
            redisTemplate.opsForValue().set("user::" + entity.getUserId().toString(), baseMapper.selectById(entity.getUserId()));
            return true;
        }
        return false;
    }

    @Override
    public boolean updateById(User entity) {
        if (super.updateById(entity)) {
            redisTemplate.opsForValue().set("user::" + entity.getUserId().toString(), baseMapper.selectById(entity.getUserId()));
            return true;
        }
        return false;
    }

}
