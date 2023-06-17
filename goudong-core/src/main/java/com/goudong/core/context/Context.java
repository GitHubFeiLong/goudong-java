package com.goudong.core.context;

import java.util.List;
import java.util.Objects;

/**
 * 接口描述：
 * 上下文对象，尽可能的简洁
 * @author cfl
 * @version 1.0
 * @date 2022/11/4 11:36
 */
public class Context {

    /**
     * 应用id
     */
    private Long appId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户的角色集合
     */
    private List<String> roles;

    /**
     * 获取用户的{@code sessionId}
     * 如果用户登录，应该将请求令牌，设置到{@code sessionId}，避免分布式环境下值不正确。
     * @return
     */
    private String sessionId;

    public Context() {
    }

    public Context(Long appId, Long userId, List<String> roles, String sessionId) {
        this.appId = appId;
        this.userId = userId;
        this.roles = roles;
        this.sessionId = sessionId;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Context context = (Context) o;
        return Objects.equals(appId, context.appId) && Objects.equals(userId, context.userId) && Objects.equals(roles, context.roles) && Objects.equals(sessionId, context.sessionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(appId, userId, roles, sessionId);
    }

    @Override
    public String toString() {
        return "Context{" +
                "appId=" + appId +
                ", userId=" + userId +
                ", roles=" + roles +
                ", sessionId='" + sessionId + '\'' +
                '}';
    }
}
