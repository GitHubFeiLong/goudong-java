package com.goudong.exception.core;

import java.text.SimpleDateFormat;
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
     * 日期时间格式 yyyy-MM-dd HH:mm:ss
     */
    public static final String DATE_TIME_FORMATTER = "yyyy-MM-dd HH:mm:ss";

    /**
     * 日期格式
     */
    public static final String DATE_FORMATTER = "yyyy-MM-dd";
    /**
     * 时间格式
     */
    public static final String TIME_FORMATTER = "HH:mm:ss";

    public static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat(DATE_TIME_FORMATTER);
    /**
     * 时间格式Formatter yyyy-MM-dd HH:mm:ss
     * @see DateConst#DATE_TIME_FORMATTER
     */
    public static final DateTimeFormatter YYYY_MM_DD_HH_MM_SS = DateTimeFormatter.ofPattern(DATE_TIME_FORMATTER);

    /**
     * 时间格式Formatter yyMMdd
     */
    public static final DateTimeFormatter YYMMDD = DateTimeFormatter.ofPattern("yyMMdd");
}
