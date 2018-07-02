package cn.edu.jit.tianyu_paas.web.controller;

import cn.edu.jit.tianyu_paas.shared.entity.Action;
import cn.edu.jit.tianyu_paas.shared.entity.ActionDetail;
import cn.edu.jit.tianyu_paas.shared.enums.ActionEnum;
import cn.edu.jit.tianyu_paas.shared.util.TResult;
import cn.edu.jit.tianyu_paas.web.global.Constants;
import cn.edu.jit.tianyu_paas.web.service.ActionDetailService;
import cn.edu.jit.tianyu_paas.web.service.ActionService;
import cn.edu.jit.tianyu_paas.web.service.AppLogService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author 天宇小凡
 */
@RestController
@RequestMapping("/actions")
public class ActionController {
    private final ActionService actionService;
    private HttpSession session;
    private final AppLogService appLogService;
    private final ActionDetailService actionDetailService;

    @Autowired
    public ActionController(ActionService actionService, HttpSession session, AppLogService appLogService, ActionDetailService actionDetailService) {
        this.actionService = actionService;
        this.session = session;
        this.appLogService = appLogService;
        this.actionDetailService = actionDetailService;
    }

    /**
     * 总览页面行为接口
     *
     * @author 卢越
     * @date 2018/6/29 16:30
     */
    @GetMapping("info")
    public TResult info() {
        List<Action> lists = actionService.selectList(new EntityWrapper<Action>().eq("user_id", session.getAttribute(Constants.SESSION_KEY_USER_ID)).orderBy("gmt_create", false).last("LIMIT 6"));

        // 添加action的行为名字
        lists.forEach(action -> action.setActionName(ActionEnum.getMessageBycode(action.getAction())));

        return TResult.success(lists);
    }

    /**
     * 获取应用的操作日志
     *
     * @author 汪继友
     * @date 2018/6/30 14:14
     */
    @GetMapping("/{appId}")
    public TResult listAppAction(@PathVariable long appId) {
        long userId = (long) session.getAttribute(Constants.SESSION_KEY_USER_ID);
        return TResult.success(actionService.listAppActionByUserId(userId, appId));
    }

    /**
     * 获取应用操作日志的详细日志（info,debug,error)
     *
     * @author 汪继友
     * @date 2018/6/30 14:27
     */
    @GetMapping("/detail")
    public TResult listActionDetails(long appId, long actionId, int level) {
        long userId = (long) session.getAttribute(Constants.SESSION_KEY_USER_ID);
        List<ActionDetail> actionDetails = actionDetailService.selectList(new EntityWrapper<ActionDetail>().eq("action_id", actionId).eq("level", level));
        return TResult.success(actionDetails);
    }
}