package cn.edu.jit.tianyu_paas.ms.controller;

import cn.edu.jit.tianyu_paas.ms.global.Constants;
import cn.edu.jit.tianyu_paas.ms.service.AdminService;
import cn.edu.jit.tianyu_paas.ms.service.UserDynamicService;
import cn.edu.jit.tianyu_paas.ms.service.UserService;
import cn.edu.jit.tianyu_paas.shared.entity.Admin;
import cn.edu.jit.tianyu_paas.shared.entity.User;
import cn.edu.jit.tianyu_paas.shared.entity.UserDynamic;
import cn.edu.jit.tianyu_paas.shared.util.PassUtil;
import cn.edu.jit.tianyu_paas.shared.util.StringUtil;
import cn.edu.jit.tianyu_paas.shared.util.TResult;
import cn.edu.jit.tianyu_paas.shared.util.TResultCode;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * @author 卢越
 * @since 2018-07-01
 */
@RestController
@RequestMapping("/admins")
public class AdminController {

    private HttpSession session;
    private AdminService adminService;
    private UserService userService;
    private UserDynamicService userDynamicService;

    @Autowired
    public AdminController(HttpSession session, AdminService adminService, UserService userService, UserDynamicService userDynamicService) {
        this.session = session;
        this.adminService = adminService;
        this.userService = userService;
        this.userDynamicService = userDynamicService;
    }

    /**
     * @param username
     * @param pwd
     * @return
     * @author 卢越
     * @since 2018-06-29
     */
    @PostMapping("/login")
    public TResult login(String username, String pwd) {
        if (StringUtil.isAnyEmpty(username, pwd)) {
            return TResult.failure(TResultCode.PARAM_NOT_COMPLETE);
        }

        Admin admin = adminService.selectOne(new EntityWrapper<Admin>().eq("username", username));

        if (admin == null) {
            return TResult.failure(TResultCode.USER_NOT_EXIST);
        }

        if (!admin.getPwd().equals(PassUtil.getMD5(pwd))) {
            return TResult.failure(TResultCode.USER_LOGIN_ERROR);
        }
        session.setAttribute(Constants.SESSION_KEY_ADMIN_ID, admin.getAdminId());
        return TResult.success();
    }

    /**
     * @return
     * @author 卢越
     * @since 2018-07-01
     * <p>
     * 所有用户信息
     */
    @GetMapping
    public TResult listUsers(Pagination page) {
        Page<User> users = userService.selectPage(new Page<User>(page.getCurrent(), page.getSize()), new EntityWrapper<User>().setSqlSelect("user_id", " name", "phone", "email", "head_img", "pwd"));

        return TResult.success(users);
    }

    /**
     * 更新用户内存，余额等
     *
     * @param userDynamic
     * @return
     * @author 卢越
     * @since 2018-07-01
     */
    @PutMapping
    public TResult updateUser(UserDynamic userDynamic) {
        if (!userDynamicService.update(userDynamic, new EntityWrapper<UserDynamic>().eq("user_id", userDynamic.getUserId()))) {
            return TResult.failure(TResultCode.RESULE_DATA_NONE);
        }

        return TResult.success();
    }

    /**
     * 删除用户
     *
     * @param userId
     * @return
     * @author 卢越
     * @since 2018-07-01
     */
    @DeleteMapping("{userId}")
    public TResult removeUser(@PathVariable(required = true) Long userId) {
        if (!userService.deleteById(userId)) {
            return TResult.failure(TResultCode.RESULE_DATA_NONE);
        }

        return TResult.success();
    }
}
