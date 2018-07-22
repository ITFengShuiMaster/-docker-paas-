package cn.edu.jit.tianyu_paas.web.controller;

import cn.edu.jit.tianyu_paas.shared.entity.App;
import cn.edu.jit.tianyu_paas.shared.entity.Machine;
import cn.edu.jit.tianyu_paas.shared.util.DockerHelperUtil;
import cn.edu.jit.tianyu_paas.shared.util.TResult;
import cn.edu.jit.tianyu_paas.shared.util.TResultCode;
import cn.edu.jit.tianyu_paas.web.service.*;
import com.spotify.docker.client.LogStream;
import io.swagger.annotations.ApiOperation;
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
@RequestMapping("/app-log")
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
    private final AppVarService appVarService;
    private final AppPortService appPortService;
    private final MachineService machineService;

    @Autowired
    public AppLogController(AppService appService, AppInfoByCustomService appInfoByCustomService, HttpSession session, AppInfoByDemoService appInfoByDemoService, DemoService demoService, AppInfoByDockerImageService appInfoByDockerImageService, AppInfoByDockerRunService appInfoByDockerRunService, AppInfoByMarketService appInfoByMarketService, AppGroupService appGroupService, MarketAppService marketAppService, AppVarService appVarService, AppPortService appPortService, ActionService actionService, MachineService machineService) {
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
        this.machineService = machineService;
    }

    @ApiOperation("根据appId获取applog信息")
    @GetMapping("/{appId}")
    public TResult listAppLog(@PathVariable Long appId) {
        App app = appService.selectById(appId);
        if (app == null) {
            return TResult.failure("没有该容器");
        }
        Machine machine = machineService.selectById(app.getMachineId());
        if (machine == null) {
            return TResult.failure(TResultCode.BUSINESS_ERROR);
        }

        try {
            String reLogs = DockerHelperUtil.query(machine.getMachineIp(), docker ->
            {
                final String logs;
                try (LogStream stream = docker.logs(app.getContainerId(), com.spotify.docker.client.DockerClient.LogsParam.stdout(), com.spotify.docker.client.DockerClient.LogsParam.stderr())) {
                    logs = stream.readFully();
                }
                return logs;
            });

            return TResult.success(reLogs.split("\\n"));
        } catch (Exception e) {
            e.printStackTrace();
            return TResult.failure(TResultCode.BUSINESS_ERROR);
        }
    }
}
