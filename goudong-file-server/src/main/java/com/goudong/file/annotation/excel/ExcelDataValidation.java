package com.goudong.file.annotation.excel;

import com.goudong.file.service.IExcelDataValidation;

import java.lang.annotation.*;

/**
 * 类描述：
 * excel的下拉
 * @author cfl
 * @date 2022/11/23 10:44
 * @version 1.0
 */
@Documented
@Target(value = ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelDataValidation {
    /**
     * 下拉数据，静态
     * @return
     */
    String[] value() default {};

    /**
     * 下拉数据来源。
     * @see IExcelDataValidation#get()
     * @return
     */
    Class<? extends IExcelDataValidation> source() default IExcelDataValidation.class;
}
