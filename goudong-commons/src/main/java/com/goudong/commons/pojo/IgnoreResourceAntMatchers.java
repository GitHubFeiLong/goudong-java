package com.goudong.commons.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpMethod;

/**
 * 类描述：
 * 忽略路径资源的实体
 * @Author msi
 * @Date 2021-08-10 20:46
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IgnoreResourceAntMatchers {
    /**
     * 请求方式
     */
    private HttpMethod httpMethod;

    /**
     * 请求资源路径
     */
    private String url;
}
