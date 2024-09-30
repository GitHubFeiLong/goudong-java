package com.goudong.core.util;

import java.util.*;

/**
 * 类描述：
 * 集合相关工具类
 * @author cfl
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
     * @param coll  集合
     * @return  true-空集合，false-非空集合
     */
    public static boolean isEmpty(Collection<?> coll) {
        return (coll == null || coll.isEmpty());
    }

    /**
     * 判断集合是否不是空集合
     * @param coll  集合
     * @return  true-非空集合，false-空集合
     */
    public static boolean isNotEmpty(Collection<?> coll) {
        return !CollectionUtil.isEmpty(coll);
    }

    //~创建集合
    //==================================================================================================================

    /**
     * 初始化一个ArrayList
     * @param elements  参数
     * @return  list
     */
    @SafeVarargs
    public static <E> ArrayList<E> newArrayList(E... elements) {
        return ListUtil.newArrayList(elements);
    }
    /**
     * 初始化一个ArrayList，内容是[start, end] 之间的所有整数
     * @param start 起始值
     * @param end 结束值
     */
    public static List<Integer> newArrayListByRange(int start, int end) {
        return ListUtil.newArrayListByRange(start, end);
    }


    /**
     * 初始化一个ArrayList
     * @param elements  参数
     * @return  set
     */
    @SafeVarargs
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
     */
    public static <T> Collection<T> subtract(final Collection<T> a, final Collection<T> b) {
        List<T> list = new ArrayList<>(a);
        for (T o : b) {
            list.remove(o);
        }
        return list;
    }
}
