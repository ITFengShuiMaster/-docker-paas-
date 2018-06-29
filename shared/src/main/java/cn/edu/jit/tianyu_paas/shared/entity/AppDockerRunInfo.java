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
public class AppDockerRunInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long appId;
    /**
     * 创建docker的命令
     */
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
        return "AppDockerRunInfo{" +
                ", appId=" + appId +
                ", cmd=" + cmd +
                "}";
    }
}
