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
public class ActionDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long actionId;
    private String content;
    /**
     * 日志等级，0info，1debug，2error
     */
    private Integer level;
    private Date gmtCreate;


    public Long getActionId() {
        return actionId;
    }

    public void setActionId(Long actionId) {
        this.actionId = actionId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    @Override
    public String toString() {
        return "ActionDetail{" +
                ", actionId=" + actionId +
                ", content=" + content +
                ", level=" + level +
                ", gmtCreate=" + gmtCreate +
                "}";
    }
}
