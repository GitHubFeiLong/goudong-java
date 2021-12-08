package com.goudong.commons.utils;

import com.goudong.commons.exception.ServerException;
import lombok.extern.slf4j.Slf4j;

/**
 * 类描述：
 * 自定义字符串工具类
 * @Author msi
 * @Date 2021-08-15 14:38
 * @Version 1.0
 */
@Slf4j
public class StringUtil {

    private static final String LEFT_BRACKET = "{";
    private static final String RIGHT_BRACKET = "}";
    private static final String ASTERISK = "*";

    /**
     * 替换路径参数 `{xxxx}`为`*`
     * @param uri 请求地址
     * @return
     */
    public static String replacePathVariable2Asterisk (String uri){
        AssertUtil.notNull(uri, "参数 uri 不能为空");
        StringBuilder result = new StringBuilder(uri);
        try {
            int indexStr = -1;
            while((indexStr = result.indexOf(LEFT_BRACKET)) != -1){
                int indexEnd = result.indexOf(RIGHT_BRACKET, indexStr);
                // 没有右括号时，直接返回值
                if (indexEnd == -1) {
                    return result.toString();
                }
                result.replace(indexStr, indexEnd + 1, ASTERISK);
            }
            return result.toString();
        } catch (StringIndexOutOfBoundsException e) {
            throw ServerException.serverException("错误" + e.getMessage());
        }
    }

    /**
     * 将模板字符串格式化,类似slf4j的日志打印，避免字符串的拼接
     * @param template 模板字符串中的{} 替换成后面的params值
     * @param params 替换模板字符串中的{}，顺序替换
     * @return
     */
    public static String format(String template, Object...params) {
        return format(StringUtil.LEFT_BRACKET, StringUtil.RIGHT_BRACKET, template, params);
    }

    /**
     * 格式化模板字符串，该方法是贪心方法（尽可能替换所有满足字符）
     * @param leftCharacter 开始字符
     * @param rightCharacter 结束字符
     * @param template 模板字符串
     * @param params 参数数组
     * @return
     */
    private static String format(String leftCharacter, String rightCharacter, String template, Object...params) {
        StringBuilder result = new StringBuilder(template);
        // 当不传params参数时，长度为0的数据
        int length = params.length;
        // params数组下标
        int paramsIndex = 0;
        try {
            int indexStr = -1;
            while((indexStr = result.indexOf(leftCharacter)) != -1 && paramsIndex < length){
                int indexEnd = result.indexOf(rightCharacter, indexStr);
                // 存在右侧结束符，就替换成 %s
                if (indexEnd != -1) {
                    result.replace(indexStr, indexEnd + 1, "%s");
                    // 下标增
                    paramsIndex++;
                }

            }
            // 格式化返回字符串结果
            return String.format(String.valueOf(result), params);
        } catch (StringIndexOutOfBoundsException e) {
            throw ServerException.serverException("错误" + e.getMessage());
        }
    }

    /**
     * 将字符串填充至指定长度
     * @param string 指定字符串
     * @param totalLength 填充至指定长度
     * @return 指定长度的字符串，使用空白字符填充
     */
    public static String fillSpace(String string, int totalLength) {
        if (totalLength < 1) {
            LogUtil.warn(log, "参数 totalLength:{} 不能小于 1.", string, totalLength);
            return string;
        }
        int length = string.length();
        if (length >= totalLength) {
            LogUtil.warn(log, "字符串:{},长度不能大于需要定义的长度 {}", string, totalLength);
            return string;
        }

        StringBuilder space = new StringBuilder();
        for (int i = 0, size = totalLength - length; i < size; i++) {
            space.append(" ");
        }

        return string + space;
    }

    public static void main(String[] args) {
        String 陈飞龙 = format("{", "}", "我是{}，今年：{}岁了,参数{{}，{}}", "陈飞龙{}{}", 18,11);
        System.out.println("陈飞龙 = " + 陈飞龙);
        String a1 = String.format("%s,%s", 1, "陈", 123123);
        System.out.println(a1);
    }
}
