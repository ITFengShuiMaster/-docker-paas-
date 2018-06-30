package cn.edu.jit.tianyu_paas.shared.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;

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
public class Action implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final Integer STATUS = 1;

    @TableId(value = "action_id", type = IdType.AUTO)
    private Long actionId;
    /**
     * 操作者id
     */
    private Long userId;
    /**
     * 操作者名字
     */
    private String userName;
    /**
     * 行为，0水平升级，1启动，2重启，3部署。
     */
    private Integer action;
    /**
     * 所操作的应用id
     */
    private Long appId;
    /**
     * 所操作的应用名字
     */
    private String appName;
    /**
     * 操作状态，0完成，1失败
     */
    private Integer status;
    private Date gmtCreate;


    public Long getActionId() {
        return actionId;
    }

    public void setActionId(Long actionId) {
        this.actionId = actionId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getAction() {
        return action;
    }

    public void setAction(Integer action) {
        this.action = action;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    @Override
    public String toString() {
        return "Action{" +
                ", actionId=" + actionId +
                ", userId=" + userId +
                ", userName=" + userName +
                ", action=" + action +
                ", appId=" + appId +
                ", appName=" + appName +
                ", status=" + status +
                ", gmtCreate=" + gmtCreate +
                "}";
    }
}
