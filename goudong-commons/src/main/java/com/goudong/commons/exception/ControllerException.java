package com.goudong.commons.exception;

import com.goudong.commons.pojo.Result;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <b>标题:    </b><br />
 * <pre>
 * </pre>
 *
 * @author 毛宇鹏
 * @date 创建于 上午11:23 2018/1/9
 */
//@ControllerAdvice
//@RestController
//@RequestMapping( value = "/error")
public class ControllerException  {
    @Resource
    private HttpServletRequest request;

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    public String httpRequestMethodNotSupportedException(HttpServletResponse response) {
        response.setStatus(200);
        return "404";
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseBody
    public String httpMediaTypeNotSupportedException(HttpServletResponse response) {
        response.setStatus(200);
        return "415";
    }

//    @ExceptionHandler(NoHandlerFoundException.class)
//    @ResponseStatus(HttpStatus.NOT_FOUND)
//    public Result notFoundPage404() {
//        System.out.println(1);
//        return Result.ofFail(BasicException.ClientException.noHandlerFoundException(request.getRequestURL().toString()));
//    }

}
