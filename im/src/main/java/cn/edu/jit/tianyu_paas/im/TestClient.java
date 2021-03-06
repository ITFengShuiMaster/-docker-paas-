package cn.edu.jit.tianyu_paas.im;

import cn.edu.jit.tianyu_paas.im.global.MinaConstant;
import cn.edu.jit.tianyu_paas.im.message.AuthenticationMessage;
import cn.edu.jit.tianyu_paas.im.message.CommonMessage;
import com.alibaba.fastjson.JSON;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.Scanner;

public class TestClient {
    public static void main(String[] args) {
        IoConnector connector = new NioSocketConnector();
        connector.setConnectTimeoutMillis(30000);
        connector.getFilterChain().addLast("logger", new LoggingFilter());
        // 如果传byte[] 可以去掉
        connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("UTF-8"))));

        connector.setHandler(new IoHandlerAdapter() {
            @Override
            public void sessionCreated(IoSession session) throws Exception {
                super.sessionCreated(session);
            }

            @Override
            public void sessionClosed(IoSession session) throws Exception {
                super.sessionClosed(session);
            }

            @Override
            public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
                super.sessionIdle(session, status);
            }

            @Override
            public void sessionOpened(IoSession session) {

            }

            @Override
            public void messageSent(IoSession session, Object message) throws Exception {
                super.messageSent(session, message);
            }

            @Override
            public void inputClosed(IoSession session) throws Exception {
                super.inputClosed(session);
            }

            @Override
            public void messageReceived(IoSession session, Object message) {
                System.out.println("recv: " + message.toString());
            }
        });

        ConnectFuture cf = connector.connect(new InetSocketAddress(MinaConstant.MINA_SERVER_PORT));
        // 等待连接创建完成
        cf.awaitUninterruptibly();
//        cf.getSession().getCloseFuture().awaitUninterruptibly();
//        connector.dispose();

        IoSession session = cf.getSession();
        Scanner scanner = new Scanner(System.in);
        AuthenticationMessage authenticationMessage = new AuthenticationMessage();
        authenticationMessage.setUsername("test1");
        authenticationMessage.setPassword("test");
        CommonMessage commonMessage = new CommonMessage();
        commonMessage.setContent("test");
        session.write(JSON.toJSONString(authenticationMessage));
        session.write(JSON.toJSONString(commonMessage));
        while (true) {
            System.out.println("input content");
            String str = scanner.nextLine();
            commonMessage.setContent(str);
            session.write(JSON.toJSONString(commonMessage));
        }
//        ConnectFuture cf = connector.connect(new InetSocketAddress(MinaConstant.MINA_SERVER_PORT));
//        cf.awaitUninterruptibly();
//        IoSession session = cf.getSession();
//        AuthenticationMessage authenticationMessage = new AuthenticationMessage();
//        authenticationMessage.setUsername("test1");
//        authenticationMessage.setPassword("test");
//        session.write(JSON.toJSONString(authenticationMessage));
//        CommonMessage commonMessage = new CommonMessage();
//        while (true) {
//            Scanner scanner = new Scanner(System.in);
//            System.out.println("input");
//            String line = scanner.nextLine();
//            commonMessage.setContent(line);
//            session.write(JSON.toJSONString(commonMessage));
//        }
    }
}