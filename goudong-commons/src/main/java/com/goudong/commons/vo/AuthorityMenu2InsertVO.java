package com.goudong.commons.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 类描述：
 * 菜单表
 * @Author e-Feilong.Chen
 * @Date 2021/8/12 15:23
 */
@Data
@NoArgsConstructor
public class AuthorityMenu2InsertVO {
    /**
     * 请求url
     */
    private String url;
    /**
     * 请求方式
     */
    private String method;
    /**
     * 菜单名字
     */
    private String menuName;
    /**
     * 备注
     */
    private String remark;
    /**
     * 应用名称
     */
    private String applicationName;
    /**
     * 类型 0:系统接口；1：前端页面
     * 默认0
     */
    private Integer type;

    public AuthorityMenu2InsertVO(String url, String method, String remark, String applicationName) {
        this.url = url;
        this.method = method;
        this.remark = remark;
        this.applicationName = applicationName;
    }
}
