package cn.edu.jit.tianyu_paas.im.global;

/**
 * @author 天宇小凡
 */
public class MinaConstant {
    /**
     * mina服务器启动的端口
     */
    public static final int MINA_SERVER_PORT = 9123;

    public static final String SESSION_KEY_USER_ID = "mina_user_id";

    public static final String SESSION_KEY_USER = "mina_user";

    public static final String SESSION_KEY_CUSTOMER_SERVICE = "customer_service";

    /**
     * 暂时的客服id，每个用户都是发给客服的。所以接收者一样，id为0
     */
    public static final Long CUSTOMER_SERVICE_ID = 0L;

}