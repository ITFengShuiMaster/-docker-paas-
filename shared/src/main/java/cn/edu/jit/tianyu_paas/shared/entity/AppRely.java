package cn.edu.jit.tianyu_paas.shared.entity;

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

    @Override
    public String toString() {
        return "AppRely{" +
                ", appId=" + appId +
                ", relyId=" + relyId +
                ", gmtCreate=" + gmtCreate +
                "}";
    }
}
