package cn.edu.jit.tianyu_paas.ms.controller;

import cn.edu.jit.tianyu_paas.ms.service.AppService;
import cn.edu.jit.tianyu_paas.ms.service.UserService;
import cn.edu.jit.tianyu_paas.shared.entity.App;
import cn.edu.jit.tianyu_paas.shared.entity.User;
import cn.edu.jit.tianyu_paas.shared.util.TResult;
import cn.edu.jit.tianyu_paas.shared.util.TResultCode;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author 倪龙康
 * @since 2018-07-01
 */
@RequestMapping("/apps")
@RestController
public class AppController {

    private final AppService appService;
    private final UserService userService;

    @Autowired
    public AppController(AppService appService, UserService userService) {
        this.appService = appService;
        this.userService = userService;
    }

    //TODO 创建应用等普通用户那里一起补充

    /**
     * 获取所有用户以及应用
     * @param page
     * @return
     */
    @GetMapping
    public TResult getApps(Pagination page){
        int num = 1;
        Page<App> apps = appService.selectPage( new Page<>(page.getCurrent(),page.getSize()),
                new EntityWrapper<App>().orderBy("gmt_create",false));
        List<App> appList = apps.getRecords();
        LinkedHashMap<String, App> maps = new LinkedHashMap<>();
        for(App app : appList){
            User user =userService.selectById(app.getUserId());
            maps.put(String.valueOf(num)+":"+user.getName(),app);
            num++;
        }
        return TResult.success(maps);
    }

    /**
     * 更新app
     * @author 倪龙康
     * @param app
     * @return
     */
    @PutMapping
    public TResult updateApp(App app){
        if(!appService.updateById(app))
            return TResult.failure(TResultCode.FAILURE);
        return TResult.success();
    }

    /**
     * 删除应用
     * @author 倪龙康
     * @param appId
     * @return
     */
    @DeleteMapping("{appId}")
    public TResult deleteApp(@PathVariable Long appId){
        if(!appService.deleteById(appId))
            return TResult.failure(TResultCode.FAILURE);
        return TResult.success();
    }
}
