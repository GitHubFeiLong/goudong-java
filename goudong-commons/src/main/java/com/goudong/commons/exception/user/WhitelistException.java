package com.goudong.commons.exception.user;

import com.goudong.commons.exception.enumerate.ClientExceptionEnum;
import com.goudong.commons.exception.enumerate.ServerExceptionEnum;
import com.goudong.commons.exception.BasicException;

/**
 * 类描述：
 *
 * @author msi
 * @version 1.0
 * @date 2022/1/9 1:59
 */
public class WhitelistException extends BasicException {
    /**
     * 客户端误操作造成异常
     *
     * @param exceptionEnum
     */
    public WhitelistException(ClientExceptionEnum exceptionEnum) {
        super(exceptionEnum);
    }

    /**
     * 客户端误操作造成异常
     *
     * @param exceptionEnum
     * @param clientMessage 自定义客户端提示信息
     */
    public WhitelistException(ClientExceptionEnum exceptionEnum, String clientMessage) {
        super(exceptionEnum, clientMessage);
    }

    /**
     * 客户端误操作造成异常
     *
     * @param exceptionEnum
     * @param clientMessage 自定义客户端提示信息
     * @param serverMessage
     */
    public WhitelistException(ClientExceptionEnum exceptionEnum, String clientMessage, String serverMessage) {
        super(exceptionEnum, clientMessage, serverMessage);
    }

    /**
     * 服务端异常
     *
     * @param exceptionEnum
     */
    public WhitelistException(ServerExceptionEnum exceptionEnum) {
        super(exceptionEnum);
    }

    /**
     * 服务端异常
     *
     * @param exceptionEnum
     * @param serverMessage 服务端错误信息
     */
    public WhitelistException(ServerExceptionEnum exceptionEnum, String serverMessage) {
        super(exceptionEnum, serverMessage);
    }

    /**
     * 服务端异常
     *
     * @param exceptionEnum 状态码相关
     * @param clientMessage 客户端提示
     * @param serverMessage 服务端提示
     */
    public WhitelistException(ServerExceptionEnum exceptionEnum, String clientMessage, String serverMessage) {
        super(exceptionEnum, clientMessage, serverMessage);
    }

    /**
     * 构造方法
     *
     * @param status        http状态码
     * @param code          自定义状态码
     * @param clientMessage 客户端显示信息
     * @param serverMessage 服务端日志显示信息
     */
    public WhitelistException(int status, String code, String clientMessage, String serverMessage) {
        super(status, code, clientMessage, serverMessage);
    }
}
