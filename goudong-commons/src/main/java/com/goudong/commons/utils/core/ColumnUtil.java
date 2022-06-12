package com.goudong.commons.utils.core;

import com.goudong.commons.function.core.SFunction;
import lombok.Data;

import java.io.Serializable;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 类描述：
 * 列字段的工具类
 * 注意：
 * 不能用于boolean 类型的field；
 * 不能用于Boolean类型且field名字以is开头
 * @author msi
 * @date 2022/6/7 15:49
 * @version 1.0
 */
public class ColumnUtil {

    //默认配置
    static String defaultSplit = "";
    static Integer defaultToType = 0;

    /**
     * 获取Field
     * @param fn
     * @param <T>
     * @return
     */
    public static <T> Field getField(SFunction<T, ?> fn){
        SerializedLambda serializedLambda = getSerializedLambda(fn);

        // 从lambda信息取出method、field、class等
        String fieldName = serializedLambda.getImplMethodName().substring("get".length());
        fieldName = fieldName.replaceFirst(fieldName.charAt(0) + "", (fieldName.charAt(0) + "").toLowerCase());
        try {
            return Class.forName(serializedLambda.getImplClass().replace("/", ".")).getDeclaredField(fieldName);
        } catch (ClassNotFoundException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取实体类的字段名称(实体声明的字段名称)
     * @param fn
     * @param <T>
     * @return
     */
    public static <T> String getFieldName(SFunction<T, ?> fn) {
        return getFieldName(fn, defaultSplit);
    }

    /**
     * 获取实体类的字段名称
     * @param fn
     * @param split 分隔符
     * @param <T>
     * @return
     */
    public static <T> String getFieldName(SFunction<T, ?> fn, String split) {
        return getFieldName(fn, split, defaultToType);
    }

    /**
     * 获取实体类的字段名称
     * @param fn
     * @param split 分隔符，多个字母自定义分隔符
     * @param toType 转换方式，多个字母以大小写方式返回 0.不做转换 1.大写 2.小写
     * @param <T>
     * @return
     */
    public static <T> String getFieldName(SFunction<T, ?> fn, String split, Integer toType) {
        SerializedLambda serializedLambda = getSerializedLambda(fn);

        // 从lambda信息取出method、field、class等
        String fieldName = serializedLambda.getImplMethodName().substring("get".length());
        fieldName = fieldName.replaceFirst(fieldName.charAt(0) + "", (fieldName.charAt(0) + "").toLowerCase());
        Field field;
        try {
            field = Class.forName(serializedLambda.getImplClass().replace("/", ".")).getDeclaredField(fieldName);
        } catch (ClassNotFoundException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }

        // 从field取出字段名，可以根据实际情况调整
        TableField tableField = field.getAnnotation(TableField.class);
        if (tableField != null && tableField.value().length() > 0) {
            return tableField.value();
        } else {
            //0.不做转换 1.大写 2.小写
            switch (toType) {
                case 1:
                    return fieldName.replaceAll("[A-Z]", split + "$0").toUpperCase();
                case 2:
                    return fieldName.replaceAll("[A-Z]", split + "$0").toLowerCase();
                default:
                    return fieldName.replaceAll("[A-Z]", split + "$0");
            }

        }
    }

    private static <T> SerializedLambda getSerializedLambda(SFunction<T, ?> fn) {
        // 从function取出序列化方法
        Method writeReplaceMethod;
        try {
            writeReplaceMethod = fn.getClass().getDeclaredMethod("writeReplace");
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        // 从序列化方法取出序列化的lambda信息
        boolean isAccessible = writeReplaceMethod.isAccessible();
        writeReplaceMethod.setAccessible(true);
        SerializedLambda serializedLambda;
        try {
            serializedLambda = (SerializedLambda) writeReplaceMethod.invoke(fn);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        writeReplaceMethod.setAccessible(isAccessible);
        return serializedLambda;
    }

    private String fileName(SerializedLambda serializedLambda) {
        /*
            lombok和idea的，可能其它ide不一样(TODO 有机会试试eclipse和myEclipse等)。
            idea的getter/setter：
                boolean 类型
                    2.1 字段是`isXxx`,那么get方法是`isXxx()`，set方法是`setXxx()`；
                    2.1 字段是`xxx`,那么get方法是`isXxx()`，set方法是`setXxx()`；
                Boolean 类型
                    字段是`isXxx`,那么get方法是`getXxx()`，set方法是`setXxx()`；
                    字段是`xxx`,那么get方法是`getXxx()`，set方法是`setXxx()`；
                其它类型
                    字段是`isXxx`,那么get方法是`getIsXxx()`，set方法是`setIsXxx()`；
                    字段是`xxx`,那么get方法是`getXxx()`，set方法是`setXxx()`；
            Lombok的getter/setter:
                boolean 类型
                    字段是`isXxx`,那么get方法是`isXxx()`，set方法是`setXxx()`；
                    字段是`xxx`,那么get方法是`isXxx()`，set方法是`setXxx()`；
                Boolean 类型
                    字段是`isXxx`,那么get方法是`getIsXxx()`，set方法是`setIsXxx()`；
                    字段是`xxx`,那么get方法是`getXxx()`，set方法是`setXxx()`；
                其它类型
                    字段是`isXxx`,那么get方法是`getIsXxx()`，set方法是`setIsXxx()`；
                    字段是`xxx`,那么get方法是`getXxx()`，set方法是`setXxx()`；
         */
        // 从lambda信息取出method、field、class等
        String implMethodName = serializedLambda.getImplMethodName();
        // 方法签名
        String implMethodSignature = serializedLambda.getImplMethodSignature();

        if (implMethodName.startsWith("get")) {
            return implMethodName.substring(3);
        }

        throw new RuntimeException("方法错误");
    }
    /**
     * 参考示例
     * @param args
     */
    public static void main(String[] args) {
        //实体类原字段名称返回
        System.out.println("实体类原字段名称返回");
        System.out.println("字段名：" + ColumnUtil.getFieldName(TestUserDemo::getLoginName));
        System.out.println("字段名：" + ColumnUtil.getFieldName(TestUserDemo::getNickName));
        System.out.println("字段名：" + ColumnUtil.getFieldName(TestUserDemo::getCompanySimpleName));
        System.out.println();
        System.out.println("实体类字段名称增加分隔符");
        System.out.println("字段名：" + ColumnUtil.getFieldName(TestUserDemo::getCompanySimpleName, "_"));
        System.out.println();
        System.out.println("实体类字段名称增加分隔符 + 大小写");
        System.out.println("字段名：" + ColumnUtil.getFieldName(TestUserDemo::getCompanySimpleName, "_", 0));
        System.out.println("字段名：" + ColumnUtil.getFieldName(TestUserDemo::getCompanySimpleName, "_", 1));
        System.out.println("字段名：" + ColumnUtil.getFieldName(TestUserDemo::getCompanySimpleName, "_", 2));
    }

    /**
     * 类描述：
     * 字段名注解,声明表字段
     * @author msi
     * @date 2022/6/7 15:20
     * @version 1.0
     */
    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface TableField {
        String value() default "";
    }

    /**
     * 类描述：
     * 测试用户实体类
     * @author msi
     * @date 2022/6/7 15:20
     * @version 1.0
     */
    @Data
    public static class TestUserDemo implements Serializable {
        private int loginName;
        private String name;
        private String companySimpleName;
        @ColumnUtil.TableField("nick")
        private String nickName;
        private String isDeleted;
    }


}

