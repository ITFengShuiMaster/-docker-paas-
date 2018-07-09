package cn.edu.jit.tianyu_paas.im.mina;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerHandler extends IoHandlerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerHandler.class);

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
        LOGGER.info(status.toString());
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) {
        session.closeOnFlush();
        LOGGER.warn("mina session exception: " + cause.getMessage());
    }

    @Override
    public void messageReceived(IoSession session, Object message) {
        LOGGER.info(message.toString());
        session.write(message);
    }

    @Override
    public void messageSent(IoSession session, Object message) {
        LOGGER.info(message.toString());
    }

    @Override
    public void inputClosed(IoSession session) {
        session.closeNow();
    }
}
