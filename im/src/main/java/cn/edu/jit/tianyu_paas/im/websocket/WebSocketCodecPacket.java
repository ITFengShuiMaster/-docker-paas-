/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.edu.jit.tianyu_paas.im.websocket;

import org.apache.mina.core.buffer.IoBuffer;

/**
 * Defines the class whose objects are understood by websocket encoder.
 *
 * @author DHRUV CHOPRA
 */
public class WebSocketCodecPacket {
    private IoBuffer packet;

    private WebSocketCodecPacket(IoBuffer buffer) {
        packet = buffer;
    }

    /*
     * Builds an instance of WebSocketCodecPacket that simply wraps around
     * the given IoBuffer.
     */
    public static WebSocketCodecPacket buildPacket(IoBuffer buffer) {
        return new WebSocketCodecPacket(buffer);
    }

    public IoBuffer getPacket() {
        return packet;
    }
}
