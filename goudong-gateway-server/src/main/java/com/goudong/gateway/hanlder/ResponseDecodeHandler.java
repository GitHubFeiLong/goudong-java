package com.goudong.gateway.hanlder;

import com.goudong.commons.security.aes.AES;
import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.Charset;

/**
 * 类描述：
 * 响应解码处理器
 * @author msi
 * @version 1.0
 * @date 2022/2/15 19:51
 */
public class ResponseDecodeHandler extends ServerHttpResponseDecorator {

    //~fields
    //==================================================================================================================
    /**
     * AES密钥
     */
    private String aesKey;
    //~methods
    //==================================================================================================================
    public ResponseDecodeHandler(ServerHttpResponse delegate, String aesKey){
        super(delegate);
        this.aesKey = aesKey;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
        if(body instanceof Flux) {
            Flux<DataBuffer> fluxBody = (Flux<DataBuffer>) body;

            return super.writeWith(fluxBody.buffer().map(dataBuffers -> {
                DataBufferFactory dataBufferFactory = new DefaultDataBufferFactory();
                DataBuffer join = dataBufferFactory.join(dataBuffers);

                byte[] content = new byte[join.readableByteCount()];
                join.read(content);
                DataBufferUtils.release(join);// 释放掉内存

                String bodyStr = new String(content, Charset.forName("UTF-8"));

                //修改响应体
                bodyStr = modifyBody(bodyStr);

                getDelegate().getHeaders().setContentLength(bodyStr.getBytes().length);
                return bufferFactory().wrap(bodyStr.getBytes());
            }));
        }
        return super.writeWith(body);
    }

    //重写这个函数即可
    private String modifyBody(String jsonStr){
        // 将响应体加密
        return AES.build().secretKey(this.aesKey).encrypt(jsonStr);
    }
}