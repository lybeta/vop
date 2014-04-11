package cn.skyliuyang;

import cn.skyliuyang.util.MemcachedUtil;
import org.junit.Test;

/**
 * Created by liuyang on 14-3-27.
 */
public class MemcachedTest {

    @Test
    public void testSet() {
        MemcachedUtil.getInstance().set("aa", 10000, "bb");
    }

    @Test
    public void testGet() {
        Object result = MemcachedUtil.getInstance().get("3056ed93ff0c48d9ab2c05d449a3073c");
        System.out.println(result);
    }
}
