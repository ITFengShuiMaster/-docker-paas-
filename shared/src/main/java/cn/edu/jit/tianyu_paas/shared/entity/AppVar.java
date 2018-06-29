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
public class AppVar implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long appId;
    /**
     * 变量名，大写
     */
    private String varName;
    /**
     * 变量值
     */
    private String varValue;
    /**
     * 变量说明
     */
    private String varExplain;
    private Date gmtCreate;


    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public String getVarName() {
        return varName;
    }

    public void setVarName(String varName) {
        this.varName = varName;
    }

    public String getVarValue() {
        return varValue;
    }

    public void setVarValue(String varValue) {
        this.varValue = varValue;
    }

    public String getVarExplain() {
        return varExplain;
    }

    public void setVarExplain(String varExplain) {
        this.varExplain = varExplain;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    @Override
    public String toString() {
        return "AppVar{" +
                ", appId=" + appId +
                ", varName=" + varName +
                ", varValue=" + varValue +
                ", varExplain=" + varExplain +
                ", gmtCreate=" + gmtCreate +
                "}";
    }
}
