package cn.edu.jit.tianyu_paas.shared.util;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * <p>
 * </p>
 *
 * @author 汪继友
 * @since 2018-07-01
 */
public class Notice implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "notice_id", type = IdType.AUTO)
    private Long noticeId;
    /**
     * 公告内容
     */
    private String content;
    /**
     * 创建者的id
     */
    private Long adminId;
    private Date gmtCreate;


    public Long getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(Long noticeId) {
        this.noticeId = noticeId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    @Override
    public String toString() {
        return "Notice{" +
                ", noticeId=" + noticeId +
                ", content=" + content +
                ", adminId=" + adminId +
                ", gmtCreate=" + gmtCreate +
                "}";
    }
}
