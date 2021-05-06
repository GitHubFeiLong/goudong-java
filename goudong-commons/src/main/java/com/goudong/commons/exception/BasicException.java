package com.goudong.commons.exception;

import com.goudong.commons.enumerate.ClientExceptionEnum;
import com.goudong.commons.enumerate.ServerExceptionEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * 类描述：
 * 自定义异常的基类，其它模块的异常继承进行扩展
 * @ClassName BaseicException
 * @Author msi
 * @Date 2020/6/10 19:41
 * @Version 1.0
 */
@Slf4j
@Getter
@Setter
@ApiModel(value = "BasicException", description = "出现异常，返回的消息")
public class BasicException extends RuntimeException{
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
    @ApiModelProperty(value = "状态码对应描述", required = true, example = "用户不存在")
    public String clientMessage;

    /**
     * 服务器状态码对应信息
     */
    @ApiModelProperty(value = "状态码对应描述", required = true, example = "用户不存在")
    public String serverMessage;



    /**
     * 构造方法
     * @param status http状态码
     * @param code 自定义状态码
     * @param clientMessage 客户端显示信息
     * @param serverMessage 服务端日志显示信息
     */
    public BasicException(int status, String code, String clientMessage, String serverMessage) {
        super(clientMessage+"\n"+serverMessage);
        this.status = status;
        this.code = code;
        this.clientMessage = clientMessage;
        this.serverMessage = serverMessage;
    }

    /**
     * 客户端误操作造成异常
     * @param exceptionEnum
     */
    public BasicException(ClientExceptionEnum exceptionEnum) {
        this(exceptionEnum.getStatus(), exceptionEnum.getCode(), exceptionEnum.getClientMessage(), exceptionEnum.getServerMessage());
    }

    /**
     * 服务端异常
     * @param exceptionEnum
     */
    public BasicException(ServerExceptionEnum exceptionEnum) {
        this(exceptionEnum.getStatus(), exceptionEnum.getCode(), exceptionEnum.getClientMessage(), exceptionEnum.getServerMessage());
    }

    /**
     * ServerExceptionEnum 枚举
     * @param exceptionEnum
     * @return
     */
    public static BasicException exception (ServerExceptionEnum exceptionEnum) {
        throw new BasicException.ServerException(exceptionEnum);
    }

    /**
     * ClientExceptionEnum枚举
     * @param exceptionEnum
     * @return
     */
    public static BasicException exception (ClientExceptionEnum exceptionEnum) {
        throw new BasicException.ClientException(exceptionEnum);
    }

    /**
     * 类描述：
     *  客户端内部错误
     * @ClassName ParamterInvalidException
     * @Author msi
     * @Date 2020/7/14 20:40
     * @Version 1.0
     */
    public static class ClientException extends BasicException {
        public static final String serverMessage = "参数错误";

        /**
         * 404 Not Found
         * 请求失败，请求所希望得到的资源未被在服务器上发现。没有信息能够告诉用户这个状况到底是暂时的还是永久的。假如服务器知道情况的话，应当使用410状态码来告知旧资源因为某些内部的配置机制问题，已经永久的不可用，而且没有任何可以跳转的地址。404这个状态码被广泛应用于当服务器不想揭示到底为何请求被拒绝或者没有其他适合的响应可用的情况下。出现这个错误的最有可能的原因是服务器端没有这个页面。
         * @return
         */
        public static BasicException noHandlerFoundException (String url) {
            throw new BasicException(404, "404404", "请求资源不存在", "请求的资源不存在:" + url);
        }

        /**
         * 405 Method Not Allowed
         * 请求行中指定的请求方法不能被用于请求相应的资源。该响应必须返回一个Allow 头信息用以表示出当前资源能够接受的请求方法的列表。
         * 鉴于 PUT，DELETE 方法会对服务器上的资源进行写操作，因而绝大部分的网页服务器都不支持或者在默认配置下不允许上述请求方法，对于此类请求均会返回405错误。
         * @return
         */
        public static BasicException methodNotAllowedException (String url) {
            throw new BasicException(405, "405405", "请求方法错误", "Method Not Allowed:" + url);
        }

        /**
         * 参数错误
         * @param clientMessage 客户端提示信息
         * @return
         */
        public static BasicException methodParamError(String clientMessage){
            log.error("服务器内部方法传参错误：{}", clientMessage);
            throw new BasicException(400, "400400", clientMessage, serverMessage);
        }
        /**
         * 参数错误
         * @param clientMessage 客户端错误信息
         * @param e 服务端错误信息
         * @return
         */
        public static BasicException methodParamError(String clientMessage, Exception e){
            log.error("服务器内部方法传参错误：{}", clientMessage);
            throw new BasicException(400, "400400", clientMessage, e.getMessage());
        }

        public ClientException(ClientExceptionEnum clientExceptionEnum) {
            super(clientExceptionEnum);
        }

    }

    /**
     * 类描述：
     *  服务器内部错误
     * @ClassName ServerException
     * @Author msi
     * @Date 2020/7/14 20:40
     * @Version 1.0
     */
    public static class ServerException extends BasicException {

        public static final String clientMessage = "服务器内部错误";

        public ServerException(ServerExceptionEnum serverExceptionEnum) {
            super(serverExceptionEnum);
        }

        public static BasicException exception(String serverMessage){
            log.error("服务器内部错误：{}", serverMessage);
            throw new BasicException(500, "500500", clientMessage, serverMessage);
        }

        public static BasicException methodParamError(String serverMessage){
            log.error("服务器内部方法传参错误：{}", serverMessage);
            throw new BasicException(400, "500400", clientMessage, serverMessage);
        }
    }

}
