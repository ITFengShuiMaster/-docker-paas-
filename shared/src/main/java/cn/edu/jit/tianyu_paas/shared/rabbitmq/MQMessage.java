package cn.edu.jit.tianyu_paas.shared.rabbitmq;

import java.util.List;

public class MQMessage {
    private MQMessageType type;
    private Object data;
    private List<Long> receivers;

    private MQMessage(MQMessageType type, Object data, List<Long> receivers) {
        this.type = type;
        this.data = data;
        this.receivers = receivers;
    }

    public static MQMessage notice(Object data, List<Long> receivers) {
        return new MQMessage(MQMessageType.NOTICE, data, receivers);
    }

    public static MQMessage message(Object data, List<Long> receivers) {
        return new MQMessage(MQMessageType.MESSAGE, data, receivers);
    }

    public MQMessageType getType() {
        return type;
    }

    public void setType(MQMessageType type) {
        this.type = type;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public List<Long> getReceivers() {
        return receivers;
    }

    public void setReceivers(List<Long> receivers) {
        this.receivers = receivers;
    }
}
