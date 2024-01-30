package com.goudong.boot.web.properties;

import lombok.Data;
import org.slf4j.event.Level;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 类描述：
 * api接口打印的内容
 * @author cfl
 * @version 1.0
 * @date 2023/4/15 17:05
 */
@Data
@ConfigurationProperties(prefix = "goudong.web.api-log")
public class ApiLogProperties {
    //~fields
    //==================================================================================================================
    /**
     * 是否开启接口日志打印
     * <ul>
     *     <li>true：开启接口日志打印</li>
     *     <li>false：关闭接口日志打印</li>
     * </ul>
     */
    private Boolean enabled = true;

    /**
     * 使用日志的级别，默认debug级别
     * @see Level
     */
    private Level level = Level.DEBUG;

    /**
     * 配置接口输出日志内容
     * <p>example:当不需要打印ip时，只需要配置{@code goudong.web.api-log.type-enabled.ip=false}即可</p>
     */
    @NestedConfigurationProperty
    private TypeEnabled typeEnabled = new TypeEnabled();

    /**
     * 对日志打印的限制
     * <ul>
     *     <li>配置请求头参数输出</li>
     *     <li>配置接口响应的内容长度输出</li>
     * </ul>
     */
    @NestedConfigurationProperty
    private PrintLogLimit printLogLimit = new PrintLogLimit();


    //~methods
    //==================================================================================================================

    /**
     * 类描述：
     * 输出类型的开关配置
     * @author cfl
     * @date 2023/4/16 9:00
     * @version 1.0
     */
    @Data
    public static class TypeEnabled {
        /**
         * 是否打印接口请求的ip
         */
        private Boolean ip = true;
        /**
         * 是否打印接口请求的uri
         */
        private Boolean uri = true;
        /**
         * 是否打印接口请求的方式
         */
        private Boolean method = true;
        /**
         * 是否打印接口请求的自定义请求头
         */
        private Boolean headParams = true;
        /**
         * 是否打印接口请求的参数
         */
        private Boolean params = true;
        /**
         * 是否打印接口请求的响应
         */
        private Boolean results = true;
        /**
         * 是否打印接口的追踪id
         */
        private Boolean traceId = true;
        /**
         * 是否打印接口请求的状态(成功失败)
         */
        private Boolean successful = true;
        /**
         * 是否打印接口请求的时间
         */
        private Boolean time = true;
    }

    /**
     * 类描述：
     * 对某些输出进行限制
     * @author cfl
     * @date 2023/4/16 9:04
     * @version 1.0
     */
    @Data
    public static class PrintLogLimit {

        /**
         * 接口请求头参数打印配置
         * <ol>
         *   <li>接口携带的请求头参数包含key，且{@code headParams.get(key) = true}时</li>
         *    <li>接口携带的请求头包含`x-`开头的参数，且{@code headParams.get(key) != false}时</li>
         * </ol>
         */
        private Map<String, Boolean> headParams = new HashMap<>();

        /**
         * Results的打印长度限制
         * <ol>
         *     <li>请求头中X-Api-Result-Length的配置优先级最高</li>
         *     <li>其次是配置的resultsLength，默认值是-1</li>
         *     <li>当值是负数时，会打印完整的返回值</li>
         * </ol>
         */
        private int resultsLength = -1;

        /**
         * 将其key设置成小写，并进行简单的过滤
         * @param headParams
         */
        public void setHeadParams(Map<String, Boolean> headParams) {
            if (headParams != null && headParams.isEmpty()) {
                this.headParams = headParams;
            } else {
                this.headParams = new HashMap<>(headParams.size());
                headParams.forEach((k, v) -> {
                    if (k != null) {
                        this.headParams.put(k.toLowerCase(), Optional.ofNullable(v).orElseGet(() -> false));
                    }
                });
            }
        }
    }

}
