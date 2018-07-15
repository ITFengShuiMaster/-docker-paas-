package cn.edu.jit.tianyu_paas.web.controller;


import cn.edu.jit.tianyu_paas.shared.entity.*;
import cn.edu.jit.tianyu_paas.shared.util.DockerClientUtil;
import cn.edu.jit.tianyu_paas.shared.util.StringUtil;
import cn.edu.jit.tianyu_paas.shared.util.TResult;
import cn.edu.jit.tianyu_paas.shared.util.TResultCode;
import cn.edu.jit.tianyu_paas.web.service.*;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.spotify.docker.client.messages.PortBinding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.*;


/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 卢越
 * @since 2018-07-13
 */
@RestController
@RequestMapping("/mountSettings")
public class MountSettingsController {

    private final MountSettingsService mountSettingsService;
    private final AppService appService;
    private final AppPortService appPortService;
    private final AppVarService appVarService;
    private final MarketAppService marketAppService;
    private final MarketAppMountService marketAppMountService;
    private HttpSession session;

    @Autowired
    public MountSettingsController(HttpSession session, MountSettingsService mountSettingsService, AppService appService, AppPortService appPortService, AppVarService appVarService, MarketAppService marketAppService, MarketAppMountService marketAppMountService) {
        this.session = session;
        this.mountSettingsService = mountSettingsService;
        this.appService = appService;
        this.appPortService = appPortService;
        this.appVarService = appVarService;
        this.marketAppService = marketAppService;
        this.marketAppMountService = marketAppMountService;
    }

    private boolean isMount(List<MarketAppMount> marketAppMounts, String mountName) {
        for (MarketAppMount ma : marketAppMounts) {
            if (mountName.equals(ma.getMountName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 插入挂载
     *
     * @param mountSettings
     * @return
     * @throws Exception
     */
    @PostMapping
    public TResult insertMountSetting(MountSettings mountSettings) throws Exception {
        App app = appService.selectById(mountSettings.getAppId());
        if (app == null) {
            return TResult.failure("没有该容器");
        }

        // TODO 还需要判断宿主机挂载的目录是否符合格式
        List<MarketAppMount> marketAppMounts = marketAppMountService.selectList(new EntityWrapper<MarketAppMount>().eq("market_app_id", app.getMarketAppId()));
        if (!isMount(marketAppMounts, mountSettings.getContainerMountName())) {
            return TResult.failure("不能挂载这个目录/文件" + mountSettings.getContainerMountName());
        }

        if (mountSettingsService.selectOne(new EntityWrapper<MountSettings>().eq("app_id", mountSettings.getAppId()).and().eq("container_mount_name", mountSettings.getContainerMountName())) != null) {
            return TResult.failure("不允许重复挂载" + mountSettings.getContainerMountName());
        }

        if (mountSettingsService.selectOne(new EntityWrapper<MountSettings>().eq("server_mount_name", mountSettings.getServerMountName())) != null) {
            return TResult.failure("该挂载点已占用" + mountSettings.getServerMountName());
        }

        if (mountSettingsService.selectOne(new EntityWrapper<MountSettings>().eq("app_id", mountSettings.getAppId()).and().eq("persistent_name", mountSettings.getPersistentName())) != null) {
            return TResult.failure("持久化名称已存在" + mountSettings.getPersistentName());
        }

        mountSettings.setGmtCreate(new Date());
        if (!mountSettingsService.insert(mountSettings)) {
            return TResult.failure(TResultCode.BUSINESS_ERROR);
        }

        return TResult.success();
    }

    /**
     * 获得容器挂载信息
     *
     * @param appId
     * @return
     */
    @GetMapping("/{appId}")
    public TResult listMountSettings(@PathVariable(required = true) Long appId) {
        return TResult.success(mountSettingsService.selectList(new EntityWrapper<MountSettings>().eq("app_id", appId)));
    }

    /**
     * 删除挂载
     *
     * @param appId
     * @param persistentName
     * @return
     */
    @DeleteMapping
    public TResult deleteMountSettings(@RequestParam(required = true) Long appId, @RequestParam(required = true) String persistentName) {
        if (!mountSettingsService.delete(new EntityWrapper<MountSettings>().eq("app_id", appId).and().eq("persistent_name", persistentName))) {
            return TResult.failure(TResultCode.BUSINESS_ERROR);
        }
        return TResult.success();
    }

    /**
     * 容器重启
     *
     * @param appId
     * @return
     * @throws Exception
     */
    @GetMapping("/restart-container/{appId}")
    public TResult restartContainer(@PathVariable(required = true) Long appId) throws Exception {
        App app = appService.selectById(appId);

        if (app == null) {
            return TResult.failure("没有该容器");
        }

        List<AppVar> appVars = appVarService.selectList(new EntityWrapper<AppVar>().eq("app_id", appId));
        List<AppPort> appPorts = appPortService.selectList(new EntityWrapper<AppPort>().eq("app_id", appId));
        List<MountSettings> mountSettings = mountSettingsService.selectList(new EntityWrapper<MountSettings>().eq("app_id", appId));

        //获得绑定端口和对外暴露的端口
        Set<String> exposePorts = new HashSet<>();
        Map<String, List<PortBinding>> portBinds = DockerClientUtil.getContainerPortBinds(appPorts, exposePorts);

        //获得容器变量
        List<String> envs = DockerClientUtil.getContainerEnvs(appVars);

        //获得挂载
        List<String> mounts = DockerClientUtil.getContainerMounts(mountSettings);

        //获得新镜相id
        String newImageName = DockerClientUtil.getNewImage(app.getContainerId());

        if (StringUtil.isEmpty(newImageName)) {
            return TResult.failure("重启失败");
        }

        //创建新容器
        String newContainerId = DockerClientUtil.createNewContainer(app.getContainerId(), newImageName, portBinds, exposePorts, envs, mounts);

        if (StringUtil.isEmpty(newContainerId)) {
            return TResult.failure("重启失败");
        }

        app.setContainerId(newContainerId);
        if (!appService.updateById(app)) {
            return TResult.failure("重启失败");
        }

        return TResult.success();
    }

}

