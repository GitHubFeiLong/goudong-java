package com.goudong.exception.util;

import com.alibaba.csp.sentinel.util.AssertUtil;
import com.goudong.exception.core.ServerException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 类描述：
 *
 * @author cfl
 * @version 1.0
 * @date 2022/10/23 20:48
 */
public class StringUtil {
    //~fields
    //==================================================================================================================

    //~methods
    //==================================================================================================================
    /**
     * 判断字符串是null或者是空串
     * @param str
     * @return
     */
    public static boolean isBlank(String str) {
        return str == null || str.trim().length() == 0;
    }

    /**
     * 字符串不是空串，不是null
     * @param str
     * @return
     */
    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }
}
