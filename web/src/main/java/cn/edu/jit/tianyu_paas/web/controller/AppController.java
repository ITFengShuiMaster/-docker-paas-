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
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.DockerCmdExecFactory;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.jaxrs.JerseyDockerCmdExecFactory;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Date;

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
    private final Logger logger = LoggerFactory.getLogger(AppController.class);
    private final AppVarService appVarService;
    private final AppPortService appPortService;
    private final MachinePortService machinePortService;
    private final MachineService machineService;
    private final MarketAppPortService marketAppPortService;
    private final MarketAppVarService marketAppVarService;

    @Autowired
    public AppController(AppService appService, AppInfoByCustomService appInfoByCustomService, HttpSession session, AppInfoByDemoService appInfoByDemoService, DemoService demoService, AppInfoByDockerImageService appInfoByDockerImageService, AppInfoByDockerRunService appInfoByDockerRunService, AppInfoByMarketService appInfoByMarketService, AppGroupService appGroupService, MarketAppService marketAppService, AppVarService appVarService, AppPortService appPortService, MachinePortService machinePortService, MachineService machineService, MarketAppPortService marketAppPortService, MarketAppVarService marketAppVarService) {
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
        this.machinePortService = machinePortService;
        this.machineService = machineService;
        this.marketAppPortService = marketAppPortService;
        this.marketAppVarService = marketAppVarService;
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
     * 获取应用信息----完善过（2018-7-6）
     *
     * @param appId
     * @return
     * @author 倪龙康, 卢越
     */
    @ApiOperation("获取应用信息")
    @GetMapping("/{appId}")
    public TResult getAppInfo(@PathVariable Long appId) {
        App app = appService.selectById(appId);
        //获取容器的信息
        app.setInspectContainerResponse(getDockerClient().inspectContainerCmd(app.getContainerId()).exec());

        return TResult.success(app);
    }

    private DockerClient getDockerClient() {
        DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder()
                .withDockerHost("tcp://120.77.146.118:2375")
                .withRegistryUsername("itfengshuimaster")
                .withRegistryPassword("wxhzq520")
                .withRegistryEmail("wxhzq520@sina.com")
                .withRegistryUrl("https://hub.docker.com/r/itfengshuimaster/mydocker/")
                .build();
        DockerCmdExecFactory dockerCmdExecFactory = new JerseyDockerCmdExecFactory()
                .withReadTimeout(100000)
                .withConnectTimeout(100000)
                .withMaxTotalConnections(100)
                .withMaxPerRouteConnections(10);

        return DockerClientBuilder.getInstance(config)
                .withDockerCmdExecFactory(dockerCmdExecFactory)
                .build();
    }

    /**
     * 从自定义源码创建应用（git仓库）
     *
     * @author 汪继友
     * @date 2018/6/29 11:11
     */
    @ApiOperation("从自定义源码创建应用（git仓库）")
    @PostMapping("/custom")
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
     * 从官方demo创建应用
     *
     * @author 汪继友
     * @date 2018/6/29 11:11
     */
    @ApiOperation("从官方demo创建应用")
    @PostMapping("/demo")
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
    @ApiOperation("从docker image创建应用")
    @PostMapping("/docker-image")
    public TResult createAppByDockerImage(@Validated App app, @Validated AppInfoByDockerImage dockerImage) {
        initApp(app, AppCreateMethodEnum.DOCKER_IMAGE);
        return appService.initContainer(app, dockerImage);
    }

    /**
     * 用docker run命令创建应用
     *
     * @author 汪继友
     * @date 2018/6/29 14:41
     */
    @ApiOperation("用docker run命令创建应用")
    @PostMapping("/docker-run")
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
    @ApiOperation("根据docker compose创建应用（创建的是应用组）")
    @PostMapping("/docker-compose")
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
    @ApiOperation("总览页面应用接口，包含分页，根据名字查询，根据状态查询")
    @GetMapping("/lists")
    public TResult listAppByNameAndStatus(@RequestParam(required = false, defaultValue = "") String name, Integer status, Pagination page) {
        App app = new App();
        app.setName(name);
        app.setStatus(status);
        app.setUserId((Long) session.getAttribute(Constants.SESSION_KEY_USER_ID));
        Page<App> appPages = appService.listAppsByNameAndStatus(app, page);

        return TResult.success(appPages);
    }

    @ApiOperation("从应用市场创建应用")
    @PostMapping("/market")
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