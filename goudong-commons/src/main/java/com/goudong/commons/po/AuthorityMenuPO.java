package com.goudong.commons.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Objects;

/**
 * 类描述：
 * 菜单表
 * @Author e-Feilong.Chen
 * @Date 2021/8/12 15:23
 */
@Data
@TableName("authority_menu")
public class AuthorityMenuPO extends BasePO{
    private static final long serialVersionUID = -6254288573268456187L;
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
    private String parentId;
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

    /**
     * 重写，当url和method相同对象相同
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AuthorityMenuPO)) return false;
        AuthorityMenuPO that = (AuthorityMenuPO) o;
        return Objects.equals(url, that.url) && Objects.equals(method, that.method);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), url, method, menuName, parentId, remark, applicationName, type);
    }
}
