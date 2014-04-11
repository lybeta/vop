package cn.skyliuyang.entity;

/**
 * Created by liuyang on 14-3-28.
 */
public class Session {
    private String sessionId;
    private Integer exp;
    private String userId;

    public Session() {
    }

    public Session(String sessionId, String userId) {
        this.sessionId = sessionId;
        this.userId = userId;
        this.exp = -1;
    }

    public Session(String sessionId, Integer exp, String userId) {
        this.sessionId = sessionId;
        this.exp = exp;
        this.userId = userId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Integer getExp() {
        return exp;
    }

    public void setExp(Integer exp) {
        this.exp = exp;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
