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
public class AppFeature implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long appId;
    private Long featureId;


    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public Long getFeatureId() {
        return featureId;
    }

    public void setFeatureId(Long featureId) {
        this.featureId = featureId;
    }

    @Override
    public String toString() {
        return "AppFeature{" +
                ", appId=" + appId +
                ", featureId=" + featureId +
                "}";
    }
}
