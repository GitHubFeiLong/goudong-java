package com.goudong.core.util;

/**
 * 类描述：
 * 数组工具类
 * @author cfl
 * @version 1.0
 * @date 2022/10/26 22:05
 */
public class ArrayUtil {
    //~fields
    //==================================================================================================================

    //~methods
    //==================================================================================================================
    /**
     * 判断数组是否是空数组
     * @param arr
     * @return
     */
    public static boolean isEmpty(Object[] arr) {
        return (arr == null || arr.length == 0);
    }

    /**
     * 判断数组是否不是空数组
     * @param arr
     * @return
     */
    public static boolean isNotEmpty(Object[] arr) {
        return !ArrayUtil.isEmpty(arr);
    }
}
