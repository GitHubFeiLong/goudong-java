package com.goudong.commons.dto.oauth2;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 类描述：
 * 保存隐藏菜单实体
 * @author cfl
 * @version 1.0
 * @date 2022/9/19 21:36
 */
@Data
public class HideMenu2CreateDTO {
    //~fields
    //==================================================================================================================
    /**
     * 匹配模式
     */
    @NotBlank
    private String path;
    /**
     * 请求的方法
     * json数组字符串
     */
    @NotBlank
    private String method;
    /**
     * 备注
     */
    private String remark;
    /**
     * 是否是系统设置的
     */
    private Boolean sys = false;

    /**
     * 是否是接口
     */
    private Boolean api = false;

    //~methods
    //==================================================================================================================


    public HideMenu2CreateDTO(String path, String method, String remark, Boolean sys, Boolean api) {
        this.path = path;
        this.method = method;
        this.remark = remark;
        this.sys = sys;
        this.api = api;
    }
}
