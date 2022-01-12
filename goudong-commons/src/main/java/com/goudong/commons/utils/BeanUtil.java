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

    /**
     * 复制对象
     * @param source
     * @param clazz
     * @param <T>
     * @return
     */
    @SneakyThrows
    public static <T> T copyProperties(Object source, Class<T> clazz) {
        if (source == null) {
            return null;
        }
        return cn.hutool.core.bean.BeanUtil.copyProperties(source, clazz);
    }

    /**
     * 复制对象
     * @param source
     * @param clazz
     * @param ignoreProperties
     * @param <T>
     * @return
     */
    public static <T> T copyProperties(Object source, Class<T> clazz, String... ignoreProperties) {
        if (source == null) {
            return null;
        }
        return cn.hutool.core.bean.BeanUtil.copyProperties(source, clazz, ignoreProperties);
    }

    @SneakyThrows
    public static <T> List<T> copyList(List sourceList, Class<T> clazz) {
        if (sourceList == null || sourceList.isEmpty()) {
            return new ArrayList<T>();
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
