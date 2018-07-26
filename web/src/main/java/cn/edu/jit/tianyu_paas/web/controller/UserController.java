package cn.edu.jit.tianyu_paas.web.controller;

import cn.edu.jit.tianyu_paas.shared.entity.*;
import cn.edu.jit.tianyu_paas.shared.global.SendPhoneCodeConstants;
import cn.edu.jit.tianyu_paas.shared.util.*;
import cn.edu.jit.tianyu_paas.web.global.Constants;
import cn.edu.jit.tianyu_paas.web.im.FeignTianyuIm;
import cn.edu.jit.tianyu_paas.web.service.*;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import java.io.IOException;
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
    private final TicketService ticketService;
    private final FeignTianyuIm feignTianyuIm;

    @Autowired
    public UserController(UserService userService, UserDynamicService userDynamicService, HttpSession session, UserLoginLogService userLoginLogService, MailUtilService mailUtilService, UserActiveService userActiveService, PhoneVerificationCodeService phoneVerificationCodeService, TicketService ticketService, FeignTianyuIm feignTianyuIm) {
        this.userService = userService;
        this.session = session;
        this.userDynamicService = userDynamicService;
        this.userLoginLogService = userLoginLogService;
        this.mailUtilService = mailUtilService;
        this.userActiveService = userActiveService;
        this.phoneVerificationCodeService = phoneVerificationCodeService;
        this.ticketService = ticketService;
        this.feignTianyuIm = feignTianyuIm;
    }

    /**
     * @param username
     * @param pwd
     * @return
     * @author 卢越
     * @since 2018-06-29
     */
    @ApiOperation("登录接口")
    @PostMapping("/login")
    public TResult login(String username, String pwd) {
        if (StringUtil.isAnyEmpty(username, pwd)) {
            return TResult.failure(TResultCode.PARAM_NOT_COMPLETE);
        }

        User user = null;
        if (RegexUtil.isPhoneNumber(username)) {
            //如果传入账号是手机号，通过手机号查询
            user = userService.selectOne(new EntityWrapper<User>().eq("phone", username));
        } else {
            //如果传入账号是邮箱，通过邮箱查询
            user = userService.selectOne(new EntityWrapper<User>().eq("email", username));
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

        // 将token插入ticket表
        Ticket ticket = new Ticket();
        ticket.setUserId(user.getUserId());
        ticket.setToken(PassUtil.generatorToken(user.getUserId()));
        ticket.setGmtCreate(new Date());
        ticket.setGmtModified(new Date());
        if (ticketService.insertOrUpdate(ticket)) {
            return TResult.success(ticket.getToken());
        }
        return TResult.failure("后台token发生错误");
    }

    /**
     * @param user
     * @return
     * @author 卢越
     * @since 2018-06-29
     */
    @ApiOperation("注册接口")
    @PostMapping("/register")
    public TResult register(@Valid User user, @RequestParam(required = false) String phoneVerifyCode, int activeMethod) {

        TResult result = userService.checkUser(user);
        if (!result.getCode().equals(TResultCode.SUCCESS.getCode())) {
            return result;
        }
        user.setGmtCreate(new Date());
        user.setGmtModified(new Date());
        user.setActive(0);
        // 先插入用户，未激活状态
        if (!userService.insert(user)) {
            return TResult.failure(TResultCode.BUSINESS_ERROR);
        }

        if (activeMethod == Constants.ACTIVE_PHONE) {
            return userService.registerAndActiveUserByPhone(user, phoneVerifyCode);
        } else {
            return userService.registerUserByEmailAndSendMail(user);
        }
    }

    /**
     * @param email
     * @return
     * @author 卢越
     * @date 2018/7/20 16:30
     */
    @ApiOperation("重新发送email")
    @GetMapping("/re-email")
    public TResult reSendEmailCode(@Validated @Email String email) {
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
            UserActive userActive = new UserActive();
            userActive.setUserEmail(user.getEmail());
            userActive.setEmailCode(emailCode);
            userActive.setEmailCodeGtmCreate(new Date());
            if (!userActiveService.insert(userActive)) {
                return TResult.failure("邮箱验证码发送失败，请重试: " + String.format(Constants.RE_SEND_EMAIL, user.getEmail()));
            }

            return TResult.success();
        }

        return TResult.failure("邮箱格式不合法！");
    }

    /**
     * 返回用户个人信息
     *
     * @return
     * @author 卢越
     * @since 2018-06-29
     */
    @ApiOperation("返回用户个人信息")
    @GetMapping("/info")
    public TResult info() {
        EntityWrapper<User> entityWrapper = new EntityWrapper<>();
        entityWrapper.setSqlSelect("user_id,name,phone,email,head_img").eq("user_id", session.getAttribute(Constants.SESSION_KEY_USER_ID));
        User user = userService.selectOne(entityWrapper);
        if (user == null) {
            return TResult.failure(TResultCode.USER_NOT_EXIST);
        }

        return TResult.success(user);
    }

    /**
     * 返回用户个人的动态：如余额，内存使用情况等
     *
     * @return
     * @author 卢越
     * @since 2018-06-29
     */
    @ApiOperation("返回用户个人的动态：如余额，内存使用情况等")
    @GetMapping("/dynamic")
    public TResult dynamic() {
        UserDynamic userDynamic = userDynamicService.selectOne(new EntityWrapper<UserDynamic>().eq("user_id", session.getAttribute(Constants.SESSION_KEY_USER_ID)));
        if (userDynamic == null) {
            return TResult.failure(TResultCode.RESULE_DATA_NONE);
        }

        return TResult.success(userDynamic);
    }

    /**
     * 账号激活
     *@author 卢越
     * @date 2018/7/16 16:30
     * @param userId
     * @param code
     * @return
     */
    @ApiOperation("账号激活接口")
    @GetMapping("/active")
    public TResult accountActive(HttpServletRequest request, HttpServletResponse response, @RequestParam(required = true) String userId, @RequestParam(required = true) String code) throws IOException {
        User user = userService.selectOne(new EntityWrapper<User>().eq("user_id", userId));
        if (user == null) {
            return TResult.failure("该用户未注册!");
        }

        if (userActiveService.selectCount(new EntityWrapper<UserActive>().eq("user_email", user.getEmail()).and().eq("email_code", code).and().ge("email_code_gtm_create", new Date(System.currentTimeMillis() - 600000))) != 0) {
            user.setActive(1);
            userService.updateById(user);
            //跳转到登录页
            response.sendRedirect(request.getContextPath() + "/login.html");
        }
        return TResult.failure("链接无效或已过期");
    }

    /**
     * @param phone
     * @return
     * @author 卢越
     * @date 2018/7/16 16:30
     */
    @ApiOperation("发送验证码")
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

    /**
     * 修改用户信息
     *@author 卢越
     * @date 2018/7/16 16:30
     * @param userId
     * @param name
     * @param headImg
     * @return TResult
     */
    @ApiOperation("修改用户信息")
    @PutMapping
    public TResult updateUser(@RequestParam(required = true) Long userId, String name, String headImg) {
        User user = userService.selectById(userId);
        if (user == null) {
            return TResult.failure("用户不存在");
        }
        user.setName(name);
        user.setHeadImg(headImg);
        user.setGmtModified(new Date());
        if (!userService.update(user, new EntityWrapper<User>().eq("user_id", user.getUserId()))) {
            return TResult.failure(TResultCode.BUSINESS_ERROR);
        }
        return TResult.success();
    }

    /**
     * 登出
     *
     * @author 汪继友
     * @date 2018/7/25 10:32
     */
    @RequestMapping("logout")
    public void logout() {
        session.removeAttribute(Constants.SESSION_KEY_USER_ID);
        session.invalidate();
    }
}