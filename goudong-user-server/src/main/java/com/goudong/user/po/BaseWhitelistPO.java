package com.goudong.user.po;

import com.goudong.commons.frame.jpa.BasePO;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 类描述：
 * 白名单
 * @author msi
 * @version 1.0
 * @date 2022/1/8 16:46
 */
@Data
@Entity
@Table(name = "base_whitelist")
@SQLDelete(sql = "update base_whitelist set deleted=true where id=?")
@Where(clause = "deleted=false")
public class BaseWhitelistPO extends BasePO {

    /**
     * 匹配模式
     */
    private String pattern;
    /**
     * 请求的方法
     * json数组字符串
     */
    private String methods;
    /**
     * 备注
     */
    private String remark;
    /**
     * 是否是系统设置的
     */
    private Boolean isSystem;
}
