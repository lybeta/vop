package cn.skyliuyang.util;

import cn.skyliuyang.service.AppService;
import cn.skyliuyang.service.impl.AppServiceImpl;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by liuyang on 14-3-28.
 */
public class SecretUtil {

    private static AppService appService = new AppServiceImpl();

    private SecretUtil() {

    }

    public static boolean checkSecret(Map<String, Object> params) {
        String appKey = URLUtil.decode(String.valueOf(params.get("app_key")));
        String secret = appService.get(appKey).getSecret();

        String receivedSign = URLUtil.decode(String.valueOf(params.get("sign")));
        params.remove("sign");

        Map<String, Object> sortedMap = new TreeMap<String, Object>(params);

        StringBuffer buffer = new StringBuffer();
        buffer.append(secret);
        Iterator<Map.Entry<String, Object>> iter = sortedMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String, Object> entry = iter.next();
            buffer.append(entry.getKey()).append(URLUtil.decode((String.valueOf(entry.getValue()))));
        }
        buffer.append(secret);

        String correctSign = MD5Util.encode(buffer.toString());
        System.out.println(buffer.toString());
        System.out.println(receivedSign);
        System.out.println(correctSign);

        if (correctSign.equals(receivedSign)) {
            return true;
        } else {
            return false;
        }

    }
}
