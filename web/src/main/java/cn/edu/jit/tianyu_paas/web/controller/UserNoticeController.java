package cn.edu.jit.tianyu_paas.web.controller;


import cn.edu.jit.tianyu_paas.shared.entity.Notice;
import cn.edu.jit.tianyu_paas.shared.entity.UserNotice;
import cn.edu.jit.tianyu_paas.shared.util.TResult;
import cn.edu.jit.tianyu_paas.shared.util.TResultCode;
import cn.edu.jit.tianyu_paas.web.global.Constants;
import cn.edu.jit.tianyu_paas.web.service.NoticeService;
import cn.edu.jit.tianyu_paas.web.service.UserNoticeService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * @author 倪龙康
 * @since 2018-07-02
 */
@RestController
@RequestMapping("user-notices")
public class UserNoticeController {

    private final HttpSession session;
    private final NoticeService noticeService;
    private final UserNoticeService userNoticeService;

    @Autowired
    public UserNoticeController(NoticeService noticeService, UserNoticeService userNoticeService, HttpSession session) {
        this.noticeService = noticeService;
        this.userNoticeService = userNoticeService;
        this.session = session;
    }

    /**
     * 分页获取所有的公告
     *
     * @param page
     * @return
     * @author 倪龙康
     */
    @GetMapping
    public TResult getAllNotices(Pagination page) {
        Page<Notice> noticePage = noticeService.selectPage(new Page<>(page.getCurrent(), page.getSize())
                , new EntityWrapper<Notice>().orderBy("gmt_create", false));
        if (noticePage == null)
            return TResult.failure(TResultCode.FAILURE);
        return TResult.success(noticePage);
    }

    /**
     * 获取公告详情，并将状态改为已读
     *
     * @param noticeId
     * @return
     * @author 倪龙康
     */
    @GetMapping("{noticeId}")
    public TResult getNoticeInfo(@PathVariable long noticeId) {
        Notice notice = noticeService.selectById(noticeId);
        if (notice == null)
            return TResult.failure(TResultCode.RESULE_DATA_NONE);
        Long userId = (Long) session.getAttribute(Constants.SESSION_KEY_USER_ID);
        UserNotice userNotice = userNoticeService.selectOne(new EntityWrapper<UserNotice>().eq("user_id", userId).eq("notice_id", noticeId));
        if (userNotice == null)
            return TResult.failure(TResultCode.RESULE_DATA_NONE);
        userNotice.setStatus(1);
        if (!userNoticeService.update(userNotice, new EntityWrapper<UserNotice>().eq("user_id", userId).eq("notice_id", noticeId)))
            return TResult.failure(TResultCode.FAILURE);
        return TResult.success(notice);
    }
}
