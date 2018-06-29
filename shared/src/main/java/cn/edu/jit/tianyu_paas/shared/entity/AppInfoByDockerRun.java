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
public class AppInfoByDockerRun implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long appId;
    /**
     * 创建docker的命令
     */
    @NotEmpty
    private String cmd;


    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    @Override
    public String toString() {
        return "AppInfoByDockerRun{" +
                ", appId=" + appId +
                ", cmd=" + cmd +
                "}";
    }
}
