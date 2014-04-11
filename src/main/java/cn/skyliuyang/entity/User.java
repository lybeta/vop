package cn.skyliuyang.entity;

import cn.skyliuyang.util.URLUtil;

import java.util.Map;

/**
 * Created by liuyang on 14-3-26.
 */
public class User {
    private String userId;
    private String password;
    private String about;

    public User() {
    }

    public User(String userId, String password) {
        this.userId = userId;
        this.password = password;
        this.about = "";
    }

    public User(String userId, String password, String about) {
        this.userId = userId;
        this.password = password;
        this.about = about;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public static User toObject(Map<String, Object> params) {
        User user = new User();
        if (params.containsKey("user_id")) {
            user.setUserId(URLUtil.decode(String.valueOf(params.get("user_id"))));
        }
        if (params.containsKey("password")) {
            user.setPassword(URLUtil.decode(String.valueOf(params.get("password"))));
        }
        if (params.containsKey("about")) {
            user.setAbout(URLUtil.decode(String.valueOf(params.get("about"))));
        }
        return user;
    }
}
