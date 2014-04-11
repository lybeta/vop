package cn.skyliuyang.util;

import net.spy.memcached.MemcachedClient;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Created by liuyang on 14-3-27.
 */
public class MemcachedUtil {

    private static String host = System.getProperty("memcached.host", "127.0.0.1");
    private static Integer port = Integer.valueOf(System.getProperty("memcached.port", "11211"));
    private static MemcachedClient client;

    private MemcachedUtil() {
        connect();
    }

    private static class Holder {
        static MemcachedUtil instance = new MemcachedUtil();
    }

    public static MemcachedUtil getInstance() {
        return Holder.instance;
    }

    public static void connect() {
        try {
            client = new MemcachedClient(new InetSocketAddress(host, port));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void set(String key, Integer exp, Object value) {
        client.set(key, exp, value);
    }

    public Object get(String key) {
        return client.get(key);
    }

    public void delete(String key) {
        System.out.println(key);
        client.delete(key);
    }

}
