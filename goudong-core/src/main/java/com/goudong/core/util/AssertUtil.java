package com.goudong.core.util;

import java.util.Collection;
import java.util.function.Supplier;

/**
 * 类描述：
 * 常见的断言
 * @author cfl
 * @version 1.0
 * @date 2022/10/26 22:08
 */
public class AssertUtil {
    //~fields
    //==================================================================================================================

    //~methods
    //==================================================================================================================
    public static void isTrue(boolean boo) {
        if (!boo) {
            throw new IllegalArgumentException();
        }
    }

    public static void isTrue(boolean boo, String errMsg) {
        if (!boo) {
            throw new IllegalArgumentException(errMsg);
        }
    }

    public static void isTrue(boolean boo, Supplier<RuntimeException> supplier) {
        if (!boo) {
            throw supplier.get();
        }
    }

    public static void isFalse(boolean boo) {
        if (boo) {
            throw new IllegalArgumentException();
        }
    }

    public static void isFalse(boolean boo, String errMsg) {
        if (boo) {
            throw new IllegalArgumentException(errMsg);
        }
    }

    public static void isFalse(boolean boo, Supplier<RuntimeException> supplier) {
        if (boo) {
            throw supplier.get();
        }
    }

    public static void isNull(Object obj) {
        if (obj != null) {
            throw new IllegalArgumentException();
        }
    }

    public static void isNull(Object obj, String errMsg) {
        if (obj != null) {
            throw new IllegalArgumentException(errMsg);
        }
    }

    public static void isNull(Object obj, Supplier<RuntimeException> supplier) {
        if (obj != null) {
            throw supplier.get();
        }
    }

    public static void notNull(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException();
        }
    }

    public static void notNull(Object obj, String errMsg) {
        if (obj == null) {
            throw new IllegalArgumentException(errMsg);
        }
    }

    public static void notNull(Object obj, Supplier<RuntimeException> supplier) {
        if (obj == null) {
            throw supplier.get();
        }
    }

    public static void notBlank(String str) {
        if (StringUtil.isBlank(str)) {
            throw new IllegalArgumentException();
        }
    }

    public static void notBlank(String str, String errMsg) {
        if (StringUtil.isBlank(str)) {
            throw new IllegalArgumentException(errMsg);
        }
    }

    public static void notBlank(String str, Supplier<RuntimeException> supplier) {
        if (StringUtil.isBlank(str)) {
            throw supplier.get();
        }
    }

    public static void isEmpty(Collection collection) {
        if (CollectionUtil.isNotEmpty(collection)) {
            throw new IllegalArgumentException();
        }
    }

    public static void isEmpty(Collection collection, String errMsg) {
        if (CollectionUtil.isNotEmpty(collection)) {
            throw new IllegalArgumentException(errMsg);
        }
    }

    public static void isEmpty(Collection collection, Supplier<RuntimeException> supplier) {
        if (CollectionUtil.isNotEmpty(collection)) {
            throw supplier.get();
        }
    }

    public static void isEmpty(Object[] arr) {
        if (ArrayUtil.isNotEmpty(arr)) {
            throw new IllegalArgumentException();
        }
    }

    public static void isEmpty(Object[] arr, String errMsg) {
        if (ArrayUtil.isNotEmpty(arr)) {
            throw new IllegalArgumentException(errMsg);
        }
    }

    public static void isEmpty(Object[] arr, Supplier<RuntimeException> supplier) {
        if (ArrayUtil.isNotEmpty(arr)) {
            throw supplier.get();
        }
    }

    public static void isNotEmpty(Collection collection) {
        if (CollectionUtil.isEmpty(collection)) {
            throw new IllegalArgumentException();
        }
    }

    public static void isNotEmpty(Collection collection, String errMsg) {
        if (CollectionUtil.isEmpty(collection)) {
            throw new IllegalArgumentException(errMsg);
        }
    }

    public static void isNotEmpty(Collection collection, Supplier<RuntimeException> supplier) {
        if (CollectionUtil.isEmpty(collection)) {
            throw supplier.get();
        }
    }
    public static void isNotEmpty(Object[] arr) {
        if (ArrayUtil.isEmpty(arr)) {
            throw new IllegalArgumentException();
        }
    }

    public static void isNotEmpty(Object[] arr, String errMsg) {
        if (ArrayUtil.isEmpty(arr)) {
            throw new IllegalArgumentException(errMsg);
        }
    }

    public static void isNotEmpty(Object[] arr, Supplier<RuntimeException> supplier) {
        if (ArrayUtil.isEmpty(arr)) {
            throw supplier.get();
        }
    }
}
