package cn.edu.jit.tianyu_paas.shared.mina_message;

/**
 * @author 天宇小凡
 */
public class CommonMessage extends MinaMessage {
    private String content;
    private Long receiver;
    private Long sender;

    public Long getSender() {
        return sender;
    }

    public void setSender(Long sender) {
        this.sender = sender;
    }

    public CommonMessage() {
        super.setMessageType(MinaMessageType.COMMON);
    }

    public CommonMessage(String content, Long receiver, Long sender) {
        this();
        this.content = content;
        this.receiver = receiver;
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