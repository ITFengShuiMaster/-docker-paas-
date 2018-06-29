package cn.edu.jit.tianyu_paas.shared.entity;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author 汪继友
 * @since 2018-06-28
 */
public class AppInfoByDockerImage implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long appId;
    /**
     * 镜像地址/名字
     */
    @NotEmpty
    private String image;


    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "AppInfoByDockerImage{" +
                ", appId=" + appId +
                ", image=" + image +
                "}";
    }
}
