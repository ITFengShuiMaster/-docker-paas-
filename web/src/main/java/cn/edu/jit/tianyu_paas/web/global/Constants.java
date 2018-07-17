package cn.edu.jit.tianyu_paas.web.global;

public class Constants {

    public static final String SESSION_KEY_USER_ID = "user";

    public static final String CREATE_IMAGE = "http://120.77.146.118:2375/images/create?fromImage=%s&tag=latest";

    public static final String RE_SEND_EMAIL = "http://localhost:8080/tianyu-paas/users/re-email?userEmail=%s";

    public static final String MAIL_CONTEXT = "http://localhost:8080/tianyu-paas/users/active?userId=%s&code=%s";

    public static final String HEAD_TOKENE = "token";

    /**
     * token过期时间
     */
    public static final long TOKEN_MAX_VALID = 60 * 60 * 1000;
}
