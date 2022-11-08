package com.goudong.core.util;

import java.util.*;

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

    //~判断
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

    //~创建集合
    //==================================================================================================================

    /**
     * 初始化一个ArrayList
     * @param elements
     * @return
     * @param <E>
     */
    public static <E> ArrayList<E> newArrayList(E... elements) {
        AssertUtil.isNotEmpty(elements);
        ArrayList<E> list = new ArrayList<>(elements.length);
        Collections.addAll(list, elements);
        return list;
    }

    /**
     * 初始化一个ArrayList
     * @param elements
     * @return
     * @param <E>
     */
    public static <E> Set<E> newHashSet(E... elements) {
        AssertUtil.isNotEmpty(elements);
        Set<E> set = new HashSet<>(elements.length);
        Collections.addAll(set, elements);
        return set;
    }

    //~集合操作
    //==================================================================================================================

    /**
     * 计算集合{@code a}和集合{@code b}的差集，并返回差集{@code list} <br>
     * 例如:
     * <pre>
     *       subtract([1,2,3,4],[2,3,4,5]) -》 [1]
     * </pre>
     * @param a 目标集合
     * @param b 与之计算差集的集合
     * @return 返回差集{@code list}
     * @param <O>
     */
    public static <O>Collection<O> subtract(final Collection<O> a, final Collection<O> b) {
        ArrayList list = new ArrayList(a);
        for (Iterator it = b.iterator(); it.hasNext();) {
            list.remove(it.next());
        }
        return list;
    }
}
