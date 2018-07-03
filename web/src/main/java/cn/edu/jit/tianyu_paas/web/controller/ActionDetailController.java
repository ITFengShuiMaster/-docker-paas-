package cn.edu.jit.tianyu_paas.web.controller;

import cn.edu.jit.tianyu_paas.shared.entity.ActionDetail;
import cn.edu.jit.tianyu_paas.shared.util.TResult;
import cn.edu.jit.tianyu_paas.shared.util.TResultCode;
import cn.edu.jit.tianyu_paas.web.global.Constants;
import cn.edu.jit.tianyu_paas.web.service.ActionDetailService;
import cn.edu.jit.tianyu_paas.web.service.ActionService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/action-details")
public class ActionDetailController {
    private final ActionDetailService actionDetailService;
    private ActionService actionService;
    private HttpSession session;

    @Autowired
    public ActionDetailController(ActionService actionService, HttpSession session, ActionDetailService actionDetailService) {
        this.actionService = actionService;
        this.session = session;
        this.actionDetailService = actionDetailService;
    }

    /**
     * 获取应用操作日志的详细日志（info,debug,error)
     *
     * @author 汪继友
     * @date 2018/6/30 14:27
     */
    @GetMapping("/detail")
    public TResult listAppInfoLog(long appId, long actionId, int level) {
        long userId = (long) session.getAttribute(Constants.SESSION_KEY_USER_ID);
        if (actionService.isActionIdExist(userId, appId, actionId)) {
            return TResult.success(actionDetailService.selectList(new EntityWrapper<ActionDetail>()
                    .eq("action_id", actionId).eq("level", level)));
        }
        return TResult.failure(TResultCode.PERMISSION_NO_ACCESS);
    }
}
