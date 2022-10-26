package com.goudong.core.util;

import java.util.Collection;

/**
 * 类描述：
 * 集合相关工具类
 * @author cfl
 * @version 1.0
 * @date 2022/10/26 21:00
 */
public class CollectionUtil {
    //~fields
    //==================================================================================================================

    //~methods
    //==================================================================================================================

    /**
     * 判断集合是否是空集合
     * @param coll
     * @return
     */
    public static boolean isEmpty(Collection coll) {
        return (coll == null || coll.isEmpty());
    }

    /**
     * 判断集合是否不是空集合
     * @param coll
     * @return
     */
    public static boolean isNotEmpty(Collection coll) {
        return !CollectionUtil.isEmpty(coll);
    }
}
