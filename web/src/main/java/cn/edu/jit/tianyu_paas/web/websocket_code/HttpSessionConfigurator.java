package cn.edu.jit.tianyu_paas.web.websocket_code;


import cn.edu.jit.tianyu_paas.shared.global.SourceCodeConstants;

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

        sec.getUserProperties().put(SourceCodeConstants.GIT_URL, request.getParameterMap().get(SourceCodeConstants.GIT_URL).get(0));
    }
}
