package cn.edu.jit.tianyu_paas.shared.entity;

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
public class MachinePort implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 机器id
     */
    private Long machineId;
    /**
     * 端口号
     */
    private Integer machinePort;
    /**
     * 端口状态 0 关闭 1 可用 2 占用
     */
    private Integer status;
    /**
     * 端口状态变更时间
     */
    private Date gmtModify;
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

    public Integer getMachinePort() {
        return machinePort;
    }

    public void setMachinePort(Integer machinePort) {
        this.machinePort = machinePort;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getGmtModify() {
        return gmtModify;
    }

    public void setGmtModify(Date gmtModify) {
        this.gmtModify = gmtModify;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    @Override
    public String toString() {
        return "MachinePort{" +
                ", machineId=" + machineId +
                ", machinePort=" + machinePort +
                ", status=" + status +
                ", gmtModify=" + gmtModify +
                ", gmtCreate=" + gmtCreate +
                "}";
    }
}
