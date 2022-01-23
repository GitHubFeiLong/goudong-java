package com.goudong.commons.constant.core;

import org.springframework.http.HttpMethod;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 类描述：
 * HttpMethod相关常量
 * @author msi
 * @version 1.0
 * @date 2022/1/9 0:24
 */
public class HttpMethodConst {

    /**
     * 集合中的元素是大写的
     */
    public static final List<String> ALL_HTTP_METHOD = Stream.of(HttpMethod.values())
            .map(HttpMethod::name)
            .collect(Collectors.toList());
}
