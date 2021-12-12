package com.goudong.commons.core.mybatisplus;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.goudong.commons.enumerate.BasicPoFieldEnum;
import org.apache.ibatis.reflection.MetaObject;

import java.time.LocalDateTime;

/**
 * 类描述：
 *
 * mybatis plus 自定义配置填充策略
 * @Author msi
 * @Date 2021-08-14 14:17
 * @Version 1.0
 */
// @Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    /**
     * 执行insert时填充数据
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        Object createTime = getFieldValByName(BasicPoFieldEnum.CREATE_TIME.getField(), metaObject);
        Object createUserId = getFieldValByName(BasicPoFieldEnum.CREATE_USER_ID.getField(), metaObject);
        if(createTime == null) {
            //字段为空，可以进行填充
            setFieldValByName(BasicPoFieldEnum.CREATE_TIME.getField(), LocalDateTime.now(), metaObject);
        }
        if (createUserId == null) {
            //字段为空，可以进行填充
            setFieldValByName(BasicPoFieldEnum.CREATE_USER_ID.getField(), 1L, metaObject);
        }
    }

    /**
     * 执行update时填充数据
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        Object updateTime = getFieldValByName(BasicPoFieldEnum.UPDATE_TIME.getField(), metaObject);
        Object updateUserId = getFieldValByName(BasicPoFieldEnum.UPDATE_USER_ID.getField(), metaObject);
        if(updateTime == null) {
            //字段为空，可以进行填充
            setFieldValByName(BasicPoFieldEnum.UPDATE_TIME.getField(), LocalDateTime.now(), metaObject);
        }
        if (updateUserId == null) {
            //字段为空，可以进行填充
            setFieldValByName(BasicPoFieldEnum.UPDATE_USER_ID.getField(), LocalDateTime.now(), metaObject);
        }
    }
}
