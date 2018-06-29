package cn.edu.jit.tianyu_paas.shared.enums;

/**
 * @author 天宇小凡
 */

public enum AppCreateMethodEnum {
    /**
     * 自定义源码创建（git地址）
     */
    CUSTOM(0),
    /**
     * 从官方demo创建
     */
    DEMO(1),
    /**
     * 从github创建（要先绑定github）
     */
    GITHUB(2),
    /**
     * 从docker镜像创建
     */
    DOCKER_IMAGE(3),
    /**
     * 从docker run创建
     */
    DOCKER_RUN(4),
    /**
     * 从应用市场创建
     */
    MARKET(5),
    /**
     * 从docker compose创建
     */
    DOCKER_COMPOSE(6);

    int code;

    AppCreateMethodEnum(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
