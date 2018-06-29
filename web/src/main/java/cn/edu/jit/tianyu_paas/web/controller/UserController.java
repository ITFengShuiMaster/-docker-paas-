package cn.edu.jit.tianyu_paas.web.controller;

import cn.edu.jit.tianyu_paas.shared.entity.User;
import cn.edu.jit.tianyu_paas.shared.util.*;
import cn.edu.jit.tianyu_paas.web.global.Constants;
import cn.edu.jit.tianyu_paas.web.service.UserService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Date;

/**
 * @author 汪继友
 * @since 2018-06-28
 */
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private HttpSession session;

    @Autowired
    public UserController(UserService userService, HttpSession session) {
        this.userService = userService;
        this.session = session;
    }

    @PostMapping("/login")
    public TResult login(String account, String pwd) {
        if (StringUtil.isAnyEmpty(account, pwd)) {
            return TResult.failure(TResultCode.PARAM_NOT_COMPLETE);
        }

        //是否是11位数字
        boolean rs = RegexUtil.regexElevenNumber(account);

        User user = null;
        if (rs) {
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
            return TResult.failure(TResultCode.USER_LOGIN_ERROR);
        }
        session.setAttribute(Constants.SESSION_KEY_USER_ID, account);
        return TResult.success();
    }

    @PostMapping("/register")
    public TResult register(User user) {
        if (user == null) {
            return TResult.failure(TResultCode.PARAM_IS_BLANK);
        }

        int count = userService.selectCount(
                new EntityWrapper<User>().eq("phone", user.getPhone()).or().eq("email", user.getEmail())
        );
        if (count != 0) {
            return TResult.failure(TResultCode.USER_HAS_EXISTED);
        }

        user.setGmtCreate(new Date());
        user.setGmtModified(new Date());
        user.setPwd(PassUtil.getMD5(user.getPwd()));
        boolean flag = userService.insert(user);
        if (!flag) {
            return TResult.failure(TResultCode.FAILURE);
        }

        return TResult.success();
    }
}