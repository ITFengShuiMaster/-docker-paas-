package cn.edu.jit.tianyu_paas.im;

import cn.edu.jit.tianyu_paas.im.global.MinaConstant;
import cn.edu.jit.tianyu_paas.im.message.AuthenticationMessage;
import cn.edu.jit.tianyu_paas.im.message.CommonMessage;
import com.alibaba.fastjson.JSON;
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

public class TestClient2 {
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
                Scanner sc = new Scanner(System.in);
                System.out.println("input:   ");
                String input = sc.nextLine();

                while (true) {
                    CommonMessage commonMessage = new CommonMessage();
                    commonMessage.setContent(input);
                    session.write(JSON.toJSONString(commonMessage));
                    System.out.println("input:   ");
                    input = sc.nextLine();
                }
            }

            @Override
            public void sessionOpened(IoSession session) {
                AuthenticationMessage authenticationMessage = new AuthenticationMessage();
                authenticationMessage.setUsername("test2");
                authenticationMessage.setPassword("test");
                CommonMessage commonMessage = new CommonMessage();
                commonMessage.setContent("test2 message");
                session.write(JSON.toJSONString(authenticationMessage));
                session.write(JSON.toJSONString(commonMessage));
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

        connector.connect(new InetSocketAddress(MinaConstant.MINA_SERVER_PORT));

    }
}