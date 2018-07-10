package cn.edu.jit.tianyu_paas.im.mina;

import cn.edu.jit.tianyu_paas.im.entity.User;
import cn.edu.jit.tianyu_paas.im.service.UserService;
import cn.edu.jit.tianyu_paas.shared.mina_message.AuthenticationMessage;
import cn.edu.jit.tianyu_paas.shared.mina_message.CommonMessage;
import cn.edu.jit.tianyu_paas.shared.mina_message.MinaMessage;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author 天宇小凡
 */
public class MyIoHandler extends IoHandlerAdapter {

    /**
     * 在线用户列表，session和用户id。线程安全
     */
    private static ConcurrentMap<IoSession, Long> onlineUserMap = new ConcurrentHashMap<>();
    private static ConcurrentMap<Long, User> onlineUserInfo = new ConcurrentHashMap<>();

    private static final Logger LOGGER = LoggerFactory.getLogger(MyIoHandler.class);

    @Autowired
    private UserService userService;

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
        LOGGER.info("close");
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

    /**
     * 处理普通消息，也就是发送信息给用户
     */
    private void handleCommonMessage(IoSession session, CommonMessage commonMessage) {
        JSONObject jsonObject = new JSONObject();
//        onlineUserMap.get(commonMessage.getReceiver()).write(jsonObject.toJSONString());
    }

    /**
     * 进行用户验证，并把用户信息保存起来
     */
    private void handleAuthenticationMessage(IoSession ioSession, AuthenticationMessage authenticationMessage) {
        User user = userService.authenticateUser(authenticationMessage.getUsername(), authenticationMessage.getPaasword());
        if (user == null) {
            ioSession.write("authentication failure");
            return;
        }
        onlineUserMap.put(ioSession, user.getUserId());
        onlineUserInfo.put(user.getUserId(), user);
    }
}