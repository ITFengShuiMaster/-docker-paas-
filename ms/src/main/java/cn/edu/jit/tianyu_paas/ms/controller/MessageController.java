package cn.edu.jit.tianyu_paas.ms.controller;


import cn.edu.jit.tianyu_paas.ms.service.MessageService;
import cn.edu.jit.tianyu_paas.ms.service.UserMessageService;
import cn.edu.jit.tianyu_paas.shared.entity.Message;
import cn.edu.jit.tianyu_paas.shared.entity.UserMessage;
import cn.edu.jit.tianyu_paas.shared.util.TResult;
import cn.edu.jit.tianyu_paas.shared.util.TResultCode;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 汪继友
 * @since 2018-07-02
 */
@RestController
@RequestMapping("/messages")
public class MessageController {
    private final MessageService messageService;
    private final UserMessageService userMessageService;
    private HttpSession session;

    @Autowired
    public MessageController(HttpSession session, MessageService messageService, UserMessageService userMessageService) {
        this.session = session;
        this.messageService = messageService;
        this.userMessageService = userMessageService;
    }

    /**
     * 插入一条message记录
     *
     * @param message
     * @param userIds
     * @return
     * @author 卢越
     * @since 2018-07-01
     */
    @ApiOperation("插入一条message记录")
    @PostMapping
    public TResult insertMessage(@Valid Message message, @RequestParam(required = true) Long[] userIds) {
        message.setGmtCreate(new Date());
        message.setGmtModified(new Date());
        if (!messageService.insert(message)) {
            return TResult.failure(TResultCode.BUSINESS_ERROR);
        }

        List<UserMessage> lists = new ArrayList<>();
        for (int i = 0; i < userIds.length; i++) {
            UserMessage userMessage = new UserMessage();
            userMessage.setMessageId(message.getMessageId());
            userMessage.setUserId(userIds[i]);
            userMessage.setStatus(0);
            lists.add(userMessage);
        }

        if (!userMessageService.insertBatch(lists)) {
            return TResult.failure(TResultCode.BUSINESS_ERROR);
        }


        return TResult.success();
    }

    /**
     * 返回所有message记录
     *
     * @return TResult
     * @author 卢越
     * @since 2018-07-01
     */
    @ApiOperation("返回所有message记录")
    @GetMapping
    public TResult listMessages() {
        return TResult.success(messageService.selectList(new EntityWrapper<Message>()));
    }

    /**
     * 删除一条message
     *
     * @param id
     * @return
     * @author 卢越
     * @since 2018-07-01
     */
    @ApiOperation("删除一条message")
    @DeleteMapping("{id}")
    public TResult deleteMessage(@PathVariable String id) {
        if (!messageService.deleteById(Long.parseLong(id))) {
            return TResult.failure(TResultCode.BUSINESS_ERROR);
        }

        return TResult.success();
    }
}

