package com.goudong.gateway.hanlder;

import org.springframework.cloud.gateway.filter.factory.rewrite.CachedBodyOutputMessage;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import reactor.core.publisher.Flux;

/**
 * 类描述：
 * 请求解码器：将前端加密的请求进行解密
 * @author msi
 * @version 1.0
 * @date 2022/2/15 20:04
 */
public class RequestDecodeHandler extends ServerHttpRequestDecorator {


    //~fields
    //==================================================================================================================

    private CachedBodyOutputMessage cachedBodyOutputMessage;
    //~methods
    //==================================================================================================================
    public RequestDecodeHandler(ServerHttpRequest delegate, CachedBodyOutputMessage cachedBodyOutputMessage) {
        super(delegate);
        this.cachedBodyOutputMessage = cachedBodyOutputMessage;
    }

    @Override
    public HttpHeaders getHeaders() {
        long contentLength = cachedBodyOutputMessage.getHeaders().getContentLength();
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
        return cachedBodyOutputMessage.getBody();
    }
}