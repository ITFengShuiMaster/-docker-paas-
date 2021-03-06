package cn.edu.jit.tianyu_paas.web.controller;

import cn.edu.jit.tianyu_paas.shared.entity.App;
import cn.edu.jit.tianyu_paas.shared.entity.AppGroup;
import cn.edu.jit.tianyu_paas.shared.util.DockerClientUtil;
import cn.edu.jit.tianyu_paas.shared.util.TResult;
import cn.edu.jit.tianyu_paas.shared.util.TResultCode;
import cn.edu.jit.tianyu_paas.web.global.Constants;
import cn.edu.jit.tianyu_paas.web.service.AppGroupService;
import cn.edu.jit.tianyu_paas.web.service.AppService;
import cn.edu.jit.tianyu_paas.web.service.MachineService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;


/**
 * @author 倪龙康
 * @since 2018-06-29
 */
@RestController
@RequestMapping("/app-groups")
public class AppGroupController {

    private AppGroupService appGroupService;
    private AppService appService;
    private final MachineService machineService;
    private HttpSession session;

    @Autowired
    public AppGroupController(AppGroupService appGroupService, AppService appService, MachineService machineService, HttpSession session) {
        this.appGroupService = appGroupService;
        this.appService = appService;
        this.machineService = machineService;
        this.session = session;
    }

    /**
     * 创建应用组
     *
     * @param groupName
     * @param compose
     * @return
     * @author 倪龙康
     */
    @ApiOperation("创建应用组")
    @PostMapping
    public TResult groupCreate(String groupName, String compose) {
        Long userId = (Long) session.getAttribute(Constants.SESSION_KEY_USER_ID);
        if (appGroupService.selectOne(new EntityWrapper<AppGroup>().eq("group_name", groupName).eq("user_id", userId)) != null) {
            return TResult.failure(TResultCode.DATA_ALREADY_EXISTED);
        }
        AppGroup appGroup = new AppGroup();
        appGroup.setUserId(userId);
        appGroup.setGmtCreate(new Date());
        appGroup.setGroupName(groupName);
        appGroup.setCompose(compose);
        if (!appGroupService.insert(appGroup)) {
            return TResult.failure(TResultCode.FAILURE);
        }
        return TResult.success(appGroup.getAppGroupId());
    }

    /**
     * 修改组名
     *
     * @param appGroup
     * @return
     * @author 倪龙康
     */
    @ApiOperation("修改组名")
    @PutMapping
    public TResult updateGroup(AppGroup appGroup) {
        Long userId = (Long) session.getAttribute(Constants.SESSION_KEY_USER_ID);
        if (!appGroupService.update(appGroup, new EntityWrapper<AppGroup>(
        ).eq("app_group_id", appGroup.getAppGroupId()).and().eq("user_id", userId))) {
            return TResult.failure(TResultCode.FAILURE);
        }
        return TResult.success();
    }

    /**
     * 获取所有组的信息
     *
     * @return
     */
    @ApiOperation("获取所有组的信息")
    @GetMapping
    public TResult listGroupsInfo() {
        Long userId = (Long) session.getAttribute(Constants.SESSION_KEY_USER_ID);
        List<AppGroup> appGroups = appGroupService.selectList(new EntityWrapper<AppGroup>().eq("user_id", userId));
        /*AppGroup appGroup = new AppGroup();
        appGroup.setAppGroupId((long) 0);
        appGroup.setGroupName("未分组");
        appGroup.setGmtCreate(new Date());
        appGroup.setUserId(userId);
        appGroups.add(appGroup);*/
        return TResult.success(appGroups);
    }

    /**
     * 根据appGroupId来获取apps
     *
     * @param appGroupId
     * @return
     * @author 倪龙康
     */
    @ApiOperation("根据appGroupId来获取app")
    @GetMapping("/{appGroupId}")
    public TResult listGroupAppsInfo(@PathVariable long appGroupId) {
        Long userId = (Long) session.getAttribute(Constants.SESSION_KEY_USER_ID);
        List<App> apps = appService.selectList(new EntityWrapper<App>().eq("app_group_id", appGroupId).eq("user_id", userId));
        return TResult.success(apps);
    }
    /**
     * 获取用户所有用户组以及用户组中的所有应用
     *
     * @return
     * @author 倪龙康
     */
    @ApiOperation("获取所有组的信息")
    @GetMapping("/groups-apps")
    public TResult showGroupAndAppInfo() {
        Long userId = (Long) session.getAttribute(Constants.SESSION_KEY_USER_ID);
        List<AppGroup> groups = appGroupService.selectList(new EntityWrapper<AppGroup>().eq("user_id", userId));
        for (AppGroup group : groups) {
            List<App> apps = appService.selectList(new EntityWrapper<App>().eq("app_group_id", group.getAppGroupId()));
            group.setApps(apps);
        }
        return TResult.success(groups);
    }

    /**
     * 删除应用组
     *
     * @param appGroupId
     * @return
     * @author 倪龙康
     */
    @ApiOperation("删除应用组")
    @DeleteMapping("/{appGroupId}")
    public TResult deleteGroup(@PathVariable Long appGroupId) {
        Long userId = (Long) session.getAttribute(Constants.SESSION_KEY_USER_ID);

        List<App> apps = appService.selectList(new EntityWrapper<App>().eq("app_group_id", appGroupId));
        for (int i = 0; i < apps.size(); i++) {
            apps.get(i).setAppGroupId(Long.parseLong("0"));
            String ip = machineService.selectById(apps.get(i).getMachineId()).getMachineIp();
            if (DockerClientUtil.isRunning(ip, apps.get(i).getContainerId())) {
                DockerClientUtil.stopContainer(ip, apps.get(i).getContainerId());
            }
        }
        if (!appService.updateBatchById(apps)) {
            return TResult.failure(TResultCode.BUSINESS_ERROR);
        }

        if (!appGroupService.delete(new EntityWrapper<AppGroup>().eq("app_group_id", appGroupId).eq("user_id", userId))) {
            return TResult.failure(TResultCode.BUSINESS_ERROR);
        }

        return TResult.success();
    }
}
