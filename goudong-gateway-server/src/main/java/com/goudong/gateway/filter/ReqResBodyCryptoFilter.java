package com.goudong.gateway.filter;

import com.goudong.boot.web.util.TraceIdUtil;
import com.goudong.commons.constant.core.HttpHeaderConst;
import com.goudong.core.security.aes.AES;
import com.goudong.core.security.rsa.ServerRSA;
import com.goudong.core.util.StringUtil;
import com.goudong.gateway.hanlder.RequestDecodeHandler;
import com.goudong.gateway.hanlder.ResponseDecodeHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.NettyWriteResponseFilter;
import org.springframework.cloud.gateway.filter.factory.rewrite.CachedBodyOutputMessage;
import org.springframework.cloud.gateway.support.BodyInserterContext;
import org.springframework.cloud.gateway.support.DefaultServerRequest;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 类描述：
 * 请求/响应进行解密/加密操作
 * @author msi
 * @date 2022/2/13 20:06
 * @version 1.0
 */
@Slf4j
@Component
public class ReqResBodyCryptoFilter implements GlobalFilter, Ordered {

    /**
     * 优先级要高于其它过滤器
     * @return
     */
    @Override
    public int getOrder() {
        return NettyWriteResponseFilter.WRITE_RESPONSE_FILTER_ORDER;
    }

    /**
     * 拦截器
     * @param exchange
     * @param chain
     * @return
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        try {
            TraceIdUtil.put();
            log.info("hhh");
            ServerHttpRequest request = exchange.getRequest();
            // 查看请求头是否自定义的请求头(X-Aes-Key)
            String aesKeyEncrypt = request.getHeaders().getFirst(HttpHeaderConst.X_AES_KEY);
            // 没有加密请求参数，直接放行
            if(StringUtil.isBlank(aesKeyEncrypt)){
                return chain.filter(exchange);
            }

            HttpMethod method = request.getMethod();

            switch (method) {
                case POST:
                    return operationExchangeByPost(exchange, chain);
                default:
                    return chain.filter(exchange);
            }
            // if (true) {
            //     // 查看请求头是否自定义的请求头(X-Aes-Key)
            //     String aesKeyEncrypt = exchange.getRequest().getHeaders().getFirst(HttpHeaderConst.X_AES_KEY);
            //     if(StringUtil.isNotBlank(aesKeyEncrypt)){
            //         // 使用RSA私钥解密密文的AES密钥
            //         String aesKey = ServerRSA.getInstance().privateKeyDecrypt(aesKeyEncrypt);
            //         AES aes = AES.build().secretKey(aesKey);
            //         MultiValueMap<String, String> v = new LinkedMultiValueMap();
            //         request.getQueryParams().keySet().forEach(k -> {
            //             String s = request.getQueryParams().get(k).get(0);
            //             v.put(k, Collections.singletonList(aes.decrypt(s)));
            //         });
            //
            //         return chain.filter((ServerWebExchange) exchange.mutate().request(new ServerHttpRequestDecorator(exchange.getRequest()){
            //             @Override
            //             public MultiValueMap<String, String> getQueryParams() {
            //                 return v;
            //             }
            //         }));
            //     }
            //
            //
            // }
        } finally {
            log.info("hhh");
            TraceIdUtil.remove();
        }
    }

    /**
     * 修改POST请求的body的具体逻辑
     * TODO 这个还没完全理解，Gateway后面要系统的再学一遍
     * @param exchange
     * @param chain
     * @return
     */
    private Mono<Void> operationExchangeByPost(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 查看请求头是否自定义的请求头(X-Aes-Key)
        String aesKeyEncrypt = exchange.getRequest().getHeaders().getFirst(HttpHeaderConst.X_AES_KEY);
        // 使用RSA私钥解密密文的AES密钥
        String aesKey = ServerRSA.getInstance().privateKeyDecrypt(aesKeyEncrypt);
        log.debug("本次请求使用了AES解密,AES密钥为{}", aesKey);

        // 修改请求头
        modifyRequestHeaders(exchange, aesKey);

        // 获取请求的媒体类型(application/json等)
        MediaType mediaType = exchange.getRequest().getHeaders().getContentType();
        // 读取请求体并使用AES解密请求参数
        ServerRequest serverRequest = new DefaultServerRequest(exchange);
        Mono<String> modifiedBody = serverRequest.bodyToMono(String.class)
                .flatMap(body -> {
                    log.debug("lllllll");
                    if (MediaType.APPLICATION_JSON.isCompatibleWith(mediaType)) {
                        // 对原先的body进行修改操作
                        // 前端加密后带双引号的字符串
                        body = body.startsWith("\"") ? body.substring(1, body.length()-1) : body;
                        return Mono.just(AES.build()
                                .secretKey(aesKey)
                                .decrypt(body)
                        );
                    }
                    return Mono.empty();
                });
        BodyInserter bodyInserter = BodyInserters.fromPublisher(modifiedBody, String.class);

        /*
            创建新的请求
         */
        HttpHeaders headers = new HttpHeaders();
        headers.putAll(exchange.getRequest().getHeaders());
        headers.remove(HttpHeaders.CONTENT_LENGTH);
        CachedBodyOutputMessage outputMessage = new CachedBodyOutputMessage(exchange, headers);
        return bodyInserter.insert(outputMessage, new BodyInserterContext())
                .then(Mono.defer(() -> {
                    // 创建自定义请求解码器
                    RequestDecodeHandler requestDecodeHandler = new RequestDecodeHandler(exchange.getRequest(), outputMessage);
                    // 创建自定义响应解码器
                    ResponseDecodeHandler responseDecodeHandler = new ResponseDecodeHandler(exchange.getResponse(), aesKey);

                    return chain.filter(exchange.mutate().request(requestDecodeHandler).response(responseDecodeHandler).build());
                }));
    }

    /**
     * 修改请求头信息
     * @param exchange
     * @param aesKey 明文的AES密钥
     */
    private void modifyRequestHeaders(ServerWebExchange exchange, String aesKey) {
        ServerHttpRequest.Builder requestBuilder = exchange.getRequest().mutate();
        // 删除请求头
        // requestBuilder.headers(k -> k.remove(HttpHeaderConst.AES_KEY));
        requestBuilder.header(HttpHeaderConst.X_AES_KEY, aesKey);
        ServerHttpRequest request = requestBuilder.build();
        exchange.mutate().request(request).build();
    }


}
