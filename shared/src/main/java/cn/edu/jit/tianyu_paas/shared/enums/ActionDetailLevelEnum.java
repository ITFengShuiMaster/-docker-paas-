package cn.edu.jit.tianyu_paas.shared.enums;

public enum ActionDetailLevelEnum {
    INFO(0),
    DEBUG(0),
    ERROR(0);
    int code;

    ActionDetailLevelEnum(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
