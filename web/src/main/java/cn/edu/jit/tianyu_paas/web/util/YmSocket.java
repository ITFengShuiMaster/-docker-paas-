package cn.edu.jit.tianyu_paas.web.util;

import cn.edu.jit.tianyu_paas.shared.entity.ActionDetail;
import cn.edu.jit.tianyu_paas.shared.entity.App;
import cn.edu.jit.tianyu_paas.shared.entity.AppPort;
import cn.edu.jit.tianyu_paas.shared.entity.MachinePort;
import cn.edu.jit.tianyu_paas.shared.global.DockerSSHConstants;
import cn.edu.jit.tianyu_paas.shared.global.SourceCodeConstants;
import cn.edu.jit.tianyu_paas.shared.util.CheckWord;
import cn.edu.jit.tianyu_paas.shared.util.DockerClientUtil;
import cn.edu.jit.tianyu_paas.shared.util.DockerJavaUtil;
import cn.edu.jit.tianyu_paas.shared.util.GitClone;
import cn.edu.jit.tianyu_paas.web.service.ActionDetailService;
import cn.edu.jit.tianyu_paas.web.service.AppPortService;
import cn.edu.jit.tianyu_paas.web.service.AppService;
import cn.edu.jit.tianyu_paas.web.service.MachinePortService;
import cn.edu.jit.tianyu_paas.web.websocket.TWebSocket;
import cn.edu.jit.tianyu_paas.web.websocket.WebSocketMessage;
import cn.edu.jit.tianyu_paas.web.websocket.WebSocketMessageType;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.Ports;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Date;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class YmSocket {

    private static ActionDetailService actionDetailService = SpringBeanFactoryUtil.getBean(ActionDetailService.class);
    private static AppService appService = SpringBeanFactoryUtil.getBean(AppService.class);
    private static AppPortService appPortService = SpringBeanFactoryUtil.getBean(AppPortService.class);


    public static void createByYm(String gitUrl, String branch, long userId, long actionId, App app) {

        MachinePortService machinePortService = SpringBeanFactoryUtil.getBean(MachinePortService.class);

        String execId = null;
        Socket socket = null;
        int memoryUsed = 0;
        ThreadPoolExecutor socketPoolExecutor = new ThreadPoolExecutor(10, 100, 10, TimeUnit.MINUTES, new LinkedBlockingDeque<>(), new DefaultThreadFactory(SocketRunable.class));
        org.slf4j.Logger logger = LoggerFactory.getLogger(YmSocket.class);

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
                        .withMemory((long) 104857600)
                        .withMemorySwap((long) -1)
                        .exec();
                dockerClient.startContainerCmd(createContainerResponse.getId()).exec();
                unUsedPorts.setStatus(2);
                machinePortService.update(unUsedPorts, new EntityWrapper<MachinePort>().eq("machine_id", unUsedPorts.getMachineId()).and().eq("machine_port", unUsedPorts.getMachinePort()));

                InspectContainerResponse inspectContainerResponse = dockerClient.inspectContainerCmd(createContainerResponse.getId()).exec();
                memoryUsed = (int) (inspectContainerResponse.getHostConfig().getMemory() / (1024 * 1024));
                app.setContainerId(createContainerResponse.getId());
                app.setMemoryUsed(memoryUsed);
                app.setMachineId((long) 2);
                appService.updateById(app);

                AppPort appPort = new AppPort();
                appPort.setAppId(app.getAppId());
                appPort.setProtocol(AppPort.TCP);
                appPort.setGmtCreate(new Date());
                appPort.setGmtModified(new Date());
                appPort.setMachineId((long) 1);
                appPort.setContainerPort(8080);
                appPort.setOutsideAccessUrl(DockerSSHConstants.N_IP);
                appPort.setIsOutsideOpen(AppPort.OUTOPEN);
                appPort.setIsInsideOpen(AppPort.INCLOSE);
                appPort.setHostPort(unUsedPorts.getMachinePort());
                appPort.setInsideAlias("xxxx");
                appPort.setInsideAccessUrl("xxxxx");
                appPortService.insert(appPort);

                //创建bash
                execId = DockerClientUtil.getExecId(DockerSSHConstants.N_IP, createContainerResponse.getId());
                socket = DockerClientUtil.getExecSocket(DockerSSHConstants.N_IP, execId);

                SocketRunable socketRunable = new SocketRunable(socket.getInputStream(), socket, userId, actionId);
                socketPoolExecutor.execute(socketRunable);

                OutputStream out = socket.getOutputStream();
                out.write(("sh maven.sh " + gitUrl + " " + branch + "\n").getBytes());
                out.flush();
            } catch (Exception e) {
                logger.error("容器创建失败");
                WebSocketMessage webSocketMessage = new WebSocketMessage();
                webSocketMessage.setData(0);
                webSocketMessage.setMessageType(WebSocketMessageType.BUILD_APPLICATION_RESULT);
                TWebSocket.sendMessageToUser(JSON.toJSONString(webSocketMessage), userId);
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
                        .withMemory((long) 104857600)
                        .withMemorySwap((long) -1)
                        .exec();
                dockerClient.startContainerCmd(createContainerResponse.getId()).exec();
                unUsedPorts.setStatus(2);
                machinePortService.update(unUsedPorts, new EntityWrapper<MachinePort>().eq("machine_id", unUsedPorts.getMachineId()).and().eq("machine_port", unUsedPorts.getMachinePort()));

                InspectContainerResponse inspectContainerResponse = dockerClient.inspectContainerCmd(createContainerResponse.getId()).exec();
                memoryUsed = (int) (inspectContainerResponse.getHostConfig().getMemory() / (1024 * 1024));
                app.setContainerId(createContainerResponse.getId());
                app.setMemoryUsed(memoryUsed);
                app.setMachineId((long) 2);
                appService.updateById(app);

                AppPort appPort = new AppPort();
                appPort.setAppId(app.getAppId());
                appPort.setProtocol(AppPort.TCP);
                appPort.setGmtCreate(new Date());
                appPort.setGmtModified(new Date());
                appPort.setMachineId((long) 1);
                appPort.setContainerPort(80);
                appPort.setOutsideAccessUrl(DockerSSHConstants.N_IP);
                appPort.setIsOutsideOpen(AppPort.OUTOPEN);
                appPort.setIsInsideOpen(AppPort.INCLOSE);
                appPort.setHostPort(unUsedPorts.getMachinePort());
                appPort.setInsideAlias("xxxx");
                appPort.setInsideAccessUrl("xxxxx");
                appPortService.insert(appPort);
                //创建bash
                execId = DockerClientUtil.getExecId(DockerSSHConstants.N_IP, createContainerResponse.getId());
                socket = DockerClientUtil.getExecSocket(DockerSSHConstants.N_IP, execId);

                SocketRunable socketRunable = new SocketRunable(socket.getInputStream(), socket, userId, actionId);
                socketPoolExecutor.execute(socketRunable);

                OutputStream out = socket.getOutputStream();
                out.write(("sh html.sh " + gitUrl + " " + branch + "\n").getBytes());
                out.flush();
            } catch (Exception e) {
                logger.error("容器创建失败");
                WebSocketMessage webSocketMessage = new WebSocketMessage();
                webSocketMessage.setData(0);
                webSocketMessage.setMessageType(WebSocketMessageType.BUILD_APPLICATION_RESULT);
                TWebSocket.sendMessageToUser(JSON.toJSONString(webSocketMessage), userId);
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
                        .withMemory((long) 104857600)
                        .withMemorySwap((long) -1)
                        .exec();
                dockerClient.startContainerCmd(createContainerResponse.getId()).exec();
                unUsedPorts.setStatus(2);
                machinePortService.update(unUsedPorts, new EntityWrapper<MachinePort>().eq("machine_id", unUsedPorts.getMachineId()).and().eq("machine_port", unUsedPorts.getMachinePort()));

                InspectContainerResponse inspectContainerResponse = dockerClient.inspectContainerCmd(createContainerResponse.getId()).exec();
                memoryUsed = (int) (inspectContainerResponse.getHostConfig().getMemory() / (1024 * 1024));
                app.setContainerId(createContainerResponse.getId());
                app.setMemoryUsed(memoryUsed);
                app.setMachineId((long) 2);
                appService.updateById(app);

                AppPort appPort = new AppPort();
                appPort.setAppId(app.getAppId());
                appPort.setProtocol(AppPort.TCP);
                appPort.setGmtCreate(new Date());
                appPort.setGmtModified(new Date());
                appPort.setMachineId((long) 1);
                appPort.setContainerPort(5000);
                appPort.setOutsideAccessUrl(DockerSSHConstants.N_IP);
                appPort.setIsOutsideOpen(AppPort.OUTOPEN);
                appPort.setIsInsideOpen(AppPort.INCLOSE);
                appPort.setHostPort(unUsedPorts.getMachinePort());
                appPort.setInsideAlias("xxxx");
                appPort.setInsideAccessUrl("xxxxx");
                appPortService.insert(appPort);

                //创建bash
                execId = DockerClientUtil.getExecId(DockerSSHConstants.N_IP, createContainerResponse.getId());
                socket = DockerClientUtil.getExecSocket(DockerSSHConstants.N_IP, execId);

                SocketRunable socketRunable = new SocketRunable(socket.getInputStream(), socket, userId, actionId);
                socketPoolExecutor.execute(socketRunable);

                OutputStream out = socket.getOutputStream();
                out.write(("sh nodejs.sh " + gitUrl + " " + branch + "\n").getBytes());
                out.flush();
            } catch (Exception e) {
                logger.error("容器创建失败");
                WebSocketMessage webSocketMessage = new WebSocketMessage();
                webSocketMessage.setData(0);
                webSocketMessage.setMessageType(WebSocketMessageType.BUILD_APPLICATION_RESULT);
                TWebSocket.sendMessageToUser(JSON.toJSONString(webSocketMessage), userId);
                e.printStackTrace();
            }
        }

    }

    public static class SocketRunable implements Runnable {
        private Socket socket;
        private InputStream inputStream;
        private long userId;
        private long actionId;


        @Autowired
        public SocketRunable(InputStream inputStream, Socket socket, long userId, long actionId) {
            this.inputStream = inputStream;
            this.socket = socket;
            this.userId = userId;
            this.actionId = actionId;
        }

        @Override
        public void run() {
            try {
                byte[] bytes = new byte[10240];
                while (true) {
                    try {
                        int n = inputStream.read(bytes);
                        String msg = new String(bytes, 0, n);
                        String[] lineMsgs = msg.split("\\n");
                        for (String m : lineMsgs) {
                            if (m.contains("success") || m.contains("fail")) {
                                m = new Date() + " " + m;
                            }
                            WebSocketMessage webSocketMessage = new WebSocketMessage();
                            webSocketMessage.setData(m.replace("%", " "));
                            webSocketMessage.setMessageType(WebSocketMessageType.BUILD_APPLICATION);
                            TWebSocket.sendMessageToUser(JSON.toJSONString(webSocketMessage), userId);
                            //System.out.println(m.replace("%"," "));
                            ActionDetail actionDetail = new ActionDetail();
                            actionDetail.setActionId(actionId);
                            actionDetail.setGmtCreate(new Date());
                            actionDetail.setContent(m);
                            if (m.contains("fail")) {
                                actionDetail.setLevel(2);
                            } else {
                                actionDetail.setLevel(0);
                            }
                            actionDetailService.insert(actionDetail);
                        }
                        if (msg.contains("success:run")) {
                            WebSocketMessage webSocketMessage = new WebSocketMessage();
                            webSocketMessage.setData(1);
                            webSocketMessage.setMessageType(WebSocketMessageType.BUILD_APPLICATION_RESULT);
                            TWebSocket.sendMessageToUser(JSON.toJSONString(webSocketMessage), userId);
                            break;
                        }
                        bytes = new byte[10240];
                    } catch (Exception e) {
                        e.printStackTrace();
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
