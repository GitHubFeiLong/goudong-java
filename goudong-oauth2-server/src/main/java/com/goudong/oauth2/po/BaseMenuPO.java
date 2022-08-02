package com.goudong.oauth2.po;

import com.goudong.commons.framework.jpa.BasePO;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.http.HttpMethod;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 类描述：
 * 菜单表
 * @Author e-Feilong.Chen
 * @Date 2021/8/12 15:23
 */
@Data
@Entity
@Table(name = "base_menu")
@SQLDelete(sql = "update base_menu set deleted=true where id=?")
@Where(clause = "deleted=false")
public class BaseMenuPO extends BasePO {
    private static final long serialVersionUID = -6254288573268456187L;
    /**
     * 父菜单主键
     */
    private String parentId;
    /**
     * 前端菜单组件的信息
     * @see Metadata
     */
    private String metadata;
    /**
     * 备注
     */
    private String remark;

    /**
     * 类描述：
     * 菜单的元信息，由于前端组件不同可能需要存储各种各样的内容。
     * @author cfl
     * @date 2022/8/2 20:39
     * @version 1.0
     */
    public static class Metadata {

        /**
         * 请求路径，
         */
        private String url;

        /**
         * 请求方式,值为{@link HttpMethod}元素的name(),例如：GET，POST。
         * 这里url 和 method 是一对一的方式，方便更细粒度鉴权。
         */
        private String method;

    }
}
