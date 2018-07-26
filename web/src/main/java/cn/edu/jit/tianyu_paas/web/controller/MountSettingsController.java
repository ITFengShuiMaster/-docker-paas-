package cn.edu.jit.tianyu_paas.web.controller;


import cn.edu.jit.tianyu_paas.shared.entity.App;
import cn.edu.jit.tianyu_paas.shared.entity.MarketAppMount;
import cn.edu.jit.tianyu_paas.shared.entity.MountSettings;
import cn.edu.jit.tianyu_paas.shared.util.RegexUtil;
import cn.edu.jit.tianyu_paas.shared.util.TResult;
import cn.edu.jit.tianyu_paas.shared.util.TResultCode;
import cn.edu.jit.tianyu_paas.web.service.*;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;


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
     *@author 卢越
     * @date 2018/7/20 16:30
     * @param mountSettings
     * @return
     * @throws Exception
     */
    @ApiOperation("插入挂载")
    @PostMapping
    public TResult insertMountSetting(MountSettings mountSettings) throws Exception {
        App app = appService.selectById(mountSettings.getAppId());
        if (app == null) {
            return TResult.failure("没有该容器");
        }

        if (!RegexUtil.isRightPath(mountSettings.getServerMountName())) {
            return TResult.failure("挂载格式错误：" + mountSettings.getServerMountName());
        }

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
     *@author 卢越
     * @date 2018/7/20 16:30
     * @param appId
     * @return
     */
    @ApiOperation("获得挂载")
    @GetMapping("/{appId}")
    public TResult listMountSettings(@PathVariable(required = true) Long appId) {
        return TResult.success(mountSettingsService.selectList(new EntityWrapper<MountSettings>().eq("app_id", appId)));
    }

    /**
     * 删除挂载
     *@author 卢越
     * @date 2018/7/20 16:30
     * @param appId
     * @param persistentName
     * @return
     */
    @ApiOperation("删除挂载")
    @DeleteMapping
    public TResult deleteMountSettings(@RequestParam(required = true) Long appId, @RequestParam(required = true) String persistentName) {
        if (!mountSettingsService.delete(new EntityWrapper<MountSettings>().eq("app_id", appId).and().eq("persistent_name", persistentName))) {
            return TResult.failure(TResultCode.BUSINESS_ERROR);
        }
        return TResult.success();
    }
}

