package cn.edu.jit.tianyu_paas.web.controller;

import cn.edu.jit.tianyu_paas.shared.entity.util.TResult;
import cn.edu.jit.tianyu_paas.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 汪继友
 * @since 2018-06-28
 */
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public TResult test() {
        return TResult.success(userService.selectById(1));
    }
}