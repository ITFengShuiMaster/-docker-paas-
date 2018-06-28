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
    public static boolean regexElevenNumber(String num) {
        String regEx = "^(\\d{11})$";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(num);
        return matcher.matches();
    }
}
