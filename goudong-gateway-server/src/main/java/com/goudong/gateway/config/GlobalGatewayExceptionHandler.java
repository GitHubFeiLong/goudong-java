package com.goudong.gateway.config;

import com.goudong.boot.web.core.BasicException;
import com.goudong.boot.web.core.ClientException;
import com.goudong.boot.web.core.ServerException;
import com.goudong.boot.web.enumerate.ClientExceptionEnum;
import com.goudong.boot.web.enumerate.ServerExceptionEnum;
import com.goudong.core.lang.Result;
import feign.FeignException;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.codec.HttpMessageWriter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.result.view.ViewResolver;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.ConnectException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * 类描述：
 * 网关异常全局处理
 * @Author e-Feilong.Chen
 * @Date 2021/8/10 15:56
 */
@Slf4j
@Getter
@Setter
public class GlobalGatewayExceptionHandler implements ErrorWebExceptionHandler {

    private List<HttpMessageReader<?>> messageReaders = Collections.emptyList();
    private List<HttpMessageWriter<?>> messageWriters = Collections.emptyList();
    private List<ViewResolver> viewResolvers = Collections.emptyList();
    private ThreadLocal<BasicException> threadLocal = new ThreadLocal<>();

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable throwable) {
        throwable.printStackTrace();
        log.error("网关异常全局处理，异常信息：{}", throwable.getMessage());
        BasicException basicException = getBasicException(throwable);

        //这里只是做个最简单的同一的异常结果输出，实际业务可根据throwable不同的异常处理不同的逻辑
        threadLocal.set(basicException);
        ServerRequest newRequest = ServerRequest.create(exchange, this.messageReaders);

        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse).route(newRequest)
                .switchIfEmpty(Mono.error(throwable))
                .flatMap((handler) -> handler.handle(newRequest))
                .flatMap((response) -> write(exchange, response));
    }

    /**
     * 根据异常返回BasicException
     * @param throwable
     * @return
     */
    private static BasicException getBasicException(Throwable throwable) {
        if (throwable instanceof BasicException) {
            return (BasicException) throwable;
        }

        // 直接路由到微服务时，服务未注册时会报如下错
        if (throwable instanceof NotFoundException) {
            NotFoundException notFoundException = (NotFoundException) throwable;
            // TODO 不知道这个状态码和异常是不是一对一，如果是那么就不需要再判断状态码了
            // 503 网关未找到服务
            if (notFoundException.getStatus() == HttpStatus.SERVICE_UNAVAILABLE) {
                // Unable to find instance for goudong-oauth2-server
                return BasicException.server(ServerExceptionEnum.SERVICE_UNAVAILABLE, notFoundException.getMessage());
            }
        }

        /*
            1. 服务已经停止，nacos中服务还未下线，此时调用时会出现建立连接异常
         */
        if (throwable instanceof FeignException) {
            if (((FeignException) throwable).getCause() instanceof ConnectException) {
                return ServerException.server(ServerExceptionEnum.SERVICE_UNAVAILABLE, throwable.getMessage());
            }
        }

        if (throwable instanceof ResponseStatusException) {
            ResponseStatusException responseStatusException = (ResponseStatusException) throwable;
            // 404 资源不存在
            if (responseStatusException.getStatus() == HttpStatus.NOT_FOUND) {
                return ClientException.client(ClientExceptionEnum.NOT_FOUND);
            }
        }

        if (throwable instanceof RuntimeException) {
            Throwable cause = throwable.getCause();
            /*
                客户端异常
                openFeign调用远程服务，服务还未注册到nacos中
             */
            if (cause instanceof com.netflix.client.ClientException) {
                return ServerException.server(ServerExceptionEnum.SERVICE_UNAVAILABLE, cause.getMessage());
            }
        }

        return BasicException.generateByServer(throwable);
    }


    /**
     * 统一返回指定异常信息(指定json格式输出)
     * @param request
     * @return
     */
    protected Mono<ServerResponse> renderErrorResponse(ServerRequest request){
        int status = Optional.ofNullable(threadLocal.get().getStatus()).orElseGet(() -> 500);
        return ServerResponse.status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(Result.ofFail(threadLocal.get())));
    }

    /**
     * 参考DefaultErrorWebExceptionHandler
     */
    private Mono<? extends Void> write(ServerWebExchange exchange, ServerResponse response) {
        exchange.getResponse().getHeaders().setContentType(response.headers().getContentType());
        return response.writeTo(exchange, new ResponseContext());
    }

    /**
     * 参考DefaultErrorWebExceptionHandler
     */
    private class ResponseContext implements ServerResponse.Context {
        private ResponseContext() {
        }

        @Override
        public List<HttpMessageWriter<?>> messageWriters() {
            return GlobalGatewayExceptionHandler.this.messageWriters;
        }

        @Override
        public List<ViewResolver> viewResolvers() {
            return GlobalGatewayExceptionHandler.this.viewResolvers;
        }
    }
}

