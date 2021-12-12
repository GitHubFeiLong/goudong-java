package com.goudong.commons.exception;


import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.goudong.commons.enumerate.ClientExceptionEnum;
import com.goudong.commons.enumerate.ServerExceptionEnum;
import com.goudong.commons.pojo.Result;
import com.goudong.commons.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
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
import java.lang.reflect.UndeclaredThrowableException;
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
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 错误日志模板
     */
    public static final String LOG_ERROR_INFO = "http响应码：{}，错误代码：{}，客户端错误信息：{}，服务端错误信息：{}";

    /**
     * 请求对象
     */
    private final HttpServletRequest request;

    /**
     * 响应对象
     */
    private final HttpServletResponse response;

    public GlobalExceptionHandler(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }


    /**
     * 当sentinel资源未配置 blockHandler、fallback 和 defaultFallback   异常
     * @param e
     * @return
     */
    @ExceptionHandler(UndeclaredThrowableException.class)
    public Result blockExceptionDispose(BlockException e){
        e.printStackTrace();
        log.error(GlobalExceptionHandler.LOG_ERROR_INFO, 200, 200, "服务器繁忙，请重试", e.getRule().toString());
        return Result.ofFail();
    }

    /**
     * 全局处理自定义异常
     * @param exception
     * @return
     */
    @ExceptionHandler(BasicException.class)
    public Result<BasicException> basicExceptionDispose(BasicException exception){
        // 设置响应码
        response.setStatus(exception.getStatus());
        // 打印错误日志
        log.error(GlobalExceptionHandler.LOG_ERROR_INFO, exception.getStatus(), exception.getCode(), exception.getClientMessage(), exception.getServerMessage());
        // 堆栈跟踪
        exception.printStackTrace();

        return Result.ofFail(exception);
    }

    /**
     * 唯一索引全局捕获
     * ps 最好自己捕获抛出详细信息给前端,但是都用400
     * @param e
     * @return
     */
    @ExceptionHandler(value = DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<BasicException> dataIntegrityViolationExceptionDispose(DataIntegrityViolationException e) {
        // 打印错误日志
        e.printStackTrace();
        BasicException basicException = null;
        String message = e.getRootCause().getMessage();

        // 外键约束
        if (message.startsWith("Duplicate entry") && message.indexOf("for key")!=-1) {
            // e.getRootCause().getMessage() => Duplicate entry '组长' for key 'uk_base_role__role_name_cn
            String value = message.split(" ")[2].replace("'","");
            String clientMessage = StringUtil.format("{} 在数据库中已存在，请勿重复新增", value);
            String serverMessage = message;
            basicException = ClientException.clientException(ClientExceptionEnum.BAD_REQUEST, clientMessage, serverMessage);
        } else if (message.startsWith("Cannot add or update a child row: a foreign key constraint fails") && message.endsWith("ON DELETE RESTRICT ON UPDATE RESTRICT)")) {
            // 数据库更新或删除表的数据，外键值不正确
            // e.getRootCause().getMessage() => Cannot add or update a child row: a foreign key constraint fails (`icc`.`base_role_menu`, CONSTRAINT `fk_base_role_menu__base_menu_id` FOREIGN KEY (`base_menu_id`) REFERENCES `base_menu` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT)
            String clientMessage = StringUtil.format("参数错误");
            String serverMessage = message;
            basicException = ClientException.clientException(ClientExceptionEnum.BAD_REQUEST, clientMessage, serverMessage);
        }

        log.error(GlobalExceptionHandler.LOG_ERROR_INFO, basicException.getStatus(), basicException.getCode(), basicException.getClientMessage(), basicException.getServerMessage());
        // 堆栈跟踪
        e.printStackTrace();
        return Result.ofFail(basicException);
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
        log.error(GlobalExceptionHandler.LOG_ERROR_INFO, basicException.getStatus(), basicException.getCode(), basicException.getClientMessage(), basicException.getServerMessage());
        // 堆栈跟踪
        exception.printStackTrace();

        return Result.ofFail(basicException);
    }

    /**
     * 捕获意料之外的异常Exception
     * @param e
     * @return
     */
    @ExceptionHandler(Throwable.class)
    public Result<Throwable> otherErrorDispose(Throwable e){
        BasicException serverException = new ServerException(ServerExceptionEnum.SERVER_ERROR);
        this.response.setStatus(serverException.status);
        // 打印错误日志
        log.error(GlobalExceptionHandler.LOG_ERROR_INFO, serverException.status, serverException.code, serverException.clientMessage, e.getMessage());
        // 堆栈跟踪
        e.printStackTrace();
        serverException.setServerMessage(e.getMessage());
        return Result.ofFail(serverException);
    }

    /*==========================================================================
        常见自定义异常
    ==========================================================================*/

    /**
     * 400 Bad Request
     * 因发送的请求语法错误,服务器无法正常读取.
     * @param e
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
    public Result<Throwable> ValidExceptionDispose(Exception e){
        List<String> messages = new ArrayList<>();
        if (e instanceof BindException) {
            List<ObjectError> list = ((BindException) e).getAllErrors();
            for (ObjectError item : list) {
                messages.add(item.getDefaultMessage());
            }
        } else if (e instanceof ConstraintViolationException) { // 属性校验失败，不满足注解
            for (ConstraintViolation<?> constraintViolation : ((ConstraintViolationException)e).getConstraintViolations()) {
                String full = StringUtil.format("参数{} {}",constraintViolation.getPropertyPath().toString(), constraintViolation.getMessage());
                messages.add(full);
            }
        } else if (e instanceof HttpMessageNotReadableException) {
            messages.add("请求参数丢失");
        } else if (e instanceof IllegalArgumentException) {
            messages.add(e.getMessage());
        } else if (e instanceof UnexpectedTypeException) { // 参数校验注解写错了（比如Integer写了@NotBlank）
            throw ServerException.serverException(ServerExceptionEnum.SERVER_ERROR, "参数校验注解写错了", e.getMessage());
        } else  {
            messages.add(((MethodArgumentNotValidException)e).getBindingResult().getFieldError().getDefaultMessage());
        }

        String message = String.join(",", messages);
        // 打印错误日志
        log.error(GlobalExceptionHandler.LOG_ERROR_INFO, HttpStatus.BAD_REQUEST.value(), "VALIDATION", message, e.getMessage());
        // 堆栈跟踪
        e.printStackTrace();

        return Result.ofFailByBadRequest(message, e.getMessage());
    }

    /**
     * 404 Not Found
     * 请求失败，请求所希望得到的资源未被在服务器上发现。没有信息能够告诉用户这个状况到底是暂时的还是永久的。假如服务器知道情况的话，应当使用410状态码来告知旧资源因为某些内部的配置机制问题，已经永久的不可用，而且没有任何可以跳转的地址。404这个状态码被广泛应用于当服务器不想揭示到底为何请求被拒绝或者没有其他适合的响应可用的情况下。出现这个错误的最有可能的原因是服务器端没有这个页面。
     * @return
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Result notFound() {
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
    public Result methodNotAllowed() {
        return Result.ofFailByMethodNotAllowed(request.getRequestURL().toString());
    }

    /**
     * 406 Not Acceptable
     * 请求的资源的内容特性无法满足请求头中的条件，因而无法生成响应实体。
     * @return
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public Result notAcceptable() {
        final String contentType = "Content-Type";
        final String header = request.getHeader(contentType);
        String message = StringUtil.format("{} 资源不支持{}:{} 方式", request.getRequestURL().toString(), contentType, header);
        return Result.ofFailByNotAcceptable(message);
    }
}
