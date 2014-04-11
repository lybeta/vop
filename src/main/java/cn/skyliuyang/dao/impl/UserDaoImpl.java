package cn.skyliuyang.dao.impl;

import cn.skyliuyang.dao.UserDao;
import cn.skyliuyang.entity.User;
import cn.skyliuyang.util.MongoDBUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * Created by liuyang on 14-3-28.
 */
public class UserDaoImpl implements UserDao {
    @Override
    public boolean insert(User user) {
        BasicDBObject object = new BasicDBObject();
        object.append("user_id", user.getUserId());
        object.append("password", user.getPassword());
        object.append("about", user.getAbout());
        MongoDBUtil.getInstance().insert("user", object);
        return true;
    }

    @Override
    public boolean remove(String id) {
        BasicDBObject object = new BasicDBObject("user_id", id);
        MongoDBUtil.getInstance().remove("user", object);
        return true;
    }

    @Override
    public boolean update(User user) {
        remove(user.getUserId());
        insert(user);
        return true;
    }

    @Override
    public User get(String id) {
        DBObject object = MongoDBUtil.getInstance().findOne("user", new BasicDBObject("user_id", id));
        User user = new User();
        if (object != null) {
            user.setUserId(String.valueOf(object.get("user_id")));
            user.setPassword(String.valueOf(object.get("password")));
            user.setAbout(String.valueOf(object.get("about")));
        }
        return user;
    }
}
