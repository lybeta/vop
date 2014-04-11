package cn.skyliuyang.dao.impl;

import cn.skyliuyang.dao.AppDao;
import cn.skyliuyang.entity.App;
import cn.skyliuyang.util.MongoDBUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * Created by liuyang on 14-3-29.
 */
public class AppDaoImpl implements AppDao {
    @Override
    public boolean insert(App app) {
        return false;
    }

    @Override
    public boolean remove(String id) {
        return false;
    }

    @Override
    public boolean update(App app) {
        return false;
    }

    @Override
    public App get(String id) {
        DBObject object = MongoDBUtil.getInstance().findOne("app", new BasicDBObject("app_key", id));
        App app = new App();
        app.setAppKey(String.valueOf(object.get("app_key")));
        app.setSecret(String.valueOf(object.get("secret")));
        return app;
    }
}
