package com.goudong.commons.utils;

import org.slf4j.Logger;

/**
 * 类描述：
 * 打印日志，根据门面的开关进行判断打印
 * @Author e-Feilong.Chen
 * @Date 2021/11/18 9:44
 */
public class LogUtil {

    /**
     * 判断是否打开debug，打印日志
     * @param log 日志对象
     * @param logTemplate 模板
     * @param param 参数
     */
    public static void debug(Logger log, String logTemplate, Object... param){
        if (log.isDebugEnabled()) {
            log.debug(logTemplate, param);
        }
    }

    /**
     * 判断是否打开info，打印日志
     * @param log 日志对象
     * @param logTemplate 模板
     * @param param 参数
     */
    public static void info(Logger log, String logTemplate, Object... param){
        if (log.isInfoEnabled()) {
            log.info(logTemplate, param);
        }
    }

    /**
     * 判断是否打开warn，打印日志
     * @param log 日志对象
     * @param logTemplate 模板
     * @param param 参数
     */
    public static void warn(Logger log, String logTemplate, Object... param){
        if (log.isErrorEnabled()) {
            log.warn(logTemplate, param);
        }
    }

    /**
     * 判断是否打开error，打印日志
     * @param log 日志对象
     * @param logTemplate 模板
     * @param param 参数
     */
    public static void error(Logger log, String logTemplate, Object... param){
        if (log.isErrorEnabled()) {
            log.error(logTemplate, param);
        }
    }

}
