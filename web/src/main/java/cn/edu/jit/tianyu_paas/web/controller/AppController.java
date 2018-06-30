package cn.edu.jit.tianyu_paas.web.controller;


import cn.edu.jit.tianyu_paas.shared.entity.App;
import cn.edu.jit.tianyu_paas.shared.entity.AppPort;
import cn.edu.jit.tianyu_paas.shared.entity.AppVar;
import cn.edu.jit.tianyu_paas.shared.entity.User;
import cn.edu.jit.tianyu_paas.shared.util.TResult;
import cn.edu.jit.tianyu_paas.shared.util.TResultCode;
import cn.edu.jit.tianyu_paas.web.global.Constants;
import cn.edu.jit.tianyu_paas.web.service.AppPortService;
import cn.edu.jit.tianyu_paas.web.service.AppService;
import cn.edu.jit.tianyu_paas.web.service.AppVarService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

/**
 * @author 倪龙康，卢越
 * @since 2018-06-29
 */

@RestController
@RequestMapping("/apps")
public class AppController {

    private AppVarService appVarService;
    private AppService appService;
    private AppPortService appPortService;

    @Autowired
    public AppController(AppVarService appVarService ,AppService appService, AppPortService appPortService) {
        this.appVarService = appVarService;
        this.appService = appService;
        this.appPortService = appPortService;
    }

    /**
     * 获取应用信息
     * @param app_id
     * @return
     */
    @GetMapping("{app_id}")
    public TResult getAppInfo(@PathVariable("app_id") Long app_id){
        App app = appService.selectById(app_id);
        return TResult.success(app.toString());
    }
    /**
     * 添加变量
     * @return
     */
    @PostMapping("vars")
    public TResult addVar(AppVar appVar){
        if (appVarService.selectCount(new EntityWrapper<AppVar>().eq("var_num",appVar.getVarName()))!=0) {
            return TResult.failure(TResultCode.DATA_ALREADY_EXISTED);
        }
        appVar.setGmtCreate(new Date());
        if(!appVarService.insert(appVar))
            return TResult.failure(TResultCode.FAILURE);
        return TResult.success();
    }

    /**
     * 修改变量
     * @param appVar
     * @return
     */
    @PutMapping("vars")
    public TResult updateVar(AppVar appVar){
        if(!appVarService.update(appVar, new EntityWrapper<AppVar>().eq("app_id", appVar.getAppId()).and().eq("var_name", appVar.getVarName())))
            return TResult.failure(TResultCode.BUSINESS_ERROR);
        return TResult.success();
    }

    /**
     * 删除变量
     * @param var_name
     * @return
     */
    @DeleteMapping("vars/{var_name}")
    public TResult deleteVar(@PathVariable("var_name") String var_name){
        if(!appVarService.delete(new EntityWrapper<AppVar>().eq("var_name",var_name)))
            return TResult.failure(TResultCode.BUSINESS_ERROR);
        return TResult.success();
    }
    /**
     * 获取变量相关信息
     * @param app_id
     * @return
     */
    @GetMapping("vars/{app_id}")
    public TResult getVarInfo(@PathVariable("app_id") Long app_id){
        List<AppVar> appVars = appVarService.selectList(new EntityWrapper<AppVar>().eq("app_id",app_id));
        if(appVars == null)
            return TResult.failure(TResultCode.RESULE_DATA_NONE);
        return TResult.success(appVars);
    }

    /**
     * 获取端口号信息
     * @param app_id
     * @return
     */
    @GetMapping("ports/{app_id}")
    public TResult getPortInfo(@PathVariable("app_id") Long app_id){
        AppPort appPort = appPortService.selectOne(new EntityWrapper<AppPort>().eq("app_id",app_id));
        if(appPort == null)
            return TResult.failure(TResultCode.RESULE_DATA_NONE);
        return TResult.success(appPort.toString());
    }

    /**
     * 新增端口
     * @param app_id
     * @param port
     * @param protocol
     * @return
     */
    @PostMapping("ports")
    public TResult addPort(Long app_id, Integer port, Integer protocol,
                           @RequestParam(required = false,defaultValue = "0") Integer is_inside_open,
                           @RequestParam(required = false,defaultValue = "xxxxxx") String inside_access_url,
                           @RequestParam(required = false,defaultValue = "xxxxxx") String inside_alias,
                           @RequestParam(required = false,defaultValue = "0") Integer is_outside_open,
                           @RequestParam(required = false,defaultValue = "xxxxxx") String outside_access_url
                           ){
        if (appPortService.selectCount(new EntityWrapper<AppPort>().eq("port",port))!=0) {
            return TResult.failure(TResultCode.DATA_ALREADY_EXISTED);
        }
        AppPort appPort = new AppPort();
        appPort.setAppId(app_id);
        appPort.setPort(port);
        appPort.setProtocol(protocol);
        appPort.setGmtModified(new Date());
        appPort.setGmtCreate(new Date());
        appPort.setIsInsideOpen(is_inside_open);
        appPort.setIsOutsideOpen(is_outside_open);
        appPort.setInsideAccessUrl(inside_access_url);
        appPort.setOutsideAccessUrl(outside_access_url);
        appPort.setInsideAlias(inside_alias);
        if(!appPortService.insert(appPort))
            return TResult.failure(TResultCode.FAILURE);
        return TResult.success();
    }

    /**
     * 更新端口相关信息
     * @param appPort
     * @return
     */
    @PutMapping("ports")
    public TResult updatePort(AppPort appPort){
        if(!appPortService.update(appPort, new EntityWrapper<AppPort>().eq("app_id", appPort.getAppId()).and().eq("port", appPort.getPort())))
            return TResult.failure(TResultCode.BUSINESS_ERROR);
        return TResult.success();
    }

    /**
     * 删除端口
     * @param port
     * @return
     */
    @DeleteMapping("ports/{port}")
    public TResult deletePort(@PathVariable("port") Integer port){

        if(!appPortService.delete(new EntityWrapper<AppPort>().eq("port",port)))
            return TResult.failure(TResultCode.BUSINESS_ERROR);
        return TResult.success();

    }
}
