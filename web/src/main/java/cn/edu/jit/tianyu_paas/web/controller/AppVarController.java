package cn.edu.jit.tianyu_paas.web.controller;

import cn.edu.jit.tianyu_paas.shared.entity.AppVar;
import cn.edu.jit.tianyu_paas.shared.util.TResult;
import cn.edu.jit.tianyu_paas.shared.util.TResultCode;
import cn.edu.jit.tianyu_paas.web.service.AppVarService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

/**
 * @author 倪龙康
 * @since 2018-06-30
 */
@RestController
@RequestMapping("/app-vars")
public class AppVarController {

    private final AppVarService appVarService;
    private final HttpSession session;
    @Autowired
    public AppVarController(AppVarService appVarService, HttpSession session) {
        this.appVarService = appVarService;
        this.session = session;
    }

    /**
     * 添加变量
     * @author 倪龙康
     * @return
     */
    @PostMapping
    public TResult addVar(AppVar appVar){
        if (appVarService.selectCount(new EntityWrapper<AppVar>().eq("var_name",appVar.getVarName()))!=0) {
            return TResult.failure(TResultCode.DATA_ALREADY_EXISTED);
        }
        appVar.setGmtCreate(new Date());
        if(!appVarService.insert(appVar))
            return TResult.failure(TResultCode.FAILURE);
        return TResult.success();
    }

    /**
     * 修改变量
     * @author 倪龙康
     * @param appVar
     * @return
     */
    @PutMapping
    public TResult updateVar(AppVar appVar){
        if(!appVarService.update(appVar, new EntityWrapper<AppVar>().eq("app_id", appVar.getAppId()).and().eq("var_name", appVar.getVarName())))
            return TResult.failure(TResultCode.BUSINESS_ERROR);
        return TResult.success();
    }

    /**
     * 删除变量
     * @author 倪龙康
     * @param varName
     * @return
     */
    @DeleteMapping("/{varName}")
    public TResult deleteVar(@PathVariable String varName){
        if(!appVarService.delete(new EntityWrapper<AppVar>().eq("var_name",varName)))
            return TResult.failure(TResultCode.BUSINESS_ERROR);
        return TResult.success();
    }
    /**
     * 获取变量相关信息
     * @author 倪龙康
     * @param appId
     * @return
     */
    @GetMapping("/{appId}")
    public TResult getVarInfo(@PathVariable Long appId){
        List<AppVar> appVars = appVarService.selectList(new EntityWrapper<AppVar>().eq("app_id",appId));
        if(appVars == null)
            return TResult.failure(TResultCode.RESULE_DATA_NONE);
        return TResult.success(appVars);
    }
}
