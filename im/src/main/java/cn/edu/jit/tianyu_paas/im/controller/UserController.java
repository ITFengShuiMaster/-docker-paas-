package cn.edu.jit.tianyu_paas.im.controller;


import cn.edu.jit.tianyu_paas.im.entity.User;
import cn.edu.jit.tianyu_paas.im.service.UserService;
import cn.edu.jit.tianyu_paas.shared.util.TResult;
import cn.edu.jit.tianyu_paas.shared.util.TResultCode;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
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
        User user = userService.selectOne(new EntityWrapper<User>().eq("user_id", userId));
        if (user == null) {
            return TResult.failure(TResultCode.RESULE_DATA_NONE);
        }
        return TResult.success(user);
    }

    /**
     *
     * @return
     * @author 张万平
     * @since 2018-07-07
     */
    @ApiOperation("添加用户")
    @PostMapping
    public TResult userAdd(User user) {
        user.setGmtCreate(new Date());
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
        if (!userService.deleteById(userId)) {
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
        if (userService.selectById(user.getUserId()) == null) {
            return TResult.failure(TResultCode.RESULE_DATA_NONE);
        }
        user.setGmtModified(new Date());
        if (!userService.updateById(user)){
            return TResult.failure(TResultCode.FAILURE);
        }
        return TResult.success(user);
    }
}

