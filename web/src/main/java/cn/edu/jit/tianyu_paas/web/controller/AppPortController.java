package cn.edu.jit.tianyu_paas.web.controller;

import cn.edu.jit.tianyu_paas.shared.entity.AppPort;
import cn.edu.jit.tianyu_paas.shared.util.TResult;
import cn.edu.jit.tianyu_paas.shared.util.TResultCode;
import cn.edu.jit.tianyu_paas.web.service.AppPortService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

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
     *
     * @param appId
     * @return
     * @author 倪龙康
     */
    @ApiOperation("获取端口号信息")
    @GetMapping("/{appId}")
    public TResult getPortInfo(@PathVariable Long appId) {
        List<AppPort> appPorts = appPortService.selectList(new EntityWrapper<AppPort>().eq("app_id", appId));
        if (appPorts == null) {
            return TResult.failure(TResultCode.RESULE_DATA_NONE);
        }
        return TResult.success(appPorts);
    }

    /**
     * 新增端口
     *
     * @param appPort
     * @return
     * @author 倪龙康
     */
    @ApiOperation("新增端口")
    @PostMapping
    public TResult addPort(AppPort appPort) {
        if (appPortService.selectCount(new EntityWrapper<AppPort>().eq("port", appPort.getHostPort()).eq("app_id", appPort.getAppId())) != 0) {
            return TResult.failure(TResultCode.DATA_ALREADY_EXISTED);
        }
        appPort.setGmtModified(new Date());
        appPort.setGmtCreate(new Date());
        if (!appPortService.insert(appPort)) {
            return TResult.failure(TResultCode.FAILURE);
        }
        return TResult.success();
    }
    //TODO 端口暂时只有这四个数据可以插入，其余的等docker再放

    /**
     * 更新端口相关信息
     *
     * @param appPort
     * @return
     * @author 倪龙康
     */
    @ApiOperation("更新端口相关信息")
    @PutMapping
    public TResult updatePort(AppPort appPort) {
        appPort.setGmtModified(new Date());
        if (!appPortService.update(appPort, new EntityWrapper<AppPort>().eq("app_id", appPort.getAppId()).and().eq("port", appPort.getHostPort()))) {
            return TResult.failure(TResultCode.BUSINESS_ERROR);
        }
        return TResult.success();
    }

    /**
     * 删除端口
     *
     * @param appId
     * @param port
     * @return
     * @author 倪龙康
     */
    @ApiOperation("删除端口")
    @DeleteMapping("/{appId}")
    public TResult deletePort(@PathVariable long appId, Integer port) {
        if (!appPortService.delete(new EntityWrapper<AppPort>().eq("port", port).eq("app_id", appId))) {
            return TResult.failure(TResultCode.BUSINESS_ERROR);
        }
        return TResult.success();
    }
}
