package com.goudong.boot.web.core;

import com.goudong.boot.web.enumerate.ServerExceptionEnum;
import com.goudong.core.lang.ExceptionEnumInterface;
import com.goudong.core.util.ArrayUtil;
import com.goudong.core.util.AssertUtil;
import com.goudong.core.util.MessageFormatUtil;
import com.goudong.core.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import java.util.Map;

/**
 * 类描述：
 * 服务器内部错误
 * @Author e-Feilong.Chen
 * @Date 2021/8/10 16:13
 */
public class ServerException extends BasicException {
    public static final Logger log = LoggerFactory.getLogger(ServerException.class);

    public static final String clientMessage = "服务器内部错误";

    /**
     * 默认500异常
     */
    public static final ServerExceptionEnum DEFAULT_EXCEPTION = ServerExceptionEnum.SERVER_ERROR;

    public ServerException() {
        super(ServerException.DEFAULT_EXCEPTION);
    }

    public ServerException(String serverMessage) {
        super(ServerException.DEFAULT_EXCEPTION, serverMessage);
    }

    public ServerException(String clientMessage, String serverMessage) {
        super(DEFAULT_EXCEPTION, clientMessage, serverMessage);
    }

    public ServerException(Throwable cause) {
        super(DEFAULT_EXCEPTION, cause);
    }

    public ServerException(String serverMessage, Throwable cause) {
        super(DEFAULT_EXCEPTION, serverMessage, cause);
    }

    public ServerException(ServerExceptionEnum serverExceptionEnum) {
        super(serverExceptionEnum);
    }

    public ServerException(ServerExceptionEnum serverExceptionEnum, Throwable cause) {
        super(serverExceptionEnum, cause);
    }

    public ServerException(ServerExceptionEnum serverExceptionEnum, String serverMessage) {
        super(serverExceptionEnum, serverMessage);
    }

    public ServerException(ServerExceptionEnum serverExceptionEnum, String serverMessage, Throwable cause) {
        super(serverExceptionEnum, serverMessage, cause);
    }

    public ServerException(ServerExceptionEnum serverExceptionEnum, String clientMessage, String serverMessage) {
        super(serverExceptionEnum, clientMessage, serverMessage);
    }

    public ServerException(ServerExceptionEnum serverExceptionEnum, String clientMessage, String serverMessage, Throwable cause) {
        super(serverExceptionEnum, clientMessage, serverMessage, cause);
    }
    /**
     * 构造方法
     * @param status http状态码
     * @param code 自定义状态码
     * @param clientMessage 客户端显示信息
     * @param serverMessage 服务端日志显示信息
     * @param cause 异常对象
     */
    public ServerException(int status, String code, String clientMessage, String serverMessage, Throwable cause) {
        super(status, code, clientMessage, serverMessage, cause);
    }

    // ~ builder创建
    // =================================================================================================================

    /**
     * 创建构建者
     * @return
     */
    public static ServerExceptionBuilder builder() {
        return new ServerExceptionBuilder(DEFAULT_EXCEPTION);
    }

    /**
     * 创建构建者
     * @return
     */
    public static ServerExceptionBuilder builder(ServerExceptionEnum exceptionEnum) {
        return new ServerExceptionBuilder(exceptionEnum);
    }
    /**
     * 类描述：
     * 创建ClientExceptionBuilder的构建对象
     * @author cfl
     * @date 2023/3/20 15:15
     * @version 1.0
     */
    public static class ServerExceptionBuilder extends BasicExceptionBuilder{
        /**
         * 有参构造，根据异常枚举对象，给属性设置默认值
         *
         * @param exceptionEnum
         */
        public ServerExceptionBuilder(ServerExceptionEnum exceptionEnum) {
            super(exceptionEnum);
        }

        /**
         * 设置Http状态码，值必须是正确的4xx或5xx状态码
         * @param status
         * @return
         */
        public ServerExceptionBuilder status(int status) {
            AssertUtil.isTrue(HttpStatus.valueOf(status).isError(), () -> ServerException.server("状态码设置错误"));
            this.status = status;
            return this;
        }

        /**
         * 设置code
         * @param code
         * @return
         */
        public ServerExceptionBuilder code(String code) {
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
        public ServerExceptionBuilder clientMessage(String clientMessage) {
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
        public ServerExceptionBuilder clientMessageTemplate(String clientMessageTemplate) {
            this.clientMessageTemplate = clientMessageTemplate;
            return this;
        }

        /**
         * 设置clientMessageParams</br>
         * 通常该方法和{@code clientMessageTemplate}一起使用，这样可以有效避免字符串拼接问题
         * @param clientMessageParams
         * @return
         */
        public ServerExceptionBuilder clientMessageParams(Object... clientMessageParams) {
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
        public ServerExceptionBuilder serverMessage(String serverMessage) {
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
        public ServerExceptionBuilder serverMessageTemplate(String serverMessageTemplate) {
            this.serverMessageTemplate = serverMessageTemplate;
            return this;
        }

        /**
         * 设置serverMessageParams</br>
         * 通常该方法和{@code serverMessageTemplate}一起使用，这样可以有效避免字符串拼接问题
         * @param serverMessageParams
         * @return
         */
        public ServerExceptionBuilder serverMessageParams(Object... serverMessageParams) {
            this.serverMessageParams = serverMessageParams;
            return this;
        }

        /**
         * 设置dataMap
         * @param dataMap
         * @return
         */
        public ServerExceptionBuilder dataMap(Map dataMap) {
            this.dataMap = dataMap;
            return this;
        }

        /**
         * 设置异常对象
         * @param cause
         * @return
         */
        public ServerExceptionBuilder cause(Throwable cause) {
            this.cause = cause;
            return this;
        }

        /**
         * 设置exceptionEnum，并使用exceptionEnum给其它成员设置默认值
         * @param exceptionEnum
         * @return
         */
        public ServerExceptionBuilder exceptionEnum(ExceptionEnumInterface exceptionEnum) {
            this.exceptionEnum = exceptionEnum;
            this.status = exceptionEnum.getStatus();
            this.code = exceptionEnum.getCode();
            this.clientMessage = exceptionEnum.getClientMessage();
            this.serverMessage = exceptionEnum.getServerMessage();
            return this;
        }

        /**
         * 根据成员变量，创建ClientException对象
         * @return BasicException
         */
        public ServerException build() {
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
            return new ServerException(status, code, clientMessage, serverMessage, cause);
        }
    }
}
