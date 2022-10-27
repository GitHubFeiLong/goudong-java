package com.goudong.core.function;

import com.goudong.core.util.AssertUtil;
import org.junit.jupiter.api.Test;

class StringSupplierTest {

    @Test
    void get() {
        AssertUtil.isTrue(true, () -> {
            System.out.println("hello world");
            return new IllegalArgumentException("111");});

        AssertUtil.isTrue(true, () -> "helloworld");
    }


}
