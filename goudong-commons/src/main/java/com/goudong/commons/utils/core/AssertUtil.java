package com.goudong.commons.utils.core;

import com.goudong.boot.exception.core.BasicException;
import com.goudong.boot.exception.core.ClientException;
import com.goudong.boot.exception.core.ServerException;
import com.goudong.boot.exception.enumerate.ClientExceptionEnum;
import com.goudong.boot.exception.enumerate.ServerExceptionEnum;
import com.goudong.core.lang.ExceptionEnumInterface;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 扩展 Assert
 * @Author msi
 * @Date 2021-05-07 15:29
 * @Version 1.0
 */
@Slf4j
public class AssertUtil extends Assert{
    /**
     * 手机号正则表达式
     */
    public static final String PHONE_REGEX = "^1([358][0-9]|4[01456879]|6[2567]|7[0135678]|9[012356789])[0-9]{8}$";

    /**
     * 邮箱正则表达式
     */
    public static final String EMAIL_REGEX =  "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";


    // ~ 断言，然后抛出参数异常
    // =================================================================================================================
    /**
     * 抛出异常
     * @param exceptionEnumInterface
     */
    private static void throwBasicExceptionByEnum(ExceptionEnumInterface exceptionEnumInterface) {
        if (exceptionEnumInterface instanceof ClientExceptionEnum) {
            throw ClientException.client((ClientExceptionEnum) exceptionEnumInterface);
        } else if (exceptionEnumInterface instanceof ServerExceptionEnum) {
            throw ServerException.serverException((ServerExceptionEnum) exceptionEnumInterface);
        }
    }

    /**
     * 正则表达式验证手机号
     * @param phone 手机号
     * @param message 错误描述信息
     * @return
     */
    public static void isPhone(String phone, String message) {
        Assert.hasLength(phone, "正则校验手机号格式，手机号不能为空");
        Assert.isTrue(phone.matches(PHONE_REGEX), message);
    }

    /**
     * 正则表达式验证手机号（抛出自定义异常）
     * @param phone 手机号
     * @param basicException 自定义异常对象
     * @return
     */
    public static void isPhone(String phone, BasicException basicException) {
        hasLength(phone, basicException);
        isTrue(phone.matches(PHONE_REGEX), basicException);
    }

    /**
     * 正则表达式验证手机号（抛出自定义异常）
     * @param phone 手机号
     * @param exceptionEnum 异常枚举对象
     * @return
     */
    public static void isPhone(String phone, ExceptionEnumInterface exceptionEnum) {
        hasLength(phone, exceptionEnum);
        isTrue(phone.matches(PHONE_REGEX), exceptionEnum);
    }


    /**
     * 正则表达式验证邮箱
     * @param email 邮箱
     * @param message 错误描述信息
     * @return
     */
    public static void isEmail(String email, String message) {
        Assert.hasLength(email, "邮箱不能为空");
        Assert.isTrue(email.matches(EMAIL_REGEX), message);
    }

    /**
     * 正则表达式验证邮箱（抛出自定义异常）
     * @param email 邮箱
     * @param basicException 自定义异常对象
     * @return
     */
    public static void isEmail(String email, BasicException basicException) {
        hasLength(email, basicException);
        isTrue(email.matches(EMAIL_REGEX), basicException);
    }

    /**
     * 正则表达式验证邮箱（抛出自定义异常）
     * @param email 邮箱
     * @param exceptionEnum 异常枚举对象
     * @return
     */
    public static void isEmail(String email, ExceptionEnumInterface exceptionEnum) {
        hasLength(email, exceptionEnum);
        isTrue(email.matches(EMAIL_REGEX), exceptionEnum);
    }

    /**
     * 断言，name是枚举clazz的成员
     * @param name 成员名称
     * @param clazz 枚举class对象
     */
    public static void isEnum (String name, Class clazz) {
        isEnum(name, clazz, (String)null);
    }
    /**
     * 断言，name是枚举clazz的成员
     * @param name 成员名称
     * @param clazz 枚举class对象
     * @param message 错误信息
     */
    public static void isEnum (String name, Class clazz, String message) {
        try {
            Enum anEnum = Enum.valueOf(clazz, name.toUpperCase());
        } catch (IllegalArgumentException e) {

            // 不为空，使用自定义异常
            if (StringUtils.isNotBlank(message)) {
                throw new IllegalArgumentException(message);
            }

            // message为空，使用通用异常信息(这里会多一个$VALUES)
            Field[] declaredFields = clazz.getDeclaredFields();
            List<String> fieldNames = Stream.of(declaredFields)
                    .filter(f->Objects.equals(f.getType().getName(), clazz.getName()))
                    .map(Field::getName)
                    .collect(Collectors.toList());
            message = String.format("参数错误,'%s'不是有效值,请使用以下值作参数：%s", name, StringUtils.join(fieldNames, ","));

            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 断言，name是枚举clazz的成员（抛出自定义异常）
     * @param name 成员名称
     * @param clazz 枚举class对象
     * @param basicException 自定义异常对象
     * @return
     */
    public static void isEnum (String name, Class clazz, BasicException basicException) {
        try {
            Enum anEnum = Enum.valueOf(clazz, name.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw basicException;
        }
    }

    /**
     * 断言，name是枚举clazz的成员（抛出自定义异常）
     * @param name 成员名称
     * @param clazz 枚举class对象
     * @param exceptionEnum 异常枚举对象
     * @return
     */
    public static void isEnum (String name, Class clazz, ExceptionEnumInterface exceptionEnum) {
        try {
            Enum anEnum = Enum.valueOf(clazz, name.toUpperCase());
        } catch (IllegalArgumentException e) {
            throwBasicExceptionByEnum(exceptionEnum);
        }
    }

    /**
     * 断言，diskPath是有效的磁盘路径
     * @param diskPath 磁盘路径
     * @param message 错误信息
     */
    public static void isDiskPath (String diskPath, String message) {
        Assert.hasLength(diskPath, "diskPath 无效");
        // 判断是否正确
        File file = new File(diskPath);
        if (!file.exists()) {
            boolean mkdirs = file.mkdirs();
            if (!mkdirs) {
                throw new IllegalArgumentException(message);
            }
        }
    }

    /**
     * 断言，diskPath是有效的磁盘路径
     * @param diskPath 磁盘路径
     * @param basicException 自定义异常对象
     */
    public static void isDiskPath (String diskPath, BasicException basicException) {
        hasLength(diskPath, basicException);
        // 判断是否正确
        File file = new File(diskPath);
        if (!file.exists()) {
            boolean mkdirs = file.mkdirs();
            if (!mkdirs) {
                throw basicException;
            }
        }
    }

    /**
     * 断言，diskPath是有效的磁盘路径
     * @param diskPath 磁盘路径
     * @param exceptionEnum 异常枚举对象
     */
    public static void isDiskPath (String diskPath, ExceptionEnumInterface exceptionEnum) {
        hasLength(diskPath, exceptionEnum);
        // 判断是否正确
        File file = new File(diskPath);
        if (!file.exists()) {
            boolean mkdirs = file.mkdirs();
            if (!mkdirs) {
                throwBasicExceptionByEnum(exceptionEnum);
            }
        }
    }

    /**
     * 断言不允许为空，如果为空就抛出basicException异常
     * @param object 被断言对象
     * @param basicException 自定义异常
     */
    public static void notNull(@Nullable Object object, BasicException basicException) {
        if (object == null) {
            throw basicException;
        }
    }

    /**
     * 断言不允许为空，如果为空就抛出 exceptionEnumInterface 对应的异常
     * 例如：ClientException、ServerExceptionEnum
     * @param object 被断言对象
     * @param exceptionEnum 自定义异常枚举
     */
    public static void notNull(@Nullable Object object, ExceptionEnumInterface exceptionEnum) {
        if (object == null) {
            throwBasicExceptionByEnum(exceptionEnum);
        }
    }

    /**
     * 断言表达式结果是true，如果是false就抛出basicException异常
     * @param expression 表达式
     * @param basicException 自定义异常
     */
    public static void isTrue(boolean expression, BasicException basicException) {
        if (!expression) {
            throw basicException;
        }
    }

    /**
     * 断言表达式结果是true，如果是false就抛出basicException异常
     * @param expression 表达式
     * @param exceptionEnum 自定义异常枚举对象
     */
    public static void isTrue(boolean expression, ExceptionEnumInterface exceptionEnum) {
        if (!expression) {
            throwBasicExceptionByEnum(exceptionEnum);
        }
    }

    /**
     * 断言对象值为null，如果不等于null，就抛出basicException异常
     * @param object 断言参数
     * @param basicException 自定义异常
     */
    public static void isNull(@Nullable Object object, BasicException basicException) {
        if (object != null) {
            throw basicException;
        }
    }

    /**
     * 断言对象值为null，如果不等于null，就抛出basicException异常
     * @param object 断言参数
     * @param exceptionEnum 自定义异常
     */
    public static void isNull(@Nullable Object object, ExceptionEnumInterface exceptionEnum) {
        if (object != null) {
            throwBasicExceptionByEnum(exceptionEnum);
        }
    }

    /**
     * 断言字符串有长度，如果字符串长度为0，就抛出basicException异常
     * @param text 断言参数
     * @param basicException 自定义异常
     */
    public static void hasLength(@Nullable String text, BasicException basicException) {
        if (!org.springframework.util.StringUtils.hasLength(text)) {
            throw basicException;
        }
    }

    /**
     * 断言字符串有长度，如果字符串长度为0，就抛出basicException异常
     * @param text 断言参数
     * @param exceptionEnum 自定义异常
     */
    public static void hasLength(@Nullable String text, ExceptionEnumInterface exceptionEnum) {
        if (!org.springframework.util.StringUtils.hasLength(text)) {
            throwBasicExceptionByEnum(exceptionEnum);
        }
    }

    /**
     * 断言字符串有有内容（不为空白字串），如果没有内容，就抛出basicException异常
     * @param text 断言参数
     * @param basicException 自定义异常
     */
    public static void hasText(@Nullable String text, BasicException basicException) {
        if (!org.springframework.util.StringUtils.hasText(text)) {
            throw basicException;
        }
    }

    /**
     * 断言字符串有有内容（不为空白字串），如果没有内容，就抛出basicException异常
     * @param text 断言参数
     * @param exceptionEnum 自定义异常
     */
    public static void hasText(@Nullable String text, ExceptionEnumInterface exceptionEnum) {
        if (!org.springframework.util.StringUtils.hasText(text)) {
            throwBasicExceptionByEnum(exceptionEnum);
        }
    }

    /**
     * 断言 textToSearch 不包含子串 substring，若包含，就抛出basicException异常
     * @param textToSearch 字串
     * @param substring 字串子串
     * @param basicException 自定义异常
     */
    public static void doesNotContain(@Nullable String textToSearch, String substring, BasicException basicException) {
        if (org.springframework.util.StringUtils.hasLength(textToSearch) && org.springframework.util.StringUtils.hasLength(substring) &&
                textToSearch.contains(substring)) {
            throw basicException;
        }
    }

    /**
     * 断言 textToSearch 不包含子串 substring，若包含，就抛出basicException异常
     * @param textToSearch 字串
     * @param substring 字串子串
     * @param exceptionEnum 自定义异常
     */
    public static void doesNotContain(@Nullable String textToSearch, String substring, ExceptionEnumInterface exceptionEnum) {
        if (org.springframework.util.StringUtils.hasLength(textToSearch) && org.springframework.util.StringUtils.hasLength(substring) &&
                textToSearch.contains(substring)) {
            throwBasicExceptionByEnum(exceptionEnum);
        }
    }

    /**
     * 断言 数组array不为空，若数组为空，就抛出basicException异常
     * @param array 数组
     * @param basicException 自定义异常
     */
    public static void notEmpty(@Nullable Object[] array, BasicException basicException) {
        if (ObjectUtils.isEmpty(array)) {
            throw basicException;
        }
    }

    /**
     * 断言 数组array不为空，若数组为空，就抛出basicException异常
     * @param array 数组
     * @param exceptionEnum 自定义异常
     */
    public static void notEmpty(@Nullable Object[] array, ExceptionEnumInterface exceptionEnum) {
        if (ObjectUtils.isEmpty(array)) {
            throwBasicExceptionByEnum(exceptionEnum);
        }
    }

    /**
     * 断言数组array不含值等于null的子元素，若有值是null的子元素，就抛出basicException异常
     * @param array 数组
     * @param basicException 自定义异常
     */
    public static void noNullElements(@Nullable Object[] array, BasicException basicException) {
        if (array != null) {
            for (Object element : array) {
                if (element == null) {
                    throw basicException;
                }
            }
        }
    }

    /**
     * 断言数组array不含值等于null的子元素，若有值是null的子元素，就抛出basicException异常
     * @param array 数组
     * @param exceptionEnum 自定义异常
     */
    public static void noNullElements(@Nullable Object[] array, ExceptionEnumInterface exceptionEnum) {
        if (array != null) {
            for (Object element : array) {
                if (element == null) {
                    throwBasicExceptionByEnum(exceptionEnum);
                }
            }
        }
    }

    /**
     * 断言集合collection对象不为空集合，若集合是空集合（没有成员），就抛出basicException异常
     * @param collection 集合
     * @param basicException 自定义异常
     */
    public static void notEmpty(@Nullable Collection<?> collection, BasicException basicException) {
        if (CollectionUtils.isEmpty(collection)) {
            throw basicException;
        }
    }

    /**
     * 断言集合collection对象不为空集合，若集合是空集合（没有成员），就抛出basicException异常
     * @param collection 集合
     * @param exceptionEnum 自定义异常
     */
    public static void notEmpty(@Nullable Collection<?> collection, ExceptionEnumInterface exceptionEnum) {
        if (CollectionUtils.isEmpty(collection)) {
            throwBasicExceptionByEnum(exceptionEnum);
        }
    }

    /**
     * 断言集合collection不为null，且元素不存在null，若集合元素存在null，就抛出basicException异常
     * @param collection 集合
     * @param basicException 自定义异常
     */
    public static void noNullElements(@Nullable Collection<?> collection, BasicException basicException) {
        if (collection != null) {
            for (Object element : collection) {
                if (element == null) {
                    throw basicException;
                }
            }
        }
    }

    /**
     * 断言集合collection不为null，且元素不存在null，若集合元素存在null，就抛出basicException异常
     * @param collection 集合
     * @param exceptionEnum 自定义异常
     */
    public static void noNullElements(@Nullable Collection<?> collection, ExceptionEnumInterface exceptionEnum) {
        if (collection != null) {
            for (Object element : collection) {
                if (element == null) {
                    throwBasicExceptionByEnum(exceptionEnum);
                }
            }
        }
    }

    /**
     * 断言集合map不为空，若为空，就抛出basicException异常
     * @param map 集合
     * @param basicException 自定义异常
     */
    public static void notEmpty(@Nullable Map<?, ?> map, BasicException basicException) {
        if (CollectionUtils.isEmpty(map)) {
            throw basicException;
        }
    }

    /**
     * 断言集合map不为空，若为空，就抛出basicException异常
     * @param map 集合
     * @param exceptionEnum 自定义异常
     */
    public static void notEmpty(@Nullable Map<?, ?> map, ExceptionEnumInterface exceptionEnum) {
        if (CollectionUtils.isEmpty(map)) {
            throwBasicExceptionByEnum(exceptionEnum);
        }
    }
}
