package cn.edu.jit.tianyu_paas.web.controller;

import cn.edu.jit.tianyu_paas.shared.entity.App;
import cn.edu.jit.tianyu_paas.shared.entity.AppGroup;
import cn.edu.jit.tianyu_paas.shared.util.TResult;
import cn.edu.jit.tianyu_paas.shared.util.TResultCode;
import cn.edu.jit.tianyu_paas.web.global.Constants;
import cn.edu.jit.tianyu_paas.web.service.AppGroupService;
import cn.edu.jit.tianyu_paas.web.service.AppService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 倪龙康
 * @since 2018-06-29
 */
@RequestMapping("/app-groups")
@RestController
public class AppGroupController {

    private AppGroupService appGroupService;
    private AppService appService;
    private HttpSession session;

    @Autowired
    public AppGroupController(AppGroupService appGroupService, AppService appService, HttpSession session) {
        this.appGroupService = appGroupService;
        this.appService = appService;
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
    @PostMapping
    public TResult groupCreate(String groupName, String compose) {
        Long userId = (Long) session.getAttribute(Constants.SESSION_KEY_USER_ID);
        if (appGroupService.selectOne(new EntityWrapper<AppGroup>().eq("group_name", groupName).eq("user_id", userId)) != null)
            return TResult.failure(TResultCode.DATA_ALREADY_EXISTED);
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
    @PutMapping
    public TResult updateGroup(AppGroup appGroup) {
        Long userId = (Long) session.getAttribute(Constants.SESSION_KEY_USER_ID);
        if (!appGroupService.update(appGroup, new EntityWrapper<AppGroup>(
        ).eq("app_id", appGroup.getAppGroupId()).and().eq("user_id", userId)))
            return TResult.failure(TResultCode.FAILURE);
        return TResult.success();
    }

    /**
     * 获取用户所有用户组以及用户组中的所有应用
     *
     * @return
     * @author 倪龙康
     */
    @GetMapping
    public TResult showGroupInfo() {
        Long userId = (Long) session.getAttribute(Constants.SESSION_KEY_USER_ID);
        Map<AppGroup, List<App>> maps = new HashMap<>();
        List<AppGroup> groups = appGroupService.selectList(new EntityWrapper<AppGroup>().eq("user_id", userId));
        for (AppGroup group : groups) {
            List<App> apps = appService.selectList(new EntityWrapper<App>().eq("app_group_id", group.getAppGroupId()));
            maps.put(group, apps);
        }
        return TResult.success(maps);
    }

    /**
     * 删除应用组
     *
     * @param appGroupId
     * @return
     * @author 倪龙康
     */
    @DeleteMapping("{appGroupId}")
    public TResult deleteGroup(@PathVariable Long appGroupId) {
        Long userId = (Long) session.getAttribute(Constants.SESSION_KEY_USER_ID);
        if (!appGroupService.delete(new EntityWrapper<AppGroup>().eq("app_group_id", appGroupId).eq("user_id", userId)))
            return TResult.failure(TResultCode.BUSINESS_ERROR);
        return TResult.success();
    }
}
