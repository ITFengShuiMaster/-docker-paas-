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
public class AppLog implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long appId;
    /**
     * 应用日志的内容
     */
    private String content;
    private Date gmtCreate;


    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    @Override
    public String toString() {
        return "AppLog{" +
                ", appId=" + appId +
                ", content=" + content +
                ", gmtCreate=" + gmtCreate +
                "}";
    }
}
