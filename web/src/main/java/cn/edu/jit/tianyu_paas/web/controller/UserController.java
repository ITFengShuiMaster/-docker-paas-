package cn.edu.jit.tianyu_paas.web.controller;

import cn.edu.jit.tianyu_paas.shared.entity.User;
import cn.edu.jit.tianyu_paas.shared.entity.UserDynamic;
import cn.edu.jit.tianyu_paas.shared.util.*;
import cn.edu.jit.tianyu_paas.web.global.Constants;
import cn.edu.jit.tianyu_paas.web.service.UserDynamicService;
import cn.edu.jit.tianyu_paas.web.service.UserLoginLogService;
import cn.edu.jit.tianyu_paas.web.service.UserService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
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
    private HttpSession session;

    @Autowired
    public UserController(UserService userService, UserDynamicService userDynamicService, HttpSession session, UserLoginLogService userLoginLogService) {
        this.userService = userService;
        this.session = session;
        this.userDynamicService = userDynamicService;
        this.userLoginLogService = userLoginLogService;
    }

    /**
     * @author 卢越
     * @since 2018-06-29
     * @param account
     * @param pwd
     * @return
     */
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
    @PostMapping("/register")
    public TResult register(@Valid User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return TResult.failure(bindingResult.getFieldError().getDefaultMessage());
        }

        if (!StringUtil.isEmpty(user.getName()) && userService.selectCount(new EntityWrapper<User>().eq("name", user.getName())) != 0) {
            return TResult.failure("名字已存在");
        }

        if (!StringUtil.isEmpty(user.getPhone()) && !RegexUtil.isPhoneNumber(user.getPhone())) {
            return TResult.failure("手机号不合法");
        }

        //如果查询出邮箱的结果不为0
        if (userService.selectCount(new EntityWrapper<User>().eq("email", user.getEmail())) != 0) {
            return TResult.failure("用户邮箱已注册");
        }

        //如果查询出的结果不为0且注册的用户手机号不为空
        if (userService.selectCount(new EntityWrapper<User>().eq("phone", user.getPhone())) != 0 && !StringUtil.isEmpty(user.getPhone())) {
            return TResult.failure("用户手机号已注册");
        }

        user.setGmtCreate(new Date());
        user.setGmtModified(new Date());
        user.setPwd(PassUtil.getMD5(user.getPwd()));
        if (!userService.insert(user)) {
            return TResult.failure(TResultCode.FAILURE);
        }

        //对新注册用户绑定UserDynamic
        UserDynamic userDynamic = new UserDynamic();
        userDynamic.setUserId(user.getUserId());
        userDynamicService.insert(userDynamic);

        return TResult.success();
    }

    /**返回用户个人信息
     * @author 卢越
     * @since 2018-06-29
     * @param userId
     * @return
     */
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
    @GetMapping("/dynamic/{userId}")
    public TResult dynamic(@PathVariable String userId) {
        UserDynamic userDynamic = userDynamicService.selectOne(new EntityWrapper<UserDynamic>().eq("user_id", userId));
        if (userDynamic == null) {
            return TResult.failure(TResultCode.RESULE_DATA_NONE);
        }

        return TResult.success(userDynamic);
    }
}