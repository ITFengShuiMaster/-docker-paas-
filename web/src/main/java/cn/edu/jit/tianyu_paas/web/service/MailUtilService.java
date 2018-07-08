package cn.edu.jit.tianyu_paas.web.service;

import cn.edu.jit.tianyu_paas.web.global.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * @author 卢越
 * @date 2018-7-8 10.48
 * 邮箱验证业务
 */
@Service
public class MailUtilService {
    private final Logger logger = LoggerFactory.getLogger(MailUtilService.class);

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${mail.fromMail.addr}")
    private String from;

    /**
     * 发送激活邮件
     *
     * @param userId
     * @param email
     * @param code
     * @return
     */
    public boolean sendRegisterMail(Long userId, String email, String code) {
        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom(from);
            //目的邮箱
            simpleMailMessage.setTo(email);
            //邮箱主题
            simpleMailMessage.setSubject("Docker平台注册服务");
            //邮箱内容 自定义
            simpleMailMessage.setText("请访问链接以完成注册: " + String.format(Constants.MAIL_CONTEXT, userId.toString(), code));
            javaMailSender.send(simpleMailMessage);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
