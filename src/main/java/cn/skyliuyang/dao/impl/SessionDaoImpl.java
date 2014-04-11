package cn.skyliuyang.dao.impl;

import cn.skyliuyang.dao.SessionDao;
import cn.skyliuyang.entity.Session;
import cn.skyliuyang.util.MemcachedUtil;

/**
 * Created by liuyang on 14-3-28.
 */
public class SessionDaoImpl implements SessionDao {
    @Override
    public boolean insert(Session session) {
        MemcachedUtil.getInstance().set(session.getSessionId(), session.getExp(), session.getUserId());
        return true;
    }

    @Override
    public boolean remove(String id) {
        MemcachedUtil.getInstance().delete(id);
        return true;
    }

    @Override
    public boolean update(Session session) {
        return false;
    }

    @Override
    public Session get(String id) {
        String userId = String.valueOf(MemcachedUtil.getInstance().get(id));
        Session session = new Session(id, userId);
        return session;
    }
}
