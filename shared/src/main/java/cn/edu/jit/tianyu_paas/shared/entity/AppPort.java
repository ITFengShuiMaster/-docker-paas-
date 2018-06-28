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
public class AppPort implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long appId;
    /**
     * 端口
     */
    private Integer port;
    /**
     * 协议，0http，1mysql,2tcp,3udp
     */
    private Integer protocol;
    /**
     * 对内服务是否开启，0关闭，1开启
     */
    private Integer isInsideOpen;
    /**
     * 对内访问地址加端口
     */
    private String insideAccessUrl;
    /**
     * 对内别名
     */
    private String insideAlias;
    /**
     * 对外服务是否开启
     */
    private Integer isOutsideOpen;
    /**
     * 对外访问地址加端口
     */
    private String outsideAccessUrl;
    private Date gmtCreate;
    private Date gmtModified;


    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Integer getProtocol() {
        return protocol;
    }

    public void setProtocol(Integer protocol) {
        this.protocol = protocol;
    }

    public Integer getIsInsideOpen() {
        return isInsideOpen;
    }

    public void setIsInsideOpen(Integer isInsideOpen) {
        this.isInsideOpen = isInsideOpen;
    }

    public String getInsideAccessUrl() {
        return insideAccessUrl;
    }

    public void setInsideAccessUrl(String insideAccessUrl) {
        this.insideAccessUrl = insideAccessUrl;
    }

    public String getInsideAlias() {
        return insideAlias;
    }

    public void setInsideAlias(String insideAlias) {
        this.insideAlias = insideAlias;
    }

    public Integer getIsOutsideOpen() {
        return isOutsideOpen;
    }

    public void setIsOutsideOpen(Integer isOutsideOpen) {
        this.isOutsideOpen = isOutsideOpen;
    }

    public String getOutsideAccessUrl() {
        return outsideAccessUrl;
    }

    public void setOutsideAccessUrl(String outsideAccessUrl) {
        this.outsideAccessUrl = outsideAccessUrl;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    @Override
    public String toString() {
        return "AppPort{" +
                ", appId=" + appId +
                ", port=" + port +
                ", protocol=" + protocol +
                ", isInsideOpen=" + isInsideOpen +
                ", insideAccessUrl=" + insideAccessUrl +
                ", insideAlias=" + insideAlias +
                ", isOutsideOpen=" + isOutsideOpen +
                ", outsideAccessUrl=" + outsideAccessUrl +
                ", gmtCreate=" + gmtCreate +
                ", gmtModified=" + gmtModified +
                "}";
    }
}
