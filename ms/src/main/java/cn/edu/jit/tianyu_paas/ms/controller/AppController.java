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
     *
     * @return
     */
    @GetMapping
    public TResult getApps() {
        List<User> users = userService.selectList(new EntityWrapper<User>());
        if (users == null)
            return TResult.failure(TResultCode.RESULE_DATA_NONE);
        for (User user : users) {
            List<App> apps = appService.selectList(new EntityWrapper<App>().eq("user_id", user.getUserId()));
            if (apps == null)
                return TResult.failure(TResultCode.RESULE_DATA_NONE);
            user.setApps(apps);
        }
        return TResult.success(users);
    }

    /**
     * 更新app
     *
     * @param app
     * @return
     * @author 倪龙康
     */
    @PutMapping
    public TResult updateApp(App app) {
        if (!appService.updateById(app))
            return TResult.failure(TResultCode.FAILURE);
        return TResult.success();
    }

    /**
     * 删除应用
     *
     * @param appId
     * @return
     * @author 倪龙康
     */
    @DeleteMapping("{appId}")
    public TResult deleteApp(@PathVariable Long appId) {
        if (!appService.deleteById(appId))
            return TResult.failure(TResultCode.FAILURE);
        return TResult.success();
    }
}
