package cn.edu.jit.tianyu_paas.shared.util;

import java.util.Random;

public class RandomPhoneCodeUtil {

    public static String getRandomCode() {
        String codeTable = "0123456789";

        Random random = new Random();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            int num = random.nextInt(codeTable.length()) % (codeTable.length() + 1);
            code.append(codeTable.charAt(num));
        }

        return code.toString();
    }
}
