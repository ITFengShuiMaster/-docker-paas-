package cn.edu.jit.tianyu_paas.web.controller;

import cn.edu.jit.tianyu_paas.shared.entity.Notice;
import cn.edu.jit.tianyu_paas.shared.util.TResult;
import cn.edu.jit.tianyu_paas.web.service.NoticeService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/notices")
public class NoticeController {
    private HttpSession session;
    private NoticeService noticeService;

    @Autowired
    public NoticeController(HttpSession session, NoticeService noticeService) {
        this.session = session;
        this.noticeService = noticeService;
    }

    @ApiOperation("获取公告列表")
    @GetMapping
    public TResult listNotices() {
        return TResult.success(noticeService.selectList(new EntityWrapper<Notice>().setSqlSelect("notice_id as noticeId", "title", "content", "gmt_create as gmtCreate")));
    }
}
