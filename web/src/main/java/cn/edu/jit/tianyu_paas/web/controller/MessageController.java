package cn.edu.jit.tianyu_paas.web.controller;

import cn.edu.jit.tianyu_paas.shared.entity.Message;
import cn.edu.jit.tianyu_paas.shared.util.TResult;
import cn.edu.jit.tianyu_paas.web.service.MessageService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/messages")
public class MessageController {
    private final MessageService messageService;
    private HttpSession session;

    @Autowired
    public MessageController(HttpSession session, MessageService messageService) {
        this.session = session;
        this.messageService = messageService;
    }

    @GetMapping
    public TResult listMessages() {
        return TResult.success(messageService.selectList(new EntityWrapper<Message>().setSqlSelect("message_id as messageId", "content", "gmt_create as gmtCreate")));
    }
}
