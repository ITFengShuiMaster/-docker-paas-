package cn.edu.jit.tianyu_paas.shared.util;

import cn.edu.jit.tianyu_paas.shared.entity.AppPort;
import cn.edu.jit.tianyu_paas.shared.entity.AppVar;
import cn.edu.jit.tianyu_paas.shared.entity.MarketApp;
import cn.edu.jit.tianyu_paas.shared.entity.MountSettings;
import cn.edu.jit.tianyu_paas.shared.global.DockerSSHConstants;
import cn.edu.jit.tianyu_paas.shared.global.MountSettingsConstants;
import com.alibaba.fastjson.JSONObject;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.messages.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.*;

/**
 * @author 卢越
 */
public class DockerClientUtil {
    private static final Logger logger = LoggerFactory.getLogger(DockerClientUtil.class);


    /**
     * 创建bash.
     *
     * @param containerId 容器id
     * @return 命令id
     */
    public static String getExecId(String ip, String containerId) {
        try {
            return DockerHelperUtil.query(ip, docker -> {
                ExecCreation execCreation = docker.execCreate(containerId, new String[]{"/bin/bash"},
                        DockerClient.ExecCreateParam.attachStdin(), DockerClient.ExecCreateParam.attachStdout(), DockerClient.ExecCreateParam.attachStderr(),
                        DockerClient.ExecCreateParam.tty(true));
                return execCreation.id();
            });
        } catch (Exception e) {
            logger.warn("connect docker container errror:" + containerId);
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 连接bash
     *
     * @param ip
     * @param execId
     * @return
     */
    public static Socket getExecSocket(String ip, String execId) {
        try {
            Socket socket = new Socket(ip, 2375);
            socket.setKeepAlive(true);
            OutputStream out = socket.getOutputStream();
            StringBuilder pw = new StringBuilder();
            pw.append("POST /exec/").append(execId).append("/start HTTP/1.1\r\n");
            pw.append("Host: " + ip + ":2375\r\n");
            pw.append("User-Agent: Docker-Client\r\n");
            pw.append("Content-Type: application/json\r\n");
            pw.append("Connection: Upgrade\r\n");
            JSONObject obj = new JSONObject();
            obj.put("Detach", false);
            obj.put("Tty", true);
            String json = obj.toJSONString();
            pw.append("Content-Length: " + json.length() + "\r\n");
            pw.append("Upgrade: tcp\r\n");
            pw.append("\r\n");
            pw.append(json);
            out.write(pw.toString().getBytes("UTF-8"));
            out.flush();
            return socket;
        } catch (IOException e) {
            logger.warn("connect docker socket errror:");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获得新镜相
     *
     * @param containerId
     * @return
     */
    public static String getNewImage(String ip, String containerId) {
        try {
            return DockerHelperUtil.query(ip, docker -> {

                //将容器提交为一个新的镜相
                ContainerInfo containerInfo = docker.inspectContainer(containerId);
                String imageName = MailUtil.getRandomEmailCode();
                final ContainerCreation newContainer = docker.commitContainer(containerId, imageName, "latest", containerInfo.config(), String.format(MountSettingsConstants.COMMIT_CONTENT, containerInfo.image()), "newContainer");
                return imageName;
            });
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获得绑定端口
     *
     * @param appPorts
     * @param exposePorts
     * @return
     */
    public static Map<String, List<PortBinding>> getContainerPortBinds(List<AppPort> appPorts, Set<String> exposePorts) {
        final Map<String, List<PortBinding>> portBindings = new HashMap<>(16);

        for (AppPort port : appPorts) {
            List<PortBinding> hostPorts = new ArrayList<>();
            hostPorts.add(PortBinding.of("0.0.0.0", port.getHostPort()));
            portBindings.put(port.getContainerPort().toString(), hostPorts);
            if (port.getIsOutsideOpen() == 1) {
                exposePorts.add(port.getContainerPort().toString());
            }
        }
        return portBindings;
    }

    /**
     * 获得环境变量
     *
     * @param appVars
     * @return
     */
    public static List<String> getContainerEnvs(List<AppVar> appVars) {
        List<String> envs = new ArrayList<>();
        for (AppVar var : appVars) {
            envs.add(var.getVarName() + "=" + var.getVarValue());
        }
        return envs;
    }

    /**
     * 获得挂载
     *
     * @param mountSettings
     * @return
     */
    public static List<String> getContainerMounts(List<MountSettings> mountSettings) {
        List<String> mountSettingsList = new ArrayList<>();
        for (MountSettings ms : mountSettings) {
            mountSettingsList.add(ms.getServerMountName() + ":" + ms.getContainerMountName());
        }
        return mountSettingsList;
    }

    /**
     * 是否在数据库中
     *
     * @param marketApps
     * @param image
     * @return
     */
    public static boolean isInMarketApp(List<MarketApp> marketApps, String image) {
        for (MarketApp app : marketApps) {
            if (image.equals(app.getName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 创建新容器
     *
     * @param oldContainerId
     * @param newImageName
     * @param portBindings
     * @param exposePorts
     * @param envs
     * @param mountSettingsList
     * @return
     */
    public static String createNewContainer(String ip, String oldContainerId, String newImageName, Map<String, List<PortBinding>> portBindings, Set<String> exposePorts, List<String> envs, List<String> mountSettingsList, List<MarketApp> marketApps) {
        final String[] newContainerId = new String[1];
        try {
            return DockerHelperUtil.query(ip, docker -> {
                //停止旧容器
                docker.stopContainer(oldContainerId, 0);

                final HostConfig hostConfig =
                        HostConfig.builder().portBindings(portBindings)
                                .appendBinds(mountSettingsList.toArray(new String[mountSettingsList.size()]))
                                .build();

                final ContainerConfig volumeConfig =
                        ContainerConfig.builder()
                                .image(newImageName).exposedPorts(exposePorts)
                                .hostConfig(hostConfig)
                                .env(envs)
                                .build();

                //开启新容器
                ContainerCreation containerCreation = docker.createContainer(volumeConfig);
                //保存新容器id，以便发生错误后扫尾
                newContainerId[0] = containerCreation.id();
                docker.startContainer(containerCreation.id());

                ContainerInfo containerInfo = docker.inspectContainer(oldContainerId);
                //移除旧镜相
                if (!isInMarketApp(marketApps, containerInfo.config().image())) {
                    docker.removeImage(containerInfo.config().image(), true, true);
                }
                //移除旧容器
                docker.removeContainer(oldContainerId);

                return containerCreation.id();
            });
        } catch (Exception e) {
            try {
                DockerHelperUtil.execute(ip, docker -> {
                    //如果新容器启动成功，停止并删除镜相
                    if (!StringUtil.isEmpty(newContainerId[0])) {
                        docker.stopContainer(newContainerId[0], 0);
                        docker.removeContainer(newContainerId[0]);
                    }

                    //开启旧容器
                    docker.startContainer(oldContainerId);
                    //移除新镜相
                    docker.removeImage(newImageName, true, true);
                });
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 开启应用
     *
     * @param ip
     * @param containerId
     * @return
     */
    public static boolean startContainer(String ip, String containerId) {
        try {
            DockerHelperUtil.execute(ip, docker -> {
                docker.startContainer(containerId);
            });
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 关闭应用
     *
     * @param ip
     * @param containerId
     * @return
     */
    public static boolean stopContainer(String ip, String containerId) {
        try {
            DockerHelperUtil.execute(ip, docker -> {
                docker.stopContainer(containerId, 0);
            });
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 重启应用
     *
     * @param ip
     * @param containerId
     * @return
     */
    public static boolean reStartContainer(String ip, String containerId) {
        try {
            DockerHelperUtil.execute(ip, docker -> {
                docker.restartContainer(containerId);
            });
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除容器
     *
     * @param ip
     * @param containerId
     * @return
     */
    public static boolean removeContainer(String ip, String containerId) {
        try {
            DockerHelperUtil.execute(ip, docker -> {
                if (isRunning(ip, containerId)) {
                    docker.stopContainer(containerId, 0);
                }
                docker.removeContainer(containerId);
            });
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 容器是否运行
     *
     * @param ip
     * @param containerId
     * @return
     */
    public static boolean isRunning(String ip, String containerId) {
        try {
            return DockerHelperUtil.query(ip, docker -> {
                ContainerInfo containerInfo = docker.inspectContainer(containerId);
                return containerInfo.state().running();
            });

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 拉取镜相
     * @param ip
     * @param image
     * @param tag
     * @return
     */
    public static boolean pullImage(String ip, String image, String tag) {
        try {
            DockerHelperUtil.execute(ip, docker -> {
                docker.pull(image + ":" + tag);
            });
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) {
        System.out.println(removeContainer(DockerSSHConstants.IP, "253906c6a25d"));
    }
}
