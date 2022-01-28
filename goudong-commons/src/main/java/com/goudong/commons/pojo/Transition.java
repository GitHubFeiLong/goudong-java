package com.goudong.commons.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * qq登录后的过渡页参数实体类，属性名就是前端的参数名
 * redirectUrl重定向指定地址，其它属性都是参数
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
     * 重定向地址
     */
    private String redirectUrl;

    /**
     * 访问令牌
     */
    @NotBlank
    private String accessToken;
    /**
     * 刷新令牌
     */
    @NotBlank
    private String refreshToken;
    /**
     * accessToken失效时长
     */
    @NotNull
    private Date accessExpires;
    /**
     * refreshToken失效时长
     */
    @NotNull
    private Date refreshExpires;
}
