package cn.edu.jit.tianyu_paas.web.websocket_ssh;

import cn.edu.jit.tianyu_paas.shared.entity.Machine;
import cn.edu.jit.tianyu_paas.shared.global.DockerSSHConstants;
import cn.edu.jit.tianyu_paas.shared.util.DockerClientUtil;
import cn.edu.jit.tianyu_paas.shared.util.DockerHelperUtil;
import cn.edu.jit.tianyu_paas.web.service.MachineService;
import cn.edu.jit.tianyu_paas.web.util.SpringBeanFactoryUtil;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author 卢越
 */
@ServerEndpoint(value = "/websocketssh", configurator = HttpSessionConfigurator.class)
@Component
public class SSHWebSocket {

    private static ThreadPoolExecutor socketPoolExecutor = new ThreadPoolExecutor(10, 100, 10, TimeUnit.MINUTES, new LinkedBlockingDeque<>(), new DefaultThreadFactory(SocketRunable.class));
    private final Logger logger = LoggerFactory.getLogger(SSHWebSocket.class);
    private final MachineService machineService = SpringBeanFactoryUtil.getBean(MachineService.class);
    private Socket socket;
    /**
     * http的session
     */
    private HttpSession httpSession = null;
    /**
     * websocket的session
     */
    private Session webSocketSession;
    /**
     * 用来和socket进行通信，并把socket的输出通过websocket写入到前台
     */
    private Runnable socketRunnable;

    @OnOpen
    public void onOpen(Session session, EndpointConfig config) throws Exception {
        this.webSocketSession = session;
        httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());

        String machineId = (String) config.getUserProperties().get(DockerSSHConstants.MACHINE_ID);
        String containerId = (String) config.getUserProperties().get(DockerSSHConstants.CONTAINER_ID);
        String width = (String) config.getUserProperties().get(DockerSSHConstants.WIDTH);
        String height = (String) config.getUserProperties().get(DockerSSHConstants.HEIGHT);

        Machine machine = machineService.selectById(machineId);

        //创建bash
        String execId = null;
        Socket socket = null;

        if ((execId = DockerClientUtil.getExecId(machine.getMachineIp(), containerId)) == null) {
            session.getBasicRemote().sendText("容器创建连接异常！");
            session.close();
        }

        if ((socket = DockerClientUtil.getExecSocket(machine.getMachineIp(), execId)) == null) {
            session.getBasicRemote().sendText("容器连接异常！");
            session.close();
        }
        //获得输出
        getExecMessage(webSocketSession, socket);
        //修改tty大小
        resizeTty(width, height, execId);
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        socketPoolExecutor.remove(this.socketRunnable);
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("socket close error");
            e.printStackTrace();
        }
    }

    /**
     * 收到客户端消息后调用的方法
     */
    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        OutputStream out = socket.getOutputStream();
        out.write(message.getBytes());
        out.flush();
    }

    @OnError
    public void onError(Session session, Throwable error) {

    }

    /**
     * 获得输出.
     *
     * @param session websocket session
     * @param socket  命令连接socket
     * @throws IOException
     */
    private void getExecMessage(Session session, Socket socket) {
        try {
            InputStream inputStream = socket.getInputStream();
            byte[] bytes = new byte[1024];
            StringBuilder returnMsg = new StringBuilder();
            while (true) {
                int n = inputStream.read(bytes);
                String msg = new String(bytes, 0, n);
                returnMsg.append(msg);
                bytes = new byte[10240];
                // 连接之后先获取输出，此处为 root@*****
                if (returnMsg.indexOf("\r\n\r\n") != -1) {
                    session.getBasicRemote().sendText(returnMsg.substring(returnMsg.indexOf("\r\n\r\n") + 4, returnMsg.length()));
                    break;
                }
            }
            SocketRunable socketRunable = new SocketRunable(inputStream, socket, session);
            socketPoolExecutor.execute(socketRunable);
            this.socket = socket;
            this.socketRunnable = socketRunable;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 修改tty大小.
     *
     * @param width
     * @param height
     * @param execId
     * @throws Exception
     */
    private void resizeTty(String width, String height, String execId) throws Exception {
        DockerHelperUtil.execute(DockerSSHConstants.IP, docker -> {
            docker.execResizeTty(execId, Integer.parseInt(height), Integer.parseInt(width));
        });
    }

    class SocketRunable implements Runnable {
        private Socket socket;
        private InputStream inputStream;
        private Session session;

        public SocketRunable(InputStream inputStream, Socket socket, Session session) {
            this.inputStream = inputStream;
            this.socket = socket;
            this.session = session;
        }

        @Override
        public void run() {
            try {
                byte[] bytes = new byte[1024];
                while (!socket.isClosed()) {
                    int n = inputStream.read(bytes);
                    String msg = new String(bytes, 0, n);
                    session.getBasicRemote().sendText(msg);
                    bytes = new byte[1024];
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
