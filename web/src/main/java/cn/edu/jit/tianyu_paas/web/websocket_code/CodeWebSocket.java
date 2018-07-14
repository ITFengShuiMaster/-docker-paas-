package cn.edu.jit.tianyu_paas.web.websocket_code;

import cn.edu.jit.tianyu_paas.shared.entity.MachinePort;
import cn.edu.jit.tianyu_paas.shared.global.SourceCodeConstants;
import cn.edu.jit.tianyu_paas.shared.util.*;
import cn.edu.jit.tianyu_paas.web.service.MachinePortService;
import cn.edu.jit.tianyu_paas.web.util.SpringBeanFactoryUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.Ports;
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
import java.util.Date;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author 卢越
 */
@ServerEndpoint(value = "/websocket-code", configurator = HttpSessionConfigurator.class)
@Component
public class CodeWebSocket {

    private static ThreadPoolExecutor socketPoolExecutor = new ThreadPoolExecutor(10, 100, 10, TimeUnit.MINUTES, new LinkedBlockingDeque<>(), new DefaultThreadFactory(SocketRunable.class));
    private final Logger logger = LoggerFactory.getLogger(CodeWebSocket.class);
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

    private MachinePortService machinePortService = SpringBeanFactoryUtil.getBean(MachinePortService.class);

    @OnOpen
    public void onOpen(Session session, EndpointConfig config) throws Exception {
        this.webSocketSession = session;
        httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());

        String gitUrl = (String) config.getUserProperties().get(SourceCodeConstants.GIT_URL);

        String execId = null;
        Socket socket = null;
        String filePath = GitClone.cloneRepository(gitUrl);
        String language = CheckWord.existsCheck(filePath);


        if ("java".equals(language)) {

            MachinePort unUsedPorts = machinePortService.selectList(new EntityWrapper<MachinePort>().eq("status", 1).last("limit 10")).get(0);

            ExposedPort tcp8080 = ExposedPort.tcp(8080);
            Ports portBindings = new Ports();
            portBindings.bind(tcp8080, Ports.Binding.bindPort(unUsedPorts.getMachinePort()));

            try {
                DockerClient dockerClient = DockerJavaUtil.getDockerClient(DockerSSHConstants.N_IP);
                CreateContainerResponse createContainerResponse = dockerClient.createContainerCmd(SourceCodeConstants.JAVA_CONTAINER_NAME)
                        .withExposedPorts(tcp8080)
                        .withPortBindings(portBindings)
                        .exec();
                dockerClient.startContainerCmd(createContainerResponse.getId()).exec();
                unUsedPorts.setStatus(2);
                machinePortService.update(unUsedPorts, new EntityWrapper<MachinePort>().eq("machine_id", unUsedPorts.getMachineId()).and().eq("machine_port", unUsedPorts.getMachinePort()));

                //创建bash
                execId = DockerClientUtil.getExecId(DockerSSHConstants.N_IP, createContainerResponse.getId());
                socket = DockerClientUtil.getExecSocket(DockerSSHConstants.N_IP, execId);

                SocketRunable socketRunable = new SocketRunable(socket.getInputStream(), socket, webSocketSession);
                socketPoolExecutor.execute(socketRunable);

                OutputStream out = socket.getOutputStream();
                out.write(("sh maven.sh " + gitUrl + "\n").getBytes());
                out.flush();
            } catch (Exception e) {
                logger.error("容器创建失败");
                webSocketSession.getBasicRemote().sendText("容器创建失败");
                e.printStackTrace();
            }
        } else if ("html".equals(language)) {
            MachinePort unUsedPorts = machinePortService.selectList(new EntityWrapper<MachinePort>().eq("status", 1).last("limit 10")).get(0);

            ExposedPort tcp80 = ExposedPort.tcp(80);
            Ports portBindings = new Ports();
            portBindings.bind(tcp80, Ports.Binding.bindPort(unUsedPorts.getMachinePort()));

            try {
                DockerClient dockerClient = DockerJavaUtil.getDockerClient(DockerSSHConstants.N_IP);
                CreateContainerResponse createContainerResponse = dockerClient.createContainerCmd(SourceCodeConstants.HTML_CONTAINER_NAME)
                        .withExposedPorts(tcp80)
                        .withPortBindings(portBindings)
                        .exec();
                dockerClient.startContainerCmd(createContainerResponse.getId()).exec();
                unUsedPorts.setStatus(2);
                machinePortService.update(unUsedPorts, new EntityWrapper<MachinePort>().eq("machine_id", unUsedPorts.getMachineId()).and().eq("machine_port", unUsedPorts.getMachinePort()));

                //创建bash
                execId = DockerClientUtil.getExecId(DockerSSHConstants.N_IP, createContainerResponse.getId());
                socket = DockerClientUtil.getExecSocket(DockerSSHConstants.N_IP, execId);

                SocketRunable socketRunable = new SocketRunable(socket.getInputStream(), socket, webSocketSession);
                socketPoolExecutor.execute(socketRunable);

                OutputStream out = socket.getOutputStream();
                out.write(("sh html.sh " + gitUrl + "\n").getBytes());
                out.flush();
            } catch (Exception e) {
                logger.error("容器创建失败");
                webSocketSession.getBasicRemote().sendText("容器创建失败");
                e.printStackTrace();
            }
        } else if ("nodejs".equals(language)) {
            MachinePort unUsedPorts = machinePortService.selectList(new EntityWrapper<MachinePort>().eq("status", 1).last("limit 10")).get(0);

            ExposedPort tcp5000 = ExposedPort.tcp(5000);
            Ports portBindings = new Ports();
            portBindings.bind(tcp5000, Ports.Binding.bindPort(unUsedPorts.getMachinePort()));

            try {
                DockerClient dockerClient = DockerJavaUtil.getDockerClient(DockerSSHConstants.N_IP);
                CreateContainerResponse createContainerResponse = dockerClient.createContainerCmd(SourceCodeConstants.NODEJS_CONTAINER_NAME)
                        .withCmd("sh", "-c", "while :; do sleep 1; done")
                        .withExposedPorts(tcp5000)
                        .withPortBindings(portBindings)
                        .exec();
                dockerClient.startContainerCmd(createContainerResponse.getId()).exec();
                unUsedPorts.setStatus(2);
                machinePortService.update(unUsedPorts, new EntityWrapper<MachinePort>().eq("machine_id", unUsedPorts.getMachineId()).and().eq("machine_port", unUsedPorts.getMachinePort()));

                //创建bash
                execId = DockerClientUtil.getExecId(DockerSSHConstants.N_IP, createContainerResponse.getId());
                socket = DockerClientUtil.getExecSocket(DockerSSHConstants.N_IP, execId);

                SocketRunable socketRunable = new SocketRunable(socket.getInputStream(), socket, webSocketSession);
                socketPoolExecutor.execute(socketRunable);

                OutputStream out = socket.getOutputStream();
                out.write(("sh nodejs.sh " + gitUrl + "\n").getBytes());
                out.flush();
            } catch (Exception e) {
                logger.error("容器创建失败");
                webSocketSession.getBasicRemote().sendText("容器创建失败");
                e.printStackTrace();
            }
        }
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
            logger.error("socket close erroar");
            e.printStackTrace();
        }
    }

    /**
     * 收到客户端消息后调用的方法
     */
    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
    }

    @OnError
    public void onError(Session session, Throwable error) {
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
                while (true) {
                    int n = inputStream.read(bytes);
                    String msg = new String(bytes, 0, n);
                    if (msg.contains("success") || msg.contains("fail")) {
                        session.getBasicRemote().sendText(new Date() + " " + msg.replace("%", " "));
                        System.out.printf(new Date() + " " + msg.replace("%", ""));
                    }
                    bytes = new byte[1024];
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
