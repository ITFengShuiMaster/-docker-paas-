package cn.edu.jit.tianyu_paas.shared.util;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

public class MapUtil {
    /**
     * 得到linkedhashmp的最后一个元素
     */
    public static <K, V> Map.Entry<K, V> getTailByReflection(LinkedHashMap<K, V> map) {
        Field tail = null;
        try {
            tail = map.getClass().getDeclaredField("tail");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        tail.setAccessible(true);
        try {
            return (Map.Entry<K, V>) tail.get(map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
