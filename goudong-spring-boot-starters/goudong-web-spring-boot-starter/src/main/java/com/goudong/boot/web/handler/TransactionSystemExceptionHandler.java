package com.goudong.boot.web.handler;

import com.goudong.boot.web.core.BasicException;
import com.goudong.boot.web.core.ClientException;
import com.goudong.boot.web.enumerate.ClientExceptionEnum;
import com.goudong.core.lang.Result;
import com.goudong.core.util.MessageFormatUtil;
import org.hibernate.validator.internal.engine.ConstraintViolationImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 类描述：
 * 事务相关的异常
 * @author cfl
 * @version 1.0
 * @date 2022/11/1 17:29
 */
@Order(10)
@RestControllerAdvice
public class TransactionSystemExceptionHandler implements HandlerInterface{

    public static final Logger log = LoggerFactory.getLogger(TransactionSystemExceptionHandler.class);

    /**
     * 事务异常
     * 1. 数据库字段长度限制(实体类上加上Validation相关注解,比如@Size)
     * @param exception
     * @return
     */
    @ExceptionHandler(value = TransactionSystemException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<BasicException> transactionSystemExceptionDispose(TransactionSystemException exception) throws NoSuchFieldException {
        BasicException basicException = BasicException.generateByServer(exception);

        // 数据库的一些校验异常(比如字段长度等)
        if (exception.getCause().getCause() instanceof ConstraintViolationException) {
            ConstraintViolationException constraintViolationException = (ConstraintViolationException) exception.getCause().getCause();
            Set<ConstraintViolation<?>> constraintViolations = constraintViolationException.getConstraintViolations();
            if (constraintViolations != null && !constraintViolations.isEmpty()) {
                List<ConstraintViolation<?>> list = new ArrayList(constraintViolations);
                // clientMessage 只取第一条
                ConstraintViolationImpl constraintViolation = (ConstraintViolationImpl) list.get(0);
                // 报错字段
                String fieldName = constraintViolation.getPropertyPath().toString();
                String paramName = fieldName;
                /*
                    示例:
                    个数必须在0和16之间
                 */
                String validationMessage = constraintViolation.getMessage();

                String clientMessage = MessageFormatUtil.format("参数{}的值{}", paramName, validationMessage);
                basicException = ClientException.client(ClientExceptionEnum.BAD_REQUEST, clientMessage, constraintViolationException.getMessage());
            }

        }

        // 打印错误日志
        log.error(LOG_ERROR_INFO,
                basicException.getStatus(),
                basicException.getCode(),
                basicException.getClientMessage(),
                basicException.getServerMessage(),
                exception.getMessage());

        // 堆栈跟踪
        printErrorMessage(log, "transactionSystemExceptionDispose", exception);

        return Result.ofFail(basicException);
    }
}
