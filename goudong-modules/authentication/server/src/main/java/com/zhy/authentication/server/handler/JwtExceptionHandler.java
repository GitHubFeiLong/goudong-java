package com.zhy.authentication.server.handler;


import com.goudong.boot.web.core.BasicException;
import com.goudong.boot.web.core.ClientException;
import com.goudong.boot.web.handler.HandlerInterface;
import com.goudong.core.lang.Result;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 类描述：
 * Jwt异常处理
 * @author cfl
 * @date 2023/7/24 9:16
 * @version 1.0
 */
@Slf4j
@Order(0)
@RestControllerAdvice
public class JwtExceptionHandler implements HandlerInterface{
    public JwtExceptionHandler() {
    }

    /**
     * jwt 签名错误
     * @param exception
     * @return
     */
    @ExceptionHandler(SignatureException.class)
    public Result<BasicException> signatureExceptionDispose(SignatureException exception) {
        BasicException basicException = ClientException.builder()
                .clientMessage("解析签名失败")
                .status(HttpStatus.UNAUTHORIZED.value())
                .code("401")
                .build();
        log.error("http响应码：{}，错误代码：{}，客户端错误信息：{}，服务端错误信息：{}，扩展信息：{}", new Object[]{basicException.getStatus(), basicException.getCode(), basicException.getClientMessage(), basicException.getServerMessage(), basicException.getDataMap()});
        this.printErrorMessage(log, "dataIntegrityViolationExceptionDispose", exception);
        return Result.ofFail(basicException);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public Result<BasicException> expiredJwtExceptionDispose(ExpiredJwtException exception) {
        BasicException basicException = ClientException.builder()
                .clientMessage("令牌已失效,请重新登录")
                .status(HttpStatus.UNAUTHORIZED.value())
                .code("401")
                .build();
        log.error("http响应码：{}，错误代码：{}，客户端错误信息：{}，服务端错误信息：{}，扩展信息：{}", new Object[]{basicException.getStatus(), basicException.getCode(), basicException.getClientMessage(), basicException.getServerMessage(), basicException.getDataMap()});
        this.printErrorMessage(log, "dataIntegrityViolationExceptionDispose", exception);
        return Result.ofFail(basicException);
    }
}

