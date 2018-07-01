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
    private final Logger logger = LoggerFactory.getLogger(TWebSocket.class);
    private static int onlineCount = 0;

    /**
     * concurrent包的线程安全Map，用来存放每个登录用户(session）对应的TWebSocket对象。
     */
    private static ConcurrentMap<Long, TWebSocket> webSocketMap = new ConcurrentHashMap<>();
    /**
     * http的session
     */
    private HttpSession httpSession = null;
    /**
     * websocket的session
     */
    private Session webSocketSession;

    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
        this.webSocketSession = session;
        httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());

        long userId = (long) httpSession.getAttribute(Constants.SESSION_KEY_USER_ID);
        webSocketMap.put(userId, this);
        addOnlineCount(); //在线数加1
        logger.debug("有新连接加入！当前在线人数为" + getOnlineCount());
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        subOnlineCount();
        logger.debug("有一连接关闭！当前在线人数为" + getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("来自客户端的消息:" + message);
    }

    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println("发生错误");
        error.printStackTrace();
    }

    private boolean sendMessage(String message) {
        try {
            this.webSocketSession.getBasicRemote().sendText(message);
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public static boolean sendMessageToUser(Long userId, String message) {
        TWebSocket tWebSocket = webSocketMap.get(userId);
        return tWebSocket.sendMessage(message);
    }

    private static synchronized int getOnlineCount() {
        return onlineCount;
    }

    private static synchronized void addOnlineCount() {
        TWebSocket.onlineCount++;
    }

    private static synchronized void subOnlineCount() {
        TWebSocket.onlineCount--;
    }
}
