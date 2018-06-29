package cn.edu.jit.tianyu_paas.shared.entity;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author 汪继友
 * @since 2018-06-28
 */
public class UserDynamic implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long userId;
    /**
     * 已使用内存，以MB为单位
     */
    private Integer memoryUsed;
    /**
     * 总可用内存，以MB为单位
     */
    private Integer memoryTotal;
    /**
     * 已使用磁盘，以MB为单位
     */
    private Integer diskUsed;
    /**
     * 总用磁盘，以MB为单位
     */
    private Integer diskTotal;
    /**
     * 账户余额
     */
    private Double balance;


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getMemoryUsed() {
        return memoryUsed;
    }

    public void setMemoryUsed(Integer memoryUsed) {
        this.memoryUsed = memoryUsed;
    }

    public Integer getMemoryTotal() {
        return memoryTotal;
    }

    public void setMemoryTotal(Integer memoryTotal) {
        this.memoryTotal = memoryTotal;
    }

    public Integer getDiskUsed() {
        return diskUsed;
    }

    public void setDiskUsed(Integer diskUsed) {
        this.diskUsed = diskUsed;
    }

    public Integer getDiskTotal() {
        return diskTotal;
    }

    public void setDiskTotal(Integer diskTotal) {
        this.diskTotal = diskTotal;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "UserDynamic{" +
                ", userId=" + userId +
                ", memoryUsed=" + memoryUsed +
                ", memoryTotal=" + memoryTotal +
                ", diskUsed=" + diskUsed +
                ", diskTotal=" + diskTotal +
                ", balance=" + balance +
                "}";
    }
}
