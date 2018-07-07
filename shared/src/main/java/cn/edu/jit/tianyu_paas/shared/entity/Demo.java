package cn.edu.jit.tianyu_paas.shared.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;

import javax.validation.constraints.NotNull;
import java.util.Date;

public class Demo {
    @TableId(value = "demo_id", type = IdType.AUTO)
    private Long demoId;

    /**
     * 仓库地址
     */
    @NotNull(message = "（repositoryUrl）仓库地址必填")
    private String repositoryUrl;
    /**
     * 分支
     */
    @NotNull(message = "（branch）分支必填")
    private String branch;
    /**
     * 语言类型
     */
    @NotNull(message = "（languageType）语言类型必填")
    private String languageType;
    /**
     * 打包方式
     */
    @NotNull(message = "（packingMethod）仓库地址必填")
    private String packingMethod;
    private Date gmtCreate;

    public Long getDemoId() {
        return demoId;
    }

    public void setDemoId(Long demoId) {
        this.demoId = demoId;
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

    public String getLanguageType() {
        return languageType;
    }

    public void setLanguageType(String languageType) {
        this.languageType = languageType;
    }

    public String getPackingMethod() {
        return packingMethod;
    }

    public void setPackingMethod(String packingMethod) {
        this.packingMethod = packingMethod;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }
}
