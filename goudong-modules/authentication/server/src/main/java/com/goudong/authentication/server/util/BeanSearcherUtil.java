package com.goudong.authentication.server.util;

import cn.zhxu.bs.bean.DbField;
import cn.zhxu.bs.util.MapBuilder;
import cn.zhxu.bs.util.MapUtils;
import com.goudong.authentication.server.rest.req.search.BasePage;
import com.goudong.core.util.StringUtil;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * 类描述：
 *
 * @Author Administrator
 * @Version 1.0
 */
public class BeanSearcherUtil {
    //~fields
    //==================================================================================================================

    //~methods
    //==================================================================================================================

    /**
     * 获取 search 的map参数
     * @param req 请求对象
     * @return beanSearch的查询参数
     */
    public static Map<String, Object> getParaMap(BasePage req) {
        MapBuilder builder = MapUtils.builder();
        Class clazz = req.getClass();
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            declaredField.setAccessible(true);
            // 获取注解
            DbField dbField = declaredField.getAnnotation(DbField.class);
            String name;
            if (dbField != null && StringUtil.isNotBlank(dbField.value())) {
                // 字段名
                name = dbField.value();
            } else {
                // 字段名
                name = declaredField.getName();
            }

            try {
                Object o = declaredField.get(req);
                builder.field(name, o);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        return builder.page(req.getPage(), req.getSize()).build();
    }
}
