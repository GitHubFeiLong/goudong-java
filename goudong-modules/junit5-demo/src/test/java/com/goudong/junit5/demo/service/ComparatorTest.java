package com.goudong.junit5.demo.service;

import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 类描述：
 *
 * @author msi
 * @version 1.0
 * @date 2022/9/21 12:56
 */
public class ComparatorTest {

    //~fields
    //==================================================================================================================

    //~methods
    //==================================================================================================================
    @Test
    void testCompare() {
        List<Integer> list = Lists.newArrayList(1, 10, 5, 6, 2, 8, 3, 9);

        List<Integer> collect = list.stream()
                .sorted(new Comparator<Integer>() {
                    @Override
                    public int compare(Integer o1, Integer o2) {
                        return o1 - o2;
                    }
                }).collect(Collectors.toList());

        // List<Integer> collect = list.stream()
        //         .sorted((o1, o2) -> o1-o2).collect(Collectors.toList());

        // List<Integer> collect = list.stream()
        //         .sorted().collect(Collectors.toList());

        System.out.println("collect = " + collect);


    }

}