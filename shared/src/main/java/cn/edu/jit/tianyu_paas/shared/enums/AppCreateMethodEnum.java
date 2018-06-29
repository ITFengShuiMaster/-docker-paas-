package cn.edu.jit.tianyu_paas.shared.enums;

/**
 * @author 天宇小凡
 */

public enum AppCreateMethodEnum {
    /**
     * 自定义源码创建（git地址）
     */
    CUSTOM,
    /**
     * 从官方demo创建
     */
    DEMO,
    /**
     * 从github创建（要先绑定github）
     */
    GITHUB,
    /**
     * 从docker镜像创建
     */
    DOCKER_IMAGE,
    /**
     * 从docker run创建
     */
    DOCKER_RUN,
    /**
     * 从应用市场创建
     */
    MARKET
}
