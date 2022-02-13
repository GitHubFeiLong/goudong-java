package com.goudong.gateway.filter;

import com.goudong.commons.constant.core.HttpHeaderConst;
import com.goudong.commons.security.aes.AES;
import com.goudong.commons.security.rsa.ServerRSA;
import com.goudong.commons.utils.core.LogUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.factory.rewrite.CachedBodyOutputMessage;
import org.springframework.cloud.gateway.support.BodyInserterContext;
import org.springframework.cloud.gateway.support.DefaultServerRequest;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 类描述：
 * post请求的参数解密,处理完成后，将请求头中的密钥(Aes-Key)删除掉
 * @author msi
 * @date 2022/2/13 20:06
 * @version 1.0
 */
@Slf4j
@Component
public class PostRequestParameterDecryptFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        // 非post请求直接放行
        if (request.getMethod() != HttpMethod.POST) {
            return chain.filter(exchange);
        }
        // post请求处理
        return operationExchange(exchange, chain);
    }

    /**
     * 修改请求参数的具体逻辑
     * TODO 这个还没完全理解，Gateway后面要系统的再学一遍
     * @param exchange
     * @param chain
     * @return
     */
    private Mono<Void> operationExchange(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 查看请求头是否自定义的请求头(Aes-Key)
        String aesKeyEncrypt = exchange.getRequest().getHeaders().getFirst(HttpHeaderConst.AES_KEY);
        if(aesKeyEncrypt != null){
            // 使用RSA私钥解密密文的AES密钥
            String aesKey = ServerRSA.getInstance().privateKeyDecrypt(aesKeyEncrypt);
            LogUtil.info(log,"本次请求使用了AES解密,AES密钥为{}", aesKey);

            // 删除Aes-Key
            ServerHttpRequest.Builder requestBuilder = exchange.getRequest().mutate();
            requestBuilder.headers(k -> k.remove(HttpHeaderConst.AES_KEY));
            ServerHttpRequest request = requestBuilder.build();
            exchange.mutate().request(request).build();

            // 获取请求的媒体类型(application/json等)
            MediaType mediaType = exchange.getRequest().getHeaders().getContentType();
            // 读取请求体并使用AES解密请求参数
            ServerRequest serverRequest = new DefaultServerRequest(exchange);
            Mono<String> modifiedBody = serverRequest.bodyToMono(String.class)
                    .flatMap(body -> {
                        if (MediaType.APPLICATION_JSON.isCompatibleWith(mediaType)) {
                            // 对原先的body进行修改操作
                            String newBody = AES.build().secretKey(aesKey).decrypt(body);
                            return Mono.just(newBody);
                        }
                        return Mono.empty();
                    });
            BodyInserter bodyInserter = BodyInserters.fromPublisher(modifiedBody, String.class);

            /*
                创建新的请求,删除Aes-Key
             */
            HttpHeaders headers = new HttpHeaders();
            headers.putAll(exchange.getRequest().getHeaders());
            headers.remove(HttpHeaders.CONTENT_LENGTH);
            CachedBodyOutputMessage outputMessage = new CachedBodyOutputMessage(exchange, headers);
            return bodyInserter.insert(outputMessage, new BodyInserterContext())
                    .then(Mono.defer(() -> {
                        ServerHttpRequestDecorator decorator = new ServerHttpRequestDecorator(
                                exchange.getRequest()) {
                            @Override
                            public HttpHeaders getHeaders() {
                                long contentLength = headers.getContentLength();
                                HttpHeaders httpHeaders = new HttpHeaders();
                                httpHeaders.putAll(super.getHeaders());
                                if (contentLength > 0) {
                                    httpHeaders.setContentLength(contentLength);
                                } else {
                                    httpHeaders.set(HttpHeaders.TRANSFER_ENCODING, "chunked");
                                }
                                return httpHeaders;
                            }

                            @Override
                            public Flux<DataBuffer> getBody() {
                                return outputMessage.getBody();
                            }
                        };
                        return chain.filter(exchange.mutate().request(decorator).build());
                    }));
        }

        return chain.filter(exchange);
    }

    /**
     * 优先级要高于其它过滤器
     * @return
     */
    @Override
    public int getOrder() {
        return 1;
    }
}