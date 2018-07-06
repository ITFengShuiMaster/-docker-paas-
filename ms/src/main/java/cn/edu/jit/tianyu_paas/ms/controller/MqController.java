package cn.edu.jit.tianyu_paas.ms.controller;

import cn.edu.jit.tianyu_paas.shared.entity.Notice;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("mq")
public class MqController {
    @Autowired
    RabbitTemplate rabbitTemplate;

    @RequestMapping("/")
    public void test() {
        List<Notice> notices = new ArrayList<>();
        Notice notice = new Notice();
        notice.setAdminId(111L);
        notice.setGmtCreate(new Date());
        notice.setContent("sg");
        notices.add(notice);
//        rabbitTemplate.convertAndSend(PublicConstants.RABBITMQ_QUEUE_NAME,MQMessage.message(notices));
    }
}
