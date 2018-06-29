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
        String regEx = "^1(3[0-9]|5[189]|8[6789])[0-9]{8}$";
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
}
