package cn.edu.jit.tianyu_paas.ms.controller;

import cn.edu.jit.tianyu_paas.ms.service.MachinePortService;
import cn.edu.jit.tianyu_paas.shared.entity.MachinePort;
import cn.edu.jit.tianyu_paas.shared.util.TResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author 卢越
 */
@RestController
@RequestMapping("/machineports")
public class MachinePortController {

    private final MachinePortService machinePortService;

    @Autowired
    public MachinePortController(MachinePortService machinePortService) {
        this.machinePortService = machinePortService;
    }

    @GetMapping("/{machineId}")
    public TResult insertPortLists(@PathVariable Long machineId) {
        List<MachinePort> ports = new ArrayList<>();

        for (int i = 10000; i <= 60000; i++) {
            MachinePort machinePort = new MachinePort();
            machinePort.setMachineId(machineId);
            machinePort.setMachinePort(i);
            machinePort.setGmtCreate(new Date());
            machinePort.setGmtModify(new Date());
            machinePort.setStatus(1);
            ports.add(machinePort);
        }

        machinePortService.insertBatch(ports);
        return TResult.success();
    }
}
