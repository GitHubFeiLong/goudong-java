package com.goudong.commons.constant.core;

import java.time.format.DateTimeFormatter;

/**
 * 类描述：
 * 日期相关的常量
 * @author msi
 * @version 1.0
 * @date 2022/1/8 17:38
 */
public class DateConst {

    /**
     * 时间格式 yyyy-MM-dd HH:mm:ss
     */
    public static final String DATE_TIME_FORMATTER = "yyyy-MM-dd HH:mm:ss";

    /**
     * 时间格式Formatter yyyy-MM-dd HH:mm:ss
     * @see DateConst#DATE_TIME_FORMATTER
     */
    public static final DateTimeFormatter YYYY_MM_DD_HH_MM_SS = DateTimeFormatter.ofPattern(DATE_TIME_FORMATTER);
}
