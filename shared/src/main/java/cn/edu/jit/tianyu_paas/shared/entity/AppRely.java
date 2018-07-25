package cn.edu.jit.tianyu_paas.shared.entity;

import com.baomidou.mybatisplus.annotations.TableField;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * <p>
 * </p>
 *
 * @author 汪继友
 * @since 2018-07-21
 */
public class AppRely implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    private Long appId;
    @NotNull
    private Long relyId;
    private Date gmtCreate;

    @TableField(exist = false)
    private String relyName;
    @TableField(exist = false)
    private String relyGroupName;

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public Long getRelyId() {
        return relyId;
    }

    public void setRelyId(Long relyId) {
        this.relyId = relyId;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public String getRelyName() {
        return relyName;
    }

    public void setRelyName(String relyName) {
        this.relyName = relyName;
    }

    public String getRelyGroupName() {
        return relyGroupName;
    }

    public void setRelyGroupName(String relyGroupName) {
        this.relyGroupName = relyGroupName;
    }

    @Override
    public String toString() {
        return "AppRely{" +
                ", appId=" + appId +
                ", relyId=" + relyId +
                ", gmtCreate=" + gmtCreate +
                "}";
    }
}
