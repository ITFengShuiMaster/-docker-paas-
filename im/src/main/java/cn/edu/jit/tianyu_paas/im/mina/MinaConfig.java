package cn.edu.jit.tianyu_paas.im.mina;

import cn.edu.jit.tianyu_paas.im.global.MinaConstant;
import cn.edu.jit.tianyu_paas.im.mina.websocket.WebSocketCodecFactory;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

/**
 * mina配置类
 *
 * @author 天宇小凡
 */
//@Configuration
public class MinaConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(MinaConfig.class);

    @Bean
    public IoAcceptor ioAcceptor() throws Exception {
        IoAcceptor acceptor = new NioSocketAcceptor();
        acceptor.getFilterChain().addLast("logger", new LoggingFilter());
        // 如果传byte[] 可以去掉
        TextLineCodecFactory textLineCodecFactory = new TextLineCodecFactory(Charset.forName("UTF-8"));
        textLineCodecFactory.setDecoderMaxLineLength(1024 * 1024);
        textLineCodecFactory.setEncoderMaxLineLength(1024 * 1024);
//        acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(textLineCodecFactory));
        acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new WebSocketCodecFactory()));
        acceptor.setHandler(new MyIoHandler());
        acceptor.getSessionConfig().setReadBufferSize(2048);
        acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);

        acceptor.bind(new InetSocketAddress(MinaConstant.MINA_SERVER_PORT));
        LOGGER.info("mina 服务器在端口：" + MinaConstant.MINA_SERVER_PORT + "已经启动");
        return acceptor;
    }
}