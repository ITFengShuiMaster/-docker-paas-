package cn.edu.jit.tianyu_paas.im.websocket;

/**
 * 前后台websocket通信的协议类型
 */
public enum WebSocketMessageType {
    /**
     * 公告
     */
    NOTICE,
    /**
     * 消息（管理员推送消息给用户）
     */
    MESSAGE,
    /**
     * 应用构建信息
     */
    BUILD_APPLICATION,
    /**
     * 构建应用的结果
     */
    BUILD_APPLICATION_RESULT
}
