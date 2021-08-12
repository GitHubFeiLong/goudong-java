package com.goudong.commons.po;

/**
 * 类描述：
 * 菜单表
 * @Author e-Feilong.Chen
 * @Date 2021/8/12 15:23
 */
public class AuthorityMenuPO {
    /**
     * 主键uuid
     */
    private String uuid;
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
     * 父菜单主键
     */
    private String parentUuid;
    /**
     * 备注
     */
    private String remark;
    /**
     * 是否被删除
     */
    private String isDelete;
    /**
     * 更新时间
     */
    private String updateTime;
    /**
     * 删除时间
     */
    private String createTime;
}
