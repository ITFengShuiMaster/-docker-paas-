package cn.edu.jit.tianyu_paas.web.controller;

import cn.edu.jit.tianyu_paas.shared.util.TResult;
import cn.edu.jit.tianyu_paas.web.global.Constants;
import cn.edu.jit.tianyu_paas.web.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;


/**
 * @author 天宇小凡
 */
@RequestMapping("app-log")
@RestController
public class AppLogController {
    private final AppService appService;
    private final AppInfoByCustomService appInfoByCustomService;
    private final HttpSession session;
    private final AppInfoByDemoService appInfoByDemoService;
    private final DemoService demoService;
    private final AppInfoByDockerImageService appInfoByDockerImageService;
    private final AppInfoByDockerRunService appInfoByDockerRunService;
    private final AppInfoByMarketService appInfoByMarketService;
    private final AppGroupService appGroupService;
    private final MarketAppService marketAppService;
    private final ActionService actionService;
    private final Logger logger = LoggerFactory.getLogger(AppLogController.class);
    private AppVarService appVarService;
    private AppPortService appPortService;
    private AppLogService appLogService;

    @Autowired
    public AppLogController(AppService appService, AppInfoByCustomService appInfoByCustomService, HttpSession session, AppInfoByDemoService appInfoByDemoService, DemoService demoService, AppInfoByDockerImageService appInfoByDockerImageService, AppInfoByDockerRunService appInfoByDockerRunService, AppInfoByMarketService appInfoByMarketService, AppGroupService appGroupService, MarketAppService marketAppService, AppVarService appVarService, AppPortService appPortService, ActionService actionService) {
        this.appService = appService;
        this.appInfoByCustomService = appInfoByCustomService;
        this.session = session;
        this.appInfoByDemoService = appInfoByDemoService;
        this.demoService = demoService;
        this.appInfoByDockerImageService = appInfoByDockerImageService;
        this.appInfoByDockerRunService = appInfoByDockerRunService;
        this.appInfoByMarketService = appInfoByMarketService;
        this.appGroupService = appGroupService;
        this.marketAppService = marketAppService;
        this.appVarService = appVarService;
        this.appPortService = appPortService;
        this.actionService = actionService;
    }

    @GetMapping("/{appId}")
    public TResult listAppLog(@PathVariable String appId) {
        long userId = (long) session.getAttribute(Constants.SESSION_KEY_USER_ID);
        return TResult.success();
    }
}
