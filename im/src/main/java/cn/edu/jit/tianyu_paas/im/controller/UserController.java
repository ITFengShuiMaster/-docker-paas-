package cn.edu.jit.tianyu_paas.im.controller;


import cn.edu.jit.tianyu_paas.im.entity.User;
import cn.edu.jit.tianyu_paas.im.global.MinaConstant;
import cn.edu.jit.tianyu_paas.im.service.UserService;
import cn.edu.jit.tianyu_paas.shared.util.PassUtil;
import cn.edu.jit.tianyu_paas.shared.util.TResult;
import cn.edu.jit.tianyu_paas.shared.util.TResultCode;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Date;

/**
 * @author 汪继友
 * @since 2018-07-07
 */
@RestController
@RequestMapping("/users")
public class UserController {

    private UserService userService;
    private HttpSession session;

    @Autowired
    public UserController(UserService userService, HttpSession session) {
        this.userService = userService;
        this.session = session;
    }

    /**
     * @author 张万平
     * @since 2018-07-07
     */
    @ApiOperation("获取用户信息")
    @GetMapping("/{userId}")
    public TResult userInfo(@PathVariable String userId) {
        User user = userService.selectOne(new EntityWrapper<User>().eq("user_id", userId));
        if (user == null) {
            return TResult.failure(TResultCode.RESULE_DATA_NONE);
        }
        return TResult.success(user);
    }

    /**
     * @author 张万平
     * @since 2018-07-07
     */
    @ApiOperation("添加用户")
    @PostMapping
    public TResult userAdd(@Validated User user) {
        user.setGmtCreate(new Date());
        user.setGmtModified(new Date());
        user.setPwd(PassUtil.getMD5(user.getPwd()));
        if (user.getType() == null) {
            user.setType(User.TYPE_COMMON);
        }
        int dbCount = userService.selectCount(new EntityWrapper<User>().eq("phone", user.getPhone()).or().eq("email", user.getEmail()));
        if (dbCount > 0) {
            return TResult.failure(TResultCode.DATA_ALREADY_EXISTED);
        }
        if (!userService.insert(user)) {
            return TResult.failure(TResultCode.FAILURE);
        }
        return TResult.success();
    }

    /**
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
        if (!userService.updateById(user)) {
            return TResult.failure(TResultCode.FAILURE);
        }
        return TResult.success(user);
    }
}

