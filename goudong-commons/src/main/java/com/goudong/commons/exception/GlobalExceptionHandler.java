package com.goudong.commons.exception;


import com.goudong.commons.enumerate.ClientExceptionEnum;
import com.goudong.commons.enumerate.ServerExceptionEnum;
import com.goudong.commons.pojo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

/**
 * 全局捕获异常
 * 1.捕获返回json格式
 * 2.捕获返回页面
 * @ClassName GlobalExceptionHandler
 * @Author msi
 * @Date 2019/7/28 21:51
 */
@Slf4j
@RestControllerAdvice(basePackages = "com.goudong")
public class GlobalExceptionHandler {
    /**
     * 错误日志模板
     */
    public static final String LOG_ERROR_INFO = "http响应码：{}，错误代码：{}，客户端错误信息：{}，服务端错误信息：{}";


    /**
     * 响应对象
     */
    @Resource
    private HttpServletResponse response;

    /**
     * 全局处理客户端错误
     * @param exception      客户端异常
     * @return
     */
    @ExceptionHandler(BasicException.class)
    public Result<BasicException> clientExceptionDispose(BasicException exception){
        // 设置响应码
        response.setStatus(exception.getStatus());
        // 打印错误日志
        log.error(GlobalExceptionHandler.LOG_ERROR_INFO, exception.getStatus(), exception.getCode(), exception.getClientMessage(), exception.getServerMessage());
        // 堆栈跟踪
        exception.printStackTrace();

        return Result.ofFail(exception);
    }

    /**
     * 请求方式错误
     * @param exception
     * @return
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<String> HttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException exception) {
        return Result.ofFail("请求方法错误");
    }

    /**
     * Spring Validation 注解异常
     * @param e
     * @return
     */
    @ExceptionHandler(value = {BindException.class, ConstraintViolationException.class, MethodArgumentNotValidException.class})
    public Result<Throwable> ValidExceptionDispose(Exception e){
        BasicException clientException = new BasicException.ClientException(ClientExceptionEnum.PARAMETER_ERROR);
        this.response.setStatus(clientException.status);
        List<String> messages = new ArrayList<>();
        if (e instanceof BindException) {
            List<ObjectError> list = ((BindException) e).getAllErrors();
            for (ObjectError item : list) {
                messages.add(item.getDefaultMessage());
            }
        } else if (e instanceof ConstraintViolationException) {
            for (ConstraintViolation<?> constraintViolation : ((ConstraintViolationException)e).getConstraintViolations()) {
                messages.add(constraintViolation.getMessage());
            }
        } else {
            messages.add(((MethodArgumentNotValidException)e).getBindingResult().getFieldError().getDefaultMessage());
        }

        String message = String.join(",", messages);
        // 打印错误日志
        log.error(GlobalExceptionHandler.LOG_ERROR_INFO, 400, "VALIDATION", message, e.getMessage());
        clientException.setClientMessage(message);
        clientException.setServerMessage(e.getMessage());
        // 堆栈跟踪
        e.printStackTrace();
        return Result.ofFail(clientException);
    }

    /**
     * 捕获意料之外的异常Exception
     * @param e
     * @return
     */
    @ExceptionHandler(Throwable.class)
    public Result<Throwable> otherErrorDispose(Throwable e){
        BasicException serverException = new BasicException.ServerException(ServerExceptionEnum.SERVER_ERROR);
        this.response.setStatus(serverException.status);
        // 打印错误日志
        log.error(GlobalExceptionHandler.LOG_ERROR_INFO, serverException.status, serverException.code, serverException.clientMessage, e.getMessage());
        // 堆栈跟踪
        e.printStackTrace();
        serverException.setServerMessage(e.getMessage());
        return Result.ofFail(serverException);
    }

}
