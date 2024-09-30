package com.goudong.core.util;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 类描述：
 * 异常工具类
 * @author cfl
 */
public class ExceptionUtil {
    //~fields
    //==================================================================================================================

    //~methods
    //==================================================================================================================

    /**
     * 获取异常的堆栈信息
     * @see org.apache.commons.lang3.exception.ExceptionUtils#getStackTrace
     * @param throwable 异常对象
     * @return  堆栈信息
     */
    public static String getStackTrace(final Throwable throwable) {
        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw, true);
        throwable.printStackTrace(pw);
        return sw.getBuffer().toString();
    }
}
