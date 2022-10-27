package com.goudong.commons.exception.redis;

/**
 * 类描述：
 *
 * @Author e-Feilong.Chen
 * @Date 2022/1/10 16:02
 */
public class RedisToolException extends RuntimeException {
    //~construct methods
    //==================================================================================================================

    /**
     * 客户端误操作造成异常
     *
     * @param clientMessage 自定义客户端提示信息
     */
    public RedisToolException(String clientMessage) {
        super(clientMessage);
    }
}
