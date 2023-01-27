package com.goudong.boot.web.util;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 类描述：
 * 异常工具类
 * @author cfl
 * @version 1.0
 * @date 2023/1/14 21:18
 */
public class ExceptionUtil {
    //~fields
    //==================================================================================================================

    //~methods
    //==================================================================================================================

    /**
     * 获取异常的堆栈信息
     * @see org.apache.commons.lang3.exception.ExceptionUtils#getStackTrace
     * @param throwable
     * @return
     */
    public static String getStackTrace(final Throwable throwable) {
        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw, true);
        throwable.printStackTrace(pw);
        return sw.getBuffer().toString();
    }
}
