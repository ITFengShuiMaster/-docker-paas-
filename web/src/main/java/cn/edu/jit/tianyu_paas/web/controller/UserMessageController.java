package cn.edu.jit.tianyu_paas.web.controller;


import cn.edu.jit.tianyu_paas.shared.entity.UserMessage;
import cn.edu.jit.tianyu_paas.shared.util.TResult;
import cn.edu.jit.tianyu_paas.shared.util.TResultCode;
import cn.edu.jit.tianyu_paas.web.global.Constants;
import cn.edu.jit.tianyu_paas.web.service.MessageService;
import cn.edu.jit.tianyu_paas.web.service.UserMessageService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 汪继友
 * @since 2018-07-02
 */
@RestController
@RequestMapping("/userMessages")
public class UserMessageController {

    private final UserMessageService userMessageService;
    private final MessageService messageService;
    private HttpSession session;

    @Autowired
    public UserMessageController(HttpSession session, UserMessageService userMessageService, MessageService messageService) {
        this.session = session;
        this.userMessageService = userMessageService;
        this.messageService = messageService;
    }

    /**
     * 返回用户未读消息
     *
     * @return
     * @author 卢越
     * @since 2018-07-01
     */
    @GetMapping
    public TResult listUserMessages() {
        return TResult.success(userMessageService.selectByUserId((Long) session.getAttribute(Constants.SESSION_KEY_USER_ID)));
    }

    /**
     * 将用户未读消息更新为已读
     *
     * @param userMessage
     * @return
     */
    @PutMapping
    public TResult updateUserMessageStatus(UserMessage userMessage) {
        if (!userMessageService.update(userMessage, new EntityWrapper<UserMessage>().setSqlSelect("status").eq("message_id", userMessage.getMessageId()).and().eq("user_id", userMessage.getUserId()))) {
            return TResult.failure(TResultCode.BUSINESS_ERROR);
        }
        return TResult.success();
    }
}
