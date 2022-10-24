package com.goudong.exception.enumerate;


import com.goudong.exception.core.BasicException;

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

    /**
     * 包含状态码
     * @param status http状态码
     * @return true 包含；false 不包含
     */
    boolean containStatus(int status);

    // ~ 定义快捷异常方法
    // =================================================================================================================
    default BasicException client() {
        throw new NoSuchMethodError();
    }

    default BasicException client(String clientMessage){
        throw new NoSuchMethodError();
    }
    default BasicException client(String clientMessage, String serverMessage){
        throw new NoSuchMethodError();
    }
    default BasicException client(String clientMessageTemplate, Object[] clientMessageParams){
        throw new NoSuchMethodError();
    }
    default BasicException client(String clientMessageTemplate, Object[] clientMessageParams, String serverMessage){
        throw new NoSuchMethodError();
    }
    default BasicException client(String clientMessage, String serverMessageTemplate, Object[] serverMessageParams){
        throw new NoSuchMethodError();
    }
    default BasicException client(String clientMessageTemplate, Object[] clientMessageParams, String serverMessageTemplate, Object[] serverMessageParams){
        throw new NoSuchMethodError();
    }

    default BasicException server() {
        throw new NoSuchMethodError();
    }

    default BasicException server(String serverMessage){
        throw new NoSuchMethodError();
    }
    default BasicException server(String clientMessage, String serverMessage){
        throw new NoSuchMethodError();
    }
    default BasicException server(String serverMessageTemplate, Object[] serverMessageParams){
        throw new NoSuchMethodError();
    }
    default BasicException server(String clientMessage, String serverMessageTemplate, Object[] serverMessageParams){
        throw new NoSuchMethodError();
    }

    default BasicException server(String clientMessageTemplate, Object[] clientMessageParams, String serverMessageTemplate, Object[] serverMessageParams){
        throw new NoSuchMethodError();
    }
}
