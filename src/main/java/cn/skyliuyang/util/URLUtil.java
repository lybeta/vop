package cn.skyliuyang.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Created by liuyang on 14-3-26.
 */
public class URLUtil {
    private URLUtil() {
    }

    public static String decode(String url) {
        String decodedUrl = "";
        try {
            decodedUrl = URLDecoder.decode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return decodedUrl;
    }
}
