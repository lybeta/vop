package cn.skyliuyang.service;

import cn.skyliuyang.entity.User;

/**
 * Created by liuyang on 14-3-26.
 */
public interface UserService extends BaseService<User> {

    public String login(User user);

    public boolean logout(String sessionId);

}
