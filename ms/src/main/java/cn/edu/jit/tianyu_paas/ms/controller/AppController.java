package cn.edu.jit.tianyu_paas.ms.controller;

import cn.edu.jit.tianyu_paas.ms.service.AppGroupService;
import cn.edu.jit.tianyu_paas.ms.service.AppService;
import cn.edu.jit.tianyu_paas.ms.service.UserService;
import cn.edu.jit.tianyu_paas.shared.entity.App;
import cn.edu.jit.tianyu_paas.shared.entity.AppGroup;
import cn.edu.jit.tianyu_paas.shared.entity.User;
import cn.edu.jit.tianyu_paas.shared.util.StringUtil;
import cn.edu.jit.tianyu_paas.shared.util.TResult;
import cn.edu.jit.tianyu_paas.shared.util.TResultCode;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    private final AppGroupService appGroupService;

    @Autowired
    public AppController(AppService appService, UserService userService, AppGroupService appGroupService) {
        this.appService = appService;
        this.userService = userService;
        this.appGroupService = appGroupService;
    }

    /**
     * 获取所有用户以及应用
     *
     * @return
     */
    @ApiOperation("获取所有用户以及应用")
    @GetMapping
    public TResult getApps(@RequestParam(defaultValue = "1") Integer current, @RequestParam(defaultValue = "7") Integer size) {
        Page<App> apps = appService.selectPage(new Page<>(current, size));
        List<App> appList = apps.getRecords();

        for (int i = 0; i < appList.size(); i++) {
            User user = userService.selectOne(new EntityWrapper<User>().eq("user_id", appList.get(i).getUserId()));
            AppGroup appGroup = appGroupService.selectById(appList.get(i).getAppGroupId());
            if (user == null) {
                appList.remove(i);
            } else {
                appList.get(i).setUsername(user.getName());
                appList.get(i).setGroupName(appGroup.getGroupName());
            }
        }

        return TResult.success(apps.setRecords(appList));
    }

    /**
     * 更新app
     *
     * @param app
     * @return
     * @author 倪龙康
     */
    @ApiOperation("更新app")
    @PutMapping
    public TResult updateApp(App app) {
        if (!appService.updateById(app)) {
            return TResult.failure(TResultCode.FAILURE);
        }
        return TResult.success();
    }

    /**
     * 删除应用
     *
     * @param appId
     * @return
     * @author 倪龙康
     */
    @ApiOperation("删除应用")
    @DeleteMapping("/{appId}")
    public TResult deleteApp(@PathVariable Long appId) {
        if (!appService.deleteById(appId)) {
            return TResult.failure(TResultCode.FAILURE);
        }
        return TResult.success();
    }

    /**
     * 根据手机号或邮箱查询app
     *
     * @param phone ""手机号
     * @param email ""邮箱
     * @return TResult
     * @author 卢越
     * @since 2018-07-01
     */
    @ApiOperation("根据手机号或邮箱查询app")
    @PostMapping("/by-phone-email")
    public TResult listAppsByPhoneAndEmail(String phone, String email) {
        User user = null;

        if (!StringUtil.isEmpty(phone)) {
            user = userService.selectOne(new EntityWrapper<User>().eq("phone", phone));
        } else if (!StringUtil.isEmpty(email)) {
            user = userService.selectOne(new EntityWrapper<User>().eq("email", email));
        } else if (!StringUtil.isEmpty(phone) && !StringUtil.isEmpty(email)) {
            user = userService.selectOne(new EntityWrapper<User>().eq("phone", phone).and().eq("email", email));
        }

        if (user == null) {
            return TResult.failure(TResultCode.RESULE_DATA_NONE);
        }

        List<App> apps = appService.selectList(new EntityWrapper<App>().eq("user_id", user.getUserId()));
        return TResult.success(apps);
    }

    /**
     * 根据app名模糊查询app
     *
     * @param name “容器名称”
     * @return TResult
     * @author 卢越
     * @since 2018-07-01
     */
    @ApiOperation("根据app名模糊查询app")
    @GetMapping("/contain")
    public TResult listAppByName(@RequestParam(defaultValue = "") String name) {
        return TResult.success(appService.selectList(new EntityWrapper<App>().like("name", name)));
    }
}
