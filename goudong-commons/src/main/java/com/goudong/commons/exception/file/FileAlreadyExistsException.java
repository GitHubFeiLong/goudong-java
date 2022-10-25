package com.goudong.commons.exception.file;


import com.goudong.exception.enumerate.ClientExceptionEnum;
import com.goudong.exception.enumerate.ServerExceptionEnum;

/**
 * 类描述：
 * 文件已经存在异常
 * @Author e-Feilong.Chen
 * @Date 2022/2/25 17:16
 */
public class FileAlreadyExistsException extends FileException{
    /**
     * 客户端误操作造成异常
     *
     * @param exceptionEnum
     * @param clientMessage 自定义客户端提示信息
     */
    public FileAlreadyExistsException(ClientExceptionEnum exceptionEnum, String clientMessage) {
        super(exceptionEnum, clientMessage);
    }

    /**
     * 客户端误操作造成异常
     *
     * @param exceptionEnum
     * @param clientMessage 自定义客户端提示信息
     * @param serverMessage
     */
    public FileAlreadyExistsException(ClientExceptionEnum exceptionEnum, String clientMessage, String serverMessage) {
        super(exceptionEnum, clientMessage, serverMessage);
    }

    /**
     * 服务端异常
     *
     * @param exceptionEnum
     * @param serverMessage 服务端错误信息
     */
    public FileAlreadyExistsException(ServerExceptionEnum exceptionEnum, String serverMessage) {
        super(exceptionEnum, serverMessage);
    }

    /**
     * 服务端异常
     *
     * @param exceptionEnum 状态码相关
     * @param clientMessage 客户端提示
     * @param serverMessage 服务端提示
     */
    public FileAlreadyExistsException(ServerExceptionEnum exceptionEnum, String clientMessage, String serverMessage) {
        super(exceptionEnum, clientMessage, serverMessage);
    }
}
