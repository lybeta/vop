package cn.skyliuyang;

import cn.skyliuyang.util.MongoDBUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by liuyang on 14-3-27.
 */
public class MongoDBTest {

    @Test
    public void testInsertUser() {
        BasicDBObject object = new BasicDBObject();
        object.append("user_id", "four");
        object.append("password", "123456");
        object.append("about", "one coder");

        MongoDBUtil.getInstance().insert("hop", "user", object);
    }

    @Test
    public void testInsertApp() {
        BasicDBObject object = new BasicDBObject();
        object.append("app_key", "weixin");
        object.append("secret", "wx");

        MongoDBUtil.getInstance().insert("hop", "app", object);
    }

    @Test
    public void testGet() {
        BasicDBObject object = new BasicDBObject("user_id", "lybeta");
//        object.append("user_id", "lybeta");
        DBObject result = MongoDBUtil.getInstance().findOne("user", object);
        Assert.assertTrue(result != null);
        Assert.assertEquals("123456", String.valueOf(result.get("password")));
    }
}
