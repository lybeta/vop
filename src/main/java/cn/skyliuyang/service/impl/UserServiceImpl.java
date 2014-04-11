package cn.skyliuyang.service.impl;

import cn.skyliuyang.dao.UserDao;
import cn.skyliuyang.dao.impl.UserDaoImpl;
import cn.skyliuyang.entity.User;
import cn.skyliuyang.service.UserService;
import cn.skyliuyang.util.MemcachedUtil;
import cn.skyliuyang.util.UUIDUtil;

/**
 * Created by liuyang on 14-3-27.
 */
public class UserServiceImpl implements UserService {

    private UserDao userDao = new UserDaoImpl();

    @Override
    public String login(User user) {
        String userId = user.getUserId();
        String password = user.getPassword();
        User realUser = get(userId);
        String sessionId = "";
        if (password.equals(realUser.getPassword())) {
            sessionId = UUIDUtil.generateUUID();
            MemcachedUtil.getInstance().set(sessionId, 3600 * 24 * 30, userId);
        }
        return sessionId;
    }

    @Override
    public boolean logout(String sessionId) {
        MemcachedUtil.getInstance().delete(sessionId);
        return true;
    }

    @Override
    public boolean create(User user) {
        userDao.insert(user);
        return true;
    }

    @Override
    public boolean delete(String id) {
        return false;
    }

    @Override
    public boolean update(User user) {
        return false;
    }

    @Override
    public User get(String id) {
        return userDao.get(id);
    }
}
