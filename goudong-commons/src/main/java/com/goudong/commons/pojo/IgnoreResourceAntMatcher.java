package com.goudong.commons.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpMethod;

import java.io.Serializable;

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
public class IgnoreResourceAntMatcher implements Serializable {
    private static final long serialVersionUID = 6793267140897074206L;
    /**
     * 请求方式
     */
    private HttpMethod httpMethod;

    /**
     * 请求资源路径(使用ant方式)
     */
    private String pattern;
}
