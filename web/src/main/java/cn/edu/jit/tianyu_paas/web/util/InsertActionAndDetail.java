package cn.edu.jit.tianyu_paas.web.util;

import cn.edu.jit.tianyu_paas.shared.entity.Action;
import cn.edu.jit.tianyu_paas.shared.entity.ActionDetail;
import cn.edu.jit.tianyu_paas.shared.entity.App;
import cn.edu.jit.tianyu_paas.shared.entity.User;
import cn.edu.jit.tianyu_paas.web.global.Constants;
import cn.edu.jit.tianyu_paas.web.service.ActionDetailService;
import cn.edu.jit.tianyu_paas.web.service.ActionService;
import cn.edu.jit.tianyu_paas.web.service.AppService;
import cn.edu.jit.tianyu_paas.web.service.UserService;

import javax.servlet.http.HttpSession;
import java.util.Date;

public class InsertActionAndDetail {

    private static ActionDetailService actionDetailService = SpringBeanFactoryUtil.getBean(ActionDetailService.class);
    private static ActionService actionService = SpringBeanFactoryUtil.getBean(ActionService.class);
    private static AppService appService = SpringBeanFactoryUtil.getBean(AppService.class);
    private static UserService userService = SpringBeanFactoryUtil.getBean(UserService.class);

    /**
     * 插入action
     * @param appId
     * @return
     */
    public static Action insertAction(long appId, HttpSession session) {

        Long userId = (Long) session.getAttribute(Constants.SESSION_KEY_USER_ID);
        User user = userService.selectById(userId);
        App app = appService.selectById(appId);
        if (app == null) {
            return null;
        }
        Action action = new Action();
        action.setUserName(user.getName());
        action.setGmtCreate(new Date());
        action.setUserId(userId);
        action.setAppId(appId);
        action.setAppName(app.getName());
        action.setAction(0);
        action.setStatus(0);
        actionService.insert(action);
        return action;
    }

    /**
     * 插入actionDetail
     * @param actionId
     * @param content
     * @param level
     * @return
     */
  public static void insertActionDetail(long actionId, String content, Integer level){

        ActionDetail actionDetail = new ActionDetail();
        actionDetail.setActionId(actionId);
        actionDetail.setLevel(level);
        actionDetail.setGmtCreate(new Date());
        actionDetail.setContent(content);
        actionDetailService.insert(actionDetail);
  }
}
