package com.goudong.boot.web.handler;

import com.goudong.boot.web.core.BasicException;
import com.goudong.boot.web.core.ServerException;
import com.goudong.boot.web.enumerate.ServerExceptionEnum;
import com.goudong.core.lang.Result;
import com.goudong.core.util.MessageFormatUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.UnexpectedTypeException;
import java.util.ArrayList;
import java.util.List;

/**
 * 类描述：
 * 全局捕获异常
 * @author cfl
 * @date 2022/10/23 21:13
 * @version 1.0
 */
@Order
@RestControllerAdvice
public class BasicExceptionHandler {

    public static final Logger log = LoggerFactory.getLogger(BasicExceptionHandler.class);

    /**
     * 错误日志模板
     */
    public static final String LOG_ERROR_INFO = "http响应码：{}，错误代码：{}，客户端错误信息：{}，服务端错误信息：{}，扩展信息：{}";

    /**
     * 请求对象
     */
    public final HttpServletRequest request;

    /**
     * 响应对象
     */
    public final HttpServletResponse response;

    public BasicExceptionHandler(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }

    /**
     * 打印日志
     * @param exceptionHandlerMethod 异常处理的方法
     * @param exception 异常对象
     */
    public static void printErrorMessage(String exceptionHandlerMethod, Throwable exception) {
        // 开启debug就打印堆栈
        if (log.isDebugEnabled()) {
            exception.printStackTrace();
        } else {
            log.error("程序捕获全局异常的方法-{}，错误信息：{}", exceptionHandlerMethod,exception);
        }
    }

    //~ 常见异常
    //==================================================================================================================
    /**
     * 全局处理自定义异常
     * @param exception
     * @return
     */
    @ExceptionHandler(BasicException.class)
    public Result<BasicException> basicExceptionDispose(BasicException exception){
        // 设置响应码
        response.setStatus(exception.getStatus());
        // if (exception.getStatus() == HttpStatus.UNAUTHORIZED.value()) {
        //     response.setHeader(Header.WWW_AUTHENTICATE.getValue(), "Basic realm=\"\"");
        // }
        // 打印错误日志
        log.error(BasicExceptionHandler.LOG_ERROR_INFO, exception.getStatus(), exception.getCode(), exception.getClientMessage(), exception.getServerMessage(), exception.getDataMap());
        // 堆栈跟踪
        printErrorMessage("basicExceptionDispose", exception);

        return Result.ofFail(exception);
    }


    /**
     * 运行时异常，其它框架抛出的异常未封装（openfeign调用服务时）
     * 后续可能会继续扩展....
     * @param exception
     * @return
     */
    @ExceptionHandler(RuntimeException.class)
    public Result<BasicException> runtimeExceptionDispose(RuntimeException exception){
        BasicException basicException = BasicException.generateByServer(exception);

        // 设置响应码
        response.setStatus(basicException.getStatus());
        // 打印错误日志
        log.error(BasicExceptionHandler.LOG_ERROR_INFO, basicException.getStatus(), basicException.getCode(), basicException.getClientMessage(), basicException.getServerMessage(), basicException.getDataMap());
        // 堆栈跟踪
        printErrorMessage("runtimeExceptionDispose", exception);

        return Result.ofFail(basicException);
    }

    /**
     * 捕获意料之外的异常Exception
     * @param exception
     * @return
     */
    @ExceptionHandler(Throwable.class)
    public Result<Throwable> otherErrorDispose(Throwable exception){
        BasicException serverException = new ServerException(ServerExceptionEnum.SERVER_ERROR);
        this.response.setStatus(serverException.status);
        // 打印错误日志
        log.error(BasicExceptionHandler.LOG_ERROR_INFO, serverException.status, serverException.code, serverException.clientMessage, exception.getMessage(), null);
        // 堆栈跟踪
        printErrorMessage("otherErrorDispose", exception);
        serverException.setServerMessage(exception.getMessage());
        return Result.ofFail(serverException);
    }

    //~ web常见异常
    //==================================================================================================================
    /**
     * 400 Bad Request
     * 因发送的请求语法错误,服务器无法正常读取.
     * @param exception
     * @return
     */
    @ExceptionHandler(value = {
            BindException.class,
            UnexpectedTypeException.class,
            ConstraintViolationException.class,
            MethodArgumentNotValidException.class,
            HttpMessageNotReadableException.class,
            IllegalArgumentException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Throwable> ValidExceptionDispose(Exception exception){
        List<String> messages = new ArrayList<>();
        if (exception instanceof BindException) {
            List<ObjectError> list = ((BindException) exception).getAllErrors();
            for (ObjectError item : list) {
                messages.add(item.getDefaultMessage());
            }
        } else if (exception instanceof ConstraintViolationException) { // 属性校验失败，不满足注解
            for (ConstraintViolation<?> constraintViolation : ((ConstraintViolationException)exception).getConstraintViolations()) {
                String full = MessageFormatUtil.format("参数{} {}",constraintViolation.getPropertyPath().toString(), constraintViolation.getMessage());
                messages.add(full);
            }
        } else if (exception instanceof HttpMessageNotReadableException) {
            messages.add("请求参数丢失");
        } else if (exception instanceof IllegalArgumentException) {
            messages.add(exception.getMessage());
        } else if (exception instanceof UnexpectedTypeException) { // 参数校验注解写错了（比如Integer写了@NotBlank）
            throw ServerException.server(ServerExceptionEnum.SERVER_ERROR, "参数校验注解写错了", exception.getMessage());
        } else  {
            messages.add(((MethodArgumentNotValidException)exception).getBindingResult().getFieldError().getDefaultMessage());
        }

        String message = String.join(",", messages);
        // 打印错误日志
        log.error(BasicExceptionHandler.LOG_ERROR_INFO, HttpStatus.BAD_REQUEST.value(), "VALIDATION", message, exception.getMessage());
        // 堆栈跟踪
        printErrorMessage("ValidExceptionDispose", exception);

        return Result.ofFailByBadRequest(message, exception.getMessage());
    }

    /**
     * 404 Not Found
     * 请求失败，请求所希望得到的资源未被在服务器上发现。没有信息能够告诉用户这个状况到底是暂时的还是永久的。假如服务器知道情况的话，应当使用410状态码来告知旧资源因为某些内部的配置机制问题，已经永久的不可用，而且没有任何可以跳转的地址。404这个状态码被广泛应用于当服务器不想揭示到底为何请求被拒绝或者没有其他适合的响应可用的情况下。出现这个错误的最有可能的原因是服务器端没有这个页面。
     * @return
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Result notFound(Exception exception) {
        // 堆栈跟踪
        printErrorMessage("notFound", exception);
        return Result.ofFailByNotFound(request.getRequestURL().toString());
    }

    /**
     * 405 Method Not Allowed
     * 请求行中指定的请求方法不能被用于请求相应的资源。该响应必须返回一个Allow 头信息用以表示出当前资源能够接受的请求方法的列表。
     *
     * 鉴于 PUT，DELETE 方法会对服务器上的资源进行写操作，因而绝大部分的网页服务器都不支持或者在默认配置下不允许上述请求方法，对于此类请求均会返回405错误。
     * @return
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public Result methodNotAllowed(HttpRequestMethodNotSupportedException exception) {
        // 堆栈跟踪
        printErrorMessage("methodNotAllowed", exception);
        return Result.ofFailByMethodNotAllowed(request.getRequestURL().toString());
    }

    /**
     * 406 Not Acceptable
     * 请求的资源的内容特性无法满足请求头中的条件，因而无法生成响应实体。
     * @return
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public Result notAcceptable(Exception exception) {
        // 堆栈跟踪
        printErrorMessage("notAcceptable", exception);
        final String contentType = "Content-Type";
        final String header = request.getHeader(contentType);
        String message = MessageFormatUtil.format("{} 资源不支持{}:{} 方式", request.getRequestURL().toString(), contentType, header);
        return Result.ofFailByNotAcceptable(message);
    }
}
