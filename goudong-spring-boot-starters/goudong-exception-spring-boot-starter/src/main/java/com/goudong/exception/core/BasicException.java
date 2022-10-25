package com.goudong.exception.core;

import cn.hutool.core.bean.BeanUtil;
import com.goudong.exception.enumerate.ClientExceptionEnum;
import com.goudong.exception.enumerate.ServerExceptionEnum;
import com.goudong.exception.util.MessageFormatUtil;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static com.goudong.exception.enumerate.ClientExceptionEnum.*;
import static com.goudong.exception.enumerate.ServerExceptionEnum.SERVER_ERROR;


/**
 * 类描述：
 * 自定义异常的基类，其它模块的异常继承进行扩展
 * @ClassName BaseicException
 * @Author msi
 * @Date 2020/6/10 19:41
 * @Version 1.0
 */
@Data
public class BasicException extends RuntimeException{

    public static final Logger log = LoggerFactory.getLogger(BasicException.class);

    /**
     * http 响应码
     */
    public int status;

    /**
     * 错误代码
     */
    public String code;

    /**
     * 客户端状态码对应信息
     */
    public String clientMessage;

    /**
     * 服务器状态码对应信息
     */
    public String serverMessage;

    /**
     * 额外信息
     */
    public Map dataMap = new HashMap();

    /**
     * 根据异常对象，返回自定义的服务异常对象
     * @param throwable 异常对象
     * @return
     */
    public static BasicException generateByServer(Throwable throwable) {
        // 默认500异常
        BasicException basicException = ServerException.server(ServerExceptionEnum.SERVER_ERROR);

        if (throwable instanceof RuntimeException) {
            return ServerException.server(ServerExceptionEnum.SERVER_ERROR, throwable.getMessage(), "RuntimeException " + throwable.getMessage());
        }

        // 空指针
        if (throwable instanceof NullPointerException) {
            return ServerException.server(ServerExceptionEnum.SERVER_ERROR, "空指针异常", "NullPointerException null");
        }

        // 上传文件错误
        if (throwable instanceof MultipartException) {
            return ClientException.client(BAD_REQUEST, "上传文件失败", "MultipartException " +throwable.getMessage());
        }

        if (throwable instanceof IndexOutOfBoundsException ) {
            return ServerException.server(ServerExceptionEnum.SERVICE_UNAVAILABLE, "");
        }
        String message = throwable.getMessage();
        if (message == null) {
            return basicException;
        }

        // openFeign调用远程服务，服务还未注册到nacos中
        if (message.startsWith("com.netflix.client.ClientException")) {
            return ServerException.server(ServerExceptionEnum.SERVICE_UNAVAILABLE, message);
        }

        return basicException;
    }

    // ~ 常用静态方法
    // =================================================================================================================
    public static BasicException ofResult(Result result) {
        BasicException basicException = new BasicException(result.getStatus(), result.getCode(), result.getClientMessage(), result.getServerMessage())
                .dataMap(result.getDataMap());
        return basicException;
    }

    // ~ quick 异常
    // =================================================================================================================

    /**
     * 根据响应码（必须在{@code ClientExceptionEnum} 和 {@code ServerExceptionEnum}中已定义），返回对象
     * @param status http响应码
     * @return 4xx ClientException; 5xx ServerException
     */
    public static BasicException quick(Integer status) {
        Optional<ClientExceptionEnum> clientOptional = Stream.of(ClientExceptionEnum.values())
                .filter(f -> f.getStatus() == status)
                .findFirst();
        if (clientOptional.isPresent()) {
            return client(clientOptional.get());
        }


        Optional<ServerExceptionEnum> serverOptional = Stream.of(ServerExceptionEnum.values())
                .filter(f -> f.getStatus() == status)
                .findFirst();
        if (serverOptional.isPresent()) {
            return server(serverOptional.get());
        }

        throw new IllegalArgumentException("参数错误,状态码未定义 " + status);
    }

    /**
     * 根据响应码（必须在{@code ClientExceptionEnum} 和 {@code ServerExceptionEnum}中已定义），返回对象
     * @param status http响应码
     * @param clientMessage 客户端提示
     * @return 4xx ClientException; 5xx ServerException
     */
    public static BasicException quick(Integer status, String clientMessage) {
        return quick(status).clientMessage(clientMessage);
    }

    /**
     * 根据响应码（必须在{@code ClientExceptionEnum} 和 {@code ServerExceptionEnum}中已定义），返回对象
     * @param status http响应码
     * @param clientMessage 客户端提示
     * @param serverMessage 服务端提示
     * @return 4xx ClientException; 5xx ServerException
     */
    public static BasicException quick(Integer status, String clientMessage, String serverMessage) {
        return quick(status).clientMessage(clientMessage).serverMessage(serverMessage);
    }

    /**
     * 根据响应码（必须在{@code ClientExceptionEnum} 和 {@code ServerExceptionEnum}中已定义），返回对象
     * @param status http响应码
     * @param messageTemplate 提示模板
     * @param messageParams 提示模板参数
     * @return 4xx ClientException; 5xx ServerException
     */
    public static BasicException quick(Integer status, String messageTemplate, Object[] messageParams) {
        String message = MessageFormatUtil.format(messageTemplate, messageParams);
        BasicException quick = quick(status);
        if (status >= 400 && status < 500) {
            return quick.clientMessage(message);
        }
        return quick.serverMessage(message);
    }

    /**
     * 根据响应码（必须在{@code ClientExceptionEnum} 和 {@code ServerExceptionEnum}中已定义），返回对象
     * @param status http响应码
     * @param clientMessageTemplate 客户端提示模板
     * @param clientMessageParams 客户端提示模板参数
     * @param serverMessage 服务端提示
     * @return 4xx ClientException; 5xx ServerException
     */
    public static BasicException quick(Integer status, String clientMessageTemplate, Object[] clientMessageParams, String serverMessage) {
        String clientMessage = MessageFormatUtil.format(clientMessageTemplate, clientMessageParams);
        return quick(status).clientMessage(clientMessage).serverMessage(serverMessage);
    }

    /**
     * 根据响应码（必须在{@code ClientExceptionEnum} 和 {@code ServerExceptionEnum}中已定义），返回对象
     * @param status http响应码
     * @param clientMessageTemplate 客户端提示模板
     * @param clientMessageParams 客户端提示模板参数
     * @param serverMessageTemplate 服务端提示
     * @param serverMessageParams 服务端提示模板参数
     * @return 4xx ClientException; 5xx ServerException
     */
    public static BasicException quick(Integer status, String clientMessageTemplate, Object[] clientMessageParams, String serverMessageTemplate, Object[] serverMessageParams) {
        String clientMessage = MessageFormatUtil.format(clientMessageTemplate, clientMessageParams);
        String serverMessage = MessageFormatUtil.format(serverMessageTemplate, serverMessageParams);
        return quick(status).clientMessage(clientMessage).serverMessage(serverMessage);
    }

    // ~ 客户端异常
    // =================================================================================================================
    /**
     * 返回默认异常
     * @see ClientException#DEFAULT_EXCEPTION
     * @return
     */
    public static BasicException client() {
        return new ClientException();
    }

    /**
     * 返回默认异常,自定义clientMessage
     * @see ClientException#DEFAULT_EXCEPTION
     * @param clientMessage 客户端提示信息
     * @return
     */
    public static BasicException client(String clientMessage) {
        return new ClientException(clientMessage);
    }

    /**
     * 返回默认异常,自定义clientMessage模板
     * @see ClientException#DEFAULT_EXCEPTION
     * @param clientMessageTemplate 客户端提示信息模板
     * @param clientMessageParams 客户端模板参数
     * @return
     */
    public static BasicException client(String clientMessageTemplate, Object[] clientMessageParams) {
        return BasicException.client(ClientException.DEFAULT_EXCEPTION, clientMessageTemplate, clientMessageParams);
    }

    /**
     * 返回指定客户端异常
     * @param exceptionEnum 指定异常类型
     * @return
     */
    public static BasicException client(ClientExceptionEnum exceptionEnum) {
        return new ClientException(exceptionEnum);
    }

    /**
     * 返回指定客户端异常，自定义clientMessage
     * @param exceptionEnum 指定异常类型
     * @param clientMessage 自定义客户端提示信息
     * @return
     */
    public static BasicException client(ClientExceptionEnum exceptionEnum, String clientMessage) {
        return new ClientException(exceptionEnum, clientMessage);
    }

    /**
     * 返回指定客户端异常，自定义clientMessage和serverMessage
     * @param exceptionEnum 客户端异常类型
     * @param clientMessage 自定义客户端提示信息
     * @param serverMessage 自定义服务端提示信息
     * @return
     */
    public static BasicException client(ClientExceptionEnum exceptionEnum, String clientMessage, String serverMessage) {
        return new ClientException(exceptionEnum, clientMessage, serverMessage);
    }

    /**
     * 返回指定客户端异常，自定义clientMessageTemplate
     * @param exceptionEnum 客户端异常类型
     * @param clientMessageTemplate 自定义客户端提示信息模板
     * @param clientMessageParams 自定义客户端提示信息模板参数
     * @return
     */
    public static BasicException client(ClientExceptionEnum exceptionEnum, String clientMessageTemplate, Object[] clientMessageParams) {
        String clientMessage = MessageFormatUtil.format(clientMessageTemplate, clientMessageParams);
        return new ClientException(exceptionEnum, clientMessage);
    }

    /**
     * 返回指定客户端异常，自定义clientMessageTemplate和serverMessage
     * @param exceptionEnum 客户端异常类型
     * @param clientMessageTemplate 自定义客户端提示信息模板
     * @param clientMessageParams 自定义客户端提示信息模板参数
     * @param serverMessage 自定义服务端提示信息
     * @return
     */
    public static BasicException client(ClientExceptionEnum exceptionEnum, String clientMessageTemplate, Object[] clientMessageParams, String serverMessage) {
        return new ClientException(exceptionEnum, MessageFormatUtil.format(clientMessageTemplate, clientMessageParams), serverMessage);
    }

    /**
     * 返回指定客户端异常，自定义clientMessage和serverMessageTemplate
     * @param exceptionEnum 客户端异常类型
     * @param clientMessage 自定义客户端提示信息
     * @param serverMessageTemplate 自定义服务端提示信息模板
     * @param serverMessageParams 自定义服务端提示信息模板参数
     * @return
     */
    public static BasicException client(ClientExceptionEnum exceptionEnum, String clientMessage, String serverMessageTemplate, Object[] serverMessageParams) {
        return new ClientException(exceptionEnum, clientMessage, MessageFormatUtil.format(serverMessageTemplate, serverMessageParams));
    }

    /**
     * 返回指定客户端异常，自定义clientMessage模板和serverMessage
     * @param exceptionEnum 客户端异常类型
     * @param clientMessageTemplate 自定义客户端提示信息模板
     * @param clientMessageParams 自定义客户端提示信息模板参数
     * @param serverMessageTemplate 自定义服务端提示信息模板
     * @param serverMessageParams 自定义服务端提示信息模板参数
     * @return
     */
    public static BasicException client(ClientExceptionEnum exceptionEnum, String clientMessageTemplate, Object[] clientMessageParams, String serverMessageTemplate, Object[] serverMessageParams) {
        return new ClientException(exceptionEnum,
                MessageFormatUtil.format(clientMessageTemplate, clientMessageParams),
                MessageFormatUtil.format(serverMessageTemplate, serverMessageParams));
    }

    //~ ClientExceptionEnum.BAD_REQUEST 400
    //==================================================================================================================

    /**
     * 返回400异常
     * @return
     */
    public static BasicException clientByBadRequest() {
        return ClientException.client(BAD_REQUEST);
    }

    /**
     * 返回400异常,自定义clientMessage
     * @param clientMessage 自定义客户端提示信息
     * @return
     */
    public static BasicException clientByBadRequest(String clientMessage) {
        return ClientException.client(BAD_REQUEST, clientMessage);
    }

    /**
     * 返回400异常,自定义clientMessage和serverMessage
     * @param clientMessage 自定义客户端提示信息
     * @param serverMessage 自定义服务端提示信息
     * @return
     */
    public static BasicException clientByBadRequest(String clientMessage, String serverMessage) {
        return ClientException.client(BAD_REQUEST, clientMessage, serverMessage);
    }

    /**
     * 返回400异常,自定义clientMessageTemplate
     * @param clientMessageTemplate 自定义客户端提示信息模板
     * @param clientMessageParams 自定义客户端提示信息模板参数
     * @return
     */
    public static BasicException clientByBadRequest(String clientMessageTemplate, Object[] clientMessageParams) {
        return ClientException.client(BAD_REQUEST, clientMessageTemplate, clientMessageParams);
    }

    /**
     * 返回400异常,自定义clientMessageTemplate和serverMessage
     * @param clientMessageTemplate 自定义客户端提示信息模板
     * @param clientMessageParams 自定义客户端提示信息模板参数
     * @param serverMessage 自定义服务端提示信息
     * @return
     */
    public static BasicException clientByBadRequest(String clientMessageTemplate, Object[] clientMessageParams, String serverMessage) {
        return ClientException.client(BAD_REQUEST, clientMessageTemplate, clientMessageParams, serverMessage);
    }

    /**
     * 返回400异常,自定义clientMessage和serverMessageTemplate
     * @param clientMessage 自定义客户端提示信息
     * @param serverMessageTemplate 自定义服务端提示信息模板
     * @param serverMessageParams 自定义服务端提示信息模板参数
     * @return
     */
    public static BasicException clientByBadRequest(String clientMessage, String serverMessageTemplate, Object[] serverMessageParams) {
        return ClientException.client(BAD_REQUEST, clientMessage, serverMessageTemplate, serverMessageParams);
    }

    /**
     * 返回400异常,自定义clientMessageTemplate和serverMessageTemplate
     * @param clientMessageTemplate 自定义服客户端提示信息模板
     * @param clientMessageParams 自定义服客户端提示信息模板参数
     * @param serverMessageTemplate 自定义服务端提示信息模板
     * @param serverMessageParams 自定义服务端提示信息模板参数
     * @return
     */
    public static BasicException clientByBadRequest(String clientMessageTemplate, Object[] clientMessageParams, String serverMessageTemplate, Object[] serverMessageParams) {
        return ClientException.client(BAD_REQUEST, clientMessageTemplate, clientMessageParams, serverMessageTemplate, serverMessageParams);
    }

    //~ ClientExceptionEnum.UNAUTHORIZED 401
    //==================================================================================================================
    /**
     * 返回401异常
     * @return
     */
    public static BasicException clientByUnauthorized() {
        return ClientException.client(UNAUTHORIZED);
    }

    /**
     * 返回401异常,自定义clientMessage
     * @param clientMessage 自定义客户端提示信息
     * @return
     */
    public static BasicException clientByUnauthorized(String clientMessage) {
        return ClientException.client(UNAUTHORIZED, clientMessage);
    }

    /**
     * 返回401异常,自定义clientMessage和serverMessage
     * @param clientMessage 自定义客户端提示信息
     * @param serverMessage 自定义服务端提示信息
     * @return
     */
    public static BasicException clientByUnauthorized(String clientMessage, String serverMessage) {
        return ClientException.client(UNAUTHORIZED, clientMessage, serverMessage);
    }

    /**
     * 返回401异常,自定义clientMessageTemplate
     * @param clientMessageTemplate 自定义客户端提示信息模板
     * @param clientMessageParams 自定义客户端提示信息模板参数
     * @return
     */
    public static BasicException clientByUnauthorized(String clientMessageTemplate, Object[] clientMessageParams) {
        return ClientException.client(UNAUTHORIZED, clientMessageTemplate, clientMessageParams);
    }

    /**
     * 返回401异常,自定义clientMessageTemplate和serverMessage
     * @param clientMessageTemplate 自定义客户端提示信息模板
     * @param clientMessageParams 自定义客户端提示信息模板参数
     * @param serverMessage 自定义服务端提示信息
     * @return
     */
    public static BasicException clientByUnauthorized(String clientMessageTemplate, Object[] clientMessageParams, String serverMessage) {
        return ClientException.client(UNAUTHORIZED, clientMessageTemplate, clientMessageParams, serverMessage);
    }

    /**
     * 返回401异常,自定义clientMessage和serverMessageTemplate
     * @param clientMessage 自定义客户端提示信息
     * @param serverMessageTemplate 自定义服务端提示信息模板
     * @param serverMessageParams 自定义服务端提示信息模板参数
     * @return
     */
    public static BasicException clientByUnauthorized(String clientMessage, String serverMessageTemplate, Object[] serverMessageParams) {
        return ClientException.client(UNAUTHORIZED, clientMessage, serverMessageTemplate, serverMessageParams);
    }

    /**
     * 返回401异常,自定义clientMessageTemplate和serverMessageTemplate
     * @param clientMessageTemplate 自定义服客户端提示信息模板
     * @param clientMessageParams 自定义服客户端提示信息模板参数
     * @param serverMessageTemplate 自定义服务端提示信息模板
     * @param serverMessageParams 自定义服务端提示信息模板参数
     * @return
     */
    public static BasicException clientByUnauthorized(String clientMessageTemplate, Object[] clientMessageParams, String serverMessageTemplate, Object[] serverMessageParams) {
        return ClientException.client(UNAUTHORIZED, clientMessageTemplate, clientMessageParams, serverMessageTemplate, serverMessageParams);
    }

    //~ ClientExceptionEnum.FORBIDDEN 403
    //==================================================================================================================
    /**
     * 返回403异常
     * @return
     */
    public static BasicException clientByForbidden() {
        return ClientException.client(FORBIDDEN);
    }

    /**
     * 返回403异常,自定义clientMessage
     * @param clientMessage 自定义客户端提示信息
     * @return
     */
    public static BasicException clientByForbidden(String clientMessage) {
        return ClientException.client(FORBIDDEN, clientMessage);
    }

    /**
     * 返回403异常,自定义clientMessage和serverMessage
     * @param clientMessage 自定义客户端提示信息
     * @param serverMessage 自定义服务端提示信息
     * @return
     */
    public static BasicException clientByForbidden(String clientMessage, String serverMessage) {
        return ClientException.client(FORBIDDEN, clientMessage, serverMessage);
    }

    /**
     * 返回403异常,自定义clientMessageTemplate
     * @param clientMessageTemplate 自定义客户端提示信息模板
     * @param clientMessageParams 自定义客户端提示信息模板参数
     * @return
     */
    public static BasicException clientByForbidden(String clientMessageTemplate, Object[] clientMessageParams) {
        return ClientException.client(FORBIDDEN, clientMessageTemplate, clientMessageParams);
    }

    /**
     * 返回403异常,自定义clientMessageTemplate和serverMessage
     * @param clientMessageTemplate 自定义客户端提示信息模板
     * @param clientMessageParams 自定义客户端提示信息模板参数
     * @param serverMessage 自定义服务端提示信息
     * @return
     */
    public static BasicException clientByForbidden(String clientMessageTemplate, Object[] clientMessageParams, String serverMessage) {
        return ClientException.client(FORBIDDEN, clientMessageTemplate, clientMessageParams, serverMessage);
    }

    /**
     * 返回403异常,自定义clientMessage和serverMessageTemplate
     * @param clientMessage 自定义客户端提示信息
     * @param serverMessageTemplate 自定义服务端提示信息模板
     * @param serverMessageParams 自定义服务端提示信息模板参数
     * @return
     */
    public static BasicException clientByForbidden(String clientMessage, String serverMessageTemplate, Object[] serverMessageParams) {
        return ClientException.client(FORBIDDEN, clientMessage, serverMessageTemplate, serverMessageParams);
    }

    /**
     * 返回403异常,自定义clientMessageTemplate和serverMessageTemplate
     * @param clientMessageTemplate 自定义服客户端提示信息模板
     * @param clientMessageParams 自定义服客户端提示信息模板参数
     * @param serverMessageTemplate 自定义服务端提示信息模板
     * @param serverMessageParams 自定义服务端提示信息模板参数
     * @return
     */
    public static BasicException clientByForbidden(String clientMessageTemplate, Object[] clientMessageParams, String serverMessageTemplate, Object[] serverMessageParams) {
        return ClientException.client(FORBIDDEN, clientMessageTemplate, clientMessageParams, serverMessageTemplate, serverMessageParams);
    }

    //~ ClientExceptionEnum.NOT_FOUND 404
    //==================================================================================================================
    /**
     * 返回404异常
     * @return
     */
    public static BasicException clientByNotFound() {
        return ClientException.client(NOT_FOUND);
    }

    /**
     * 返回404异常,自定义clientMessage
     * @param clientMessage 自定义客户端提示信息
     * @return
     */
    public static BasicException clientByNotFound(String clientMessage) {
        return ClientException.client(NOT_FOUND, clientMessage);
    }

    /**
     * 返回404异常,自定义clientMessage和serverMessage
     * @param clientMessage 自定义客户端提示信息
     * @param serverMessage 自定义服务端提示信息
     * @return
     */
    public static BasicException clientByNotFound(String clientMessage, String serverMessage) {
        return ClientException.client(NOT_FOUND, clientMessage, serverMessage);
    }

    /**
     * 返回404异常,自定义clientMessageTemplate
     * @param clientMessageTemplate 自定义客户端提示信息模板
     * @param clientMessageParams 自定义客户端提示信息模板参数
     * @return
     */
    public static BasicException clientByNotFound(String clientMessageTemplate, Object[] clientMessageParams) {
        return ClientException.client(NOT_FOUND, clientMessageTemplate, clientMessageParams);
    }

    /**
     * 返回404异常,自定义clientMessageTemplate和serverMessage
     * @param clientMessageTemplate 自定义客户端提示信息模板
     * @param clientMessageParams 自定义客户端提示信息模板参数
     * @param serverMessage 自定义服务端提示信息
     * @return
     */
    public static BasicException clientByNotFound(String clientMessageTemplate, Object[] clientMessageParams, String serverMessage) {
        return ClientException.client(NOT_FOUND, clientMessageTemplate, clientMessageParams, serverMessage);
    }

    /**
     * 返回404异常,自定义clientMessage和serverMessageTemplate
     * @param clientMessage 自定义客户端提示信息
     * @param serverMessageTemplate 自定义服务端提示信息模板
     * @param serverMessageParams 自定义服务端提示信息模板参数
     * @return
     */
    public static BasicException clientByNotFound(String clientMessage, String serverMessageTemplate, Object[] serverMessageParams) {
        return ClientException.client(NOT_FOUND, clientMessage, serverMessageTemplate, serverMessageParams);
    }

    /**
     * 返回404异常,自定义clientMessageTemplate和serverMessageTemplate
     * @param clientMessageTemplate 自定义服客户端提示信息模板
     * @param clientMessageParams 自定义服客户端提示信息模板参数
     * @param serverMessageTemplate 自定义服务端提示信息模板
     * @param serverMessageParams 自定义服务端提示信息模板参数
     * @return
     */
    public static BasicException clientByNotFound(String clientMessageTemplate, Object[] clientMessageParams, String serverMessageTemplate, Object[] serverMessageParams) {
        return ClientException.client(NOT_FOUND, clientMessageTemplate, clientMessageParams, serverMessageTemplate, serverMessageParams);
    }

    //~ ClientExceptionEnum.NOT_ACCEPTABLE 406
    //==================================================================================================================
    /**
     * 返回406异常
     * @return
     */
    public static BasicException clientByNotAcceptable() {
        return ClientException.client(NOT_ACCEPTABLE);
    }

    /**
     * 返回406异常,自定义clientMessage
     * @param clientMessage 自定义客户端提示信息
     * @return
     */
    public static BasicException clientByNotAcceptable(String clientMessage) {
        return ClientException.client(NOT_ACCEPTABLE, clientMessage);
    }

    /**
     * 返回406异常,自定义clientMessage和serverMessage
     * @param clientMessage 自定义客户端提示信息
     * @param serverMessage 自定义服务端提示信息
     * @return
     */
    public static BasicException clientByNotAcceptable(String clientMessage, String serverMessage) {
        return ClientException.client(NOT_ACCEPTABLE, clientMessage, serverMessage);
    }

    /**
     * 返回406异常,自定义clientMessageTemplate
     * @param clientMessageTemplate 自定义客户端提示信息模板
     * @param clientMessageParams 自定义客户端提示信息模板参数
     * @return
     */
    public static BasicException clientByNotAcceptable(String clientMessageTemplate, Object[] clientMessageParams) {
        return ClientException.client(NOT_ACCEPTABLE, clientMessageTemplate, clientMessageParams);
    }

    /**
     * 返回406异常,自定义clientMessageTemplate和serverMessage
     * @param clientMessageTemplate 自定义客户端提示信息模板
     * @param clientMessageParams 自定义客户端提示信息模板参数
     * @param serverMessage 自定义服务端提示信息
     * @return
     */
    public static BasicException clientByNotAcceptable(String clientMessageTemplate, Object[] clientMessageParams, String serverMessage) {
        return ClientException.client(NOT_ACCEPTABLE, clientMessageTemplate, clientMessageParams, serverMessage);
    }

    /**
     * 返回406异常,自定义clientMessage和serverMessageTemplate
     * @param clientMessage 自定义客户端提示信息
     * @param serverMessageTemplate 自定义服务端提示信息模板
     * @param serverMessageParams 自定义服务端提示信息模板参数
     * @return
     */
    public static BasicException clientByNotAcceptable(String clientMessage, String serverMessageTemplate, Object[] serverMessageParams) {
        return ClientException.client(NOT_ACCEPTABLE, clientMessage, serverMessageTemplate, serverMessageParams);
    }

    /**
     * 返回406异常,自定义clientMessageTemplate和serverMessageTemplate
     * @param clientMessageTemplate 自定义服客户端提示信息模板
     * @param clientMessageParams 自定义服客户端提示信息模板参数
     * @param serverMessageTemplate 自定义服务端提示信息模板
     * @param serverMessageParams 自定义服务端提示信息模板参数
     * @return
     */
    public static BasicException clientByNotAcceptable(String clientMessageTemplate, Object[] clientMessageParams, String serverMessageTemplate, Object[] serverMessageParams) {
        return ClientException.client(NOT_ACCEPTABLE, clientMessageTemplate, clientMessageParams, serverMessageTemplate, serverMessageParams);
    }

    //~ ClientExceptionEnum.UNPROCESSABLE_ENTITY 422
    //==================================================================================================================
    /**
     * 返回422异常
     * @return
     */
    public static BasicException clientByUnprocessableEntity() {
        return ClientException.client(UNPROCESSABLE_ENTITY);
    }

    /**
     * 返回422异常,自定义clientMessage
     * @param clientMessage 自定义客户端提示信息
     * @return
     */
    public static BasicException clientByUnprocessableEntity(String clientMessage) {
        return ClientException.client(UNPROCESSABLE_ENTITY, clientMessage);
    }

    /**
     * 返回422异常,自定义clientMessage和serverMessage
     * @param clientMessage 自定义客户端提示信息
     * @param serverMessage 自定义服务端提示信息
     * @return
     */
    public static BasicException clientByUnprocessableEntity(String clientMessage, String serverMessage) {
        return ClientException.client(UNPROCESSABLE_ENTITY, clientMessage, serverMessage);
    }

    /**
     * 返回422异常,自定义clientMessageTemplate
     * @param clientMessageTemplate 自定义客户端提示信息模板
     * @param clientMessageParams 自定义客户端提示信息模板参数
     * @return
     */
    public static BasicException clientByUnprocessableEntity(String clientMessageTemplate, Object[] clientMessageParams) {
        return ClientException.client(UNPROCESSABLE_ENTITY, clientMessageTemplate, clientMessageParams);
    }

    /**
     * 返回422异常,自定义clientMessageTemplate和serverMessage
     * @param clientMessageTemplate 自定义客户端提示信息模板
     * @param clientMessageParams 自定义客户端提示信息模板参数
     * @param serverMessage 自定义服务端提示信息
     * @return
     */
    public static BasicException clientByUnprocessableEntity(String clientMessageTemplate, Object[] clientMessageParams, String serverMessage) {
        return ClientException.client(UNPROCESSABLE_ENTITY, clientMessageTemplate, clientMessageParams, serverMessage);
    }

    /**
     * 返回422异常,自定义clientMessage和serverMessageTemplate
     * @param clientMessage 自定义客户端提示信息
     * @param serverMessageTemplate 自定义服务端提示信息模板
     * @param serverMessageParams 自定义服务端提示信息模板参数
     * @return
     */
    public static BasicException clientByUnprocessableEntity(String clientMessage, String serverMessageTemplate, Object[] serverMessageParams) {
        return ClientException.client(UNPROCESSABLE_ENTITY, clientMessage, serverMessageTemplate, serverMessageParams);
    }

    /**
     * 返回422异常,自定义clientMessageTemplate和serverMessageTemplate
     * @param clientMessageTemplate 自定义服客户端提示信息模板
     * @param clientMessageParams 自定义服客户端提示信息模板参数
     * @param serverMessageTemplate 自定义服务端提示信息模板
     * @param serverMessageParams 自定义服务端提示信息模板参数
     * @return
     */
    public static BasicException clientByUnprocessableEntity(String clientMessageTemplate, Object[] clientMessageParams, String serverMessageTemplate, Object[] serverMessageParams) {
        return ClientException.client(UNPROCESSABLE_ENTITY, clientMessageTemplate, clientMessageParams, serverMessageTemplate, serverMessageParams);
    }

    //~ ClientExceptionEnum.TOO_MANY_REQUESTS 429
    //==================================================================================================================
    /**
     * 返回429异常
     * @return
     */
    public static BasicException clientByTooManyRequests() {
        return ClientException.client(TOO_MANY_REQUESTS);
    }

    /**
     * 返回429异常,自定义clientMessage
     * @param clientMessage 自定义客户端提示信息
     * @return
     */
    public static BasicException clientByTooManyRequests(String clientMessage) {
        return ClientException.client(TOO_MANY_REQUESTS, clientMessage);
    }

    /**
     * 返回429异常,自定义clientMessage和serverMessage
     * @param clientMessage 自定义客户端提示信息
     * @param serverMessage 自定义服务端提示信息
     * @return
     */
    public static BasicException clientByTooManyRequests(String clientMessage, String serverMessage) {
        return ClientException.client(TOO_MANY_REQUESTS, clientMessage, serverMessage);
    }

    /**
     * 返回429异常,自定义clientMessageTemplate
     * @param clientMessageTemplate 自定义客户端提示信息模板
     * @param clientMessageParams 自定义客户端提示信息模板参数
     * @return
     */
    public static BasicException clientByTooManyRequests(String clientMessageTemplate, Object[] clientMessageParams) {
        return ClientException.client(TOO_MANY_REQUESTS, clientMessageTemplate, clientMessageParams);
    }

    /**
     * 返回429异常,自定义clientMessageTemplate和serverMessage
     * @param clientMessageTemplate 自定义客户端提示信息模板
     * @param clientMessageParams 自定义客户端提示信息模板参数
     * @param serverMessage 自定义服务端提示信息
     * @return
     */
    public static BasicException clientByTooManyRequests(String clientMessageTemplate, Object[] clientMessageParams, String serverMessage) {
        return ClientException.client(TOO_MANY_REQUESTS, clientMessageTemplate, clientMessageParams, serverMessage);
    }

    /**
     * 返回429异常,自定义clientMessage和serverMessageTemplate
     * @param clientMessage 自定义客户端提示信息
     * @param serverMessageTemplate 自定义服务端提示信息模板
     * @param serverMessageParams 自定义服务端提示信息模板参数
     * @return
     */
    public static BasicException clientByTooManyRequests(String clientMessage, String serverMessageTemplate, Object[] serverMessageParams) {
        return ClientException.client(TOO_MANY_REQUESTS, clientMessage, serverMessageTemplate, serverMessageParams);
    }

    /**
     * 返回429异常,自定义clientMessageTemplate和serverMessageTemplate
     * @param clientMessageTemplate 自定义服客户端提示信息模板
     * @param clientMessageParams 自定义服客户端提示信息模板参数
     * @param serverMessageTemplate 自定义服务端提示信息模板
     * @param serverMessageParams 自定义服务端提示信息模板参数
     * @return
     */
    public static BasicException clientByTooManyRequests(String clientMessageTemplate, Object[] clientMessageParams, String serverMessageTemplate, Object[] serverMessageParams) {
        return ClientException.client(TOO_MANY_REQUESTS, clientMessageTemplate, clientMessageParams, serverMessageTemplate, serverMessageParams);
    }


    // ~ 服务端异常
    // =================================================================================================================
    /**
     * 返回默认异常,自定义serverMessage
     * @param serverMessage 自定义服务端提示信息
     * @return
     */
    public static BasicException server(String serverMessage) {
        return new ServerException(ServerExceptionEnum.SERVER_ERROR, serverMessage);
    }

    /**
     * 返回默认异常,自定义serverMessageTemplate
     * @param serverMessageTemplate 自定义服务端提示信息模板
     * @param serverMessageParams 自定义服务端提示信息模板参数
     * @return
     */
    public static BasicException server(String serverMessageTemplate, Object[] serverMessageParams) {
        return BasicException.server(MessageFormatUtil.format(serverMessageTemplate, serverMessageParams));
    }

    /**
     * 返回指定服务端异常
     * @param exceptionEnum 指定异常类型
     * @return
     */
    public static BasicException server(ServerExceptionEnum exceptionEnum) {
        return new ServerException(exceptionEnum);
    }

    /**
     * 返回指定服务端异常，自定义serverMessage
     * @param exceptionEnum 指定异常类型
     * @param serverMessage 自定义服务端提示信息
     * @return
     */
    public static BasicException server(ServerExceptionEnum exceptionEnum, String serverMessage) {
        return new ServerException(exceptionEnum, serverMessage);
    }

    /**
     * 返回指定服务端异常,自定义clientMessage和serverMessage
     * @param exceptionEnum 指定异常类型
     * @param clientMessage 自定义客户端提示信息
     * @param serverMessage 自定义服务端提示信息
     * @return
     */
    public static BasicException server(ServerExceptionEnum exceptionEnum, String clientMessage, String serverMessage) {
        return new ServerException(exceptionEnum, clientMessage, serverMessage);
    }

    /**
     * 返回指定服务端异常,自定义serverMessageTemplate
     * @param exceptionEnum 指定异常类型
     * @param serverMessageTemplate 自定义服务端提示信息模板
     * @param serverMessageParams 自定义服务端提示信息模板参数
     * @return
     */
    public static BasicException server(ServerExceptionEnum exceptionEnum, String serverMessageTemplate, Object[] serverMessageParams) {
        String serverMessage = MessageFormatUtil.format(serverMessageTemplate, serverMessageParams);
        return new ServerException(exceptionEnum, serverMessage);
    }

    /**
     * 返回指定服务端异常，自定义clientMessage和serverMessageTemplate
     * @param exceptionEnum 指定异常类型
     * @param clientMessage 自定义客户端提示信息
     * @param serverMessageTemplate 自定义服务端提示信息模板
     * @param serverMessageParams 自定义服务端提示信息模板参数
     * @return
     */
    public static BasicException server(ServerExceptionEnum exceptionEnum, String clientMessage, String serverMessageTemplate, Object[] serverMessageParams) {
        return new ServerException(exceptionEnum, clientMessage, MessageFormatUtil.format(serverMessageTemplate, serverMessageParams));
    }

    /**
     * 返回指定服务端异常，自定义clientMessageTemplate和serverMessage
     * @param exceptionEnum 指定异常类型
     * @param clientMessageTemplate 自定义客户端提示信息模板
     * @param clientMessageParams 自定义客户端提示信息模板参数
     * @param serverMessage 自定义服务端提示信息
     * @return
     */
    public static BasicException server(ServerExceptionEnum exceptionEnum, String clientMessageTemplate, Object[] clientMessageParams, String serverMessage) {
        return new ServerException(exceptionEnum, MessageFormatUtil.format(clientMessageTemplate, clientMessageParams), serverMessage);
    }

    /**
     * 返回指定服务端异常，自定义clientMessageTemplate和serverMessageTemplate
     * @param exceptionEnum 指定异常类型
     * @param clientMessageTemplate 自定义客户端提示信息模板
     * @param clientMessageParams 自定义客户端提示信息模板参数
     * @param serverMessageTemplate 自定义服务端提示信息模板
     * @param serverMessageParams 自定义服务端提示信息模板参数
     * @return
     */
    public static BasicException server(ServerExceptionEnum exceptionEnum, String clientMessageTemplate, Object[] clientMessageParams, String serverMessageTemplate, Object[] serverMessageParams) {

        return new ServerException(exceptionEnum,
                MessageFormatUtil.format(clientMessageTemplate, clientMessageParams),
                MessageFormatUtil.format(serverMessageTemplate, serverMessageParams));
    }

    // ~ ServerExceptionEnum.SERVER_ERROR 500
    // =================================================================================================================

    /**
     * 返回500异常
     * @return
     */
    public static BasicException serverByServerError() {
        throw ServerException.server(SERVER_ERROR);
    }

    /**
     * 返回500异常,自定义serverMessage
     * @param serverMessage 自定义服务端提示信息
     * @return
     */
    public static BasicException serverByServerError(String serverMessage) {
        throw ServerException.server(SERVER_ERROR, serverMessage);
    }

    /**
     * 返回500异常,自定义clientMessage和serverMessage
     * @param clientMessage 自定义客户端提示信息
     * @param serverMessage 自定义服务端提示信息
     * @return
     */
    public static BasicException serverByServerError(String clientMessage, String serverMessage) {
        throw ServerException.server(SERVER_ERROR, clientMessage, serverMessage);
    }

    /**
     * 返回500异常,自定义serverMessageTemplate
     * @param serverMessageTemplate 自定义服务端提示信息模板
     * @param serverMessageParams 自定义服务端提示信息模板参数
     * @return
     */
    public static BasicException serverByServerError(String serverMessageTemplate, Object[] serverMessageParams) {
        throw ServerException.server(SERVER_ERROR, serverMessageTemplate, serverMessageParams);
    }

    /**
     * 返回500异常,自定义clientMessage和serverMessageTemplate
     * @param clientMessage 自定义客户端提示信息
     * @param serverMessageTemplate 自定义服务端提示信息模板
     * @param serverMessageParams 自定义服务端提示信息模板参数
     * @return
     */
    public static BasicException serverByServerError(String clientMessage, String serverMessageTemplate, Object[] serverMessageParams) {
        throw ServerException.server(SERVER_ERROR, clientMessage, serverMessageTemplate, serverMessageParams);
    }

    /**
     * 返回500异常,自定义clientMessage和serverMessageTemplate
     * @param clientMessageTemplate 自定义客户端提示信息模板
     * @param clientMessageParams 自定义客户端提示信息模板参数
     * @param serverMessageTemplate 自定义服务端提示信息模板
     * @param serverMessageParams 自定义服务端提示信息模板参数
     * @return
     */
    public static BasicException serverByServerError(String clientMessageTemplate, Object[] clientMessageParams, String serverMessageTemplate, Object[] serverMessageParams) {
        throw ServerException.server(SERVER_ERROR, clientMessageTemplate, clientMessageParams, serverMessageTemplate, serverMessageParams);
    }

    // ~ 常用构造方法
    // =================================================================================================================
    public BasicException() {
        super();
    }

    public BasicException(String message) {
        this(BAD_REQUEST, message);
    }

    /**
     * 客户端误操作造成异常
     * @param exceptionEnum
     */
    public BasicException(ClientExceptionEnum exceptionEnum) {
        this(exceptionEnum.getStatus(), exceptionEnum.getCode(), exceptionEnum.getClientMessage(), exceptionEnum.getServerMessage());
    }

    /**
     * 客户端误操作造成异常
     * @param exceptionEnum
     * @param clientMessage 自定义客户端提示信息
     */
    public BasicException(ClientExceptionEnum exceptionEnum, String clientMessage) {
        this(exceptionEnum.getStatus(), exceptionEnum.getCode(), clientMessage, exceptionEnum.getServerMessage());
    }

    /**
     * 客户端误操作造成异常
     * @param exceptionEnum
     * @param clientMessage 自定义客户端提示信息
     */
    public BasicException(ClientExceptionEnum exceptionEnum, String clientMessage, String serverMessage) {
        this(exceptionEnum.getStatus(), exceptionEnum.getCode(), clientMessage, serverMessage);
    }

    /**
     * 服务端异常
     * @param exceptionEnum
     */
    public BasicException(ServerExceptionEnum exceptionEnum) {
        this(exceptionEnum.getStatus(), exceptionEnum.getCode(), exceptionEnum.getClientMessage(), exceptionEnum.getServerMessage());
    }

    /**
     * 服务端异常
     * @param exceptionEnum
     * @param serverMessage 服务端错误信息
     */
    public BasicException(ServerExceptionEnum exceptionEnum, String serverMessage) {
        this(exceptionEnum.getStatus(), exceptionEnum.getCode(), exceptionEnum.getClientMessage(), serverMessage);
    }

    /**
     * 服务端异常
     * @param exceptionEnum 状态码相关
     * @param clientMessage 客户端提示
     * @param serverMessage 服务端提示
     * @param serverMessage 服务端错误信息
     */
    public BasicException(ServerExceptionEnum exceptionEnum, String clientMessage, String serverMessage) {
        this(exceptionEnum.getStatus(), exceptionEnum.getCode(), clientMessage, serverMessage);
    }

    /**
     * 构造方法
     * @param status http状态码
     * @param code 自定义状态码
     * @param clientMessage 客户端显示信息
     * @param serverMessage 服务端日志显示信息
     */
    public BasicException(int status, String code, String clientMessage, String serverMessage) {
        super(clientMessage+"\t"+serverMessage);
        this.status = status;
        this.code = code;
        this.clientMessage = clientMessage;
        this.serverMessage = serverMessage;
    }

    //~ 实例方法
    //==================================================================================================================
    /**
     * 使用此方法，将返回指定类型的对象，在堆栈打印时方便查看,推荐使用其他子类异常时使用
     * 使用方式示例：{@code BasicException.client("用户不存在").convert(UserException.class)}
     * 这样会返回一个 UserException实例，在进行e.printStackTrace() 时会显示UserException的堆栈信息。
     * @param target 转换后的实例类型
     * @return
     */
    public BasicException convert(Class<? extends BasicException> target) {
        BasicException basicException = BeanUtil.copyProperties(this, target);
        return basicException;
    }

    /**
     * 设置错误代码
     * @param code
     * @return
     */
    public BasicException code(String code) {
        this.code = code;
        return this;
    }

    /**
     * 设置clientMessage
     * @param clientMessage
     * @return
     */
    public BasicException clientMessage(String clientMessage) {
        this.clientMessage = clientMessage;
        return this;
    }

    /**
     * 设置serverMessage
     * @param serverMessage
     * @return
     */
    public BasicException serverMessage(String serverMessage) {
        this.serverMessage = serverMessage;
        return this;
    }

    /**
     * 设置dataMap
     * @param dataMap
     * @return
     */
    public BasicException dataMap(Map dataMap) {
        this.dataMap = dataMap;
        return this;
    }

    /**
     * 设置dataMap
     * @param dataMap
     * @return
     */
    public BasicException dataMap(AbstractDataMap dataMap) {
        this.dataMap = BeanUtil.beanToMap(dataMap, false, true);
        return this;
    }

    /**
     * 扩展dataMap
     * @param dataMap
     * @return
     */
    public BasicException dataMapPut(Map dataMap) {
        this.dataMap.putAll(dataMap);
        return this;
    }

    /**
     * 扩展dataMap
     * @param dataMap
     * @return
     */
    public BasicException dataMapPut(AbstractDataMap dataMap) {
        this.dataMap.putAll(BeanUtil.beanToMap(dataMap, false, true));
        return this;
    }

    /**
     * 扩展dataMap
     * @param key
     * @param value
     * @return
     */
    public BasicException dataMapPut(String key, Object value) {
        this.dataMap.put(key, value);
        return this;
    }

    /**
     * 扩展dataMap
     * @param kv 字符串数组，奇数位是key，偶数位是value
     * @return
     */
    public BasicException dataMapPut(String... kv) {
        // 不是偶数位
        if (kv.length < 2 && kv.length % 2 != 0) {
            throw new IllegalArgumentException("参数kv数组不正确，要是2的倍数，其中奇数是key偶数是value");
        }

        // 步长为2
        for (int i = 0, length = kv.length; i < length; i+=2) {
            String key = kv[i];
            String value = kv[i+1];
            this.dataMap.put(key, value);
        }

        return this;
    }
}
