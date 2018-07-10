package cn.edu.jit.tianyu_paas.web.mapper;

import cn.edu.jit.tianyu_paas.shared.entity.MachinePort;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author 倪龙康
 * @since 2018-07-08
 */
public interface MachinePortMapper extends BaseMapper<MachinePort> {

    /**
     * 将已使用的机器端口状态更新
     *
     * @param machinePort
     * @return
     */
    @Select("UPDATE machine_port AS mp SET mp.status = #{port.status} WHERE mp.machine_id = #{port.machineId} AND mp.machine_port = #{port.machinePort}")
    Integer updateMachinePortStatusByIdAndPort(@Param("port") MachinePort machinePort);
}
