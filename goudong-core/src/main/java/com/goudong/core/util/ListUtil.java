package com.goudong.core.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 类描述：
 *
 * @author cfl
 * @version 1.0
 * @date 2022/11/24 12:55
 */
public class ListUtil {
    //~fields
    //==================================================================================================================

    //~methods
    //==================================================================================================================

    /**
     * 初始化一个ArrayList
     * @param elements
     * @return
     * @param <E>
     */
    public static <E> ArrayList<E> newArrayList(E... elements) {
        if (elements.length == 0) {
            return new ArrayList<>(0);
        }
        ArrayList<E> list = new ArrayList<>(elements.length);
        Collections.addAll(list, elements);
        return list;
    }
    /**
     * 初始化一个ArrayList，内容是[start, end] 之间的所有整数
     * @param start 起始值
     * @param end 结束值
     * @return
     */
    public static List<Integer> newArrayListByRange(int start, int end) {
        AssertUtil.isTrue(start <= end);
        int size = end - start;
        List<Integer> list = new ArrayList<>(size);
        for (int i = start; i <= end; i++) {
            list.add(i);
        }
        return list;
    }

    /**
     * 将集合转为数组(深拷贝)
     * @param collection 集合
     * @return 数组
     * @param <T> 集合泛型
     */
    public static <T> T[] toArray(List<T> collection) {
        T[] ts = (T[]) Array.newInstance(collection.getClass(), collection.size());
        for (int i = 0, size = collection.size(); i < size; i++) {
            ts[i] = collection.get(i);
        }
        return ts;
    }
}
