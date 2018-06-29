package cn.edu.jit.tianyu_paas.web.controller;


import cn.edu.jit.tianyu_paas.shared.entity.App;
import cn.edu.jit.tianyu_paas.shared.entity.AppCustomInfo;
import cn.edu.jit.tianyu_paas.shared.util.StringUtil;
import cn.edu.jit.tianyu_paas.shared.util.TResult;
import cn.edu.jit.tianyu_paas.shared.util.TResultCode;
import cn.edu.jit.tianyu_paas.web.global.Constants;
import cn.edu.jit.tianyu_paas.web.service.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Date;

/**
 * @author 倪龙康，卢越
 * @since 2018-06-29
 */

@RestController
@RequestMapping("/apps")
public class AppController {

    private AppService appService;
    private HttpSession session;

    @Autowired
    public AppController(AppService appService, HttpSession session) {
        this.appService = appService;
        this.session = session;
    }

    /**
     * 从自定义源码创建应用（git仓库）
     *
     * @author 汪继友
     * @date 2018/6/29 11:11
     */
    @PostMapping("custom")
    public TResult createApp(@Validated App app, AppCustomInfo custom) {
        if (StringUtil.isAnyEmpty(app.getName())) {
            return TResult.failure(TResultCode.PARAM_IS_BLANK);
        }
        if (app.getAppGroupId() == 0) {
            return TResult.failure(TResultCode.PARAM_IS_INVALID);
        }

        long userId = (Long) session.getAttribute(Constants.SESSION_KEY_USER_ID);
        app.setUserId(userId);
        app.setGmtCreate(new Date());

        // TODO 检测仓库，并给应用设置memory, disk等
        app.setCreateMethod(0);

        return TResult.success();
    }
}
