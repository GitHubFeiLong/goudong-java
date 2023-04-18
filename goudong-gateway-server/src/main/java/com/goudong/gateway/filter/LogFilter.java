// package com.goudong.gateway.filter;
//
// import com.fasterxml.jackson.databind.ObjectMapper;
// import com.goudong.boot.web.core.ApiLog;
// import com.goudong.boot.web.properties.ApiLogProperties;
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.cloud.gateway.filter.GatewayFilterChain;
// import org.springframework.cloud.gateway.filter.GlobalFilter;
// import org.springframework.core.Ordered;
// import org.springframework.core.io.buffer.DataBuffer;
// import org.springframework.core.io.buffer.DataBufferUtils;
// import org.springframework.http.HttpHeaders;
// import org.springframework.http.server.reactive.ServerHttpRequest;
// import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
// import org.springframework.stereotype.Component;
// import org.springframework.util.StopWatch;
// import org.springframework.web.server.ServerWebExchange;
// import reactor.core.publisher.Flux;
// import reactor.core.publisher.Mono;
//
// import javax.annotation.Resource;
// import java.io.UnsupportedEncodingException;
// import java.util.HashMap;
// import java.util.Map;
//
// /**
//  * 类描述：
//  * 日志
//  * @author cfl
//  * @version 1.0
//  * @date 2023/4/16 17:01
//  */
// @Slf4j
// @Component
// public class LogFilter implements GlobalFilter, Ordered {
//
//     //~fields
//     //==================================================================================================================
//     @Resource
//     private ApiLogProperties apiLogProperties;
//
//     @Resource
//     private ObjectMapper objectMapper;
//
//     //~methods
//     //==================================================================================================================
//     @Override
//     public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//         // 计时器，当是开启接口打印时才创建对象，提高程序性能
//         StopWatch stopWatch = null;
//         ServerHttpRequest request = exchange.getRequest();
//         if (apiLogProperties.getEnabled()) {
//             String methodValue = request.getMethodValue();
//             String contentType = request.getHeaders().getFirst(HttpHeaders.CONTENT_TYPE);
//
//             if ("POST".equals(methodValue)) {
//                 return DataBufferUtils.join(exchange.getRequest().getBody())
//                         .flatMap(dataBuffer -> {
//                             byte[] bytes = new byte[dataBuffer.readableByteCount()];
//                             dataBuffer.read(bytes);
//
//                             try {
//                                 // 请求参数
//                                 String bodyString = new String(bytes, "utf-8");
//                                 exchange.getAttributes().put("POST_BODY", bodyString);
//
//                             } catch (UnsupportedEncodingException e) {
//
//                             }
//                             DataBufferUtils.release(dataBuffer);
//                             Flux<DataBuffer> cachedFlux = Flux.defer(() -> {
//                                 DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
//
//                                 return Mono.just(buffer);
//                             });
//
//                             ServerHttpRequest mutatedRequest = new ServerHttpRequestDecorator(exchange.getRequest()) {
//                                 @Override
//                                 public Flux<DataBuffer> getBody() {
//                                     return cachedFlux;
//                                 }
//                             };
//
//                             return chain.filter(exchange.mutate().request(mutatedRequest).build());
//                         });
//             }
//
//             ApiLog apiLog = null;
//             stopWatch = new StopWatch();  // 创建计时器
//             stopWatch.start();
//             // String ip = IpUtil.getStringIp(request);
//             String ip = "";
//             String uri = request.getURI().getPath();
//             Map<String, String> requestHead = getRequestHead(request);
//             Object params = getArgs(request);
//
//             // 创建apiLog对象
//             apiLog = new ApiLog();
//             apiLog.setIp(ip);
//             apiLog.setUri(uri);
//             apiLog.setMethod(methodValue);
//             apiLog.setHeadParams(requestHead);
//             apiLog.setParams(params);
//         }
//         boolean successful = false;
//         try {
//             Mono<Void> filter = chain.filter(exchange);
//             successful = true;
//             return filter;
//         } finally {
//             if (apiLogProperties.getEnabled()) {
//                 stopWatch.stop();
//                 long time = stopWatch != null ? stopWatch.getTotalTimeMillis() : -1;
//                 apiLog.setResults("");
//                 apiLog.setSuccessful(successful);
//                 apiLog.setTime(time);
//
//                 // 输出接口日志
//                 apiLog.printLogString(apiLogProperties, objectMapper);
//             }
//         }
//     }
//
//
//     @Override
//     public int getOrder() {
//         return HIGHEST_PRECEDENCE;
//     }
//     /**
//      * 获取本次请求头
//      * @param request
//      * @return
//      */
//     private Map<String, String> getRequestHead(ServerHttpRequest request) {
//         request.getHeaders().entrySet().forEach(p->{
//             log.debug("{} -> {}", p.getKey(), p.getValue());
//         });
//         return new HashMap<>();
//     }
//
//
//     /**
//      * 获取请求参数
//      * @param request
//      * @return
//      */
//     private Object getArgs(ServerHttpRequest request) {
//         // List<Class> filter = ListUtil.newArrayList(
//         //
//         // );
//         if ("GET".equals(request.getMethodValue())) {
//             return request.getQueryParams();
//         }
//
//         return request.getBody();
//
//     }
// }
