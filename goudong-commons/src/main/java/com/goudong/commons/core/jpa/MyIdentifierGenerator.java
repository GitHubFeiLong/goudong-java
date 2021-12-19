package com.goudong.commons.core.jpa;

import cn.hutool.core.util.IdUtil;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;

/**
 * 类描述：
 * 自定义id生成器
 * @author msi
 * @date 2021/12/11 20:45
 * @version 1.0
 */
public class MyIdentifierGenerator implements IdentifierGenerator {

    @Override
    public Serializable generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object o) throws HibernateException {
        if (o == null) throw new HibernateException(new NullPointerException());
        Long id = ((BasePO) o).getId();
        return id == null ? IdUtil.getSnowflake(1, 1).nextId() : id;
    }
}
