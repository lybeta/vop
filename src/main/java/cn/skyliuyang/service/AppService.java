package cn.skyliuyang.service;

import cn.skyliuyang.entity.App;

import java.util.Map;

/**
 * Created by liuyang on 14-3-26.
 */
public interface AppService extends cn.skyliuyang.service.BaseService<App> {

    public boolean checkSign(Map<String, Object> paramsMap);
}
