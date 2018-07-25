package cn.edu.jit.tianyu_paas.im.message;

import cn.edu.jit.tianyu_paas.im.entity.User;

/**
 * @author 天宇小凡
 */
public class CommonMessage extends MinaMessage {
    private String content;
    private Long receiver;
    private User sender;
    private Long gmtCreate;

    public CommonMessage(String content, Long receiver, User sender) {
        this();
        this.content = content;
        this.receiver = receiver;
        this.sender = sender;
    }

    public Long getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Long gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public CommonMessage() {
        super.setMessageType(MinaMessageType.COMMON);
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getReceiver() {
        return receiver;
    }

    public void setReceiver(Long receiver) {
        this.receiver = receiver;
    }
}