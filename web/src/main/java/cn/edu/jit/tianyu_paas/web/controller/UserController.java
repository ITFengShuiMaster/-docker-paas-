package cn.edu.jit.tianyu_paas.web.controller;

import cn.edu.jit.tianyu_paas.shared.entity.PhoneVerificationCode;
import cn.edu.jit.tianyu_paas.shared.entity.User;
import cn.edu.jit.tianyu_paas.shared.entity.UserActive;
import cn.edu.jit.tianyu_paas.shared.entity.UserDynamic;
import cn.edu.jit.tianyu_paas.shared.global.SendPhoneCodeConstants;
import cn.edu.jit.tianyu_paas.shared.util.*;
import cn.edu.jit.tianyu_paas.web.global.Constants;
import cn.edu.jit.tianyu_paas.web.service.*;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Date;

/**
 * @author 汪继友
 * @since 2018-06-28
 */
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final UserDynamicService userDynamicService;
    private final UserLoginLogService userLoginLogService;
    private final MailUtilService mailUtilService;
    private final UserActiveService userActiveService;
    private final PhoneVerificationCodeService phoneVerificationCodeService;
    private HttpSession session;

    @Autowired
    public UserController(UserService userService, UserDynamicService userDynamicService, HttpSession session, UserLoginLogService userLoginLogService, MailUtilService mailUtilService, UserActiveService userActiveService, PhoneVerificationCodeService phoneVerificationCodeService) {
        this.userService = userService;
        this.session = session;
        this.userDynamicService = userDynamicService;
        this.userLoginLogService = userLoginLogService;
        this.mailUtilService = mailUtilService;
        this.userActiveService = userActiveService;
        this.phoneVerificationCodeService = phoneVerificationCodeService;
    }

    private void initUser(User user) {
        user.setGmtCreate(new Date());
        user.setGmtModified(new Date());
        user.setPwd(PassUtil.getMD5(user.getPwd()));
        user.setActive(0);
    }

    private UserActive initUserActive(User user, String emailCode) {
        UserActive userActive = new UserActive();
        userActive.setUserEmail(user.getEmail());
        userActive.setEmailCode(emailCode);
        userActive.setEmailCodeGtmCreate(new Date());
        return userActive;
    }

    /**
     * @author 卢越
     * @since 2018-06-29
     * @param account
     * @param pwd
     * @return
     */
    @ApiOperation("登录接口")
    @PostMapping("/login")
    public TResult login(String account, String pwd) {
        if (StringUtil.isAnyEmpty(account, pwd)) {
            return TResult.failure(TResultCode.PARAM_NOT_COMPLETE);
        }

        User user = null;
        if (RegexUtil.isPhoneNumber(account)) {
            //如果传入账号是手机号，通过手机号查询
            user = userService.selectOne(new EntityWrapper<User>().eq("phone", account));
        } else {
            //如果传入账号是邮箱，通过邮箱查询
            user = userService.selectOne(new EntityWrapper<User>().eq("email", account));
        }

        if (user == null) {
            return TResult.failure(TResultCode.USER_NOT_EXIST);
        }

        if (user.getActive() == 0) {
            return TResult.failure("该账号未激活");
        }

        if (!user.getPwd().equals(PassUtil.getMD5(pwd))) {
            //用户登录失败，将登录失败信息插入数据表
            userLoginLogService.insert(UserLoginLogUtil.getUserLoginLog(user.getUserId(), 0));
            return TResult.failure(TResultCode.USER_LOGIN_ERROR);
        }

        session.setAttribute(Constants.SESSION_KEY_USER_ID, user.getUserId());
        //用户登录成功，将登录成功信息插入数据表
        userLoginLogService.insert(UserLoginLogUtil.getUserLoginLog(user.getUserId(), 1));

        return TResult.success();
    }

    /**
     * @author 卢越
     * @since 2018-06-29
     * @param user
     * @return
     */
    @ApiOperation("注册接口")
    @PostMapping("/register")
    public TResult register(@Valid User user, String phoneCode, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return TResult.failure(bindingResult.getFieldError().getDefaultMessage());
        }

        if (StringUtil.isAllEmpty(user.getPhone(), user.getEmail())) {
            return TResult.failure("邮箱和手机号必填一个");
        }

        if (userService.selectCount(new EntityWrapper<User>().eq("name", user.getName())) != 0) {
            return TResult.failure("名字已存在");
        }

        if (!StringUtil.isEmpty(user.getPhone()) && !RegexUtil.isPhoneNumber(user.getPhone())) {
            return TResult.failure("手机号不合法");
        }

        if (!StringUtil.isEmpty(user.getEmail()) && !RegexUtil.isEmail(user.getEmail())) {
            return TResult.failure("邮箱格式不合法");
        }

        //如果查询出邮箱的结果不为0
        if (userService.selectCount(new EntityWrapper<User>().eq("email", user.getEmail())) != 0 && !StringUtil.isEmpty(user.getEmail())) {
            return TResult.failure("用户邮箱已注册");
        }

        //如果查询出的结果不为0且注册的用户手机号不为空
        if (userService.selectCount(new EntityWrapper<User>().eq("phone", user.getPhone())) != 0 && !StringUtil.isEmpty(user.getPhone())) {
            return TResult.failure("用户手机号已注册");
        }

        if (!StringUtil.isEmpty(user.getEmail())) {
            initUser(user);

            if (!userService.insert(user)) {
                return TResult.failure(TResultCode.BUSINESS_ERROR);
            }

            //发送邮箱验证
            String emailCode = MailUtil.getRandomEmailCode();

            if (!mailUtilService.sendRegisterMail(user.getUserId(), user.getEmail(), emailCode)) {
                return TResult.failure("邮箱验证发送失败，请重试：" + String.format(Constants.RE_SEND_EMAIL, user.getEmail()));
            }

            //插入邮箱验证码
            UserActive userActive = initUserActive(user, emailCode);

            if (!userActiveService.insert(userActive)) {
                return TResult.failure("邮箱验证码发送失败，请重试: " + String.format(Constants.RE_SEND_EMAIL, user.getEmail()));
            }
        } else if (!StringUtil.isEmpty(user.getPhone())) {
            if (StringUtil.isEmpty(phoneCode)) {
                return TResult.failure("请填写手机验证码");
            }
            initUser(user);

            if (phoneVerificationCodeService.selectOne(new EntityWrapper<PhoneVerificationCode>().eq("phone", user.getPhone()).and().eq("phone_code", phoneCode).and().ge("gmt_create", new Date(System.currentTimeMillis() - 60000))) != null) {
                user.setActive(1);
                if (!userService.insert(user)) {
                    return TResult.failure(TResultCode.BUSINESS_ERROR);
                }
            } else {
                return TResult.failure("手机验证码已过期");
            }
        }

        //对新注册用户绑定UserDynamic
        UserDynamic userDynamic = new UserDynamic();
        userDynamic.setUserId(user.getUserId());
        userDynamicService.insert(userDynamic);

        return TResult.success();
    }

    @GetMapping("/re-email")
    public TResult reSendEmailCode(@RequestParam(required = true) String email) {
        if (RegexUtil.isEmail(email)) {
            User user = userService.selectOne(new EntityWrapper<User>().eq("email", email));
            if (user == null) {
                return TResult.failure("没有该用户");
            }

            String emailCode = MailUtil.getRandomEmailCode();
            if (!mailUtilService.sendRegisterMail(user.getUserId(), user.getEmail(), emailCode)) {
                return TResult.failure("邮箱验证发送失败，请重试：" + String.format(Constants.RE_SEND_EMAIL, user.getEmail()));
            }

            //插入邮箱验证码
            UserActive userActive = initUserActive(user, emailCode);
            if (!userActiveService.insert(userActive)) {
                return TResult.failure("邮箱验证码发送失败，请重试: " + String.format(Constants.RE_SEND_EMAIL, user.getEmail()));
            }

            return TResult.success();
        }

        return TResult.failure("邮箱格式不合法！");
    }

    /**返回用户个人信息
     * @author 卢越
     * @since 2018-06-29
     * @param userId
     * @return
     */
    @ApiOperation("返回用户个人信息")
    @GetMapping("/info/{userId}")
    public TResult info(@PathVariable String userId) {
        EntityWrapper<User> entityWrapper = new EntityWrapper<>();
        entityWrapper.setSqlSelect("user_id,name,phone,email,head_img").eq("user_id", userId);
        User user = userService.selectOne(entityWrapper);
        if (user == null) {
            return TResult.failure(TResultCode.USER_NOT_EXIST);
        }

        return TResult.success(user);
    }

    /**返回用户个人的动态：如余额，内存使用情况等
     * @author 卢越
     * @since 2018-06-29
     * @param userId
     * @return
     */
    @ApiOperation("返回用户个人的动态：如余额，内存使用情况等")
    @GetMapping("/dynamic/{userId}")
    public TResult dynamic(@PathVariable String userId) {
        UserDynamic userDynamic = userDynamicService.selectOne(new EntityWrapper<UserDynamic>().eq("user_id", userId));
        if (userDynamic == null) {
            return TResult.failure(TResultCode.RESULE_DATA_NONE);
        }

        return TResult.success(userDynamic);
    }

    /**
     * 账号激活
     *
     * @param userId
     * @param code
     * @return
     */
    @ApiOperation("账号激活接口")
    @GetMapping("/active")
    public TResult accountActive(@RequestParam(required = true) String userId, @RequestParam(required = true) String code) {
        User user = userService.selectOne(new EntityWrapper<User>().eq("user_id", userId));
        if (user == null) {
            return TResult.failure("该用户未注册!");
        }

        if (userActiveService.selectCount(new EntityWrapper<UserActive>().eq("user_email", user.getEmail()).and().eq("email_code", code).and().ge("email_code_gtm_create", new Date(System.currentTimeMillis() - 600000))) != 0) {
            user.setActive(1);
            userService.updateById(user);
            // TODO 应该跳转到登录页
            return TResult.success("账号激活成功");
        }
        return TResult.failure("链接无效或已过期");
    }

    @GetMapping("phone-code/{phone}")
    public TResult sendPhoneCode(@PathVariable(required = true) String phone) {
        if (RegexUtil.isPhoneNumber(phone)) {
            String[] params = {RandomPhoneCodeUtil.getRandomCode(), "1"};
            SmsSingleSender ssender = new SmsSingleSender(SendPhoneCodeConstants.APP_ID, SendPhoneCodeConstants.APP_KEY);
            try {
                SmsSingleSenderResult result = ssender.sendWithParam("86", phone,
                        SendPhoneCodeConstants.TEMPLATE_ID, params, "", "", "");

                if (result.getResponse().statusCode == 200) {
                    PhoneVerificationCode phoneVerificationCode = new PhoneVerificationCode();
                    phoneVerificationCode.setPhone(phone);
                    phoneVerificationCode.setPhoneCode(params[0]);
                    phoneVerificationCode.setGmtCreate(new Date());
                    if (!phoneVerificationCodeService.insert(phoneVerificationCode)) {
                        return TResult.failure("验证码发送失败，请重试！");
                    }
                } else {
                    return TResult.failure("验证码发送失败，请重试！");
                }

                return TResult.success();
            } catch (Exception e) {
                e.printStackTrace();
                return TResult.failure("验证码发送失败，请重试！");
            }
        }

        return TResult.failure("手机号码格式不正确！");
    }

    @PutMapping
    public TResult updateUser(@RequestParam(required = true) Long userId, String name, String headImg) {
        User user = new User();
        user.setUserId(userId);
        user.setName(name);
        user.setHeadImg(headImg);
        user.setGmtModified(new Date());
        if (!userService.update(user, new EntityWrapper<User>().eq("user_id", user.getUserId()))) {
            return TResult.failure(TResultCode.BUSINESS_ERROR);
        }
        return TResult.success();
    }
}