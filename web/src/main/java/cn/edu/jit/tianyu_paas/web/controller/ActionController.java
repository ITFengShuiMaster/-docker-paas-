package cn.edu.jit.tianyu_paas.web.controller;

import cn.edu.jit.tianyu_paas.shared.entity.Action;
import cn.edu.jit.tianyu_paas.shared.enums.ActionEnum;
import cn.edu.jit.tianyu_paas.shared.util.DateUtil;
import cn.edu.jit.tianyu_paas.shared.util.TResult;
import cn.edu.jit.tianyu_paas.web.global.Constants;
import cn.edu.jit.tianyu_paas.web.service.ActionService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/actions")
public class ActionController {
    private ActionService actionService;
    private HttpSession session;

    @Autowired
    public ActionController(ActionService actionService, HttpSession session) {
        this.actionService = actionService;
        this.session = session;
    }

    /**
     * 总览页面行为接口
     *
     * @author 卢越
     * @date 2018/6/29 16:30
     */
    @GetMapping
    public TResult info() {
        List<Action> lists = actionService.selectList(new EntityWrapper<Action>().eq("user_id", session.getAttribute(Constants.SESSION_KEY_USER_ID)).orderBy("gmt_create", false).last("LIMIT 6"));
        List<Map<String, Object>> actions = new ArrayList<>();
        for (Action action : lists) {
            Map<String, Object> map = new HashMap<>(16);
            map.put("userId", action.getUserId());
            map.put("userName", action.getUserName());
            map.put("action", ActionEnum.getMessageBycode(action.getAction()));
            map.put("appId", action.getAppId());
            map.put("appName", action.getAppName());
            map.put("status", Action.STATUS.equals(action.getStatus()) ? "完成" : "失败");
            map.put("date", DateUtil.getSimpleDate(action.getGmtCreate()));
            actions.add(map);
        }

        return TResult.success(actions);
    }
}
