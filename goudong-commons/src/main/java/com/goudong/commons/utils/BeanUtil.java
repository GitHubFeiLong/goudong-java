package com.goudong.commons.utils;

import lombok.SneakyThrows;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 对象属性复制，当source=null时，将target设置成null
 * @Author msi
 * @Date 2021-05-11 13:46
 * @Version 1.0
 */
public class BeanUtil {

    @SneakyThrows
    public static <T> T copyProperties(Object source, Class<?> clazz) {
        if (source == null) {
            return null;
        }
        // 强转
        Object o = clazz.newInstance();
        // 复制
        BeanUtils.copyProperties(source, o);

        return (T)o;
    }

    @SneakyThrows
    public static List copyList(List sourceList, Class<?> clazz) {
        if (sourceList == null || sourceList.isEmpty()) {
            return new ArrayList<>();
        }

        List result = new ArrayList(sourceList.size());
        for (Object sourceObj : sourceList) {
            Object targetObj = clazz.newInstance();
            // 复制
            BeanUtils.copyProperties(sourceObj, targetObj);

            result.add(targetObj);
        }

        return result;
    }


}
