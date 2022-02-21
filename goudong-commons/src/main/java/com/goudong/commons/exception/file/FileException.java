package com.goudong.commons.exception.file;

import com.goudong.commons.enumerate.core.ClientExceptionEnum;
import com.goudong.commons.enumerate.core.ServerExceptionEnum;
import com.goudong.commons.exception.BasicException;

/**
 * 类描述：
 * 文件模块的异常
 * @Author e-Feilong.Chen
 * @Date 2022/2/21 15:12
 */
public class FileException extends BasicException {


    //~construct methods
    //==================================================================================================================

    /**
     * 客户端误操作造成异常
     *
     * @param exceptionEnum
     */
    public FileException(ClientExceptionEnum exceptionEnum) {
        super(exceptionEnum);
    }

    /**
     * 客户端误操作造成异常
     *
     * @param exceptionEnum
     * @param clientMessage 自定义客户端提示信息
     */
    public FileException(ClientExceptionEnum exceptionEnum, String clientMessage) {
        super(exceptionEnum, clientMessage);
    }

    /**
     * 客户端误操作造成异常
     *
     * @param exceptionEnum
     * @param clientMessage 自定义客户端提示信息
     * @param serverMessage
     */
    public FileException(ClientExceptionEnum exceptionEnum, String clientMessage, String serverMessage) {
        super(exceptionEnum, clientMessage, serverMessage);
    }

    /**
     * 服务端异常
     *
     * @param exceptionEnum
     */
    public FileException(ServerExceptionEnum exceptionEnum) {
        super(exceptionEnum);
    }

    /**
     * 服务端异常
     *
     * @param exceptionEnum
     * @param serverMessage 服务端错误信息
     */
    public FileException(ServerExceptionEnum exceptionEnum, String serverMessage) {
        super(exceptionEnum, serverMessage);
    }

    /**
     * 服务端异常
     *
     * @param exceptionEnum 状态码相关
     * @param clientMessage 客户端提示
     * @param serverMessage 服务端提示
     */
    public FileException(ServerExceptionEnum exceptionEnum, String clientMessage, String serverMessage) {
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
    public FileException(int status, String code, String clientMessage, String serverMessage) {
        super(status, code, clientMessage, serverMessage);
    }
}
