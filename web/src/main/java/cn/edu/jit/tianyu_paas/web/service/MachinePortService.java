package cn.edu.jit.tianyu_paas.web.service;

import cn.edu.jit.tianyu_paas.shared.entity.MachinePort;
import cn.edu.jit.tianyu_paas.web.mapper.MachinePortMapper;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 卢越
 * @since 2018-07-08
 */
@Service
public class MachinePortService extends ServiceImpl<MachinePortMapper, MachinePort> {

    /**
     * 将已使用的机器端口状态更新
     *
     * @param usedMachinePortList
     * @return
     */
    public boolean updateMachinePortStatusByIdAndPort(Map<MachinePort, Integer> usedMachinePortList) {
        for (MachinePort usedPort : usedMachinePortList.keySet()) {
            if (baseMapper.update(usedPort, new EntityWrapper<MachinePort>().eq("machine_id", usedPort.getMachineId()).and().eq("machine_port", usedPort.getMachinePort())) == 0) {
                return false;
            }
        }
        return true;
    }
}
