package com.goudong.commons.utils;

import com.goudong.commons.exception.ServerException;

/**
 * 类描述：
 * 自定义字符串工具类
 * @Author msi
 * @Date 2021-08-15 14:38
 * @Version 1.0
 */
public class StringUtil {

    public static final String LEFT_BRACKET = "{";
    public static final String RIGHT_BRACKET = "}";
    public static final String ASTERISK = "*";

    /**
     * 将字符串中的括号替换成指定字符串
     * @param rawStr
     * @param replaceStr
     * @return
     */
    public static String replaceBracket (String rawStr, String replaceStr) {
        AssertUtil.notNull(rawStr, "参数 rawStr 不能为空");
        AssertUtil.notNull(replaceStr, "参数 replaceStr 不能为空");
        StringBuilder result = new StringBuilder(rawStr);
        try {
            int indexStr = -1;
            while((indexStr = result.indexOf(LEFT_BRACKET)) != -1){
                int indexEnd = result.indexOf(RIGHT_BRACKET, indexStr);
                // 没有右括号时，直接返回值
                if (indexEnd == -1) {
                    return result.toString();
                }
                result.replace(indexStr, indexEnd + 1, replaceStr);
            }
            return result.toString();
        } catch (StringIndexOutOfBoundsException e) {
            throw ServerException.serverException("错误" + e.getMessage());
        }
    }

}
