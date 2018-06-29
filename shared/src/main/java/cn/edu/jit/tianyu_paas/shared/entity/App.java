package cn.edu.jit.tianyu_paas.shared.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
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
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class App implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 应用id
     */
    @TableId(value = "app_id", type = IdType.AUTO)
    private Long appId;
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
    @NotEmpty
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
