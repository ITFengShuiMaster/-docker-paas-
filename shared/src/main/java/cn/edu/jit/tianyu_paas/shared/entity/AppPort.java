package cn.edu.jit.tianyu_paas.shared.entity;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * <p>
 * </p>
 *
 * @author 汪继友
 * @since 2018-06-28
 */
public class AppPort implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final int HTTP = 0;
    public static final int MYSQL = 1;
    public static final int TCP = 2;
    public static final int UDP = 3;
    public static final int INOPEN = 1;
    public static final int INCLOSE = 0;
    public static final int OUTOPEN = 1;
    public static final int OUTCLOSE = 0;

    @NotNull
    private Long appId;
    /**
     * 主机端口
     */
    @NotNull
    private Integer hostPort;
    /**
     * 容器端口
     */
    @NotNull
    private Integer containerPort;
    /**
     * 协议，0http，1mysql,2tcp,3udp
     */
    @NotNull
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

    /**
     * 机器id
     */
    private Long machineId;


    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public Integer getHostPort() {
        return hostPort;
    }

    public void setHostPort(Integer hostPort) {
        this.hostPort = hostPort;
    }

    public Integer getContainerPort() {
        return containerPort;
    }

    public void setContainerPort(Integer containerPort) {
        this.containerPort = containerPort;
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

    public Long getMachineId() {
        return machineId;
    }

    public void setMachineId(Long machineId) {
        this.machineId = machineId;
    }

    @Override
    public String toString() {
        return "AppPort{" +
                "appId=" + appId +
                ", hostPort=" + hostPort +
                ", containerPort=" + containerPort +
                ", protocol=" + protocol +
                ", isInsideOpen=" + isInsideOpen +
                ", insideAccessUrl='" + insideAccessUrl + '\'' +
                ", insideAlias='" + insideAlias + '\'' +
                ", isOutsideOpen=" + isOutsideOpen +
                ", outsideAccessUrl='" + outsideAccessUrl + '\'' +
                ", gmtCreate=" + gmtCreate +
                ", gmtModified=" + gmtModified +
                ", machineId=" + machineId +
                '}';
    }
}
