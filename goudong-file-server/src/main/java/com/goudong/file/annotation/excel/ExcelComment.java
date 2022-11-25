package com.goudong.file.annotation.excel;

import java.lang.annotation.*;

/**
 * 类描述：
 * Excel的批注注解
 * @author cfl
 * @date 2022/11/22 18:16
 * @version 1.0
 */
@Documented
@Target({ElementType.FIELD}) // 可以放在字段上
@Retention(RetentionPolicy.RUNTIME) //该注解不仅被保存到class文件中，jvm加载class文件之后，仍然存在；
public @interface ExcelComment {

    String value() default "";
}
