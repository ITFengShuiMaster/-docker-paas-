package cn.edu.jit.tianyu_paas.web.controller;


import cn.edu.jit.tianyu_paas.shared.entity.App;
import cn.edu.jit.tianyu_paas.shared.util.TResult;
import cn.edu.jit.tianyu_paas.shared.util.TResultCode;
import cn.edu.jit.tianyu_paas.web.global.Constants;
import cn.edu.jit.tianyu_paas.web.service.AppService;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/app")
public class AppController {

    private AppService appService;
    private HttpSession session;

    @Autowired
    public AppController(AppService appService, HttpSession session) {
        this.appService = appService;
        this.session = session;
    }

    /**
     * 创建应用
     *
     * @param app
     * @return
     */
    @PostMapping("create")
    public TResult AppCreate(App app) {
        if (app == null) {
            return TResult.failure( TResultCode.PARAM_IS_BLANK );
        }

        app.setUserId( (Long) session.getAttribute( Constants.SESSION_KEY_USER_ID ) );
        app.setGmtCreate( new Date() );

        boolean flag = appService.insert( app );
        if (!flag) {
            return TResult.failure( TResultCode.FAILURE );
        }

        return TResult.success();
    }


}
