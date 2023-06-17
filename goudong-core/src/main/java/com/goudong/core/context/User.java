package com.goudong.core.context;

/**
 * 接口描述：
 * redis中保存的用户需要实现本接口
 * @author cfl
 * @version 1.0
 * @date 2022/11/4 11:36
 */
@Deprecated
public interface User {

    Long getId();

    /**
     * 获取用户的{@code sessionId}
     * 如果用户登录，应该将其{@code sessionId}设置成用户的令牌，避免分布式环境下值不正确。
     * @return
     */
    String getSessionId();
}
