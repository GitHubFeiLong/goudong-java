package com.goudong.core.util;

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
        AssertUtil.isNotEmpty(elements);
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
}
