/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.edu.jit.tianyu_paas.im.mina.websocket;

/**
 * Wraps around a string that represents a websocket handshake response from
 * the server to the browser.
 *
 * @author DHRUV CHOPRA
 */
public class WebSocketHandShakeResponse {

    private String response;

    public WebSocketHandShakeResponse(String response) {
        this.response = response;
    }

    public String getResponse() {
        return this.response;
    }
}
