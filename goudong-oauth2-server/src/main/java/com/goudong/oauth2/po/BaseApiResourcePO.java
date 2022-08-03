package com.goudong.oauth2.po;

import com.goudong.commons.framework.jpa.BasePO;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;


/**
 * 类描述：
 * 保存系统中所有api接口资源(BaseApiResource)实体类
 * @author cfl
 * @date 2022/8/2 22:50
 * @version 1.0
 */
@Data
@Entity
@Table(name = "base_api_resource")
@SQLDelete(sql = "update base_api_resource set deleted=true where id=?")
@Where(clause = "deleted=false")
public class BaseApiResourcePO extends BasePO implements Serializable  {
    /**
     * 路径Pattern
     */
    private String pattern;
    /**
     * 请求方式
     */
    private String method;
    /**
     * 接口所在应用
     */
    private String applicationName;
    /**
     * 备注
     */
    private String remark;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseApiResourcePO that = (BaseApiResourcePO) o;
        return Objects.equals(pattern, that.pattern) && Objects.equals(method, that.method) && Objects.equals(applicationName, that.applicationName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pattern, method, applicationName);
    }
}

