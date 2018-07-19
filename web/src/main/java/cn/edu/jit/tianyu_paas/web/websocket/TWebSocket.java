package cn.edu.jit.tianyu_paas.web.websocket;

import cn.edu.jit.tianyu_paas.web.global.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author 天宇小凡
 */
@ServerEndpoint(value = "/websocket", configurator = HttpSessionConfigurator.class)
@Component
public class TWebSocket {
    private static final Logger LOGGER = LoggerFactory.getLogger(TWebSocket.class);
    private static int onlineCount = 0;
    /**
     * concurrent包的线程安全Map，用来存放每个登录用户(session）对应的TWebSocket对象。
     */
    private static ConcurrentMap<Long, TWebSocket> webSocketMap = new ConcurrentHashMap<>();
    private final Logger logger = LoggerFactory.getLogger(TWebSocket.class);
    /**
     * websocket的session
     */
    private Session webSocketSession;

    private static synchronized int getOnlineCount() {
        return onlineCount;
    }

    private static synchronized void addOnlineCount() {
        TWebSocket.onlineCount++;
    }

    private static synchronized void subOnlineCount() {
        TWebSocket.onlineCount--;
    }

    public static boolean sendMessageToUser(String message, Long userId) {
        TWebSocket tWebSocket = webSocketMap.get(userId);
        if (tWebSocket == null) {
            LOGGER.info("user not online");
            return false;
        }
        return tWebSocket.sendMessage(message);
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        subOnlineCount();
        logger.debug("有一连接关闭！当前在线人数为" + getOnlineCount());
    }

    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
        this.webSocketSession = session;

        HttpSession httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        long userId = (long) httpSession.getAttribute(Constants.SESSION_KEY_USER_ID);

        webSocketMap.put(userId, this);
        addOnlineCount(); //在线数加1
        logger.debug("有新连接加入！当前在线人数为" + getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        logger.info("来自客户端的消息:" + message);
    }

    private boolean sendMessage(String message) {
        try {
            this.webSocketSession.getBasicRemote().sendText(message);
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    @OnError
    public void onError(Session session, Throwable error) {
        logger.error("发生错误");
        error.printStackTrace();
    }
}
