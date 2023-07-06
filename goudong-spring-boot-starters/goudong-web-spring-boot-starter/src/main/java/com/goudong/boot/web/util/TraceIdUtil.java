package com.goudong.boot.web.util;

import org.slf4j.MDC;

import java.util.UUID;

/**
 * 类描述：
 * TraceId工具
 * @author cfl
 * @version 1.0
 * @date 2023/4/18 10:32
 */
public class TraceIdUtil {
    private static final String TRACE_ID = "traceId";

    /**
     * 添加
     * @return
     */
    public static String put() {
        // 添加请求日志的全局唯一id
        String idStr = UUID.randomUUID().toString().replaceAll("-", "");
        MDC.put(TRACE_ID, idStr);
        return idStr;
    }

    /**
     * 添加自定义的id
     * @param traceId
     * @return
     */
    public static String put(String traceId) {
        MDC.put(TRACE_ID, traceId);
        return traceId;
    }

    /**
     * 获取
     * @return
     */
    public static String get() {
        return MDC.get(TRACE_ID);
    }

    /**
     * 删除
     */
    public static void remove() {
        MDC.remove(TRACE_ID);
    }
}
