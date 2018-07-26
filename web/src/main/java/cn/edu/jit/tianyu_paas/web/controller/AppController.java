package cn.edu.jit.tianyu_paas.web.controller;

import cn.edu.jit.tianyu_paas.shared.entity.*;
import cn.edu.jit.tianyu_paas.shared.enums.AppCreateMethodEnum;
import cn.edu.jit.tianyu_paas.shared.enums.AppStatusEnum;
import cn.edu.jit.tianyu_paas.shared.global.DockerConstants;
import cn.edu.jit.tianyu_paas.shared.util.*;
import cn.edu.jit.tianyu_paas.web.global.Constants;
import cn.edu.jit.tianyu_paas.web.service.*;
import cn.edu.jit.tianyu_paas.web.util.InsertActionAndDetail;
import cn.edu.jit.tianyu_paas.web.util.YmSocket;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.github.dockerjava.api.DockerClient;
import com.spotify.docker.client.messages.PortBinding;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * @author 倪龙康，卢越
 * @since 2018-06-29
 */

@RestController
@RequestMapping("/apps")
public class AppController {

    private final ActionService actionService;
    private final UserService userService;
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
    private final MountSettingsService mountSettingsService;
    @Value("${docker_run.host.address}")
    private String hostName;
    @Value("${docker_run.host.username}")
    private String userName;
    @Value("${docker_run.host.pwd}")
    private String pwd;

    @Autowired
    public AppController(AppService appService, AppInfoByCustomService appInfoByCustomService, HttpSession session, AppInfoByDemoService appInfoByDemoService, DemoService demoService, AppInfoByDockerImageService appInfoByDockerImageService, AppInfoByDockerRunService appInfoByDockerRunService, AppInfoByMarketService appInfoByMarketService, AppGroupService appGroupService, MarketAppService marketAppService, AppVarService appVarService, AppPortService appPortService, MachinePortService machinePortService, MachineService machineService, MarketAppPortService marketAppPortService, MarketAppVarService marketAppVarService, UserService userService, ActionService actionService, MountSettingsService mountSettingsService) {
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
        this.userService = userService;
        this.actionService = actionService;
        this.mountSettingsService = mountSettingsService;
    }

    private void initApp(App app, AppCreateMethodEnum createMethodEnum) {
        long userId = (Long) session.getAttribute(Constants.SESSION_KEY_USER_ID);
        app.setUserId(userId);
        app.setGmtCreate(new Date());
        app.setStatus(AppStatusEnum.SHUTDOWN.getCode());
        app.setCreateMethod(createMethodEnum.getCode());
        //设置memory
        app.setMemoryUsed(100);
        // TODO 检测仓库，并给应用设置memory, disk等
    }

    private void initAction(Action action, App app) {
        User user = userService.selectById((Long) session.getAttribute(Constants.SESSION_KEY_USER_ID));
        action.setUserId(user.getUserId());
        action.setUserId(user.getUserId());
        action.setUserName(user.getName());
        action.setAppId(app.getAppId());
        action.setAppName(app.getName());
        action.setGmtCreate(new Date());
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
        if (app == null) {
            TResult.failure("该应用不存在");
        }
        //获取容器的信息
        DockerClient dockerClient = DockerJavaUtil.getDockerClient(machineService.selectById(app.getMachineId()).getMachineIp());
        app.setInspectContainerResponse(dockerClient.inspectContainerCmd(app.getContainerId()).exec());

        return TResult.success(app);
    }

    /**
     * 从自定义源码创建应用（git仓库）
     *
     * @author 倪龙康
     * @date 2018/6/29 11:11
     */
    @ApiOperation("从自定义源码创建应用（git仓库）")
    @PostMapping("/custom")
    public TResult createAppByCustom(@Validated App app, @Validated AppInfoByCustom custom) {
        if (appService.selectOne(new EntityWrapper<App>().eq("name", app.getName())) != null) {
            return TResult.failure("容器名已存在");
        }
        initApp(app, AppCreateMethodEnum.CUSTOM);
        Long userId = (Long) session.getAttribute(Constants.SESSION_KEY_USER_ID);
        app.setGmtCreate(new Date());
        app.setStatus(1);
        app.setCreateMethod(0);
        app.setAppGroupId(app.getAppGroupId());
        if (appService.insert(app)) {
            custom.setAppId(app.getAppId());
            // 插入创建方式
            appInfoByCustomService.insert(custom);
            User user = userService.selectById(userId);
            Action action = new Action();
            action.setUserId(userId);
            action.setActionName("创建应用");
            action.setUserName(user.getName());
            action.setAppId(app.getAppId());
            action.setAppName(app.getName());
            action.setAction(1);
            action.setStatus(1);
            action.setGmtCreate(new Date());
            if (!actionService.insert(action)) {
                return TResult.failure(TResultCode.BUSINESS_ERROR);
            }
            YmSocket.createByYm(custom.getRepositoryUrl(), custom.getBranch(), userId, action.getActionId(), app);
            Action action1 = new Action();
            action1.setUserId(userId);
            action1.setActionName("部署完成");
            action1.setUserName(user.getName());
            action1.setAppId(app.getAppId());
            action1.setAppName(app.getName());
            action1.setAction(3);
            action1.setStatus(1);
            action1.setGmtCreate(new Date());
            actionService.insert(action);
            return TResult.success();
        }
        return TResult.failure(TResultCode.BUSINESS_ERROR);
    }

    /**
     * 从官方demo创建应用
     *
     * @author 倪龙康
     * @date 2018/6/29 11:11
     */
    @ApiOperation("从官方demo创建应用")
    @PostMapping("/demo")
    public TResult createAppByDemo(@Validated App app, @Validated AppInfoByDemo infoByDemo) {
        if (appService.selectOne(new EntityWrapper<App>().eq("name", app.getName())) != null) {
            return TResult.failure("容器名已存在");
        }
        Demo demo = demoService.selectById(infoByDemo.getDemoId());
        // 没有找到demo应用
        if (demo == null) {
            return TResult.failure(TResultCode.RESULE_DATA_NONE);
        }
        initApp(app, AppCreateMethodEnum.DEMO);
        Long userId = (Long) session.getAttribute(Constants.SESSION_KEY_USER_ID);
        app.setGmtCreate(new Date());
        app.setStatus(1);
        app.setCreateMethod(1);
        app.setAppGroupId(app.getAppGroupId());
        if (appService.insert(app)) {
            infoByDemo.setAppId(app.getAppId());
            appInfoByDemoService.insert(infoByDemo);
            User user = userService.selectById(userId);
            Action action = new Action();
            action.setUserId(userId);
            action.setActionName("创建应用");
            action.setUserName(user.getName());
            action.setAppId(app.getAppId());
            action.setAppName(app.getName());
            action.setAction(1);
            action.setStatus(1);
            action.setGmtCreate(new Date());
            if (!actionService.insert(action)) {
                return TResult.failure(TResultCode.BUSINESS_ERROR);
            }
            YmSocket.createByYm(demo.getRepositoryUrl(), "master", userId, action.getActionId(), app);
            Action action1 = new Action();
            action1.setUserId(userId);
            action1.setActionName("部署完成");
            action1.setUserName(user.getName());
            action1.setAppId(app.getAppId());
            action1.setAppName(app.getName());
            action1.setAction(3);
            action1.setStatus(1);
            action1.setGmtCreate(new Date());
            actionService.insert(action);
            return TResult.success();
        }
        return TResult.failure(TResultCode.BUSINESS_ERROR);
    }

    /**
     * 从docker image创建应用
     *
     * @author 卢越
     * @date 2018/6/29 11:11
     */
    @ApiOperation("从docker image创建应用")
    @PostMapping("/docker-image")
    public TResult createAppByDockerImage(@Validated App app, @Validated AppInfoByDockerImage dockerImage) {
        Action action = new Action();
        if (appService.selectOne(new EntityWrapper<App>().eq("name", app.getName())) != null) {
            return TResult.failure("容器名已存在");
        }
        initApp(app, AppCreateMethodEnum.DOCKER_IMAGE);

        TResult tResult = appService.initContainer(app, dockerImage.getImage());

        if (tResult.getCode() == 1) {
            dockerImage.setAppId(app.getAppId());
            appInfoByDockerImageService.insert(dockerImage);

            Machine machine = machineService.selectById(app.getMachineId());
            initAction(action, app);
            action.setAction(3);

            if (DockerClientUtil.isRunning(machine.getMachineIp(), app.getContainerId())) {
                action.setStatus(0);
            } else {
                action.setStatus(1);
            }

            actionService.insert(action);
            return tResult;
        }

        return tResult;
    }

    /**
     * 用docker run命令创建应用
     *
     * @author 卢越
     * @date 2018/6/29 14:41
     */
    @ApiOperation("用docker run命令创建应用")
    @PostMapping("/docker-run")
    public TResult createAppByDockerRun(@Validated App app, @Validated AppInfoByDockerRun dockerRun) {
        Action action = new Action();
        initApp(app, AppCreateMethodEnum.DOCKER_RUN);

        if (appService.selectOne(new EntityWrapper<App>().eq("name", app.getName())) != null) {
            return TResult.failure("容器名已存在");
        }

        if (!dockerRun.getCmd().contains("-d")) {
            return TResult.failure("请添加-d参数以保证容器后台正常运行");
        }

        String res = DockerRunUtils.dockerRunCmd(hostName, userName, pwd, dockerRun.getCmd()).trim();

        if (!StringUtil.isEmpty(res)) {
            app.setContainerId(res);
            app.setMachineId(Long.parseLong("1"));
            app.setMarketAppId(Long.parseLong("1"));

            if (DockerClientUtil.isRunning(hostName, res)) {
                app.setStatus(1);
                action.setStatus(0);
            } else {
                app.setStatus(0);
                action.setStatus(1);
            }

            appService.insert(app);
            initAction(action, app);
            action.setAction(3);
            actionService.insert(action);
            dockerRun.setAppId(app.getAppId());
            appInfoByDockerRunService.insert(dockerRun);

            return TResult.success();
        }

        return TResult.failure("运行命令有误，请重新尝试！");
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

    /**
     * @param app
     * @param market
     * @return
     * @author 卢越
     * @date 2018/7/18 16:30
     */
    @ApiOperation("从应用市场创建应用")
    @PostMapping("/market")
    public TResult createAppByMarket(@Validated App app, @Validated AppInfoByMarket market) {
        Action action = new Action();

        if (appService.selectOne(new EntityWrapper<App>().eq("name", app.getName())) != null) {
            return TResult.failure("容器名已存在");
        }

        MarketApp marketApp = marketAppService.selectById(market.getMarketAppId());
        if (marketApp == null) {
            return TResult.failure(TResultCode.RESULE_DATA_NONE);
        }
        initApp(app, AppCreateMethodEnum.MARKET);

        TResult tResult = appService.initContainer(app, marketApp.getName());
        Machine machine = machineService.selectById(app.getMachineId());
        if (tResult.getCode() == 1) {
            market.setAppId(app.getAppId());
            appInfoByMarketService.insert(market);

            initAction(action, app);
            action.setAction(3);

            if (DockerClientUtil.isRunning(machine.getMachineIp(), app.getContainerId())) {
                action.setStatus(0);
            } else {
                action.setStatus(1);
            }

            actionService.insert(action);
            return tResult;
        }

        return tResult;
    }

    /**
     * 更新app信息，主要用来资源伸缩
     *
     * @param app
     * @return
     * @author 倪龙康
     */
    @ApiOperation("更新app信息")
    @PutMapping
    public TResult updateApp(App app) {
        long userId = (Long) session.getAttribute(Constants.SESSION_KEY_USER_ID);

        Machine machine = machineService.selectById(app.getMachineId());
        DockerClient dockerClient = DockerJavaUtil.getDockerClient(machine.getMachineIp());
        if (dockerClient.updateContainerCmd(app.getContainerId()).withMemory((long) (app.getMemoryUsed() * 1024 * 1024))
                .withMemorySwap((long) -1).exec() != null) {
            if (appService.update(app, new EntityWrapper<App>().eq("app_id", app.getAppId()).eq("user_id", userId))) {
                return TResult.success();
            }
        }
        return TResult.failure(TResultCode.BUSINESS_ERROR);
    }

    /**
     * 启动容器
     * @author 卢越
     * @date 2018/7/20 16:30
     * @param appId
     * @return
     */
    @GetMapping("/start/{appId}")
    public TResult startContainer(@PathVariable Long appId) {
        Action action = InsertActionAndDetail.insertAction(appId, session);
        if (action == null) {
            return TResult.failure(TResultCode.BUSINESS_ERROR);
        }
        App app = appService.selectById(appId);
        InsertActionAndDetail.insertActionDetail(action.getActionId(), "查询此应用是否存在...", 0);
        if (app == null) {
            InsertActionAndDetail.insertActionDetail(action.getActionId(), "此应用不存在", 2);
            return TResult.failure(TResultCode.RESULE_DATA_NONE);
        }
        InsertActionAndDetail.insertActionDetail(action.getActionId(), "查询此机器是否存在...", 0);
        Machine machine = machineService.selectById(app.getMachineId());
        if (machine == null) {
            InsertActionAndDetail.insertActionDetail(action.getActionId(), "此机器不存在", 2);
            return TResult.failure(TResultCode.BUSINESS_ERROR);
        }

        InsertActionAndDetail.insertActionDetail(action.getActionId(),"查询此容器是否已经开启...",0);
        if (DockerClientUtil.isRunning(machine.getMachineIp(), app.getContainerId())) {
            app.setStatus(1);
            appService.updateById(app);
            action.setStatus(1);
            action.setAction(1);
            action.setActionName("容器已开启");
            actionService.updateById(action);
            return TResult.failure("容器已开启");
        }

        InsertActionAndDetail.insertActionDetail(action.getActionId(), "查询此容器是否启动成功...", 0);
        if (!DockerClientUtil.startContainer(machine.getMachineIp(), app.getContainerId())) {
            action.setStatus(1);
            action.setAction(1);
            action.setActionName("启动失败");
            actionService.updateById(action);
            return TResult.failure("启动失败");
        }

        if (!DockerClientUtil.isRunning(machine.getMachineIp(), app.getContainerId())) {
            action.setStatus(1);
            action.setAction(1);
            action.setActionName("容器启动失败");
            actionService.updateById(action);
            return TResult.failure("启动失败");
        }

        action.setStatus(0);
        action.setAction(1);
        action.setActionName("容器启动成功");
        actionService.updateById(action);

        app.setStatus(1);
        appService.updateById(app);
        return TResult.success();
    }

    /**
     * 关闭容器
     *@author 卢越
     * @date 2018/7/20 16:30
     * @param appId
     * @return
     */
    @GetMapping("/stop/{appId}")
    public TResult stopContainer(@PathVariable Long appId) {
        Action action = InsertActionAndDetail.insertAction(appId, session);
        if (action == null) {
            return TResult.failure(TResultCode.BUSINESS_ERROR);
        }
        App app = appService.selectById(appId);
        InsertActionAndDetail.insertActionDetail(action.getActionId(), "查询此应用是否存在...", 0);
        if (app == null) {
            InsertActionAndDetail.insertActionDetail(action.getActionId(), "此应用不存在", 2);
            return TResult.failure(TResultCode.RESULE_DATA_NONE);
        }
        InsertActionAndDetail.insertActionDetail(action.getActionId(), "查询此机器是否存在...", 0);
        Machine machine = machineService.selectById(app.getMachineId());
        if (machine == null) {
            InsertActionAndDetail.insertActionDetail(action.getActionId(), "此机器不存在", 2);
            return TResult.failure(TResultCode.BUSINESS_ERROR);
        }

        InsertActionAndDetail.insertActionDetail(action.getActionId(), "查询此容器是否已经关闭...", 0);
        if (!DockerClientUtil.isRunning(machine.getMachineIp(), app.getContainerId())) {
            app.setStatus(0);
            appService.updateById(app);
            action.setStatus(1);
            action.setAction(4);
            action.setActionName("容器已经关闭");
            actionService.updateById(action);
            return TResult.failure("容器已关闭");
        }
        InsertActionAndDetail.insertActionDetail(action.getActionId(), "查询此容器是否关闭成功...", 0);
        if (!DockerClientUtil.stopContainer(machine.getMachineIp(), app.getContainerId())) {
            return TResult.failure("关闭失败");
        }

        app.setStatus(0);
        appService.updateById(app);
        action.setStatus(0);
        action.setAction(4);
        action.setActionName("容器关闭成功");
        actionService.updateById(action);
        return TResult.success();
    }

    /**
     * 容器重启
     *@author 卢越
     * @date 2018/7/20 16:30
     * @param appId
     * @return
     */
    @ApiOperation("容器重启")
    @GetMapping("/restart-container/{appId}")
    public TResult restartContainer(@PathVariable Long appId) {
        Action action = InsertActionAndDetail.insertAction(appId, session);
        if (action == null) {
            return TResult.failure(TResultCode.BUSINESS_ERROR);
        }
        App app = appService.selectById(appId);
        InsertActionAndDetail.insertActionDetail(action.getActionId(), "查询此机器是否存在...", 0);
        Machine machine = machineService.selectById(app.getMachineId());
        List<MarketApp> marketApps = marketAppService.selectList(new EntityWrapper<MarketApp>());
        InsertActionAndDetail.insertActionDetail(action.getActionId(), "查询该容器是否存在...", 0);
        if (app == null) {
            InsertActionAndDetail.insertActionDetail(action.getActionId(), "该容器不存在", 2);
            return TResult.failure("没有该容器");
        }

        List<AppVar> appVars = appVarService.selectList(new EntityWrapper<AppVar>().eq("app_id", appId));
        List<AppPort> appPorts = appPortService.selectList(new EntityWrapper<AppPort>().eq("app_id", appId));
        List<MountSettings> mountSettings = mountSettingsService.selectList(new EntityWrapper<MountSettings>().eq("app_id", appId));

        InsertActionAndDetail.insertActionDetail(action.getActionId(), "获得绑定端口和对外暴露的端口...", 0);
        //获得绑定端口和对外暴露的端口
        Set<String> exposePorts = new HashSet<>();
        Map<String, List<PortBinding>> portBinds = DockerClientUtil.getContainerPortBinds(appPorts, exposePorts);

        InsertActionAndDetail.insertActionDetail(action.getActionId(), "获得容器变量...", 0);
        //获得容器变量
        List<String> envs = DockerClientUtil.getContainerEnvs(appVars);

        InsertActionAndDetail.insertActionDetail(action.getActionId(), "获得挂载...", 0);
        //获得挂载
        List<String> mounts = DockerClientUtil.getContainerMounts(mountSettings);

        InsertActionAndDetail.insertActionDetail(action.getActionId(), "获得新镜相id...", 0);
        //获得新镜相id
        String newImageName = DockerClientUtil.getNewImage(machine.getMachineIp(), app.getContainerId());

        action.setAction(2);

        if (StringUtil.isEmpty(newImageName)) {
            action.setStatus(1);
            action.setActionName("重启失败");
            actionService.updateById(action);
            return TResult.failure("重启失败");
        }

        InsertActionAndDetail.insertActionDetail(action.getActionId(), "创建新容器...", 0);
        //创建新容器
        String newContainerId = DockerClientUtil.createNewContainer(machine.getMachineIp(), app.getContainerId(), newImageName, portBinds, exposePorts, envs, mounts, marketApps);

        InsertActionAndDetail.insertActionDetail(action.getActionId(), "新容器启动中...", 0);
        if (StringUtil.isEmpty(newContainerId)) {
            action.setStatus(1);
            action.setActionName("容器重启失败");
            actionService.updateById(action);
            return TResult.failure("重启失败");
        }

        if (!DockerClientUtil.isRunning(machine.getMachineIp(), newContainerId)) {
            app.setStatus(0);
            appService.updateById(app);

            action.setStatus(1);
            action.setActionName("容器创建成功，重启失败，请手动重启");
            actionService.updateById(action);
            return TResult.failure("容器创建成功，重启失败，请手动重启");
        }

        app.setContainerId(newContainerId);
        if (!appService.updateById(app)) {
            action.setStatus(1);
            action.setActionName("重启失败");
            actionService.updateById(action);
            return TResult.failure("重启失败");
        }

        action.setStatus(0);
        action.setActionName("重启成功");
        actionService.updateById(action);

        return TResult.success();
    }

    /**
     * 删除应用
     *@author 卢越
     * @date 2018/7/20 16:30
     * @param appId
     * @return
     */
    @DeleteMapping("{appId}")
    public TResult deleteApp(@PathVariable(required = true) Long appId) {
        App app = appService.selectById(appId);
        if (app == null) {
            return TResult.failure(TResultCode.RESULE_DATA_NONE);
        }

        Machine machine = machineService.selectById(app.getMachineId());
        if (machine == null) {
            return TResult.failure(TResultCode.BUSINESS_ERROR);
        }

        if (!DockerClientUtil.removeContainer(machine.getMachineIp(), app.getContainerId())) {
            return TResult.failure(TResultCode.BUSINESS_ERROR);
        }
        if (!appService.deleteById(app.getAppId())) {
            return TResult.failure(TResultCode.BUSINESS_ERROR);
        }

        return TResult.success();
    }

    /**
     * 批量开启
     * @author 卢越
     * @date 2018/7/20 16:30
     * @param appIds
     * @return
     */
    @PostMapping("/batch-start")
    public TResult batchStartApps(Long[] appIds) {
        List<App> apps = appService.isDataRight(appIds);
        if (apps == null) {
            return TResult.failure(TResultCode.DATA_IS_WRONG);
        }

        return appService.batchStartContainer(apps);
    }

    /**
     * 批量关闭
     * @author 卢越
     * @date 2018/7/20 16:30
     * @param appIds
     * @return
     */
    @PostMapping("/batch-stop")
    public TResult batchStopApps(Long[] appIds) {
        List<App> apps = appService.isDataRight(appIds);
        if (apps == null) {
            return TResult.failure(TResultCode.DATA_IS_WRONG);
        }

        return appService.batchStopContainer(apps);
    }

    /**
     * 批量重启
     * @author 卢越
     * @date 2018/7/20 16:30
     * @param appIds
     * @return
     */
    @PostMapping("/batch-restart")
    public TResult batchReStartApps(Long[] appIds) {
        List<App> apps = appService.isDataRight(appIds);
        if (apps == null) {
            return TResult.failure(TResultCode.DATA_IS_WRONG);
        }

        return appService.batchReStartContainer(apps);
    }

    /**
     * 导出挂载卷地址
     *@author 卢越
     * @date 2018/7/20 16:30
     * @param appId
     * @param containerMount
     * @return
     */
    @PostMapping("/mount-export")
    public TResult getMountExportUrl(Long appId, String containerMount) {
        App app = appService.selectById(appId);
        if (app == null) {
            return TResult.failure(TResultCode.DATA_IS_WRONG);
        }
        Machine machine = machineService.selectById(app.getMachineId());
        if (machine == null) {
            return TResult.failure(TResultCode.BUSINESS_ERROR);
        }

        return TResult.success(String.format(DockerConstants.mountUrl, machine.getMachineIp(), app.getContainerId(), containerMount));
    }

    /**
     * 导出容器地址
     * @author 卢越
     * @date 2018/7/20 16:30
     * @param appId
     * @return
     */
    @PostMapping("/container-export")
    public TResult getContainerExportUrl(Long appId) {
        App app = appService.selectById(appId);
        if (app == null) {
            return TResult.failure(TResultCode.DATA_IS_WRONG);
        }
        Machine machine = machineService.selectById(app.getMachineId());
        if (machine == null) {
            return TResult.failure(TResultCode.BUSINESS_ERROR);
        }

        return TResult.success(String.format(DockerConstants.containerUrl, machine.getMachineIp(), app.getContainerId()));
    }
}