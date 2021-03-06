package cn.edu.jit.tianyu_paas.shared.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import com.github.dockerjava.api.command.InspectContainerResponse;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author 汪继友
 * @since 2018-06-28
 */
public class App implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 应用id
     */
    @TableId(value = "app_id", type = IdType.AUTO)
    private Long appId;
    /**
     * 应用容器的id
     */
    private String containerId;
    /**
     * 创建应用的用户id
     */
    private Long userId;
    /**
     * 应用名称
     */
    @NotNull
    private String name;
    /**
     * 应用使用的内存量，以MB为单位
     */
    private Integer memoryUsed;
    /**
     * 应用状态，0已关闭,1运行中,2异常，默认为0
     */
    private Integer status;
    /**
     * 创建方式，0自定义，1官方demo，3github项目，4docker镜像，5应用市场
     */
    private Integer createMethod;
    /**
     * 属于哪个应用组
     */
    @Min(1)
    private Long appGroupId;
    /**
     * 应用组的名称，非表中字段
     */
    @TableField(exist = false)
    private String groupName;
    /**
     * 创建时间
     */
    private Date gmtCreate;

    /**
     * 应用市场id
     */
    private Long marketAppId;

    /**
     * 应用所属用户的用户名
     */
    @TableField(exist = false)
    private String username;

    /**
     * 机器id
     */
    private Long machineId;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * 获取容器的信息
     */
    @TableField(exist = false)
    private InspectContainerResponse inspectContainerResponse;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getCreateMethod() {
        return createMethod;
    }

    public void setCreateMethod(Integer createMethod) {
        this.createMethod = createMethod;
    }

    public Long getAppGroupId() {
        return appGroupId;
    }

    public void setAppGroupId(Long appGroupId) {
        this.appGroupId = appGroupId;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public InspectContainerResponse getInspectContainerResponse() {
        return inspectContainerResponse;
    }

    public void setInspectContainerResponse(InspectContainerResponse inspectContainerResponse) {
        this.inspectContainerResponse = inspectContainerResponse;
    }

    public Long getMarketAppId() {
        return marketAppId;
    }

    public void setMarketAppId(Long marketAppId) {
        this.marketAppId = marketAppId;
    }

    public Long getMachineId() {
        return machineId;
    }

    public void setMachineId(Long machineId) {
        this.machineId = machineId;
    }

    @Override
    public String toString() {
        return "App{" +
                ", appId=" + appId +
                ", userId=" + userId +
                ", memoryUsed=" + memoryUsed +
                ", status=" + status +
                ", createMethod=" + createMethod +
                ", appGroupId=" + appGroupId +
                ", gmtCreate=" + gmtCreate +
                "}";
    }
}
