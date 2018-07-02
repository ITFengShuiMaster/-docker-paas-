package cn.edu.jit.tianyu_paas.ms.controller;

import cn.edu.jit.tianyu_paas.ms.global.Constants;
import cn.edu.jit.tianyu_paas.ms.service.AdminService;
import cn.edu.jit.tianyu_paas.ms.service.UserDynamicService;
import cn.edu.jit.tianyu_paas.ms.service.UserLoginLogService;
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

    private final AdminService adminService;
    private final UserService userService;
    private final UserDynamicService userDynamicService;
    private final UserLoginLogService userLoginLogService;
    private HttpSession session;

    @Autowired
    public AdminController(HttpSession session, AdminService adminService, UserService userService, UserDynamicService userDynamicService, UserLoginLogService userLoginLogService) {
        this.session = session;
        this.adminService = adminService;
        this.userService = userService;
        this.userDynamicService = userDynamicService;
        this.userLoginLogService = userLoginLogService;
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
        return TResult.success(userService.selectPage(new Page<User>(page.getCurrent(), page.getSize()), new EntityWrapper<User>().setSqlSelect("user_id", " name", "phone", "email", "head_img", "gmt_create", "gmt_modified")));
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
    public TResult deleteUser(@PathVariable(required = true) Long userId) {
        if (!userService.deleteById(userId)) {
            return TResult.failure(TResultCode.RESULE_DATA_NONE);
        }

        return TResult.success();
    }

    /**
     * 返回最近多少天登录的用户,还可以根据访问量查询
     *
     * @param days       "天数"
     * @param views      "用户访问量"
     * @param pagination "分页插件"
     * @return TResult
     * @author 卢越
     * @since 2018-07-01
     */
    @GetMapping("/access-users")
    public TResult listAccessUsers(@RequestParam(value = "days", defaultValue = "1") Integer days,
                                   @RequestParam(value = "views", defaultValue = "0") Integer views, Pagination pagination) {
        Page<User> users = userService.selectAccessUsers(days, views, pagination);

        return TResult.success(users);
    }

    /**
     * 查询最近几天注册的用户，默认三天
     *
     * @param days
     * @return
     * @author 卢越
     * @since 2018-07-01
     */
    @GetMapping("/register-recent-days")
    public TResult listRegisterInRecentDays(@RequestParam(value = "days", defaultValue = "3") Integer days) {
        return TResult.success(userService.selectList(new EntityWrapper<User>().setSqlSelect("user_id", " name", "phone", "email", "head_img", "gmt_create", "gmt_modified").where("gmt_create BETWEEN DATE_SUB(NOW(), INTERVAL {0} DAY) AND NOW()", days)));
    }

    /**
     * 查询一个月未登录的用户
     *
     * @return
     * @author 卢越
     * @since 2018-07-01
     */
    @GetMapping("/unloginInMonth")
    public TResult listUnloginUsersInThreeMonth() {
        return TResult.success(userService.selectUnloginUsersInThreeMonth());
    }

    /**
     * 查询欠费的用户
     *
     * @return
     * @author 卢越
     * @since 2018-07-01
     */
    @GetMapping("/arrears-users")
    public TResult listArrearsUsers() {
        return TResult.success(userService.selectArrearsUsers());
    }

    /**
     * 返回注册的用户数量，不加days参数返回总用户数量，加上days则指定天数查询注册的数量
     *
     * @return
     * @author 卢越
     * @since 2018-07-01
     */
    @GetMapping("/amount-of-users")
    public TResult getAmountOfUsers(@RequestParam(value = "days", defaultValue = "0") Integer days) {
        if (days.equals(0)) {
            return TResult.success(userService.selectCount(new EntityWrapper<User>()));
        }

        return TResult.success(userService.selectCount(new EntityWrapper<User>().where("gmt_create >= DATE_SUB(CURDATE(), INTERVAL {0} DAY)", days)));
    }

}
