package cn.edu.jit.tianyu_paas.shared.util;

public class StringUtil {
    public static boolean isEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }

    public static boolean isAnyEmpty(String... str) {
        if (str == null) {
            return true;
        }
        for (String i : str) {
            if (isEmpty(i)) {
                return true;
            }
        }
        return false;
    }

}
