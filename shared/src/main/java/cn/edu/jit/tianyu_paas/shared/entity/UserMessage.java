package cn.edu.jit.tianyu_paas.shared.entity;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author 汪继友
 * @since 2018-07-02
 */
public class UserMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long userId;
    private Long messageId;
    /**
     * 状态，0未读，1已读
     */
    private Integer status;


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "UserMessage{" +
                ", userId=" + userId +
                ", messageId=" + messageId +
                ", status=" + status +
                "}";
    }
}
