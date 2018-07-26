package cn.edu.jit.tianyu_paas.im.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 汪继友
 * @since 2018-07-07
 */
public class OfflineMessage implements Serializable {

    @TableId(value = "offline_message_id", type = IdType.AUTO)
    private Long offlineMessageId;
    private Long sender;
    private Long receiver;
    private String content;
    private Date gmtCreate;
    @TableField(exist = false)
    private User senderUser;

    public User getSenderUser() {
        return senderUser;
    }

    public void setSenderUser(User senderUser) {
        this.senderUser = senderUser;
    }

    public Long getOfflineMessageId() {
        return offlineMessageId;
    }

    public void setOfflineMessageId(Long offlineMessageId) {
        this.offlineMessageId = offlineMessageId;
    }

    public Long getSender() {
        return sender;
    }

    public void setSender(Long sender) {
        this.sender = sender;
    }

    public Long getReceiver() {
        return receiver;
    }

    public void setReceiver(Long receiver) {
        this.receiver = receiver;
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
        return "Message{" +
                ", offlineMessageId=" + offlineMessageId +
                ", sender=" + sender +
                ", receiver=" + receiver +
                ", content=" + content +
                ", gmtCreate=" + gmtCreate +
                "}";
    }
}
