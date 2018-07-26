package cn.edu.jit.tianyu_paas.shared.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 卢越
 * @since 2018-06-28
 */
public class RegexUtil {

    /**
     * 正则判断字符串是否是11位数字
     *
     * @param num
     * @return
     */
    public static boolean isPhoneNumber(String num) {
        String regEx = "^1(3[0-9]|5[0-9]|8[0-9])[0-9]{8}$";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(num);
        return matcher.matches();
    }

    public static boolean isEmail(String email) {
        String regEx = "^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*\\.[a-zA-Z0-9]{2,6}$";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean isRightPath(String path) {
        String regEx = "^/\\S{1,}$";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(path);
        return matcher.matches();
    }

    public static boolean isRightVar(String var) {
        String regEx = "^[A-Z]+[A-Z0-9]{1,}(\\S{0,}[A-Z]+[A-Z0-9]{1,}){0,}$";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(var);
        return matcher.matches();
    }

    public static void main(String[] args) {
        System.out.println(isRightVar("MYSQL_"));
    }
}
