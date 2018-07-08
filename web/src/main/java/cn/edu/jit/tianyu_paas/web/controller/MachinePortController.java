package cn.edu.jit.tianyu_paas.web.controller;


import cn.edu.jit.tianyu_paas.shared.entity.MachinePort;
import cn.edu.jit.tianyu_paas.shared.util.TResult;
import cn.edu.jit.tianyu_paas.web.service.MachinePortService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 卢越
 * @since 2018-07-08
 */
@RestController
@RequestMapping("/machinePort")
public class MachinePortController {

    private final MachinePortService machinePortService;
    private HttpSession session;

    public MachinePortController(HttpSession session, MachinePortService machinePortService) {
        this.session = session;
        this.machinePortService = machinePortService;
    }

    /**
     * 初始化机器的可用端口
     *
     * @param machineId
     * @return
     * @author 卢越
     */
    @GetMapping
    public TResult insertPortLists(@RequestParam(required = true) Long machineId) {
        MachinePortService machinePortService = new MachinePortService();
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

