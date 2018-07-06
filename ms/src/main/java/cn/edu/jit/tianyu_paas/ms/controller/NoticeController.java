package cn.edu.jit.tianyu_paas.ms.controller;


import cn.edu.jit.tianyu_paas.ms.global.Constants;
import cn.edu.jit.tianyu_paas.ms.service.NoticeService;
import cn.edu.jit.tianyu_paas.ms.service.UserNoticeService;
import cn.edu.jit.tianyu_paas.ms.service.UserService;
import cn.edu.jit.tianyu_paas.shared.entity.Notice;
import cn.edu.jit.tianyu_paas.shared.entity.User;
import cn.edu.jit.tianyu_paas.shared.entity.UserNotice;
import cn.edu.jit.tianyu_paas.shared.global.PublicConstants;
import cn.edu.jit.tianyu_paas.shared.rabbitmq.MQMessage;
import cn.edu.jit.tianyu_paas.shared.util.TResult;
import cn.edu.jit.tianyu_paas.shared.util.TResultCode;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import io.swagger.annotations.ApiOperation;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * @author 倪龙康
 * @since 2018-07-01
 */
@RestController
@RequestMapping("/notices")
public class NoticeController {

    private final NoticeService noticeService;
    private final UserService userService;
    private final UserNoticeService userNoticeService;
    private final HttpSession session;
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public NoticeController(NoticeService noticeService, HttpSession session, UserService userService, UserNoticeService userNoticeService, RabbitTemplate rabbitTemplate) {
        this.noticeService = noticeService;
        this.session = session;
        this.userService = userService;
        this.userNoticeService = userNoticeService;
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * 创建公告
     *
     * @param notice
     * @return
     * @author 倪龙康
     */
    @ApiOperation("创建公告")
    @PostMapping
    public TResult noticeCreate(Notice notice) {
        Long adminId = (Long) session.getAttribute(Constants.SESSION_KEY_ADMIN_ID);
        notice.setAdminId(adminId);
        notice.setGmtCreate(new Date());
        if (!noticeService.insert(notice)) {
            return TResult.failure(TResultCode.FAILURE);
        }
        List<User> users = userService.selectList(new EntityWrapper<>());
        List<UserNotice> uesrNotices = new ArrayList<>();
        List<Long> receivers = new ArrayList<>();
        for (User user : users) {
            UserNotice userNotice = new UserNotice();
            userNotice.setNoticeId(notice.getNoticeId());
            userNotice.setStatus(UserNotice.STATUS_UNREAD);
            userNotice.setUserId(user.getUserId());
            uesrNotices.add(userNotice);

            receivers.add(user.getUserId());
        }
        if (userNoticeService.insertBatch(uesrNotices)) {

            // 往消息队列发送json格式的字符串消息
            rabbitTemplate.convertAndSend(PublicConstants.RABBITMQ_QUEUE_NAME, JSON.toJSONString(MQMessage.notice(notice, receivers)));
            return TResult.success();
        }
        return TResult.failure(TResultCode.BUSINESS_ERROR);
    }

    /**
     * 获取公告
     *
     * @param page
     * @return
     * @author 倪龙康
     */
    @ApiOperation("获取公告")
    @GetMapping
    public TResult getNotice(Pagination page) {
        Page<Notice> notices = noticeService.selectPage(new Page<>(page.getCurrent(), page.getSize()),
                new EntityWrapper<Notice>().orderBy("gmt_create", false));
        return TResult.success(notices);
    }

    /**
     * 更新公告
     *
     * @param notice
     * @return
     * @author 倪龙康
     */
    @ApiOperation("更新公告")
    @PutMapping
    public TResult updateNotice(Notice notice) {
        if (!noticeService.update(notice, new EntityWrapper<Notice>().eq("notice_id", notice.getNoticeId()))) {
            return TResult.failure(TResultCode.FAILURE);
        }
        return TResult.success();
    }

    /**
     * 删除公告
     *
     * @param noticeId
     * @return
     * @author 倪龙康
     */
    @ApiOperation("删除公告")
    @DeleteMapping("/{noticeId}")
    public TResult deleteNotice(@PathVariable Long noticeId) {
        if (!noticeService.deleteById(noticeId)) {
            return TResult.failure(TResultCode.FAILURE);
        }
        return TResult.success();
    }
}

