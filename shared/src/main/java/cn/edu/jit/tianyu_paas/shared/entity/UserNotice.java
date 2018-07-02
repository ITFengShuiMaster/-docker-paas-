package cn.edu.jit.tianyu_paas.shared.entity;

import java.io.Serializable;

/**
 * <p>
 * <p>
 * </p>
 *
 * @author 倪龙康
 * @since 2018-07-02
 */
public class UserNotice implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long noticeId;
    private Long userId;
    /**
     * 状态，0未读，1已读
     */
    private Integer status;


    public Long getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(Long noticeId) {
        this.noticeId = noticeId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "UserNotice{" +
                ", noticeId=" + noticeId +
                ", userId=" + userId +
                ", status=" + status +
                "}";
    }
}
