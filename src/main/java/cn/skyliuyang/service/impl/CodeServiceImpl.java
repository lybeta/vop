package cn.skyliuyang.service.impl;

import cn.skyliuyang.entity.Code;
import cn.skyliuyang.service.CodeService;
import cn.skyliuyang.util.MemcachedUtil;

/**
 * Created by liuyang on 14-3-26.
 */
public class CodeServiceImpl implements CodeService {


    @Override
    public boolean checkCode(String code, String appKey) {
        Code c = (Code) MemcachedUtil.getInstance().get(code);
        String correctAppKey = c.getAppKey();
        if (correctAppKey.equals(appKey)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String getAccessToken(String code) {
        Code c = get(code);
        return c.getAccessToken();
    }

    @Override
    public boolean create(Code code) {
        MemcachedUtil.getInstance().set(code.getCode(), 3600, code);
        return false;
    }

    @Override
    public boolean delete(String code) {
        MemcachedUtil.getInstance().delete(code);
        return false;
    }

    @Override
    public boolean update(Code code) {
        return false;
    }

    @Override
    public Code get(String code) {
        return (Code) MemcachedUtil.getInstance().get(code);
    }
}
