package cn.skyliuyang.util;

import java.util.UUID;

/**
 * Created by liuyang on 14-3-26.
 */
public class UUIDUtil {
    private UUIDUtil() {
    }

    public static String generateUUID() {
        String s = UUID.randomUUID().toString();
        return s.replaceAll("-", "");
    }
}
