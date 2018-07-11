package cn.edu.jit.tianyu_paas.web.rabbitmq;

import cn.edu.jit.tianyu_paas.shared.entity.Message;
import cn.edu.jit.tianyu_paas.shared.entity.Notice;
import cn.edu.jit.tianyu_paas.shared.global.PublicConstants;
import cn.edu.jit.tianyu_paas.shared.rabbitmq.MQMessage;
import cn.edu.jit.tianyu_paas.web.websocket.TWebSocket;
import cn.edu.jit.tianyu_paas.web.websocket.WebSocketMessage;
import cn.edu.jit.tianyu_paas.web.websocket.WebSocketMessageType;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 消息队列接收者
 */
@Component
public class RabbitMQReceiver {

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQReceiver.class);

    @RabbitHandler
    @RabbitListener(queues = PublicConstants.RABBITMQ_QUEUE_NAME)
    public void process(String messageStr) {
        LOGGER.debug(messageStr);
        MQMessage mqMessage = JSON.parseObject(messageStr, MQMessage.class);

        LOGGER.debug(mqMessage.toString());
        List<Long> receivers = mqMessage.getReceivers();

        WebSocketMessage webSocketMessage = new WebSocketMessage();
        webSocketMessage.setData(mqMessage.getData());

        switch (mqMessage.getType()) {
            case NOTICE:
                webSocketMessage.setMessageType(WebSocketMessageType.NOTICE);
                break;
            case MESSAGE:
                webSocketMessage.setMessageType(WebSocketMessageType.MESSAGE);
                break;
            default:
                break;
        }

        // 发送到websocket
        receivers.forEach(receiver -> {
            TWebSocket.sendMessageToUser(JSON.toJSONString(webSocketMessage), receiver);
        });
    }
}
