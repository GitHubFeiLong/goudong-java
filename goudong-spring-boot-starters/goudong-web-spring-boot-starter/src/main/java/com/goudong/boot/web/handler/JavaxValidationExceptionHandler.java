package com.goudong.boot.web.handler;

import com.goudong.boot.web.core.BasicException;
import com.goudong.boot.web.core.ServerException;
import com.goudong.boot.web.enumerate.ClientExceptionEnum;
import com.goudong.boot.web.enumerate.ServerExceptionEnum;
import com.goudong.core.lang.Result;
import com.goudong.core.util.MessageFormatUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.UnexpectedTypeException;
import java.util.ArrayList;
import java.util.List;

/**
 * 类描述：
 * javax.validation 相关的异常处理
 * @author cfl
 * @version 1.0
 * @date 2022/11/2 16:37
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class JavaxValidationExceptionHandler implements HandlerInterface {

    private static final Logger log = LoggerFactory.getLogger(JavaxValidationExceptionHandler.class);

    /**
     * 500 参数校验注解写错了
     * @param exception
     * @return
     */
    @ExceptionHandler(UnexpectedTypeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<BasicException> UnexpectedTypeExceptionExceptionDispose(Exception exception){
        BasicException basicException = ServerException.server(ServerExceptionEnum.SERVER_ERROR, "参数校验注解写错了", exception.getMessage());

        // 打印错误日志
        log.error(LOG_ERROR_INFO,
                basicException.getStatus(),
                basicException.getCode(),
                basicException.getClientMessage(),
                basicException.getServerMessage(),
                exception.getMessage());

        // 堆栈跟踪
        printErrorMessage(log, "UnexpectedTypeExceptionExceptionDispose", exception);

        return Result.ofFail(basicException);
    }


    /**
     * 400 参数校验注解不满足
     * @param exception
     * @return
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Throwable> ConstraintViolationExceptionDispose(Exception exception){
        List<String> messages = new ArrayList<>();
        // 属性校验失败，不满足注解
        for (ConstraintViolation<?> constraintViolation : ((ConstraintViolationException)exception).getConstraintViolations()) {
            String full = MessageFormatUtil.format("参数{} {}",constraintViolation.getPropertyPath().toString(), constraintViolation.getMessage());
            messages.add(full);
        }

        String message = String.join(",", messages);

        BasicException basicException = ClientExceptionEnum.BAD_REQUEST.client(message, exception.getMessage());
        // 打印错误日志
        log.error(LOG_ERROR_INFO,
                basicException.getStatus(),
                basicException.getCode(),
                basicException.getClientMessage(),
                basicException.getServerMessage(),
                exception.getMessage());

        // 堆栈跟踪
        printErrorMessage(log, "ConstraintViolationExceptionDispose", exception);

        return Result.ofFail(basicException);
    }
}
