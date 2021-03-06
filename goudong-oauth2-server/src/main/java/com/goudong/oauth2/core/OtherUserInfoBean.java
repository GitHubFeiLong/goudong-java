package com.goudong.oauth2.core;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * 类描述：
 * qq用户或微信用户
 * @Author msi
 * @Date 2021-05-03 15:35
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OtherUserInfoBean {
    @NotBlank(message = "openId不能为空")
    private String openId;
    @NotBlank(message = "昵称不能为空")
    private String nickname;
    @NotBlank(message = "30*30头像地址不能为空")
    private String headPortrait30;
    @NotBlank(message = "账号类型不能为空")
    private String userType;
}
