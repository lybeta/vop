package cn.skyliuyang.service.impl;

import cn.skyliuyang.dao.AppDao;
import cn.skyliuyang.dao.impl.AppDaoImpl;
import cn.skyliuyang.entity.App;
import cn.skyliuyang.service.AppService;
import cn.skyliuyang.util.SecretUtil;

import java.util.Map;

/**
 * Created by liuyang on 14-3-26.
 */
public class AppServiceImpl implements AppService {

    private AppDao appDao = new AppDaoImpl();

    @Override
    public boolean checkSign(Map<String, Object> paramsMap) {
        return SecretUtil.checkSecret(paramsMap);
    }

    @Override
    public boolean create(App app) {
        return false;
    }

    @Override
    public boolean delete(String id) {
        return false;
    }

    @Override
    public boolean update(App app) {
        return false;
    }

    @Override
    public App get(String id) {
        return appDao.get(id);
    }
}
