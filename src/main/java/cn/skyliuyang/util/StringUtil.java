package cn.skyliuyang.util;

/**
 * Created by liuyang on 14-3-26.
 */
public class StringUtil {
    private StringUtil() {
    }

    public static boolean isEmpty(String str) {
        return ((str == null) || (str.length() == 0));
    }
}
