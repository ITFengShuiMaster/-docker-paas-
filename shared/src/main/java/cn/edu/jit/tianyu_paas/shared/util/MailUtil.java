package cn.edu.jit.tianyu_paas.shared.util;

import java.util.UUID;

public class MailUtil {

    public static String getRandomEmailCode() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replace("-", "");
    }

    public static void main(String[] args) {
        System.out.println(getRandomEmailCode());
    }
}
