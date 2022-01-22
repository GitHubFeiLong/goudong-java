package com.goudong.commons.utils.core;

/**
 * 类描述：
 * 判断类型是否是基本类型
 * @author msi
 * @version 1.0
 * @date 2022/1/22 21:48
 */
public class PrimitiveTypeUtil {

    //~fields
    //==================================================================================================================

    //~methods
    //==================================================================================================================

    /**
     * 判断对象是否是基本类型或基本类型对应的包装类型。
     * @param obj
     * @return
     */
    public static boolean isPrimitive (Object obj) {
        try {
            return ((Class<?>)obj.getClass().getField("TYPE").get(null)).isPrimitive();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 判断对象是否是基本类型或基本类型对应的包装类型。
     * @param obj
     * @return
     */
    public static boolean isBasicType (Object obj) {
        if (obj instanceof String) {
            return true;
        }
        return isPrimitive(obj);
    }
}