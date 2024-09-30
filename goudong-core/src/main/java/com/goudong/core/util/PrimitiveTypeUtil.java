package com.goudong.core.util;

/**
 * 类描述：
 * 判断类型是否是基本类型
 * @author msi
 */
public class PrimitiveTypeUtil {

    //~fields
    //==================================================================================================================

    //~methods
    //==================================================================================================================

    /**
     * 判断对象是否是基本类型或基本类型对应的包装类型。
     * @param obj   判断对象
     * @return  true-是基本类型或基本类型对应的包装类型，false-不是基本类型或基本类型对应的包装类型
     */
    public static boolean isBasicType (Object obj) {
        if (obj instanceof String) {
            return true;
        }
        try {
            return ((Class<?>)obj.getClass().getField("TYPE").get(null)).isPrimitive();
        } catch (Exception e) {
            return false;
        }
    }

}