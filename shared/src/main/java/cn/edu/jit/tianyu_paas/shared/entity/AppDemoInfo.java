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
public class AppDemoInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long appId;
    private Long demoId;


    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public Long getDemoId() {
        return demoId;
    }

    public void setDemoId(Long demoId) {
        this.demoId = demoId;
    }

    @Override
    public String toString() {
        return "AppDemoInfo{" +
                ", appId=" + appId +
                ", demoId=" + demoId +
                "}";
    }
}
