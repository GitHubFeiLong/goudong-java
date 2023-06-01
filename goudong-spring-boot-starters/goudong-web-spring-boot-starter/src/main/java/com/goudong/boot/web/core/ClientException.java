package com.goudong.boot.web.core;

import com.goudong.boot.web.enumerate.ClientExceptionEnum;
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
 * 客户端内部错误
 * @Author e-Feilong.Chen
 * @Date 2021/8/10 16:12
 */
public class ClientException extends BasicException {

    public static final Logger log = LoggerFactory.getLogger(ClientException.class);

    /**
     * 默认400异常
     */
    public static final ClientExceptionEnum DEFAULT_EXCEPTION = ClientExceptionEnum.BAD_REQUEST;

    /**
     * 代替 构造方法(用起舒服些)
     * @param clientExceptionEnum 客户端错误枚举
     * @return
     */
    @Deprecated
    public static ClientException clientException (ClientExceptionEnum clientExceptionEnum) {
        return new ClientException(clientExceptionEnum);
    }

    /**
     * 代替 构造方法(用起舒服些)
     * @param clientExceptionEnum 客户端错误枚举
     * @param clientMessage 动态自定义客户端提示信息
     * @return
     */
    @Deprecated
    public static ClientException clientException (ClientExceptionEnum clientExceptionEnum, String clientMessage) {
        return new ClientException(clientExceptionEnum, clientMessage);
    }

    /**
     * 代替 构造方法(用起舒服些)
     * @param clientExceptionEnum 客户端错误枚举
     * @param clientMessage 动态自定义客户端提示信息
     * @param serverMessage 动态自定义服务端异常提示信息
     * @return
     */
    @Deprecated
    public static ClientException clientException (ClientExceptionEnum clientExceptionEnum, String clientMessage, String serverMessage) {
        return new ClientException(clientExceptionEnum, clientMessage, serverMessage);
    }

    public ClientException() {
        super(DEFAULT_EXCEPTION);
    }

    public ClientException(String clientMessage) {
        super(DEFAULT_EXCEPTION, clientMessage);
    }

    public ClientException(String clientMessage, String serverMessage) {
        super(DEFAULT_EXCEPTION, clientMessage, serverMessage);
    }

    public ClientException(Throwable cause) {
        super(DEFAULT_EXCEPTION, cause);
    }

    public ClientException(String clientMessage, Throwable cause) {
        super(DEFAULT_EXCEPTION, clientMessage, cause);
    }

    public ClientException(ClientExceptionEnum clientExceptionEnum) {
        super(clientExceptionEnum);
    }

    public ClientException(ClientExceptionEnum clientExceptionEnum, Throwable cause) {
        super(clientExceptionEnum, cause);
    }

    public ClientException(ClientExceptionEnum clientExceptionEnum, String clientMessage) {
        super(clientExceptionEnum, clientMessage);
    }

    public ClientException(ClientExceptionEnum clientExceptionEnum, String clientMessage, Throwable cause) {
        super(clientExceptionEnum, clientMessage, cause);
    }

    public ClientException(ClientExceptionEnum clientExceptionEnum, String clientMessage, String serverMessage) {
        super(clientExceptionEnum, clientMessage, serverMessage);
    }

    public ClientException(ClientExceptionEnum clientExceptionEnum, String clientMessage, String serverMessage, Throwable cause) {
        super(clientExceptionEnum, clientMessage, serverMessage, cause);
    }

    /**
     * 构造方法
     * @param status http状态码
     * @param code 自定义状态码
     * @param clientMessage 客户端显示信息
     * @param serverMessage 服务端日志显示信息
     * @param cause 异常对象
     */
    public ClientException(int status, String code, String clientMessage, String serverMessage, Throwable cause) {
        super(status, code, clientMessage, serverMessage, cause);
    }

    // ~ builder创建
    // =================================================================================================================

    /**
     * 创建构建者
     * @return
     */
    public static ClientExceptionBuilder builder() {
        return new ClientExceptionBuilder(DEFAULT_EXCEPTION);
    }

    /**
     * 创建构建者
     * @return
     */
    public static ClientExceptionBuilder builder(ClientExceptionEnum exceptionEnum) {
        return new ClientExceptionBuilder(exceptionEnum);
    }

    /**
     * 类描述：
     * 创建ClientExceptionBuilder的构建对象
     * @author cfl
     * @date 2023/3/20 15:15
     * @version 1.0
     */
    public static class ClientExceptionBuilder extends BasicExceptionBuilder{
        /**
         * 有参构造，根据异常枚举对象，给属性设置默认值
         *
         * @param exceptionEnum
         */
        public ClientExceptionBuilder(ClientExceptionEnum exceptionEnum) {
            super(exceptionEnum);
        }

        /**
         * 设置Http状态码，值必须是正确的4xx或5xx状态码
         * @param status
         * @return
         */
        public ClientExceptionBuilder status(int status) {
            AssertUtil.isTrue(HttpStatus.valueOf(status).isError(), () -> ServerException.server("状态码设置错误"));
            this.status = status;
            return this;
        }

        /**
         * 设置code
         * @param code
         * @return
         */
        public ClientExceptionBuilder code(String code) {
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
        public ClientExceptionBuilder clientMessage(String clientMessage) {
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
        public ClientExceptionBuilder clientMessageTemplate(String clientMessageTemplate) {
            this.clientMessageTemplate = clientMessageTemplate;
            return this;
        }

        /**
         * 设置clientMessageParams</br>
         * 通常该方法和{@code clientMessageTemplate}一起使用，这样可以有效避免字符串拼接问题
         * @param clientMessageParams
         * @return
         */
        public ClientExceptionBuilder clientMessageParams(Object... clientMessageParams) {
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
        public ClientExceptionBuilder serverMessage(String serverMessage) {
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
        public ClientExceptionBuilder serverMessageTemplate(String serverMessageTemplate) {
            this.serverMessageTemplate = serverMessageTemplate;
            return this;
        }

        /**
         * 设置serverMessageParams</br>
         * 通常该方法和{@code serverMessageTemplate}一起使用，这样可以有效避免字符串拼接问题
         * @param serverMessageParams
         * @return
         */
        public ClientExceptionBuilder serverMessageParams(Object... serverMessageParams) {
            this.serverMessageParams = serverMessageParams;
            return this;
        }

        /**
         * 设置dataMap
         * @param dataMap
         * @return
         */
        public ClientExceptionBuilder dataMap(Map dataMap) {
            this.dataMap = dataMap;
            return this;
        }

        /**
         * 设置异常对象
         * @param cause
         * @return
         */
        public ClientExceptionBuilder cause(Throwable cause) {
            this.cause = cause;
            return this;
        }

        /**
         * 设置exceptionEnum，并使用exceptionEnum给其它成员设置默认值
         * @param exceptionEnum
         * @return
         */
        public ClientExceptionBuilder exceptionEnum(ExceptionEnumInterface exceptionEnum) {
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
        public ClientException build() {
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
            return new ClientException(status, code, clientMessage, serverMessage, cause);
        }
    }
}
