//package com.goudong.commons.controller;
//
//import com.goudong.commons.exception.BasicException;
//import com.goudong.commons.pojo.Result;
//import com.goudong.commons.pojo.Url;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.catalina.connector.RequestFacade;
//import org.apache.catalina.connector.ResponseFacade;
//import org.springframework.boot.web.servlet.error.ErrorController;
//import org.springframework.http.HttpStatus;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.annotation.Resource;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
///**
// * 其他 ControllerAdvice 不能捕获的异常，例如404 405异常
// * @Author msi
// * @Date 2021-05-06 13:13
// * @Version 1.0
// */
//@Slf4j
//@RestController
//public class ErrorHandlerController implements ErrorController {
//
//    @Resource
//    private Url url1;
//    /**
//     * 手动抛出 404、405 等异常，让 GlobalExceptionHandler 进行处理
//     * @param request
//     */
//    @RequestMapping("/error")
//    public void handleError(HttpServletRequest request, HttpServletResponse response){
//
//        //获取statusCode:401,404,500
//        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
//        HttpStatus httpStatus = HttpStatus.valueOf(statusCode);
////        ((RequestFacade) ((ApplicationHttpRequest) request)._getHttpServletRequest()).request.getRequestURL()
////        String url = ((RequestFacade) ((ApplicationHttpRequest) request).request).request.getRequestURL();
////        response.response.request.coyoteRequest
//        log.info(url1.getUrl());
//        String url = request.getRequestURL().toString();
//        switch (httpStatus) {
//            case NOT_FOUND: // 404
//                // 打印错误日志
//                Result.ofFail(BasicException.ClientException.noHandlerFoundException(url));
//                break;
//            case METHOD_NOT_ALLOWED: // 405
//                // 打印错误日志
//                Result.ofFail(BasicException.ClientException.methodNotAllowedException(url));
//                break;
//            default:
//
//        }
//
//    }
//
//
//    /**
//     * @deprecated
//     */
//    @Override
//    public String getErrorPath() {
//        return "/error";
//    }
//}
