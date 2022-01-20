package com.goudong.commons.enumerate.core;

/**
 * 接口描述：
 * 定义异常枚举的方法
 * @Author msi
 * @Date 2021-05-15 10:41
 * @Version 1.0
 */
public interface ExceptionEnumInterface {
    /**
     * 响应码
     */
    int getStatus();
    /**
     * 错误代码
     */
    String getCode();
    /**
     * 客户看见的提示信息
     */
    String getClientMessage();
    /**
     * 服务器日志信息
     */
    String getServerMessage();
}
