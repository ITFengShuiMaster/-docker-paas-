package cn.edu.jit.tianyu_paas.im.controller;

import cn.edu.jit.tianyu_paas.im.entity.Ticket;
import cn.edu.jit.tianyu_paas.im.entity.User;
import cn.edu.jit.tianyu_paas.im.service.TicketService;
import cn.edu.jit.tianyu_paas.im.service.UserService;
import cn.edu.jit.tianyu_paas.shared.util.PassUtil;
import cn.edu.jit.tianyu_paas.shared.util.TResult;
import cn.edu.jit.tianyu_paas.shared.util.TResultCode;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotEmpty;
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
    private final TicketService ticketService;

    @Autowired
    public UserController(UserService userService, HttpSession session, TicketService ticketService) {
        this.userService = userService;
        this.session = session;
        this.ticketService = ticketService;
    }

    /**
     * 登录接口，登录后返回token
     *
     * @param username
     * @param pwd
     * @return
     */
    @PostMapping("login")
    public TResult login(@Validated @NotEmpty String username, @Validated @NotEmpty String pwd) {
        User user = userService.selectOne(new EntityWrapper<User>().eq("phone", username).or().eq("email", username));
        if (user == null || !user.getPwd().equals(PassUtil.getMD5(pwd))) {
            return TResult.failure(TResultCode.RESULE_DATA_NONE);
        }
        Ticket ticket = new Ticket();
        ticket.setUserId(user.getUserId());
        ticket.setToken(PassUtil.generatorToken(user.getUserId()));
        ticket.setGmtCreate(new Date());
        if (ticketService.insertOrUpdate(ticket)) {
            user.setPwd("");
            return TResult.success(user);
        }
        return TResult.failure(TResultCode.BUSINESS_ERROR);
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
        user.setPwd("");
        return TResult.success(user);
    }

    /**
     * 插入后把id返回
     *
     * @author 汪继友
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
        return TResult.success(user.getUserId());
    }

    /**
     * @author 张万平
     * @since 2018-07-07
     */
    @ApiOperation("删除用户")
    @PostMapping("/{userId}")
    public TResult userDelete(@PathVariable("userId") long userId) {
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

