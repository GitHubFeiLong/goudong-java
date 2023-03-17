package com.goudong.boot.web.core;

import cn.hutool.core.bean.BeanUtil;
import com.goudong.boot.web.enumerate.ClientExceptionEnum;
import com.goudong.boot.web.enumerate.ServerExceptionEnum;
import com.goudong.core.lang.BasicExceptionInterface;
import com.goudong.core.lang.ExceptionEnumInterface;
import com.goudong.core.lang.Result;
import com.goudong.core.util.ArrayUtil;
import com.goudong.core.util.AssertUtil;
import com.goudong.core.util.MessageFormatUtil;
import com.goudong.core.util.StringUtil;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static com.goudong.boot.web.enumerate.ClientExceptionEnum.*;
import static com.goudong.boot.web.enumerate.ServerExceptionEnum.SERVER_ERROR;


/**
 * 类描述：
 * 自定义异常的基类，其它模块的异常继承进行扩展
 *
 * @see ClientException
 * @see ServerException
 * @see Result
 * @see ClientExceptionEnum
 * @see ServerExceptionEnum
 * @author cfl
 * @date 2022/11/3 17:23
 * @version 1.0
 */
@Data
public class BasicException extends RuntimeException implements BasicExceptionInterface {

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

        // 空指针
        if (throwable instanceof NullPointerException) {
            return ServerException.server(ServerExceptionEnum.SERVER_ERROR, "空指针异常", "NullPointerException null");
        }

        // 上传文件错误
        if (throwable instanceof MultipartException) {
            return ClientException.client(BAD_REQUEST, "上传文件失败", "MultipartException " +throwable.getMessage());
        }

        if (throwable instanceof IndexOutOfBoundsException ) {
            return ServerException.server(ServerExceptionEnum.SERVICE_UNAVAILABLE, "下标越界", "IndexOutOfBoundsException");
        }

        if (throwable instanceof RuntimeException) {
            return ServerException.server(ServerExceptionEnum.SERVER_ERROR, throwable.getMessage(), "RuntimeException " + throwable.getMessage());
        }

        String message = throwable.getMessage();
        if (message == null) {
            return basicException;
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
     * 将e加入异常堆栈，并返回默认异常
     * @param e 异常
     * @return
     */
    public static BasicException client(Throwable e) {
        return new ClientException(e);
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
     * 将e加入异常堆栈，返回默认异常,自定义clientMessage
     * @see ClientException#DEFAULT_EXCEPTION
     * @param clientMessage 客户端提示信息
     * @param e 异常
     * @return
     */
    public static BasicException client(String clientMessage, Throwable e) {
        return new ClientException(clientMessage, e);
    }

    /**
     * 返回默认异常,自定义clientMessage模板
     * @see ClientException#DEFAULT_EXCEPTION
     * @param clientMessageTemplate 客户端提示信息模板
     * @param clientMessageParams 客户端模板参数
     * @return
     */
    public static BasicException client(String clientMessageTemplate, Object[] clientMessageParams) {
        return client(ClientException.DEFAULT_EXCEPTION, clientMessageTemplate, clientMessageParams);
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
        return client(BAD_REQUEST, clientMessageTemplate, clientMessageParams, serverMessageTemplate, serverMessageParams);
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
        return client(UNAUTHORIZED, clientMessageTemplate, clientMessageParams, serverMessageTemplate, serverMessageParams);
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
        return client(FORBIDDEN, clientMessageTemplate, clientMessageParams, serverMessageTemplate, serverMessageParams);
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
        return client(NOT_FOUND, clientMessageTemplate, clientMessageParams, serverMessageTemplate, serverMessageParams);
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
        return client(NOT_ACCEPTABLE, clientMessageTemplate, clientMessageParams, serverMessageTemplate, serverMessageParams);
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
        return client(UNPROCESSABLE_ENTITY, clientMessageTemplate, clientMessageParams, serverMessageTemplate, serverMessageParams);
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
        return client(TOO_MANY_REQUESTS, clientMessageTemplate, clientMessageParams, serverMessageTemplate, serverMessageParams);
    }


    // ~ 服务端异常
    // =================================================================================================================
    /**
     * 返回默认异常,自定义serverMessage
     * @return
     */
    public static BasicException server() {
        return new ServerException(ServerExceptionEnum.SERVER_ERROR);
    }

    /**
     * 将e加入异常堆栈，返回默认异常,自定义serverMessage
     * @param e 异常
     * @return
     */
    public static BasicException server(Throwable e) {
        return new ServerException(ServerExceptionEnum.SERVER_ERROR, e);
    }

    /**
     * 返回默认异常,自定义serverMessage
     * @param serverMessage 自定义服务端提示信息
     * @return
     */
    public static BasicException server(String serverMessage) {
        return new ServerException(ServerExceptionEnum.SERVER_ERROR, serverMessage);
    }

    /**
     * 将e加入异常堆栈，返回默认异常,自定义serverMessage
     * @param serverMessage 自定义服务端提示信息
     *  @param e 异常
     * @return
     */
    public static BasicException server(String serverMessage, Throwable e) {
        return new ServerException(ServerExceptionEnum.SERVER_ERROR, serverMessage, e);
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
        throw server(SERVER_ERROR, clientMessageTemplate, clientMessageParams, serverMessageTemplate, serverMessageParams);
    }

    // ~ builder创建
    // =================================================================================================================

    /**
     * 创建构建者
     * @return
     */
    public static BasicExceptionBuilder builder() {
        return new BasicExceptionBuilder();
    }

    /**
     * 创建构建者
     * @return
     */
    public static BasicExceptionBuilder builder(ExceptionEnumInterface exceptionEnum) {
        return new BasicExceptionBuilder(exceptionEnum);
    }

    // ~ 常用构造方法
    // =================================================================================================================
    public BasicException() {
        super();
    }

    public BasicException(String message) {
        this(BAD_REQUEST, message);
    }

    public BasicException(Throwable cause) {
        super(cause);
    }

    public BasicException(String message, Throwable cause) {
        super(message, cause);
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
     * @param cause
     */
    public BasicException(ClientExceptionEnum exceptionEnum, Throwable cause) {
        this(exceptionEnum.getStatus(), exceptionEnum.getCode(), exceptionEnum.getClientMessage(), Optional.ofNullable(cause.getMessage()).orElseGet(()->exceptionEnum.getServerMessage()), cause);
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
     * @param cause
     */
    public BasicException(ClientExceptionEnum exceptionEnum, String clientMessage, Throwable cause) {
        this(exceptionEnum.getStatus(), exceptionEnum.getCode(), clientMessage, Optional.ofNullable(cause.getMessage()).orElseGet(()->exceptionEnum.getServerMessage()), cause);
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
     * 客户端误操作造成异常
     * @param exceptionEnum
     * @param clientMessage 自定义客户端提示信息
     * @param cause
     */
    public BasicException(ClientExceptionEnum exceptionEnum, String clientMessage, String serverMessage, Throwable cause) {
        this(exceptionEnum.getStatus(), exceptionEnum.getCode(), clientMessage, serverMessage, cause);
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
     * @param cause
     */
    public BasicException(ServerExceptionEnum exceptionEnum, Throwable cause) {
        this(exceptionEnum.getStatus(), exceptionEnum.getCode(), exceptionEnum.getClientMessage(), Optional.ofNullable(cause.getMessage()).orElseGet(()->exceptionEnum.getServerMessage()), cause);
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
     * @param exceptionEnum
     * @param serverMessage 服务端错误信息
     * @param cause
     */
    public BasicException(ServerExceptionEnum exceptionEnum, String serverMessage, Throwable cause) {
        this(exceptionEnum.getStatus(), exceptionEnum.getCode(), exceptionEnum.getClientMessage(), serverMessage, cause);
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
     * 服务端异常
     * @param exceptionEnum 状态码相关
     * @param clientMessage 客户端提示
     * @param serverMessage 服务端提示
     * @param serverMessage 服务端错误信息
     * @param cause
     */
    public BasicException(ServerExceptionEnum exceptionEnum, String clientMessage, String serverMessage, Throwable cause) {
        this(exceptionEnum.getStatus(), exceptionEnum.getCode(), clientMessage, serverMessage, cause);
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

    /**
     * 构造方法
     * @param status http状态码
     * @param code 自定义状态码
     * @param clientMessage 客户端显示信息
     * @param serverMessage 服务端日志显示信息
     * @param cause 异常对象
     */
    public BasicException(int status, String code, String clientMessage, String serverMessage, Throwable cause) {
        super(clientMessage+"\t"+serverMessage, cause);
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

    /**
     * 类描述：
     * 创建BasicException的构建对象
     * @author cfl
     * @date 2023/3/17 19:34
     * @version 1.0
     */
    public static class BasicExceptionBuilder {

        /**
         * http 响应码
         */
        private int status;

        /**
         * 错误代码
         */
        private String code;

        /**
         * 客户端状态码对应信息
         */
        private String clientMessage;

        /**
         * 客户端模板
         */
        private String clientMessageTemplate;

        /**
         * 客户端模板参数
         */
        private Object[] clientMessageParams;

        /**
         * 服务器状态码对应信息
         */
        private String serverMessage;

        /**
         * 服务端模板
         */
        private String serverMessageTemplate;

        /**
         * 服务端模板参数
         */
        private Object[] serverMessageParams;

        /**
         * 额外信息
         */
        private Map dataMap = new HashMap();

        /**
         * 异常信息
         */
        private Throwable cause;

        /**
         * 异常类型
         */
        private ExceptionEnumInterface exceptionEnum;

        /**
         * 无参构造
         */
        public BasicExceptionBuilder() {
            // 默认设置500状态码
            this.status = SERVER_ERROR.getStatus();
            // 默认设置成失败
            this.code = Result.FAIL;
        }

        /**
         * 有参构造，根据异常枚举对象，给属性设置默认值
         * @param exceptionEnum
         */
        public BasicExceptionBuilder(ExceptionEnumInterface exceptionEnum) {
            this.exceptionEnum = exceptionEnum;
            this.status = exceptionEnum.getStatus();
            this.code = exceptionEnum.getCode();
            this.clientMessage = exceptionEnum.getClientMessage();
            this.serverMessage = exceptionEnum.getServerMessage();
        }

        /**
         * 设置Http状态码，值必须是正确的4xx或5xx状态码
         * @param status
         * @return
         */
        public BasicExceptionBuilder status(int status) {
            AssertUtil.isTrue(HttpStatus.valueOf(status).isError(), () -> ServerException.server("状态码设置错误"));
            this.status = status;
            return this;
        }

        /**
         * 设置code
         * @param code
         * @return
         */
        public BasicExceptionBuilder code(String code) {
            this.code = code;
            return this;
        }

        /**
         * 设置clientMessage</br>
         * <pre>
         * 注意：
         * 1. 该方法应该避免与{@code clientMessageTemplate}、{@code clientMessageParams}方法一起使用
         * 2. 如果一起使用:
         *  2.1 {@code clientMessage} 在前面，那么{@code clientMessageTemplate}、{@code clientMessageParams}会生效
         *  2.2 {@code clientMessage} 在后面，那么{@code clientMessage}会生效
         * </pre>
         * @param clientMessage
         * @return
         */
        public BasicExceptionBuilder clientMessage(String clientMessage) {
            this.clientMessage = clientMessage;
            this.clientMessageTemplate = null;
            this.clientMessageParams = null;
            return this;
        }

        /**
         * 设置clientMessageTemplate</br>
         * 通常该方法和{@code clientMessageParams}一起使用，这样可以有效避免字符串拼接问题
         * @param clientMessageTemplate
         * @return
         */
        public BasicExceptionBuilder clientMessageTemplate(String clientMessageTemplate) {
            this.clientMessageTemplate = clientMessageTemplate;
            return this;
        }

        /**
         * 设置clientMessageParams</br>
         * 通常该方法和{@code clientMessageTemplate}一起使用，这样可以有效避免字符串拼接问题
         * @param clientMessageParams
         * @return
         */
        public BasicExceptionBuilder clientMessageParams(Object... clientMessageParams) {
            this.clientMessageParams = clientMessageParams;
            return this;
        }

        /**
         * 设置serverMessage
         * <pre>
         * 注意：
         * 1. 该方法应该避免与{@code serverMessageTemplate}、{@code serverMessageParams}方法一起使用
         * 2. 如果一起使用:
         *  2.1 {@code serverMessage} 在前面，那么{@code serverMessageTemplate}、{@code serverMessageParams}会生效
         *  2.2 {@code serverMessage} 在后面，那么{@code serverMessage}会生效
         * </pre>
         * @param serverMessage
         * @return
         */
        public BasicExceptionBuilder serverMessage(String serverMessage) {
            this.serverMessage = serverMessage;
            this.serverMessageTemplate = null;
            this.serverMessageParams = null;
            return this;
        }

        /**
         * 设置serverMessageTemplate</br>
         * 通常该方法和{@code serverMessageParams}一起使用，这样可以有效避免字符串拼接问题
         * @param serverMessageTemplate
         * @return
         */
        public BasicExceptionBuilder serverMessageTemplate(String serverMessageTemplate) {
            this.serverMessageTemplate = serverMessageTemplate;
            return this;
        }

        /**
         * 设置serverMessageParams</br>
         * 通常该方法和{@code serverMessageTemplate}一起使用，这样可以有效避免字符串拼接问题
         * @param serverMessageParams
         * @return
         */
        public BasicExceptionBuilder serverMessageParams(Object... serverMessageParams) {
            this.serverMessageParams = serverMessageParams;
            return this;
        }

        /**
         * 设置dataMap
         * @param dataMap
         * @return
         */
        public BasicExceptionBuilder dataMap(Map dataMap) {
            this.dataMap = dataMap;
            return this;
        }

        /**
         * 设置异常对象
         * @param cause
         * @return
         */
        public BasicExceptionBuilder cause(Throwable cause) {
            this.cause = cause;
            return this;
        }

        /**
         * 设置exceptionEnum，并使用exceptionEnum给其它成员设置默认值
         * @param exceptionEnum
         * @return
         */
        public BasicExceptionBuilder exceptionEnum(ExceptionEnumInterface exceptionEnum) {
            this.exceptionEnum = exceptionEnum;
            this.status = exceptionEnum.getStatus();
            this.code = exceptionEnum.getCode();
            this.clientMessage = exceptionEnum.getClientMessage();
            this.serverMessage = exceptionEnum.getServerMessage();
            return this;
        }

        /**
         * 根据成员变量，创建BasicException对象
         * @return BasicException
         */
        public BasicException build() {
            // 优先使用 clientMessageTemplate + clientMessageParams 的组合方式设置 clientMessage
            if (StringUtil.isNotBlank(clientMessageTemplate)) {
                clientMessage = clientMessageTemplate;
                if (ArrayUtil.isNotEmpty(clientMessageParams)) {
                    clientMessage = MessageFormatUtil.format(clientMessageTemplate, clientMessageParams);
                }
            }

            // 优先使用 serverMessageTemplate + serverMessageParams 的组合方式设置 serverMessage
            if (StringUtil.isNotBlank(serverMessageTemplate)) {
                serverMessage = serverMessageTemplate;
                if (ArrayUtil.isNotEmpty(serverMessageParams)) {
                    serverMessage = MessageFormatUtil.format(serverMessageTemplate, serverMessageParams);
                }
            }

            // 创建 BasicException 对象
            return new BasicException(status, code, clientMessage, serverMessage, cause);
        }
    }
}
