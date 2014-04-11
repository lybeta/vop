package cn.skyliuyang.service.impl;

import cn.skyliuyang.dao.SessionDao;
import cn.skyliuyang.dao.impl.SessionDaoImpl;
import cn.skyliuyang.entity.Session;
import cn.skyliuyang.service.SessionService;

/**
 * Created by liuyang on 14-3-28.
 */
public class SessionServiceImpl implements SessionService {

    private SessionDao sessionDao = new SessionDaoImpl();

    @Override
    public boolean create(Session session) {
        return sessionDao.insert(session);
    }

    @Override
    public boolean delete(String id) {
        return sessionDao.remove(id);
    }

    @Override
    public boolean update(Session session) {
        return false;
    }

    @Override
    public Session get(String id) {
        return sessionDao.get(id);
    }
}
