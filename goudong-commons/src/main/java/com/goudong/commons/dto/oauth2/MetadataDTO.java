package com.goudong.commons.dto.oauth2;

import lombok.Data;
import org.springframework.http.HttpMethod;

/**
 * 类描述：
 * 菜单的元信息，由于前端组件不同可能需要存储各种各样的内容。
 * @author cfl
 * @date 2022/8/2 20:39
 * @version 1.0
 */
@Data
public class MetadataDTO {
    //~fields
    //==================================================================================================================
    /**
     * 请求路径，
     */
    private String url;

    /**
     * 请求方式,值为{@link HttpMethod}元素的name(),例如：GET，POST。
     * 这里url 和 method 是一对一的方式，方便更细粒度鉴权。
     */
    private String method;

    //~methods
    //==================================================================================================================
}