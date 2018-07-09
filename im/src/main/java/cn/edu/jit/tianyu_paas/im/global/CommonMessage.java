package cn.edu.jit.tianyu_paas.im.global;

public class CommonMessage extends MinaMessage {
    private String content;
    private String receivers;

    public CommonMessage() {
        super.setMessageType(MinaMessageType.COMMON);
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getReceivers() {
        return receivers;
    }

    public void setReceivers(String receivers) {
        this.receivers = receivers;
    }
}