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
        switch (code) {
            case 0:
                return ActionEnum.LEVELUPGRADE.getMessage();
            case 1:
                return ActionEnum.STARTUP.getMessage();
            case 2:
                return ActionEnum.REBOOT.getMessage();
            case 3:
                return ActionEnum.DEPLOY.getMessage();
            default:
                return ActionEnum.LEVELUPGRADE.getMessage();
        }
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
