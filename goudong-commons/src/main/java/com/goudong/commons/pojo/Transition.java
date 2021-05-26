package com.goudong.commons.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 过渡页参数实体类，属性名就是前端的参数名
 * @Author msi
 * @Date 2021-05-26 16:13
 * @Version 1.0
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Transition {

    /**
     * token字符串
     */
    private String token;

    private String redirectUrl;

}
