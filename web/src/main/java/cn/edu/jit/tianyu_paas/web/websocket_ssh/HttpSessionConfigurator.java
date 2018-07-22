package cn.edu.jit.tianyu_paas.web.websocket_ssh;

import cn.edu.jit.tianyu_paas.shared.global.DockerSSHConstants;

import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

/**
 * @author 卢越
 */
public class HttpSessionConfigurator extends ServerEndpointConfig.Configurator {
    @Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
        HttpSession httpSession = (HttpSession) request.getHttpSession();
        sec.getUserProperties().put(HttpSession.class.getName(), httpSession);

        // TODO 有个head头不知道要不要加
        sec.getUserProperties().put(DockerSSHConstants.MACHINE_ID, request.getParameterMap().get(DockerSSHConstants.MACHINE_ID).get(0));
        sec.getUserProperties().put(DockerSSHConstants.CONTAINER_ID, request.getParameterMap().get(DockerSSHConstants.CONTAINER_ID).get(0));
        sec.getUserProperties().put(DockerSSHConstants.WIDTH, request.getParameterMap().get(DockerSSHConstants.WIDTH).get(0));
        sec.getUserProperties().put(DockerSSHConstants.HEIGHT, request.getParameterMap().get(DockerSSHConstants.HEIGHT).get(0));
    }
}
