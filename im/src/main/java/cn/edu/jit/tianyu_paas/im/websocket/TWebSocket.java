package cn.edu.jit.tianyu_paas.im.websocket;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author 天宇小凡
 */
@ServerEndpoint(value = "/im-server")
@Component
public class TWebSocket {
    private static final Logger LOGGER = LoggerFactory.getLogger(TWebSocket.class);
    private static ConcurrentMap<Long, Session> userMap = new ConcurrentHashMap<>();
    private static ConcurrentMap<Session, User> sessionUserMap = new ConcurrentHashMap<>();
    private static Session customerServiceSession = null;
    private final Logger logger = LoggerFactory.getLogger(TWebSocket.class);
    private UserService userService = SpringBeanFactoryUtil.getBean(UserService.class);
    private MessageService messageService = SpringBeanFactoryUtil.getBean(MessageService.class);
    private OfflineMessageService offlineMessageService = SpringBeanFactoryUtil.getBean(OfflineMessageService.class);
    private Session webSocketSession;
    private int type = User.TYPE_COMMON;
    private User user = null;

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        // 普通用户离线
        if (type == User.TYPE_COMMON) {
            sessionUserMap.remove(webSocketSession);
        } else {
            // 客服离线
            customerServiceSession = null;
        }
    }

    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
        this.webSocketSession = session;
    }

    /**
     * 收到客户端消息后调用的方法
     */
    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        logger.info("来自客户端的消息:" + message);
        MinaMessage minaMessage = JSON.parseObject(message, MinaMessage.class);
        switch (minaMessage.getMessageType()) {
            case COMMON:
                CommonMessage commonMessage = JSON.parseObject(message, CommonMessage.class);
                commonMessage.setGmtCreate(System.currentTimeMillis());
                handleCommonMessage(session, commonMessage);
                break;
            case AUTHENTICATION:
                handleAuthenticationMessage(session, JSON.parseObject(message, AuthenticationMessage.class));
                break;
            default:
                break;
        }
    }

    private void handleCommonMessage(Session session, CommonMessage commonMessage) throws IOException {
        commonMessage.setSender(this.user);
        boolean online = false;
        if (commonMessage.getReceiver().equals(MinaConstant.CUSTOMER_SERVICE_ID)) {
            if (customerServiceSession != null) {
                online = true;
                customerServiceSession.getBasicRemote().sendText(JSON.toJSONString(commonMessage));
            }
        } else {
            Session receiverSession = userMap.get(commonMessage.getReceiver());
            if (userMap.get(commonMessage.getReceiver()) != null) {
                online = true;
                receiverSession.getBasicRemote().sendText(JSON.toJSONString(commonMessage));
            }
        }
        if (online) {
            insertOnlineMessage(user.getUserId(), commonMessage.getContent(), commonMessage.getReceiver());
        } else {
            insertOfflineMessage(user.getUserId(), commonMessage.getContent(), commonMessage.getReceiver());
        }
    }

    private void handleAuthenticationMessage(Session session, AuthenticationMessage authenticationMessage) throws IOException {
        User user = userService.selectOne(new EntityWrapper<User>().eq("phone", authenticationMessage.getUsername())
                .or().eq("email", authenticationMessage.getUsername()));
        if (user == null || !user.getPwd().equals(PassUtil.getMD5(authenticationMessage.getPaasword()))) {
            session.getBasicRemote().sendText("user not exsit or password incorrect");
            session.close();
            return;
        }
        this.user = user;
        switch (user.getType()) {
            case User.TYPE_COMMON:
                userMap.put(user.getUserId(), session);
                break;
            case User.TYPE_CUSTOMER_SERVICE:
                customerServiceSession = session;
                break;
            default:
                break;
        }

    }

    @OnError
    public void onError(Session session, Throwable error) {
        logger.error("发生错误");
        error.printStackTrace();
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
