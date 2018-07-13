package cn.edu.jit.tianyu_paas.shared.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * <p>
 * </p>
 *
 * @author 卢越
 * @since 2018-07-13
 */
public class MountSettings implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long appId;
    /**
     * 持久化名称
     */
    private String persistentName;
    /**
     * 服务器准备挂载的目录
     */
    private String serverMountName;
    /**
     * 容器要被挂载的目录
     */
    private String containerMountName;
    /**
     * 0 本地存储 1 共享存储 2 内存文件存储
     */
    private Integer type;
    private Date gmtCreate;


    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public String getPersistentName() {
        return persistentName;
    }

    public void setPersistentName(String persistentName) {
        this.persistentName = persistentName;
    }

    public String getServerMountName() {
        return serverMountName;
    }

    public void setServerMountName(String serverMountName) {
        this.serverMountName = serverMountName;
    }

    public String getContainerMountName() {
        return containerMountName;
    }

    public void setContainerMountName(String containerMountName) {
        this.containerMountName = containerMountName;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    @Override
    public String toString() {
        return "MountSettings{" +
                ", appId=" + appId +
                ", persistentName=" + persistentName +
                ", serverMountName=" + serverMountName +
                ", containerMountName=" + containerMountName +
                ", type=" + type +
                ", gmtCreate=" + gmtCreate +
                "}";
    }
}
