package cn.edu.jit.tianyu_paas.shared.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author 倪龙康
 * @since 2018-07-02
 */
public class Notice implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "notice_id", type = IdType.AUTO)
    private Long noticeId;
    /**
     * 标题
     */
    private String title;
    /**
     * 公告内容
     */
    private String content;
    /**
     * 创建者的id
     */
    private Long adminId;
    private Date gmtCreate;



    @TableField(exist = false)

    private UserNotice userNotice;

    public UserNotice getUserNotice() {
        return userNotice;
    }
    public void setUserNotice(UserNotice userNotice) {
        this.userNotice = userNotice;
    }


    public Long getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(Long noticeId) {
        this.noticeId = noticeId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
                ", title=" + title +
                ", content=" + content +
                ", adminId=" + adminId +
                ", gmtCreate=" + gmtCreate +
                "}";
    }
}
