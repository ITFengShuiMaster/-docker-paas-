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
public class AppInfoByCustom implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long appId;
    /**
     * git仓库地址
     */
    @NotEmpty
    private String repositoryUrl;
    /**
     * 代码分支
     */
    @NotEmpty
    private String branch;


    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public String getRepositoryUrl() {
        return repositoryUrl;
    }

    public void setRepositoryUrl(String repositoryUrl) {
        this.repositoryUrl = repositoryUrl;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    @Override
    public String toString() {
        return "AppInfoByCustom{" +
                ", appId=" + appId +
                ", repositoryUrl=" + repositoryUrl +
                ", branch=" + branch +
                "}";
    }
}
