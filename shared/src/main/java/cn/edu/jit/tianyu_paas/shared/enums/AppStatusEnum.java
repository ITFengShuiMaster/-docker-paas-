package cn.edu.jit.tianyu_paas.shared.enums;

public enum AppStatusEnum {

    SHUTDOWN(0, "已关闭"),
    RUNNING(1, "运行中"),
    ABNORMAL(2, "异常");

    int code;
    String message;

    AppStatusEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
