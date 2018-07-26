package cn.edu.jit.tianyu_paas.web.controller;

import cn.edu.jit.tianyu_paas.shared.entity.AppPort;
import cn.edu.jit.tianyu_paas.shared.entity.Machine;
import cn.edu.jit.tianyu_paas.shared.entity.MachinePort;
import cn.edu.jit.tianyu_paas.shared.util.TResult;
import cn.edu.jit.tianyu_paas.shared.util.TResultCode;
import cn.edu.jit.tianyu_paas.web.service.AppPortService;
import cn.edu.jit.tianyu_paas.web.service.MachinePortService;
import cn.edu.jit.tianyu_paas.web.service.MachineService;
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
    private final MachineService machineService;
    private final MachinePortService machinePortService;

    @Autowired
    public AppPortController(AppPortService appPortService, MachineService machineService, MachinePortService machinePortService) {
        this.appPortService = appPortService;
        this.machineService = machineService;
        this.machinePortService = machinePortService;
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
        List<Machine> machines = machineService.selectList(new EntityWrapper<Machine>());
        for(Machine machine: machines) {
            Integer port = machinePortService.selectList(new EntityWrapper<MachinePort>().eq("machine_id", machine.getMachineId()).and().eq("status", 1).last("limit 50")).get(0).getMachinePort();
            if (port != null) {
                appPort.setHostPort(port);
                appPort.setMachineId(machine.getMachineId());
                break;
            }
        }
        if (appPortService.selectCount(new EntityWrapper<AppPort>().eq("host_port", appPort.getHostPort())) != 0) {
            return TResult.failure(TResultCode.DATA_ALREADY_EXISTED);
        }
        appPort.setGmtModified(new Date());
        appPort.setGmtCreate(new Date());
        appPort.setInsideAccessUrl("127.0.0.1:"+appPort.getHostPort());
        appPort.setInsideAlias("");
        if (!appPortService.insert(appPort)) {
            return TResult.failure(TResultCode.FAILURE);
        }
        return TResult.success();
    }

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
        if (!appPortService.update(appPort, new EntityWrapper<AppPort>().eq("app_id", appPort.getAppId()).and().eq("host_port", appPort.getHostPort()))) {
            return TResult.failure(TResultCode.BUSINESS_ERROR);
        }
        return TResult.success();
    }

    /**
     * 删除端口
     *
     * @param appId
     * @param hostPort
     * @return
     * @author 倪龙康
     */
    @ApiOperation("删除端口")
    @DeleteMapping("/{appId}")
    public TResult deletePort(@PathVariable long appId, Integer hostPort) {
        if (!appPortService.delete(new EntityWrapper<AppPort>().eq("host_port", hostPort).eq("app_id", appId))) {
            return TResult.failure(TResultCode.BUSINESS_ERROR);
        }
        return TResult.success();
    }
}
