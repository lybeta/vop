package cn.skyliuyang.service;

import cn.skyliuyang.entity.Code;

/**
 * Created by liuyang on 14-3-26.
 */
public interface CodeService extends BaseService<Code> {

    public boolean checkCode(String code, String appKey);

    public String getAccessToken(String code);
}
