package cn.edu.jit.tianyu_paas.shared.mina_message;

public class CommonMessage extends MinaMessage {
    private String content;
    private Long receiver;

    public CommonMessage() {
        super.setMessageType(MinaMessageType.COMMON);
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