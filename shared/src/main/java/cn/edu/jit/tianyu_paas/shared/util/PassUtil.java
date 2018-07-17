package cn.edu.jit.tianyu_paas.shared.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/**
 * 加密解密工具类
 */
public class PassUtil {
    public static String getMD5(String str) {
        StringBuilder sb = new StringBuilder();
        char[] chars = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A',
                'B', 'C', 'D', 'E', 'F'};
        byte[] b = str.getBytes();
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("md5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
        byte[] md5 = md.digest(b);
        for (byte m : md5) {
            sb.append(chars[(m >> 4) & 0x0f]);
            sb.append(chars[m & 0x0f]);
        }
        return sb.toString();
    }

    public static String getSha1(String str) {
        if (str == null || str.length() == 0) {
            return null;
        }
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        MessageDigest mdTemp;
        try {
            mdTemp = MessageDigest.getInstance("SHA1");
            mdTemp.update(str.getBytes("UTF-8"));
            byte[] md = mdTemp.digest();
            char buf[] = new char[md.length * 2];
            int k = 0;
            for (byte b0 : md) {
                buf[k++] = hexDigits[b0 >>> 4 & 0xf];
                buf[k++] = hexDigits[b0 & 0xf];
            }
            return new String(buf);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 根据userId，当前时间戳和随机字符串，再通过md5加密生成token
     *
     * @param userId
     * @return
     */
    public static String generatorToken(long userId) {
        return getMD5(userId + System.currentTimeMillis() + UUID.randomUUID().toString());
    }

    public static String getMD5(Object obj) {
        return getMD5(obj.toString());
    }

    public static void main(String[] args) {
        //test md5 = 098F6BCD4621D373CADE4E832627B4F6
        System.out.println(getMD5("test"));
//        System.out.println(getMD5(getMD5("test")));
    }
}
