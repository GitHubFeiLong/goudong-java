package com.goudong.commons.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

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
}
