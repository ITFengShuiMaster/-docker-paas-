package cn.edu.jit.tianyu_paas.im.mina;

import cn.edu.jit.tianyu_paas.im.entity.Message;
import cn.edu.jit.tianyu_paas.im.entity.OfflineMessage;
import cn.edu.jit.tianyu_paas.im.entity.User;
import cn.edu.jit.tianyu_paas.im.global.MinaConstant;
import cn.edu.jit.tianyu_paas.im.message.AuthenticationMessage;
import cn.edu.jit.tianyu_paas.im.message.CommonMessage;
import cn.edu.jit.tianyu_paas.im.message.MinaMessage;
import cn.edu.jit.tianyu_paas.im.service.MessageService;
import cn.edu.jit.tianyu_paas.im.service.OfflineMessageService;
import cn.edu.jit.tianyu_paas.im.service.UserService;
import cn.edu.jit.tianyu_paas.im.util.SpringBeanFactoryUtil;
import cn.edu.jit.tianyu_paas.shared.util.PassUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * @author 天宇小凡
 */
public class MyIoHandler extends IoHandlerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyIoHandler.class);

    private UserService userService = SpringBeanFactoryUtil.getBean(UserService.class);
    private MessageService messageService = SpringBeanFactoryUtil.getBean(MessageService.class);
    private OfflineMessageService offlineMessageService = SpringBeanFactoryUtil.getBean(OfflineMessageService.class);

    @Override
    public void sessionCreated(IoSession session) {
    }

    @Override
    public void sessionOpened(IoSession session) {
    }

    @Override
    public void sessionClosed(IoSession session) {
        User user = (User) session.getAttribute(MinaConstant.SESSION_KEY_USER);
        switch (user.getType()) {
            case User.TYPE_COMMON:
                GlobalSession.userLogout(session, user.getUserId());
                break;
            case User.TYPE_CUSTOMER_SERVICE:
                GlobalSession.customerServiceLogout(session, user.getUserId());
                break;
            default:
                break;
        }
    }

    /**
     * session 进入空闲状态
     */
    @Override
    public void sessionIdle(IoSession session, IdleStatus status) {
        LOGGER.info("idle" + status.toString());
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) {
        session.closeOnFlush();
        LOGGER.warn("mina session exception: " + cause.getMessage());
    }

    @Override
    public void messageReceived(IoSession session, Object message) {
        LOGGER.info("recv: " + message.toString());
        MinaMessage minaMessage = JSON.parseObject(message.toString(), MinaMessage.class);
        switch (minaMessage.getMessageType()) {
            case COMMON:
                CommonMessage commonMessage = JSON.parseObject(message.toString(), CommonMessage.class);
                commonMessage.setGmtCreate(System.currentTimeMillis());
                handleCommonMessage(session, commonMessage);
                break;
            case AUTHENTICATION:
                handleAuthenticationMessage(session, JSON.parseObject(message.toString(), AuthenticationMessage.class));
                break;
            default:
                break;
        }
        LOGGER.info(minaMessage.toString());
    }

    @Override
    public void messageSent(IoSession session, Object message) {
        // LOGGER.info(mina_message.toString());
    }

    private void handleAuthenticationMessage(IoSession ioSession, AuthenticationMessage authenticationMessage) {
        User user = userService.selectOne(new EntityWrapper<User>().eq("phone", authenticationMessage.getUsername())
                .or().eq("email", authenticationMessage.getUsername()));
        if (user == null || !user.getPwd().equals(PassUtil.getMD5(authenticationMessage.getPaasword()))) {
            ioSession.write("user not exsit or password incorrect");
            ioSession.closeOnFlush();
            return;
        }
        ioSession.setAttribute(MinaConstant.SESSION_KEY_USER, user);
        switch (user.getType()) {
            case User.TYPE_COMMON:
                GlobalSession.userLogin(ioSession, user.getUserId());
                break;
            case User.TYPE_CUSTOMER_SERVICE:
                GlobalSession.customerServiceLogin(ioSession, user.getUserId());
                break;
            default:
                break;
        }
    }

    private void handleCommonMessage(IoSession ioSession, CommonMessage commonMessage) {
        User user = (User) ioSession.getAttribute(MinaConstant.SESSION_KEY_USER);
        // 如果用户未认证，则断开连接
        if (user == null) {
            ioSession.write("you are not authenticate");
            ioSession.closeOnFlush();
            return;
        }
        commonMessage.setSender(user);
        boolean online = false;
        long receiver = MinaConstant.CUSTOMER_SERVICE_ID;
        switch (user.getType()) {
            case User.TYPE_COMMON:
                online = GlobalSession.userSendMessage(ioSession, commonMessage);
                break;
            case User.TYPE_CUSTOMER_SERVICE:
                receiver = commonMessage.getReceiver();
                online = GlobalSession.customerServiceSendMessage(ioSession, commonMessage);
                break;
            default:
                LOGGER.error("OMG, how can you arrive here");
                break;
        }
        if (online) {
            insertOnlineMessage(user.getUserId(), commonMessage.getContent(), receiver);
        } else {
            insertOfflineMessage(user.getUserId(), commonMessage.getContent(), receiver);
        }
    }

    /**
     * 插入到消息表
     */
    private void insertOnlineMessage(long sender, String content, long receiver) {
        Message message = new Message();
        message.setSender(sender);
        message.setContent(content);
        message.setReceiver(receiver);
        message.setGmtCreate(new Date());
        messageService.insert(message);
    }

    /**
     * 插入到离线消息表
     */
    private void insertOfflineMessage(long sender, String content, long receiver) {
        OfflineMessage offlineMessage = new OfflineMessage();
        offlineMessage.setSender(sender);
        offlineMessage.setContent(content);
        offlineMessage.setReceiver(receiver);
        offlineMessage.setGmtCreate(new Date());
        offlineMessageService.insert(offlineMessage);
    }
}