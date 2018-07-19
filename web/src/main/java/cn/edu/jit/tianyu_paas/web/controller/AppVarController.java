package cn.edu.jit.tianyu_paas.web.controller;

import cn.edu.jit.tianyu_paas.shared.entity.App;
import cn.edu.jit.tianyu_paas.shared.entity.AppVar;
import cn.edu.jit.tianyu_paas.shared.util.RegexUtil;
import cn.edu.jit.tianyu_paas.shared.util.TResult;
import cn.edu.jit.tianyu_paas.shared.util.TResultCode;
import cn.edu.jit.tianyu_paas.web.service.AppService;
import cn.edu.jit.tianyu_paas.web.service.AppVarService;
import cn.edu.jit.tianyu_paas.web.service.MarketAppVarService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import io.swagger.annotations.ApiOperation;
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
    private final MarketAppVarService marketAppVarService;
    private final AppService appService;
    private final HttpSession session;
    @Autowired
    public AppVarController(AppVarService appVarService, MarketAppVarService marketAppVarService, AppService appService, HttpSession session) {
        this.appVarService = appVarService;
        this.marketAppVarService = marketAppVarService;
        this.appService = appService;
        this.session = session;
    }

    /**
     * 添加变量
     * @author 倪龙康
     * @return
     */
    @ApiOperation("添加变量")
    @PostMapping
    public TResult addVar(AppVar appVar){
        App app = appService.selectById(appVar.getAppId());

        if (app == null) {
            return TResult.failure(TResultCode.RESULE_DATA_NONE);
        }

        if (appVarService.selectCount(new EntityWrapper<AppVar>().eq("var_name",appVar.getVarName()).eq("app_id",appVar.getAppId()))!=0) {
            return TResult.failure(TResultCode.DATA_ALREADY_EXISTED);
        }

        if (!RegexUtil.isRightVar(appVar.getVarName())) {
            return TResult.failure("不是一个合法的变量格式");
        }

        appVar.setVarExplain("xxx");
        appVar.setGmtCreate(new Date());
        if(!appVarService.insert(appVar)) {
            return TResult.failure(TResultCode.FAILURE);
        }
        return TResult.success();
    }

    /**
     * 修改变量
     * @author 倪龙康
     * @param appVar
     * @return
     */
    @ApiOperation("修改变量")
    @PutMapping
    public TResult updateVar(AppVar appVar){
        if(!appVarService.update(appVar, new EntityWrapper<AppVar>().eq("app_id", appVar.getAppId()).and().eq("var_name", appVar.getVarName()))) {
            return TResult.failure(TResultCode.BUSINESS_ERROR);
        }
        return TResult.success();
    }

    /**
     * 删除变量
     * @author 倪龙康
     * @param varName
     * @return
     */
    @ApiOperation("删除变量")
    @DeleteMapping("/{appId}")
    public TResult deleteVar(@PathVariable long appId, String varName){
        if(!appVarService.delete(new EntityWrapper<AppVar>().eq("app_id",appId).eq("var_name",varName))) {
            return TResult.failure(TResultCode.BUSINESS_ERROR);
        }
        return TResult.success();
    }
    /**
     * 获取变量相关信息
     * @author 倪龙康
     * @param appId
     * @return
     */
    @ApiOperation("获取变量相关信息")
    @GetMapping("/{appId}")
    public TResult getVarInfo(@PathVariable Long appId){
        List<AppVar> appVars = appVarService.selectList(new EntityWrapper<AppVar>().eq("app_id",appId));
        if(appVars == null) {
            return TResult.failure(TResultCode.RESULE_DATA_NONE);
        }
        return TResult.success(appVars);
    }
}
