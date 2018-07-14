package cn.edu.jit.tianyu_paas.im.mina;

import cn.edu.jit.tianyu_paas.im.entity.Message;
import cn.edu.jit.tianyu_paas.im.entity.OfflineMessage;
import cn.edu.jit.tianyu_paas.im.entity.User;
import cn.edu.jit.tianyu_paas.im.global.MinaConstant;
import cn.edu.jit.tianyu_paas.im.service.MessageService;
import cn.edu.jit.tianyu_paas.im.service.OfflineMessageService;
import cn.edu.jit.tianyu_paas.im.service.UserService;
import cn.edu.jit.tianyu_paas.shared.mina_message.AuthenticationMessage;
import cn.edu.jit.tianyu_paas.shared.mina_message.CommonMessage;
import cn.edu.jit.tianyu_paas.shared.mina_message.MinaMessage;
import cn.edu.jit.tianyu_paas.shared.util.PassUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * @author 天宇小凡
 */
public class MyIoHandler extends IoHandlerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyIoHandler.class);

    @Autowired
    private UserService userService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private OfflineMessageService offlineMessageService;

    @Override
    public void sessionCreated(IoSession session) {
        LOGGER.info("create");
    }

    @Override
    public void sessionOpened(IoSession session) {
        LOGGER.info("open");
    }

    @Override
    public void sessionClosed(IoSession session) {
        User user = (User) session.getAttribute(MinaConstant.SESSION_KEY_USER);
        switch (user.getType()) {
            case User.TYPE_COMMON:
                GlobalSession.userLogout(session, user.getUserId());
                break;
            case User.TYPE_CUSTOMER_SERVICE:
                GlobalSession.customerServiceLogout(session);
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
        LOGGER.info(message.toString());
        MinaMessage minaMessage = JSON.parseObject(message.toString(), MinaMessage.class);
        switch (minaMessage.getMessageType()) {
            case COMMON:
                handleCommonMessage(session, (CommonMessage) minaMessage);
                break;
            case AUTHENTICATION:
                handleAuthenticationMessage(session, (AuthenticationMessage) minaMessage);
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
        if (!user.getPwd().equals(PassUtil.getMD5(authenticationMessage.getPaasword()))) {
            return;
        }
        ioSession.setAttribute(MinaConstant.SESSION_KEY_USER_ID, user.getUserId());
        ioSession.setAttribute(MinaConstant.SESSION_KEY_USER, user);
        switch (user.getType()) {
            case User.TYPE_COMMON:
                GlobalSession.userLogin(ioSession, user.getUserId());
                break;
            case User.TYPE_CUSTOMER_SERVICE:
                GlobalSession.customerServiceLogin(ioSession);
                break;
            default:
                break;
        }
    }

    private void handleCommonMessage(IoSession ioSession, CommonMessage commonMessage) {
        User user = (User) ioSession.getAttribute(MinaConstant.SESSION_KEY_USER);
        commonMessage.setSender(user.getUserId());
        Message message = new Message();
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
        message.setContent(commonMessage.getContent());
        message.setGmtCreate(new Date());
        message.setSender(user.getUserId());
        if (online) {
            insertOnlineMessage(user.getUserId(), commonMessage.getContent(), receiver);
        } else {
            insertOfflineMessage(user.getUserId(), commonMessage.getContent(), receiver);
        }
    }

    private void insertOnlineMessage(long sender, String content, long receiver) {
        Message message = new Message();
        message.setSender(sender);
        message.setContent(content);
        message.setReceiver(receiver);
        message.setGmtCreate(new Date());
        messageService.insert(message);
    }

    private void insertOfflineMessage(long sender, String content, long receiver) {
        OfflineMessage offlineMessage = new OfflineMessage();
        offlineMessage.setSender(sender);
        offlineMessage.setContent(content);
        offlineMessage.setReceiver(receiver);
        offlineMessage.setGmtCreate(new Date());
        offlineMessageService.insert(offlineMessage);
    }
}