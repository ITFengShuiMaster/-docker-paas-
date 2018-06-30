package cn.edu.jit.tianyu_paas.web.controller;


import cn.edu.jit.tianyu_paas.shared.entity.*;
import cn.edu.jit.tianyu_paas.shared.enums.AppCreateMethodEnum;
import cn.edu.jit.tianyu_paas.shared.enums.AppStatusEnum;
import cn.edu.jit.tianyu_paas.shared.util.TResult;
import cn.edu.jit.tianyu_paas.shared.util.TResultCode;
import cn.edu.jit.tianyu_paas.web.global.Constants;
import cn.edu.jit.tianyu_paas.web.service.*;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

/**
 * @author 倪龙康，卢越
 * @since 2018-06-29
 */

@RestController
@RequestMapping("/apps")
public class AppController {

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
    private AppVarService appVarService;
    private AppPortService appPortService;

    private final Logger logger = LoggerFactory.getLogger(AppController.class);

    @Autowired
    public AppController(AppService appService, AppInfoByCustomService appInfoByCustomService, HttpSession session, AppInfoByDemoService appInfoByDemoService, DemoService demoService, AppInfoByDockerImageService appInfoByDockerImageService, AppInfoByDockerRunService appInfoByDockerRunService, AppInfoByMarketService appInfoByMarketService, AppGroupService appGroupService, MarketAppService marketAppService, AppVarService appVarService, AppPortService appPortService) {
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
    }

    /**
     * 获取应用信息
     *
     * @param app_id
     * @return
     * @author 倪龙康
     */
    @GetMapping("{app_id}")
    public TResult getAppInfo(@PathVariable("app_id") Long app_id) {
        App app = appService.selectById(app_id);
        return TResult.success(app.toString());
    }

    /**
     * 添加变量
     *
     * @return
     * @author 倪龙康
     */
    @PostMapping("vars")
    public TResult addVar(AppVar appVar) {
        if (appVarService.selectCount(new EntityWrapper<AppVar>().eq("var_num", appVar.getVarName())) != 0) {
            return TResult.failure(TResultCode.DATA_ALREADY_EXISTED);
        }
        appVar.setGmtCreate(new Date());
        if (!appVarService.insert(appVar))
            return TResult.failure(TResultCode.FAILURE);
        return TResult.success();
    }

    private void initApp(App app, AppCreateMethodEnum createMethodEnum) {
        long userId = (Long) session.getAttribute(Constants.SESSION_KEY_USER_ID);
        app.setUserId(userId);
        app.setGmtCreate(new Date());
        app.setStatus(AppStatusEnum.SHUTDOWN.getCode());
        app.setCreateMethod(createMethodEnum.getCode());
        // TODO 检测仓库，并给应用设置memory, disk等
    }

    /**
     * 从自定义源码创建应用（git仓库）
     *
     * @author 汪继友
     * @date 2018/6/29 11:11
     */
    @PostMapping("custom")
    public TResult createAppByCustom(@Validated App app, @Validated AppInfoByCustom custom) {
        initApp(app, AppCreateMethodEnum.CUSTOM);

        if (appService.insert(app)) {
            custom.setAppId(app.getAppId());
            // 插入创建方式
            appInfoByCustomService.insert(custom);
            return TResult.success();
        }
        return TResult.failure(TResultCode.BUSINESS_ERROR);
    }

    /**
     * 修改变量
     *
     * @param appVar
     * @return
     * @author 倪龙康
     */
    @PutMapping("vars")
    public TResult updateVar(AppVar appVar) {
        if (!appVarService.update(appVar, new EntityWrapper<AppVar>().eq("app_id", appVar.getAppId()).and().eq("var_name", appVar.getVarName())))
            return TResult.failure(TResultCode.BUSINESS_ERROR);
        return TResult.success();
    }

    /**
     * 删除变量
     *
     * @param var_name
     * @return
     * @author 倪龙康
     */
    @DeleteMapping("vars/{var_name}")
    public TResult deleteVar(@PathVariable("var_name") String var_name) {
        if (!appVarService.delete(new EntityWrapper<AppVar>().eq("var_name", var_name)))
            return TResult.failure(TResultCode.BUSINESS_ERROR);
        return TResult.success();
    }

    /**
     * 获取变量相关信息
     *
     * @param app_id
     * @return
     * @author 倪龙康
     */
    @GetMapping("vars/{app_id}")
    public TResult getVarInfo(@PathVariable("app_id") Long app_id) {
        List<AppVar> appVars = appVarService.selectList(new EntityWrapper<AppVar>().eq("app_id", app_id));
        if (appVars == null)
            return TResult.failure(TResultCode.RESULE_DATA_NONE);
        return TResult.success(appVars);
    }

    /**
     * 获取端口号信息
     *
     * @param app_id
     * @return
     * @author 倪龙康
     */
    @GetMapping("ports/{app_id}")
    public TResult getPortInfo(@PathVariable("app_id") Long app_id) {
        AppPort appPort = appPortService.selectOne(new EntityWrapper<AppPort>().eq("app_id", app_id));
        if (appPort == null)
            return TResult.failure(TResultCode.RESULE_DATA_NONE);
        return TResult.success(appPort.toString());
    }

    /**
     * 新增端口
     *
     * @param app_id
     * @param port
     * @param protocol
     * @return
     * @author 倪龙康
     */
    @PostMapping("ports")
    public TResult addPort(Long app_id, Integer port, Integer protocol,
                           @RequestParam(required = false, defaultValue = "0") Integer is_inside_open,
                           @RequestParam(required = false, defaultValue = "xxxxxx") String inside_access_url,
                           @RequestParam(required = false, defaultValue = "xxxxxx") String inside_alias,
                           @RequestParam(required = false, defaultValue = "0") Integer is_outside_open,
                           @RequestParam(required = false, defaultValue = "xxxxxx") String outside_access_url
    ) {
        if (appPortService.selectCount(new EntityWrapper<AppPort>().eq("port", port)) != 0) {
            return TResult.failure(TResultCode.DATA_ALREADY_EXISTED);
        }
        AppPort appPort = new AppPort();
        appPort.setAppId(app_id);
        appPort.setPort(port);
        appPort.setProtocol(protocol);
        appPort.setGmtModified(new Date());
        appPort.setGmtCreate(new Date());
        appPort.setIsInsideOpen(is_inside_open);
        appPort.setIsOutsideOpen(is_outside_open);
        appPort.setInsideAccessUrl(inside_access_url);
        appPort.setOutsideAccessUrl(outside_access_url);
        appPort.setInsideAlias(inside_alias);
        if (!appPortService.insert(appPort))
            return TResult.failure(TResultCode.FAILURE);
        return TResult.success();
    }

    /**
     * 更新端口相关信息
     *
     * @param appPort
     * @return
     * @author 倪龙康
     */
    @PutMapping("ports")
    public TResult updatePort(AppPort appPort) {
        if (!appPortService.update(appPort, new EntityWrapper<AppPort>().eq("app_id", appPort.getAppId()).and().eq("port", appPort.getPort())))
            return TResult.failure(TResultCode.BUSINESS_ERROR);
        return TResult.success();
    }

    /**
     * 删除端口
     *
     * @param port
     * @return
     * @author 倪龙康
     */
    @DeleteMapping("ports/{port}")
    public TResult deletePort(@PathVariable("port") Integer port) {

        if (!appPortService.delete(new EntityWrapper<AppPort>().eq("port", port)))
            return TResult.failure(TResultCode.BUSINESS_ERROR);
        return TResult.success();
    }

    /**
     * 从官方demo创建应用
     *
     * @author 汪继友
     * @date 2018/6/29 11:11
     */
    @PostMapping("demo")
    public TResult createAppByDemo(@Validated App app, @Validated AppInfoByDemo infoByDemo) {
        Demo demo = demoService.selectById(infoByDemo.getDemoId());
        // 没有找到demo应用
        if (demo == null) {
            return TResult.failure(TResultCode.RESULE_DATA_NONE);
        }
        initApp(app, AppCreateMethodEnum.DEMO);

        if (appService.insert(app)) {
            infoByDemo.setAppId(app.getAppId());
            appInfoByDemoService.insert(infoByDemo);
            return TResult.success();
        }
        return TResult.failure(TResultCode.BUSINESS_ERROR);
    }

    /**
     * 从docker image创建应用
     *
     * @author 汪继友
     * @date 2018/6/29 11:11
     */
    @PostMapping("docker-image")
    public TResult createAppByDockerImage(@Validated App app, @Validated AppInfoByDockerImage dockerImage) {
        initApp(app, AppCreateMethodEnum.DOCKER_IMAGE);

        if (appService.insert(app)) {
            dockerImage.setAppId(app.getAppId());
            appInfoByDockerImageService.insert(dockerImage);
            return TResult.success();
        }
        return TResult.failure(TResultCode.BUSINESS_ERROR);
    }

    /**
     * 用docker run命令创建应用
     *
     * @author 汪继友
     * @date 2018/6/29 14:41
     */
    @PostMapping("docker-run")
    public TResult createAppByDockerRun(@Validated App app, @Validated AppInfoByDockerRun dockerRun) {
        initApp(app, AppCreateMethodEnum.DOCKER_RUN);

        if (appService.insert(app)) {
            dockerRun.setAppId(app.getAppId());
            appInfoByDockerRunService.insert(dockerRun);
            return TResult.success();
        }
        return TResult.failure(TResultCode.BUSINESS_ERROR);
    }

    /**
     * 根据docker compose创建应用（创建的是应用组）
     *
     * @author 汪继友
     * @date 2018/6/29 14:58
     */
    @PostMapping("docker-compose")
    public TResult createAppByDockerCompose(@Validated AppGroup appGroup) {
        long userId = (Long) session.getAttribute(Constants.SESSION_KEY_USER_ID);
        AppGroup dbGroup = appGroupService.selectOne(new EntityWrapper<AppGroup>().eq("group_name", appGroup.getGroupName()));
        if (dbGroup != null) {
            return TResult.failure(TResultCode.DATA_ALREADY_EXISTED);
        }
        appGroup.setUserId(userId);
        appGroup.setGmtCreate(new Date());
        // TODO 生成应用
        appGroupService.insert(appGroup);
        return TResult.success();
    }

    /**
     * 总览页面应用接口，包含分页，根据名字查询，根据状态查询
     *
     * @author 卢越
     * @date 2018/6/29 16:30
     */
    @GetMapping("/info")
    public TResult info(@RequestParam(required = false, defaultValue = "") String name, Integer status,
                        @RequestParam(value = "current", defaultValue = "1") Integer current,
                        @RequestParam(value = "size", defaultValue = "3") Integer size) {

        App app = new App();
        app.setName(name);
        app.setStatus(status);
        // TODO 6为测试数据，实际应该改为session.getAttribute(Constants.SESSION_KEY_USER_ID)
        app.setUserId(Long.parseLong("6"));
        Page page = appService.selectAppListPage(app, current, size);

        return TResult.success(page);
    }

    @PostMapping("market")
    public TResult createAppByMarket(@Validated App app, @Validated AppInfoByMarket market) {
        MarketApp marketApp = marketAppService.selectById(market.getMarketAppId());
        if (marketApp == null) {
            return TResult.failure(TResultCode.RESULE_DATA_NONE);
        }
        initApp(app, AppCreateMethodEnum.MARKET);
        if (appService.insert(app)) {
            market.setAppId(app.getAppId());
            appInfoByMarketService.insert(market);
            return TResult.success();
        }
        return TResult.failure(TResultCode.BUSINESS_ERROR);
    }
}