package com.goudong.core.util;

import com.goudong.core.function.StringSupplier;

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

    /**
     * 断言{@code boo=true}
     * @param boo
     * @throws IllegalArgumentException
     */
    public static void isTrue(boolean boo) {
        if (!boo) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * 断言{@code boo=true}
     * @param boo
     * @param errMsg 自定义异常描述
     * @throws IllegalArgumentException
     */
    public static void isTrue(boolean boo, String errMsg) {
        if (!boo) {
            throw new IllegalArgumentException(errMsg);
        }
    }

    /**
     * 断言{@code boo=true}
     * @param boo
     * @param supplier 延迟执行自定义异常描述
     * @throws IllegalArgumentException
     */
    public static void isTrue(boolean boo, StringSupplier supplier) {
        if (!boo) {
            throw new IllegalArgumentException(supplier.get());
        }
    }

    /**
     * 断言{@code boo=true}
     * @param boo
     * @param supplier 自定义异常
     */
    public static void isTrue(boolean boo, Supplier<RuntimeException> supplier) {
        if (!boo) {
            throw supplier.get();
        }
    }

    /**
     * 断言{@code boo=false}
     * @param boo
     * @throws IllegalArgumentException
     */
    public static void isFalse(boolean boo) {
        if (boo) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * 断言{@code boo=false}
     * @param boo
     * @param errMsg 自定义异常描述
     * @throws IllegalArgumentException
     */
    public static void isFalse(boolean boo, String errMsg) {
        if (boo) {
            throw new IllegalArgumentException(errMsg);
        }
    }

    /**
     * 断言{@code boo=false}
     * @param boo
     * @param supplier 延迟执行自定义异常描述
     * @throws IllegalArgumentException
     */
    public static void isFalse(boolean boo, StringSupplier supplier) {
        if (boo) {
            throw new IllegalArgumentException(supplier.get());
        }
    }

    /**
     * 断言{@code boo=false}
     * @param boo
     * @param supplier  自定义异常
     */
    public static void isFalse(boolean boo, Supplier<RuntimeException> supplier) {
        if (boo) {
            throw supplier.get();
        }
    }

    /**
     * 断言{@code obj=null}
     * @param obj
     * @throws IllegalArgumentException
     */
    public static void isNull(Object obj) {
        if (obj != null) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * 断言{@code obj=null}
     * @param obj
     * @param errMsg 自定义异常描述
     * @throws IllegalArgumentException
     */
    public static void isNull(Object obj, String errMsg) {
        if (obj != null) {
            throw new IllegalArgumentException(errMsg);
        }
    }

    /**
     * 断言{@code obj=null}
     * @param obj
     * @param supplier 延迟执行自定义异常描述
     * @throws IllegalArgumentException
     */
    public static void isNull(Object obj, StringSupplier supplier) {
        if (obj != null) {
            throw new IllegalArgumentException(supplier.get());
        }
    }

    /**
     * 断言{@code obj=null}
     * @param obj
     * @param supplier 自定义异常
     */
    public static void isNull(Object obj, Supplier<RuntimeException> supplier) {
        if (obj != null) {
            throw supplier.get();
        }
    }

    /**
     * 断言{@code obj!=null}
     * @param obj
     * @throws IllegalArgumentException
     */
    public static void isNotNull(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * 断言{@code obj!=null}
     * @param obj
     * @param errMsg 自定义异常描述
     * @throws IllegalArgumentException
     */
    public static void isNotNull(Object obj, String errMsg) {
        if (obj == null) {
            throw new IllegalArgumentException(errMsg);
        }
    }

    /**
     * 断言{@code obj!=null}
     * @param obj
     * @param supplier 延迟执行自定义异常描述
     * @throws IllegalArgumentException
     */
    public static void isNotNull(Object obj, StringSupplier supplier) {
        if (obj == null) {
            throw new IllegalArgumentException(supplier.get());
        }
    }

    /**
     * 断言{@code obj!=null}
     * @param obj
     * @param supplier 自定义异常
     */
    public static void isNotNull(Object obj, Supplier<RuntimeException> supplier) {
        if (obj == null) {
            throw supplier.get();
        }
    }

    /**
     * 断言{@code str!=null && str.trim().length()!=0}
     * @param str
     * @throws IllegalArgumentException
     */
    public static void isNotBlank(String str) {
        if (StringUtil.isBlank(str)) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * 断言{@code str!=null && str.trim().length()!=0}
     * @param str
     * @param errMsg 自定义异常描述
     * @throws IllegalArgumentException
     */
    public static void isNotBlank(String str, String errMsg) {
        if (StringUtil.isBlank(str)) {
            throw new IllegalArgumentException(errMsg);
        }
    }

    /**
     * 断言{@code str!=null && str.trim().length()!=0}
     * @param str
     * @param supplier 延迟执行自定义异常描述
     * @throws IllegalArgumentException
     */
    public static void isNotBlank(String str, StringSupplier supplier) {
        if (StringUtil.isBlank(str)) {
            throw new IllegalArgumentException(supplier.get());
        }
    }

    /**
     * 断言{@code str!=null && str.trim().length()!=0}
     * @param str
     * @param supplier 自定义异常
     */
    public static void isNotBlank(String str, Supplier<RuntimeException> supplier) {
        if (StringUtil.isBlank(str)) {
            throw supplier.get();
        }
    }

    /**
     * 断言{@code collection=null || collection.isEmpty()=true}
     * @param collection
     * @throws IllegalArgumentException
     */
    public static void isEmpty(Collection collection) {
        if (CollectionUtil.isNotEmpty(collection)) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * 断言{@code collection=null || collection.isEmpty()=true}
     * @param collection
     * @param errMsg 自定义异常描述
     * @throws IllegalArgumentException
     */
    public static void isEmpty(Collection collection, String errMsg) {
        if (CollectionUtil.isNotEmpty(collection)) {
            throw new IllegalArgumentException(errMsg);
        }
    }

    /**
     * 断言{@code collection=null || collection.isEmpty()=true}
     * @param collection
     * @param supplier 延迟执行自定义异常描述
     * @throws IllegalArgumentException
     */
    public static void isEmpty(Collection collection, StringSupplier supplier) {
        if (CollectionUtil.isNotEmpty(collection)) {
            throw new IllegalArgumentException(supplier.get());
        }
    }

    /**
     * 断言{@code collection=null || collection.isEmpty()=true}
     * @param collection
     * @param supplier 自定义异常
     */
    public static void isEmpty(Collection collection, Supplier<RuntimeException> supplier) {
        if (CollectionUtil.isNotEmpty(collection)) {
            throw supplier.get();
        }
    }

    /**
     * 断言{@code arr=null || arr.length=0}
     * @param arr
     * @throws IllegalArgumentException
     */
    public static void isEmpty(Object[] arr) {
        if (ArrayUtil.isNotEmpty(arr)) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * 断言{@code arr=null || arr.length=0}
     * @param arr
     * @param errMsg 自定义异常描述
     * @throws IllegalArgumentException
     */
    public static void isEmpty(Object[] arr, String errMsg) {
        if (ArrayUtil.isNotEmpty(arr)) {
            throw new IllegalArgumentException(errMsg);
        }
    }

    /**
     * 断言{@code arr=null || arr.length=0}
     * @param arr
     * @param supplier 延迟执行自定义异常描述
     * @throws IllegalArgumentException
     */
    public static void isEmpty(Object[] arr, StringSupplier supplier) {
        if (ArrayUtil.isNotEmpty(arr)) {
            throw new IllegalArgumentException(supplier.get());
        }
    }

    /**
     * 断言{@code arr=null || arr.length=0}
     * @param arr
     * @param supplier 自定义异常
     * @throws IllegalArgumentException
     */
    public static void isEmpty(Object[] arr, Supplier<RuntimeException> supplier) {
        if (ArrayUtil.isNotEmpty(arr)) {
            throw supplier.get();
        }
    }

    /**
     * 断言{@code collection!=null && collection.isEmpty()=false}
     * @param collection
     * @throws IllegalArgumentException
     */
    public static void isNotEmpty(Collection collection) {
        if (CollectionUtil.isEmpty(collection)) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * 断言{@code collection!=null && collection.isEmpty()=false}
     * @param collection
     * @param errMsg 自定义异常描述
     * @throws IllegalArgumentException
     */
    public static void isNotEmpty(Collection collection, String errMsg) {
        if (CollectionUtil.isEmpty(collection)) {
            throw new IllegalArgumentException(errMsg);
        }
    }

    /**
     * 断言{@code collection!=null && collection.isEmpty()=false}
     * @param collection
     * @param supplier 延迟执行自定义异常描述
     * @throws IllegalArgumentException
     */
    public static void isNotEmpty(Collection collection, StringSupplier supplier) {
        if (CollectionUtil.isEmpty(collection))  {
            throw new IllegalArgumentException(supplier.get());
        }
    }

    /**
     * 断言{@code collection!=null && collection.isEmpty()=false}
     * @param collection
     * @param supplier  自定义异常
     */
    public static void isNotEmpty(Collection collection, Supplier<RuntimeException> supplier) {
        if (CollectionUtil.isEmpty(collection)) {
            throw supplier.get();
        }
    }

    /**
     * 断言{@code arr!=null && arr.length!=0}
     * @param arr
     * @throws IllegalArgumentException
     */
    public static void isNotEmpty(Object[] arr) {
        if (ArrayUtil.isEmpty(arr)) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * 断言{@code arr!=null && arr.length!=0}
     * @param arr
     * @param errMsg 自定义异常描述
     * @throws IllegalArgumentException
     */
    public static void isNotEmpty(Object[] arr, String errMsg) {
        if (ArrayUtil.isEmpty(arr)) {
            throw new IllegalArgumentException(errMsg);
        }
    }

    /**
     * 断言{@code arr!=null && arr.length!=0}
     * @param arr
     * @param supplier 自定义异常描述
     * @throws IllegalArgumentException
     */
    public static void isNotEmpty(Object[] arr, StringSupplier supplier) {
        if (ArrayUtil.isEmpty(arr)) {
            throw new IllegalArgumentException(supplier.get());
        }
    }

    /**
     * 断言{@code arr!=null && arr.length!=0}
     * @param arr
     * @param supplier 自定义异常
     */
    public static void isNotEmpty(Object[] arr, Supplier<RuntimeException> supplier) {
        if (ArrayUtil.isEmpty(arr)) {
            throw supplier.get();
        }
    }
}
