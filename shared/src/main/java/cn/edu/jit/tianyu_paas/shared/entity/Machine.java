package cn.edu.jit.tianyu_paas.shared.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * <p>
 * </p>
 *
 * @author 卢越
 * @since 2018-07-08
 */
public class Machine implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 机器id
     */
    @TableId(value = "machine_id", type = IdType.AUTO)
    private Long machineId;
    /**
     * 机器名
     */
    private String machineName;
    /**
     * 机器ip地址
     */
    private String machineIp;
    /**
     * 创建时间
     */
    private Date gmtCreate;


    public Long getMachineId() {
        return machineId;
    }

    public void setMachineId(Long machineId) {
        this.machineId = machineId;
    }

    public String getMachineName() {
        return machineName;
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    public String getMachineIp() {
        return machineIp;
    }

    public void setMachineIp(String machineIp) {
        this.machineIp = machineIp;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    @Override
    public String toString() {
        return "Machine{" +
                ", machineId=" + machineId +
                ", machineName=" + machineName +
                ", machineIp=" + machineIp +
                ", gmtCreate=" + gmtCreate +
                "}";
    }
}
