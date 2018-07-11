package cn.edu.jit.tianyu_paas.web.websocket;

public class WebSocketMessage {
    private WebSocketMessageType messageType;
    private Object data;

    public WebSocketMessage() {
    }

    public WebSocketMessage(WebSocketMessageType messageType, Object data) {
        this.messageType = messageType;
        this.data = data;
    }

    public WebSocketMessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(WebSocketMessageType messageType) {
        this.messageType = messageType;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
