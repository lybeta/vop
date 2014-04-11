package cn.skyliuyang.entity;

import java.io.Serializable;

/**
 * Created by liuyang on 14-3-26.
 */
public class Code implements Serializable{
    private String code;
    private String appKey;
    private String accessToken;

    public Code(String code, String appKey, String accessToken) {
        this.code = code;
        this.appKey = appKey;
        this.accessToken = accessToken;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }
}
