package cn.edu.jit.tianyu_paas.im.controller;


import cn.edu.jit.tianyu_paas.im.entity.User;
import cn.edu.jit.tianyu_paas.im.service.UserService;
import cn.edu.jit.tianyu_paas.shared.util.TResult;
import cn.edu.jit.tianyu_paas.shared.util.TResultCode;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.netflix.discovery.converters.Auto;
import io.swagger.annotations.ApiOperation;
import org.bouncycastle.util.test.TestResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.TraversableResolver;
import javax.validation.Valid;
import java.util.Date;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 汪继友
 * @since 2018-07-07
 */
@Controller
@RequestMapping("/user")
public class UserController {

    private UserService userService;
    private HttpSession session;

    @Autowired
    public UserController(UserService userService, HttpSession session) {
        this.userService = userService;
        this.session = session;
    }

    /**
     * @param userId
     * @return
     * @author 张万平
     * @since 2018-07-07
     */
    @ApiOperation("获取用户信息")
    @GetMapping("/info/{userId}")
    public TResult userInfo(@PathVariable String userId) {
        User user = new User();
        if ((user = userService.selectOne(new EntityWrapper<User>().eq("user_id", userId))) == null) {
            return TResult.failure(TResultCode.RESULE_DATA_NONE);
        }
        return TResult.success(user);
    }

    /**
     *
     * @param username
     * @return
     * @author 张万平
     * @since 2018-07-07
     */
    @ApiOperation("添加用户")
    @PostMapping
    public TResult userAdd(String username) {
        User user = new User();
        user.setName(username);
        if (!userService.insert(user)) {
            return TResult.failure(TResultCode.FAILURE);
        }
        return TResult.success();
    }

    /**
     *
     * @param userId
     * @return
     * @author 张万平
     * @since 2018-07-07
     */
    @ApiOperation("删除用户")
    @DeleteMapping
    public TResult userDelete(long userId) {
        User user = new User();
        user.setUserId(userId);
        if (!userService.delete(new EntityWrapper<User>().eq("user_id", userId))) {
            return TResult.failure(TResultCode.FAILURE);
        }
        return TResult.success();
    }

    /**
     *
     * @param user
     * @return
     * @author 张万平
     * @since 2018-07-07
     */
    @ApiOperation("修改用户信息")
    @PutMapping
    public TResult userModify(User user) {
        if (userService.selectOne(new EntityWrapper<User>().eq("user_id", user.getUserId())) == null) {
            return TResult.failure(TResultCode.RESULE_DATA_NONE);
        }
        user.setGmtModified(new Date());
        if (!userService.update(user, new EntityWrapper<User>().eq("user_id", user.getUserId()))){
            return TResult.failure(TResultCode.FAILURE);
        }
        return TResult.success(user);
    }
}

