package cn.edu.jit.tianyu_paas.web.controller;


import cn.edu.jit.tianyu_paas.shared.entity.App;
import cn.edu.jit.tianyu_paas.shared.entity.AppRely;
import cn.edu.jit.tianyu_paas.shared.util.TResult;
import cn.edu.jit.tianyu_paas.shared.util.TResultCode;
import cn.edu.jit.tianyu_paas.web.service.AppGroupService;
import cn.edu.jit.tianyu_paas.web.service.AppRelyService;
import cn.edu.jit.tianyu_paas.web.service.AppService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 卢越
 * @since 2018-07-21
 */
@RestController
@RequestMapping("/app-relys")
public class AppRelyController {
    private final AppRelyService appRelyService;
    private final AppService appService;
    private final AppGroupService appGroupService;
    private HttpSession session;

    @Autowired
    public AppRelyController(HttpSession session, AppRelyService appRelyService, AppService appService, AppGroupService appGroupService) {
        this.session = session;
        this.appRelyService = appRelyService;
        this.appService = appService;
        this.appGroupService = appGroupService;
    }

    /**
     * @param appId
     * @return
     * @author 卢越
     * @date 2018/7/20 16:30
     */
    @ApiOperation("根据appId获取依赖列表")
    @GetMapping("{appId}")
    public TResult listRelys(@PathVariable Long appId) {
        List<AppRely> relies = appRelyService.selectList(new EntityWrapper<AppRely>().eq("app_id", appId));
        for (int i = 0; i < relies.size(); i++) {
            relies.get(i).setRelyName(appService.selectById(relies.get(i).getRelyId()).getName());
            relies.get(i).setRelyGroupName(appGroupService.selectById(relies.get(i).getRelyGroupId()).getGroupName());
        }
        return TResult.success(relies);
    }

    /**
     * @param appRely
     * @return
     * @author 卢越
     * @date 2018/7/20 16:30
     */
    @ApiOperation("添加依赖")
    @PostMapping
    public TResult insertRelys(AppRely appRely) {
        appRely.setGmtCreate(new Date());
        if (appRelyService.insert(appRely)) {
            return TResult.success();
        }
        return TResult.failure(TResultCode.BUSINESS_ERROR);
    }

    /**
     * @param appRely
     * @return
     * @author 卢越
     * @date 2018/7/22 16:30
     */
    @ApiOperation("更新依赖")
    @PutMapping
    public TResult updateRelys(AppRely appRely) {
        if (appRelyService.updateById(appRely)) {
            return TResult.success();
        }
        return TResult.failure(TResultCode.BUSINESS_ERROR);
    }

    /**
     * @param appId
     * @param relyId
     * @return
     * @author 卢越
     * @date 2018/7/20 16:30
     */
    @ApiOperation("删除依赖")
    @DeleteMapping("{appId}")
    public TResult deleteRelys(@PathVariable Long appId, @RequestParam(required = true) Long relyId) {
        if (appRelyService.delete(new EntityWrapper<AppRely>().eq("app_id", appId).and().eq("rely_id", relyId))) {
            return TResult.success();
        }
        return TResult.failure(TResultCode.BUSINESS_ERROR);
    }

    /**
     * @param groupId
     * @return
     * @author 卢越
     * @date 2018/7/20 16:30
     */
    @ApiOperation("返回应用组内依赖关系图的邻接矩阵和名字")
    @GetMapping("/rely/{groupId}")
    public TResult getAdjacencyMatrix(@PathVariable Long groupId) {
        List<App> apps = appService.selectList(new EntityWrapper<App>().eq("app_group_id", groupId));

        Map<String, Object> res = new HashMap<>(16);
        Map<Long, Integer> appMap = new HashMap<>(16);
        String[] appNames = new String[apps.size()];
        for (int i = 0; i < apps.size(); i++) {
            appMap.put(apps.get(i).getAppId(), i);
            appNames[i] = apps.get(i).getName();
        }

        int[][] martix = new int[apps.size()][apps.size()];
        for (int i = 0; i < apps.size(); i++) {
            List<AppRely> appRelies = appRelyService.selectList(new EntityWrapper<AppRely>().eq("app_id", apps.get(i).getAppId()).and().eq("rely_group_id", groupId));
            for (AppRely ar : appRelies) {
                int relyIndex = appMap.get(ar.getRelyId());
                int appIndex = appMap.get(apps.get(i).getAppId());
                martix[appIndex][relyIndex] = martix[relyIndex][appIndex] = 1;
            }
        }

        res.put("appName", appNames);
        res.put("data", martix);

        return TResult.success(res);
    }
}

