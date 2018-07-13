package cn.edu.jit.tianyu_paas.shared.util;


import com.alibaba.fastjson.JSONObject;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.messages.ExecCreation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class DockerUtil {
    private static final Logger logger = LoggerFactory.getLogger(DockerUtil.class);


    /**
     * ??bash.
     *
     * @param containerId ??id
     * @return ??id
     */
    public static String getExecId(String containerId) {
        try {
            return DockerHelper.query("47.106.37.79", docker -> {
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

    public static Socket getExecSocket(String execId) {
        try {
            Socket socket = new Socket("47.106.37.79", 2375);
            socket.setKeepAlive(true);
            OutputStream out = socket.getOutputStream();
            StringBuilder pw = new StringBuilder();
            pw.append("POST /exec/").append(execId).append("/start HTTP/1.1\r\n");
            pw.append("Host: " + "47.106.37.79" + ":2375\r\n");
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
}
