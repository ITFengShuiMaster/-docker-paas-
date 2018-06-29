package cn.edu.jit.tianyu_paas.shared.entity;

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
public class AppDependency implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long appId;
    private Long dependencyAppId;
    private Date gmtCreate;


    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public Long getDependencyAppId() {
        return dependencyAppId;
    }

    public void setDependencyAppId(Long dependencyAppId) {
        this.dependencyAppId = dependencyAppId;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    @Override
    public String toString() {
        return "AppDependency{" +
                ", appId=" + appId +
                ", dependencyAppId=" + dependencyAppId +
                ", gmtCreate=" + gmtCreate +
                "}";
    }
}
