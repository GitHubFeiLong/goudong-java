package com.goudong.core.util;

import com.goudong.core.function.StringSupplier;
import com.goudong.core.lang.RegexConst;

import java.util.Collection;
import java.util.Objects;
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

    //~ equals and not equals
    //==================================================================================================================
    /**
     * 断言{@code expected} 与 {@code actual}相等
     * @param expected 预期值
     * @param actual 实际值
     * @throws IllegalArgumentException
     */
    public static void isEquals(byte expected, byte actual) {
        if (expected != actual) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * 断言{@code expected} 与 {@code actual}相等
     * @param expected 预期值
     * @param actual 实际值
     * @param errMsg 自定义异常描述
     * @throws IllegalArgumentException
     */
    public static void isEquals(byte expected, byte actual, String errMsg) {
        if (expected != actual) {
            throw new IllegalArgumentException(errMsg);
        }
    }

    /**
     * 断言{@code expected} 与 {@code actual}相等
     * @param expected 预期值
     * @param actual 实际值
     * @param supplier 延迟执行自定义异常描述
     * @throws IllegalArgumentException
     */
    public static void isEquals(byte expected, byte actual, StringSupplier supplier) {
        if (expected != actual) {
            throw new IllegalArgumentException(supplier.get());
        }
    }

    /**
     * 断言{@code expected} 与 {@code actual}相等
     * @param expected 预期值
     * @param actual 实际值
     * @param supplier 自定义异常
     * @throws IllegalArgumentException
     */
    public static void isEquals(byte expected, byte actual, Supplier<RuntimeException> supplier) {
        if (expected != actual) {
            throw supplier.get();
        }
    }

    /**
     * 断言{@code expected} 与 {@code actual}相等
     * @param expected 预期值
     * @param actual 实际值
     * @throws IllegalArgumentException
     */
    public static void isNotEquals(byte expected, byte actual) {
        if (expected == actual) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * 断言{@code expected} 与 {@code actual}相等
     * @param expected 预期值
     * @param actual 实际值
     * @param errMsg 自定义异常描述
     * @throws IllegalArgumentException
     */
    public static void isNotEquals(byte expected, byte actual, String errMsg) {
        if (expected == actual) {
            throw new IllegalArgumentException(errMsg);
        }
    }

    /**
     * 断言{@code expected} 与 {@code actual}相等
     * @param expected 预期值
     * @param actual 实际值
     * @param supplier 延迟执行自定义异常描述
     * @throws IllegalArgumentException
     */
    public static void isNotEquals(byte expected, byte actual, StringSupplier supplier) {
        if (expected == actual) {
            throw new IllegalArgumentException(supplier.get());
        }
    }

    /**
     * 断言{@code expected} 与 {@code actual}相等
     * @param expected 预期值
     * @param actual 实际值
     * @param supplier 自定义异常
     * @throws IllegalArgumentException
     */
    public static void isNotEquals(byte expected, byte actual, Supplier<RuntimeException> supplier) {
        if (expected == actual) {
            throw supplier.get();
        }
    }

    /**
     * 断言{@code expected} 与 {@code actual}相等
     * @param expected 预期值
     * @param actual 实际值
     * @throws IllegalArgumentException
     */
    public static void isEquals(char expected, char actual) {
        if (expected != actual) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * 断言{@code expected} 与 {@code actual}相等
     * @param expected 预期值
     * @param actual 实际值
     * @param errMsg 自定义异常描述
     * @throws IllegalArgumentException
     */
    public static void isEquals(char expected, char actual, String errMsg) {
        if (expected != actual) {
            throw new IllegalArgumentException(errMsg);
        }
    }

    /**
     * 断言{@code expected} 与 {@code actual}相等
     * @param expected 预期值
     * @param actual 实际值
     * @param supplier 延迟执行自定义异常描述
     * @throws IllegalArgumentException
     */
    public static void isEquals(char expected, char actual, StringSupplier supplier) {
        if (expected != actual) {
            throw new IllegalArgumentException(supplier.get());
        }
    }

    /**
     * 断言{@code expected} 与 {@code actual}相等
     * @param expected 预期值
     * @param actual 实际值
     * @param supplier 自定义异常
     * @throws IllegalArgumentException
     */
    public static void isEquals(char expected, char actual, Supplier<RuntimeException> supplier) {
        if (expected != actual) {
            throw supplier.get();
        }
    }

    /**
     * 断言{@code expected} 与 {@code actual}相等
     * @param expected 预期值
     * @param actual 实际值
     * @throws IllegalArgumentException
     */
    public static void isNotEquals(char expected, char actual) {
        if (expected == actual) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * 断言{@code expected} 与 {@code actual}相等
     * @param expected 预期值
     * @param actual 实际值
     * @param errMsg 自定义异常描述
     * @throws IllegalArgumentException
     */
    public static void isNotEquals(char expected, char actual, String errMsg) {
        if (expected == actual) {
            throw new IllegalArgumentException(errMsg);
        }
    }

    /**
     * 断言{@code expected} 与 {@code actual}相等
     * @param expected 预期值
     * @param actual 实际值
     * @param supplier 延迟执行自定义异常描述
     * @throws IllegalArgumentException
     */
    public static void isNotEquals(char expected, char actual, StringSupplier supplier) {
        if (expected == actual) {
            throw new IllegalArgumentException(supplier.get());
        }
    }

    /**
     * 断言{@code expected} 与 {@code actual}相等
     * @param expected 预期值
     * @param actual 实际值
     * @param supplier 自定义异常
     * @throws IllegalArgumentException
     */
    public static void isNotEquals(char expected, char actual, Supplier<RuntimeException> supplier) {
        if (expected == actual) {
            throw supplier.get();
        }
    }

    /**
     * 断言{@code expected} 与 {@code actual}相等
     * @param expected 预期值
     * @param actual 实际值
     * @throws IllegalArgumentException
     */
    public static void isEquals(short expected, short actual) {
        if (expected != actual) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * 断言{@code expected} 与 {@code actual}相等
     * @param expected 预期值
     * @param actual 实际值
     * @param errMsg 自定义异常描述
     * @throws IllegalArgumentException
     */
    public static void isEquals(short expected, short actual, String errMsg) {
        if (expected != actual) {
            throw new IllegalArgumentException(errMsg);
        }
    }

    /**
     * 断言{@code expected} 与 {@code actual}相等
     * @param expected 预期值
     * @param actual 实际值
     * @param supplier 延迟执行自定义异常描述
     * @throws IllegalArgumentException
     */
    public static void isEquals(short expected, short actual, StringSupplier supplier) {
        if (expected != actual) {
            throw new IllegalArgumentException(supplier.get());
        }
    }

    /**
     * 断言{@code expected} 与 {@code actual}相等
     * @param expected 预期值
     * @param actual 实际值
     * @param supplier 自定义异常
     * @throws IllegalArgumentException
     */
    public static void isEquals(short expected, short actual, Supplier<RuntimeException> supplier) {
        if (expected != actual) {
            throw supplier.get();
        }
    }

    /**
     * 断言{@code expected} 与 {@code actual}不相等
     * @param expected 预期值
     * @param actual 实际值
     * @throws IllegalArgumentException
     */
    public static void isNotEquals(short expected, short actual) {
        if (expected == actual) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * 断言{@code expected} 与 {@code actual}相等
     * @param expected 预期值
     * @param actual 实际值
     * @param errMsg 自定义异常描述
     * @throws IllegalArgumentException
     */
    public static void isNotEquals(short expected, short actual, String errMsg) {
        if (expected == actual) {
            throw new IllegalArgumentException(errMsg);
        }
    }

    /**
     * 断言{@code expected} 与 {@code actual}相等
     * @param expected 预期值
     * @param actual 实际值
     * @param supplier 延迟执行自定义异常描述
     * @throws IllegalArgumentException
     */
    public static void isNotEquals(short expected, short actual, StringSupplier supplier) {
        if (expected == actual) {
            throw new IllegalArgumentException(supplier.get());
        }
    }

    /**
     * 断言{@code expected} 与 {@code actual}相等
     * @param expected 预期值
     * @param actual 实际值
     * @param supplier 自定义异常
     * @throws IllegalArgumentException
     */
    public static void isNotEquals(short expected, short actual, Supplier<RuntimeException> supplier) {
        if (expected == actual) {
            throw supplier.get();
        }
    }

    /**
     * 断言{@code expected} 与 {@code actual}相等
     * @param expected 预期值
     * @param actual 实际值
     * @throws IllegalArgumentException
     */
    public static void isEquals(int expected, int actual) {
        if (expected != actual) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * 断言{@code expected} 与 {@code actual}相等
     * @param expected 预期值
     * @param actual 实际值
     * @param errMsg 自定义异常描述
     * @throws IllegalArgumentException
     */
    public static void isEquals(int expected, int actual, String errMsg) {
        if (expected != actual) {
            throw new IllegalArgumentException(errMsg);
        }
    }

    /**
     * 断言{@code expected} 与 {@code actual}相等
     * @param expected 预期值
     * @param actual 实际值
     * @param supplier 延迟执行自定义异常描述
     * @throws IllegalArgumentException
     */
    public static void isEquals(int expected, int actual, StringSupplier supplier) {
        if (expected != actual) {
            throw new IllegalArgumentException(supplier.get());
        }
    }

    /**
     * 断言{@code expected} 与 {@code actual}相等
     * @param expected 预期值
     * @param actual 实际值
     * @param supplier 自定义异常
     * @throws IllegalArgumentException
     */
    public static void isEquals(int expected, int actual, Supplier<RuntimeException> supplier) {
        if (expected != actual) {
            throw supplier.get();
        }
    }

    /**
     * 断言{@code expected} 与 {@code actual}相等
     * @param expected 预期值
     * @param actual 实际值
     * @throws IllegalArgumentException
     */
    public static void isNotEquals(int expected, int actual) {
        if (expected == actual) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * 断言{@code expected} 与 {@code actual}相等
     * @param expected 预期值
     * @param actual 实际值
     * @param errMsg 自定义异常描述
     * @throws IllegalArgumentException
     */
    public static void isNotEquals(int expected, int actual, String errMsg) {
        if (expected == actual) {
            throw new IllegalArgumentException(errMsg);
        }
    }

    /**
     * 断言{@code expected} 与 {@code actual}相等
     * @param expected 预期值
     * @param actual 实际值
     * @param supplier 延迟执行自定义异常描述
     * @throws IllegalArgumentException
     */
    public static void isNotEquals(int expected, int actual, StringSupplier supplier) {
        if (expected == actual) {
            throw new IllegalArgumentException(supplier.get());
        }
    }

    /**
     * 断言{@code expected} 与 {@code actual}相等
     * @param expected 预期值
     * @param actual 实际值
     * @param supplier 自定义异常
     * @throws IllegalArgumentException
     */
    public static void isNotEquals(int expected, int actual, Supplier<RuntimeException> supplier) {
        if (expected == actual) {
            throw supplier.get();
        }
    }

    /**
     * 断言{@code expected} 与 {@code actual}相等
     * @param expected 预期值
     * @param actual 实际值
     * @throws IllegalArgumentException
     */
    public static void isEquals(long expected, long actual) {
        if (expected != actual) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * 断言{@code expected} 与 {@code actual}相等
     * @param expected 预期值
     * @param actual 实际值
     * @param errMsg 自定义异常描述
     * @throws IllegalArgumentException
     */
    public static void isEquals(long expected, long actual, String errMsg) {
        if (expected != actual) {
            throw new IllegalArgumentException(errMsg);
        }
    }

    /**
     * 断言{@code expected} 与 {@code actual}相等
     * @param expected 预期值
     * @param actual 实际值
     * @param supplier 延迟执行自定义异常描述
     * @throws IllegalArgumentException
     */
    public static void isEquals(long expected, long actual, StringSupplier supplier) {
        if (expected != actual) {
            throw new IllegalArgumentException(supplier.get());
        }
    }

    /**
     * 断言{@code expected} 与 {@code actual}相等
     * @param expected 预期值
     * @param actual 实际值
     * @param supplier 自定义异常
     * @throws IllegalArgumentException
     */
    public static void isEquals(long expected, long actual, Supplier<RuntimeException> supplier) {
        if (expected != actual) {
            throw supplier.get();
        }
    }

    /**
     * 断言{@code expected} 与 {@code actual}相等
     * @param expected 预期值
     * @param actual 实际值
     * @throws IllegalArgumentException
     */
    public static void isNotEquals(long expected, long actual) {
        if (expected == actual) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * 断言{@code expected} 与 {@code actual}相等
     * @param expected 预期值
     * @param actual 实际值
     * @param errMsg 自定义异常描述
     * @throws IllegalArgumentException
     */
    public static void isNotEquals(long expected, long actual, String errMsg) {
        if (expected == actual) {
            throw new IllegalArgumentException(errMsg);
        }
    }

    /**
     * 断言{@code expected} 与 {@code actual}相等
     * @param expected 预期值
     * @param actual 实际值
     * @param supplier 延迟执行自定义异常描述
     * @throws IllegalArgumentException
     */
    public static void isNotEquals(long expected, long actual, StringSupplier supplier) {
        if (expected == actual) {
            throw new IllegalArgumentException(supplier.get());
        }
    }

    /**
     * 断言{@code expected} 与 {@code actual}相等
     * @param expected 预期值
     * @param actual 实际值
     * @param supplier 自定义异常
     * @throws IllegalArgumentException
     */
    public static void isNotEquals(long expected, long actual, Supplier<RuntimeException> supplier) {
        if (expected == actual) {
            throw supplier.get();
        }
    }

    /**
     * 断言{@code expected} 与 {@code actual}相等
     * @param expected 预期值
     * @param actual 实际值
     * @throws IllegalArgumentException
     */
    public static void isEquals(float expected, float actual) {
        if (expected != actual) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * 断言{@code expected} 与 {@code actual}相等
     * @param expected 预期值
     * @param actual 实际值
     * @param errMsg 自定义异常描述
     * @throws IllegalArgumentException
     */
    public static void isEquals(float expected, float actual, String errMsg) {
        if (expected != actual) {
            throw new IllegalArgumentException(errMsg);
        }
    }

    /**
     * 断言{@code expected} 与 {@code actual}相等
     * @param expected 预期值
     * @param actual 实际值
     * @param supplier 延迟执行自定义异常描述
     * @throws IllegalArgumentException
     */
    public static void isEquals(float expected, float actual, StringSupplier supplier) {
        if (expected != actual) {
            throw new IllegalArgumentException(supplier.get());
        }
    }

    /**
     * 断言{@code expected} 与 {@code actual}相等
     * @param expected 预期值
     * @param actual 实际值
     * @param supplier 自定义异常
     * @throws IllegalArgumentException
     */
    public static void isEquals(float expected, float actual, Supplier<RuntimeException> supplier) {
        if (expected != actual) {
            throw supplier.get();
        }
    }

    /**
     * 断言{@code expected} 与 {@code actual}相等
     * @param expected 预期值
     * @param actual 实际值
     * @throws IllegalArgumentException
     */
    public static void isNotEquals(float expected, float actual) {
        if (expected == actual) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * 断言{@code expected} 与 {@code actual}相等
     * @param expected 预期值
     * @param actual 实际值
     * @param errMsg 自定义异常描述
     * @throws IllegalArgumentException
     */
    public static void isNotEquals(float expected, float actual, String errMsg) {
        if (expected == actual) {
            throw new IllegalArgumentException(errMsg);
        }
    }

    /**
     * 断言{@code expected} 与 {@code actual}相等
     * @param expected 预期值
     * @param actual 实际值
     * @param supplier 延迟执行自定义异常描述
     * @throws IllegalArgumentException
     */
    public static void isNotEquals(float expected, float actual, StringSupplier supplier) {
        if (expected == actual) {
            throw new IllegalArgumentException(supplier.get());
        }
    }

    /**
     * 断言{@code expected} 与 {@code actual}相等
     * @param expected 预期值
     * @param actual 实际值
     * @param supplier 自定义异常
     * @throws IllegalArgumentException
     */
    public static void isNotEquals(float expected, float actual, Supplier<RuntimeException> supplier) {
        if (expected == actual) {
            throw supplier.get();
        }
    }

    /**
     * 断言{@code expected} 与 {@code actual}相等
     * @param expected 预期值
     * @param actual 实际值
     * @throws IllegalArgumentException
     */
    public static void isEquals(double expected, double actual) {
        if (expected != actual) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * 断言{@code expected} 与 {@code actual}相等
     * @param expected 预期值
     * @param actual 实际值
     * @param errMsg 自定义异常描述
     * @throws IllegalArgumentException
     */
    public static void isEquals(double expected, double actual, String errMsg) {
        if (expected != actual) {
            throw new IllegalArgumentException(errMsg);
        }
    }

    /**
     * 断言{@code expected} 与 {@code actual}相等
     * @param expected 预期值
     * @param actual 实际值
     * @param supplier 延迟执行自定义异常描述
     * @throws IllegalArgumentException
     */
    public static void isEquals(double expected, double actual, StringSupplier supplier) {
        if (expected != actual) {
            throw new IllegalArgumentException(supplier.get());
        }
    }

    /**
     * 断言{@code expected} 与 {@code actual}相等
     * @param expected 预期值
     * @param actual 实际值
     * @param supplier 自定义异常
     * @throws IllegalArgumentException
     */
    public static void isEquals(double expected, double actual, Supplier<RuntimeException> supplier) {
        if (expected != actual) {
            throw supplier.get();
        }
    }

    /**
     * 断言{@code expected} 与 {@code actual}相等
     * @param expected 预期值
     * @param actual 实际值
     * @throws IllegalArgumentException
     */
    public static void isNotEquals(double expected, double actual) {
        if (expected == actual) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * 断言{@code expected} 与 {@code actual}相等
     * @param expected 预期值
     * @param actual 实际值
     * @param errMsg 自定义异常描述
     * @throws IllegalArgumentException
     */
    public static void isNotEquals(double expected, double actual, String errMsg) {
        if (expected == actual) {
            throw new IllegalArgumentException(errMsg);
        }
    }

    /**
     * 断言{@code expected} 与 {@code actual}相等
     * @param expected 预期值
     * @param actual 实际值
     * @param supplier 延迟执行自定义异常描述
     * @throws IllegalArgumentException
     */
    public static void isNotEquals(double expected, double actual, StringSupplier supplier) {
        if (expected == actual) {
            throw new IllegalArgumentException(supplier.get());
        }
    }

    /**
     * 断言{@code expected} 与 {@code actual}相等
     * @param expected 预期值
     * @param actual 实际值
     * @param supplier 自定义异常
     * @throws IllegalArgumentException
     */
    public static void isNotEquals(double expected, double actual, Supplier<RuntimeException> supplier) {
        if (expected == actual) {
            throw supplier.get();
        }
    }

    /**
     * 断言{@code expected} 与 {@code actual}相等
     * @param expected 预期值
     * @param actual 实际值
     * @throws IllegalArgumentException
     */
    public static void isEquals(boolean expected, boolean actual) {
        if (expected != actual) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * 断言{@code expected} 与 {@code actual}相等
     * @param expected 预期值
     * @param actual 实际值
     * @param errMsg 自定义异常描述
     * @throws IllegalArgumentException
     */
    public static void isEquals(boolean expected, boolean actual, String errMsg) {
        if (expected != actual) {
            throw new IllegalArgumentException(errMsg);
        }
    }

    /**
     * 断言{@code expected} 与 {@code actual}相等
     * @param expected 预期值
     * @param actual 实际值
     * @param supplier 延迟执行自定义异常描述
     * @throws IllegalArgumentException
     */
    public static void isEquals(boolean expected, boolean actual, StringSupplier supplier) {
        if (expected != actual) {
            throw new IllegalArgumentException(supplier.get());
        }
    }

    /**
     * 断言{@code expected} 与 {@code actual}相等
     * @param expected 预期值
     * @param actual 实际值
     * @param supplier 自定义异常
     * @throws IllegalArgumentException
     */
    public static void isEquals(boolean expected, boolean actual, Supplier<RuntimeException> supplier) {
        if (expected != actual) {
            throw supplier.get();
        }
    }

    /**
     * 断言{@code expected} 与 {@code actual}相等
     * @param expected 预期值
     * @param actual 实际值
     * @throws IllegalArgumentException
     */
    public static void isNotEquals(boolean expected, boolean actual) {
        if (expected == actual) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * 断言{@code expected} 与 {@code actual}相等
     * @param expected 预期值
     * @param actual 实际值
     * @param errMsg 自定义异常描述
     * @throws IllegalArgumentException
     */
    public static void isNotEquals(boolean expected, boolean actual, String errMsg) {
        if (expected == actual) {
            throw new IllegalArgumentException(errMsg);
        }
    }

    /**
     * 断言{@code expected} 与 {@code actual}相等
     * @param expected 预期值
     * @param actual 实际值
     * @param supplier 延迟执行自定义异常描述
     * @throws IllegalArgumentException
     */
    public static void isNotEquals(boolean expected, boolean actual, StringSupplier supplier) {
        if (expected == actual) {
            throw new IllegalArgumentException(supplier.get());
        }
    }

    /**
     * 断言{@code expected} 与 {@code actual}相等
     * @param expected 预期值
     * @param actual 实际值
     * @param supplier 自定义异常
     * @throws IllegalArgumentException
     */
    public static void isNotEquals(boolean expected, boolean actual, Supplier<RuntimeException> supplier) {
        if (expected == actual) {
            throw supplier.get();
        }
    }

    /**
     * 断言{@code expected} 与 {@code actual}相等
     * @param expected 预期值
     * @param actual 实际值
     * @throws IllegalArgumentException
     */
    public static void isEquals(Object expected, Object actual) {
        if (!Objects.equals(expected, actual)) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * 断言{@code expected} 与 {@code actual}相等
     * @param expected 预期值
     * @param actual 实际值
     * @param errMsg 自定义异常描述
     * @throws IllegalArgumentException
     */
    public static void isEquals(Object expected, Object actual, String errMsg) {
        if (!Objects.equals(expected, actual)) {
            throw new IllegalArgumentException(errMsg);
        }
    }

    /**
     * 断言{@code expected} 与 {@code actual}相等
     * @param expected 预期值
     * @param actual 实际值
     * @param supplier 延迟执行自定义异常描述
     * @throws IllegalArgumentException
     */
    public static void isEquals(Object expected, Object actual, StringSupplier supplier) {
        if (!Objects.equals(expected, actual)) {
            throw new IllegalArgumentException(supplier.get());
        }
    }

    /**
     * 断言{@code expected} 与 {@code actual}相等
     * @param expected 预期值
     * @param actual 实际值
     * @param supplier 自定义异常
     * @throws IllegalArgumentException
     */
    public static void isEquals(Object expected, Object actual, Supplier<RuntimeException> supplier) {
        if (!Objects.equals(expected, actual)) {
            throw supplier.get();
        }
    }

    /**
     * 断言{@code expected} 与 {@code actual}相等
     * @param expected 预期值
     * @param actual 实际值
     * @throws IllegalArgumentException
     */
    public static void isNotEquals(Object expected, Object actual) {
        if (Objects.equals(expected, actual)) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * 断言{@code expected} 与 {@code actual}相等
     * @param expected 预期值
     * @param actual 实际值
     * @param errMsg 自定义异常描述
     * @throws IllegalArgumentException
     */
    public static void isNotEquals(Object expected, Object actual, String errMsg) {
        if (Objects.equals(expected, actual)) {
            throw new IllegalArgumentException(errMsg);
        }
    }

    /**
     * 断言{@code expected} 与 {@code actual}相等
     * @param expected 预期值
     * @param actual 实际值
     * @param supplier 延迟执行自定义异常描述
     * @throws IllegalArgumentException
     */
    public static void isNotEquals(Object expected, Object actual, StringSupplier supplier) {
        if (Objects.equals(expected, actual)) {
            throw new IllegalArgumentException(supplier.get());
        }
    }

    /**
     * 断言{@code expected} 与 {@code actual}相等
     * @param expected 预期值
     * @param actual 实际值
     * @param supplier 自定义异常
     * @throws IllegalArgumentException
     */
    public static void isNotEquals(Object expected, Object actual, Supplier<RuntimeException> supplier) {
        if (Objects.equals(expected, actual)) {
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
     * 断言{@code str}是一个正确的邮箱
     * @param str
     * @throws IllegalArgumentException
     */
    public static void isEmail(String str) {
        isNotNull(str);
        isTrue(str.matches(RegexConst.EMAIL));
    }

    /**
     * 断言{@code str}是一个正确的邮箱
     * @param str
     * @param errMsg 自定义异常描述
     * @throws IllegalArgumentException
     */
    public static void isEmail(String str, String errMsg) {
        isNotNull(str, errMsg);
        isTrue(str.matches(RegexConst.EMAIL), errMsg);
    }

    /**
     * 断言{@code str}是一个正确的邮箱
     * @param str
     * @param supplier 延迟执行自定义异常描述
     * @throws IllegalArgumentException
     */
    public static void isEmail(String str, StringSupplier supplier) {
        isNotNull(str, supplier);
        isTrue(str.matches(RegexConst.EMAIL), supplier);
    }

    /**
     * 断言{@code str}是一个正确的邮箱
     * @param str
     * supplier 自定义异常
     */
    public static void isEmail(String str, Supplier<RuntimeException> supplier) {
        isNotNull(str, supplier);
        isTrue(str.matches(RegexConst.EMAIL), supplier);
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

    /**
     * 断言{@code name}是枚举{@code clazz}的成员
     * @param name 成员名称
     * @param clazz 枚举class对象
     */
    public static void isEnum (String name, Class<? extends Enum> clazz) {
        isNotBlank(name);
        isNotNull(clazz);
        Enum.valueOf(clazz, name);
    }

    /**
     * 断言{@code name}是枚举{@code clazz}的成员
     * @param name 成员名称
     * @param clazz 枚举class对象
     * @param errMsg 自定义异常描述
     */
    public static void isEnum (String name, Class<? extends Enum> clazz, String errMsg) {
        isNotBlank(name, errMsg);
        isNotNull(clazz, errMsg);
        try {
            Enum.valueOf(clazz, name);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(errMsg);
        }
    }

    /**
     * 断言{@code name}是枚举{@code clazz}的成员
     * @param name 成员名称
     * @param clazz 枚举class对象
     * @param supplier 自定义异常描述
     */
    public static void isEnum (String name, Class<? extends Enum> clazz, StringSupplier supplier) {
        isNotBlank(name, supplier);
        isNotNull(clazz, supplier);
        try {
            Enum.valueOf(clazz, name);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(supplier.get());
        }
    }

    /**
     * 断言{@code name}是枚举{@code clazz}的成员
     * @param name 成员名称
     * @param clazz 枚举class对象
     * @param supplier – 自定义异常
     */
    public static void isEnum (String name, Class<? extends Enum> clazz, Supplier<RuntimeException> supplier) {
        isNotBlank(name, supplier);
        isNotNull(clazz, supplier);
        try {
            Enum.valueOf(clazz, name);
        } catch (IllegalArgumentException e) {
            throw supplier.get();
        }
    }



}
