package cn.edu.jit.tianyu_paas.shared.enums;

public enum ActionEnum {
    /*水平升级*/
    LEVELUPGRADE(0, "水平升级"),
    /*启动*/
    STARTUP(1, "启动"),
    /*重启*/
    REBOOT(2, "重启"),
    /*部署*/
    DEPLOY(3, "部署");

    int code;
    String message;

    ActionEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static String getMessageBycode(int code) {
        for (ActionEnum actionEnum : ActionEnum.values()) {
            if (actionEnum.getCode() == code) {
                return actionEnum.getMessage();
            }
        }
        return "";
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return this.name();
    }
}
