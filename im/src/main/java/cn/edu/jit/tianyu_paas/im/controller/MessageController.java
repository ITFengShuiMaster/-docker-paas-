package cn.edu.jit.tianyu_paas.im.controller;


import cn.edu.jit.tianyu_paas.im.entity.Message;
import cn.edu.jit.tianyu_paas.im.entity.User;
import cn.edu.jit.tianyu_paas.im.global.MinaConstant;
import cn.edu.jit.tianyu_paas.im.service.MessageService;
import cn.edu.jit.tianyu_paas.im.service.OfflineMessageService;
import cn.edu.jit.tianyu_paas.im.service.UserService;
import cn.edu.jit.tianyu_paas.shared.util.TResult;
import cn.edu.jit.tianyu_paas.shared.util.TResultCode;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author 汪继友
 * @since 2018-07-07
 */
@RestController
@RequestMapping("/messages")
public class MessageController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageController.class);

    private final MessageService messageService;
    private final UserService userService;
    private final HttpSession session;
    private final OfflineMessageService offlineMessageService;

    @Autowired
    public MessageController(MessageService messageService, UserService userService, HttpSession session, OfflineMessageService offlineMessageService) {
        this.messageService = messageService;
        this.userService = userService;
        this.session = session;
        this.offlineMessageService = offlineMessageService;
    }

    /**
     * 列出用户的所有消息（不包含离线消息，离线消息会在用户接取之后放到消息表中）
     *
     * @return
     */
    @GetMapping
    public TResult listMessages() {
        long userId = ((User) session.getAttribute(MinaConstant.SESSION_KEY_USER)).getUserId();
        return TResult.success(messageService.selectList(new EntityWrapper<Message>().eq("sender", userId)));
    }

    @GetMapping("/{userId}")
    public TResult listMessagesWithUser(@PathVariable long userId) {
        User user = (User) session.getAttribute(MinaConstant.SESSION_KEY_USER);
        if (user.getType() != User.TYPE_CUSTOMER_SERVICE) {
            LOGGER.warn("request illegal !, user id : " + user.getUserId());
            return TResult.failure(TResultCode.INTERFACE_FORBID_VISIT);
        }
        List<Message> messages = messageService.selectList(new EntityWrapper<Message>().eq("receiver", user.getUserId()));
        return TResult.success(messages);
    }
}