package cn.edu.jit.tianyu_paas.web.rabbitmq;

import cn.edu.jit.tianyu_paas.shared.entity.Message;
import cn.edu.jit.tianyu_paas.shared.entity.Notice;
import cn.edu.jit.tianyu_paas.shared.global.PublicConstants;
import cn.edu.jit.tianyu_paas.shared.rabbitmq.MQMessage;
import cn.edu.jit.tianyu_paas.web.websocket.TWebSocket;
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
        System.out.println(mqMessage.toString());
        /*List<Long> receivers = mqMessage.getReceivers();
        switch (mqMessage.getType()) {
            case NOTICE:
                Notice notice = JSON.parseObject(mqMessage.getData().toString(), Notice.class);
                receiveNotices(notice, receivers);
                break;
            case MESSAGE:
                Message mina_message = JSON.parseObject(mqMessage.getData().toString(), Message.class);
                receiveMessages(mina_message, receivers);
                break;
            default:
                break;
        }*/
    }

    /**
     * 从消息队列接收到公告
     */
    private void receiveNotices(Notice notice, List<Long> receivers) {
        receivers.forEach(userId -> {
            TWebSocket.sendMessageToUser(JSON.toJSONString(notice), userId);
        });
    }

    /**
     * 从消息队列接收到消息
     */
    private void receiveMessages(Message message, List<Long> receivers) {
        receivers.forEach(userId -> {
            TWebSocket.sendMessageToUser(JSON.toJSONString(message), userId);
        });
    }
}
