package cn.edu.jit.tianyu_paas.web.controller;

import cn.edu.jit.tianyu_paas.shared.entity.AppPort;
import cn.edu.jit.tianyu_paas.shared.util.TResult;
import cn.edu.jit.tianyu_paas.shared.util.TResultCode;
import cn.edu.jit.tianyu_paas.web.service.AppPortService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * @author 倪龙康
 * @since 2018-06-30
 */
@RestController
@RequestMapping("/app-ports")
public class AppPortController {

    private final AppPortService appPortService;

    @Autowired
    public AppPortController(AppPortService appPortService) {
        this.appPortService = appPortService;
    }

    /**
     * 获取端口号信息
     * @author 倪龙康
     * @param appId
     * @return
     */
    @GetMapping("{appId}")
    public TResult getPortInfo(@PathVariable Long appId){
        AppPort appPort = appPortService.selectOne(new EntityWrapper<AppPort>().eq("app_id",appId));
        if(appPort == null)
            return TResult.failure(TResultCode.RESULE_DATA_NONE);
        return TResult.success(appPort.toString());
    }

    /**
     * 新增端口
     * @author 倪龙康
     * @param appPort
     * @return
     */
    @PostMapping
    public TResult addPort(AppPort appPort){
        if (appPortService.selectCount(new EntityWrapper<AppPort>().eq("port",appPort.getPort()))!=0) {
            return TResult.failure(TResultCode.DATA_ALREADY_EXISTED);
        }
        appPort.setGmtModified(new Date());
        appPort.setGmtCreate(new Date());
        if(!appPortService.insert(appPort))
            return TResult.failure(TResultCode.FAILURE);
        return TResult.success();
    }
    //TODO 端口暂时只有这四个数据可以插入，其余的等docker再放

    /**
     * 更新端口相关信息
     * @author 倪龙康
     * @param appPort
     * @return
     */
    @PutMapping
    public TResult updatePort(AppPort appPort){
        appPort.setGmtModified(new Date());
        if(!appPortService.update(appPort, new EntityWrapper<AppPort>().eq("app_id", appPort.getAppId()).and().eq("port", appPort.getPort())))
            return TResult.failure(TResultCode.BUSINESS_ERROR);
        return TResult.success();
    }

    /**
     * 删除端口
     * @author 倪龙康
     * @param port
     * @return
     */
    @DeleteMapping("{port}")
    public TResult deletePort(@PathVariable Integer port) {

        if (!appPortService.delete(new EntityWrapper<AppPort>().eq("port", port)))
            return TResult.failure(TResultCode.BUSINESS_ERROR);
        return TResult.success();
    }
}
