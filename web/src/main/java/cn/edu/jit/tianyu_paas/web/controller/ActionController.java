package cn.edu.jit.tianyu_paas.web.controller;

import cn.edu.jit.tianyu_paas.shared.entity.Action;
import cn.edu.jit.tianyu_paas.shared.util.TResult;
import cn.edu.jit.tianyu_paas.web.service.ActionService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

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

    @GetMapping("/info")
    public TResult info(){
        // TODO 6为测试数据，实际应该改为session.getAttribute(Constants.SESSION_KEY_USER_ID)
        List<Action> lists = actionService.selectList(new EntityWrapper<Action>().eq("user_id", 6).orderBy("gmt_create", false));
        List<String> actions = new ArrayList<>();
        for (Action action : lists) {
            StringBuilder sb = new StringBuilder();
            sb.append(action.getUserName());
            switch (action.getAction()) {
                case 0:
                    sb.append("水平升级");
                    break;
                case 1:
                    sb.append("启动");
                    break;
                case 2:
                    sb.append("重启");
                    break;
                case 3:
                    sb.append("部署");
                    break;
                default:
                    break;
            }
            sb.append(action.getAppName() + "应用");
            sb.append(action.getStatus() == 1 ? "完成" : "失败");
            actions.add(sb.toString());
        }

        return TResult.success(actions);
    }
}
