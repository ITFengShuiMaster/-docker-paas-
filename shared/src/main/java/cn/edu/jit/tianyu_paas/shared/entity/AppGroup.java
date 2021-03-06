package cn.edu.jit.tianyu_paas.shared.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author 汪继友
 * @since 2018-06-28
 */
@Data
public class AppGroup implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "app_group_id", type = IdType.AUTO)
    private Long appGroupId;

    /**
     * 创建者id
     */
    private Long userId;
    /**
     * 应用组名字
     */
    @NotEmpty
    private String groupName;
    /**
     * 从compose创建应用组的内容
     */
    @NotEmpty
    private String compose;
    /**
     * 创建时间
     */
    private Date gmtCreate;

    @TableField(exist = false)
    private List<App> apps;

    public List<App> getApps() {
        return apps;
    }

    public void setApps(List<App> apps) {
        this.apps = apps;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getAppGroupId() {
        return appGroupId;
    }

    public void setAppGroupId(Long appGroupId) {
        this.appGroupId = appGroupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getCompose() {
        return compose;
    }

    public void setCompose(String compose) {
        this.compose = compose;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    @Override
    public String toString() {
        return "AppGroup{" +
                ", appGroupId=" + appGroupId +
                ", groupName=" + groupName +
                ", compose=" + compose +
                ", gmtCreate=" + gmtCreate +
                "}";
    }
}
