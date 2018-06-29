package cn.edu.jit.tianyu_paas.web.controller;

import cn.edu.jit.tianyu_paas.shared.entity.AppGroup;
import cn.edu.jit.tianyu_paas.shared.util.TResult;
import cn.edu.jit.tianyu_paas.shared.util.TResultCode;
import cn.edu.jit.tianyu_paas.web.global.Constants;
import cn.edu.jit.tianyu_paas.web.service.AppGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Date;

/**
 * @author 倪龙康
 * @since 2018-06-29
 */
@RequestMapping("/group")
@RestController
public class AppGroupController {

    private AppGroupService appGroupService;
    private HttpSession session;

    @Autowired
    public AppGroupController(AppGroupService appGroupService, HttpSession session) {
        this.appGroupService = appGroupService;
        this.session = session;
    }

    /**
     * 创建应用组
     *
     * @param appGroup
     * @return
     */
    @PostMapping("create")
    public TResult GroupCreate(AppGroup appGroup) {
        if (appGroup == null) {
            return TResult.failure( TResultCode.PARAM_IS_BLANK );
        }
        appGroup.setUserId( (Long) session.getAttribute( Constants.SESSION_KEY_USER_ID ) );
        appGroup.setGmtCreate( new Date() );

        boolean flag = appGroupService.insert( appGroup );
        if (!flag) {
            return TResult.failure( TResultCode.FAILURE );
        }
        return TResult.success( appGroup.getAppGroupId() );
    }
}
