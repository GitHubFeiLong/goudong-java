package com.goudong.core.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Collection;
import java.util.List;

@ExtendWith({})
class CollectionUtilTest {

    @Test
    void subtract() {
        List<Integer> l1 = CollectionUtil.newArrayList(1, 2, 3, 4, 5, 1, 2, 3, 4, 5);
        List<Integer> l2 = CollectionUtil.newArrayList(2, 4, 6, 8, 5);
        Collection<Integer> subtract = CollectionUtil.subtract(l1, l2);
        System.out.println("subtract = " + subtract);
    }
}
