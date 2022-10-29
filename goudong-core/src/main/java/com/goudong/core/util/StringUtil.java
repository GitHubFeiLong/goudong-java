package com.goudong.core.util;

import java.util.List;

/**
 * 类描述：
 * String工具类
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

    /**
     * 切割字符串，以700+-的最近一个'\n'结束
     * TODO
     * @param content
     * @param startIndex
     * @param result
     */
    void split(String content, int startIndex, List<String> result) {
        // 表明已到截取完成
        if (startIndex >= content.length() - 1) {
            return;
        }
        int endIndex = content.indexOf("\n", startIndex + 700);

        String sub;
        if (endIndex == -1) {
            sub = content.substring(startIndex);
            result.add(sub);
            return;
        }

        sub = content.substring(startIndex, endIndex);
        result.add(sub);
        // 左闭右开，换行符不需要
        split(content, endIndex + 1, result);
    }
}
