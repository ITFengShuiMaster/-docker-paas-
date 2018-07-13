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

    public static boolean isAllEmpty(String... str) {
        if (str == null) {
            return true;
        }

        int count = 0;
        for (String i : str) {
            if (isEmpty(i)) {
                count++;
            }
        }

        return count == str.length;

    }

    public static void main(String[] args) {
        System.out.println(isAllEmpty("", ""));
    }

}
