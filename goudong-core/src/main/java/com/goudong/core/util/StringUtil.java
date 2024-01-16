package com.goudong.core.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

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
    /**
     * 定义SpringBuilder的初始容量
     * @since 1.0
     */
    public static final int STRING_BUILDER_SIZE = 256;

    /**
     * The empty String {@code ""}.
     * @since 1.0
     */
    public static final String EMPTY = "";


    //~methods
    //==================================================================================================================
    /**
     * 判断字符串是null或者是空串
     * @param str
     * @return
     */
    public static boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
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
     * 将{@code str}中满足{@code regex}的字符串替换成{@code args[i] // i 递增}
     * @param str 需要格式化的字符串
     * @param regex 正则表达式
     * @param args 需要{@code regex}的值
     * @return
     */
    public static String formatByRegex(String str, String regex, Object... args) {
        AssertUtil.isNotNull(regex, () -> new IllegalArgumentException("regex不能为null"));

        if (args != null && args.length > 0 && StringUtil.isNotBlank(str)) {
            for (int i = 0; i < args.length; i++) {
                str = str.replaceFirst(regex, String.valueOf(args[i]));
            }

            return str;
        }

        return str;
    }

    /**
     * 将{@code content} 从0开始，指定步长{@code step}查找下次{@code split}进行切割，最后将结果放在@{code result}中
     * @param split 切割的字符
     * @param content 被切割的内容
     * @param step 步长，
     * @return result
     */
    public static List<String> split(char split, String content, int step) {
        if (content == null || content.length() == 0 || step <= 0) {
            return new ArrayList<>(0);
        }
        List<String> result = new ArrayList<>();
        split(split, content, 0, step, result);
        return result;
    }
    /**
     * 将{@code content} 从{@code startIndex}开始，指定步长{@code step}查找下次{@code split}进行切割，将每次分割后的结果放在@{code result}中
     * @param split 切割字符
     * @param content 文本内容
     * @param startIndex 开始的下标 0
     * @param step 下一次
     * @param result 切割后的结果集
     */
    private static void split(char split, String content, int startIndex, int step , List<String> result) {
        // 表明已到截取完成
        if (startIndex >= content.length() - 1) {
            return;
        }
        int endIndex = content.indexOf(split, startIndex + step);

        String sub;

        // 切割到最后了，直接返回
        if (endIndex == -1) {
            sub = content.substring(startIndex);
            result.add(sub);
            return;
        }

        sub = content.substring(startIndex, endIndex);
        result.add(sub);
        // 左闭右开，换行符不需要
        split(split, content, endIndex + 1, step, result);
    }

    /**
     * 切割字符串，每个子串最大都只有 step字符
     * @param content
     * @param step
     * @return
     */
    public static List<String> split(String content, int step) {
        if (content == null || content.length() == 0 || step <= 0) {
            return new ArrayList<>(0);
        }
        int length = content.length();
        int start = 0, end = 0, maxEndIndex = length - 1;

        List<String> result = new ArrayList<>(length / step + 1);
        while (end < maxEndIndex) {
            // 加上步长
            end += step;
            end = end > maxEndIndex ? maxEndIndex : end;
            String substring;
            if(end > maxEndIndex) {
                // 截取到末尾
                substring = content.substring(start);
            } else {
                substring = content.substring(start, end);
            }

            result.add(substring);

            start = end;
        }

        return result;
    }

    /**
     * 将{@code iterable}使用分隔符{@code separator}进行拼接
     * @param iterable
     * @param separator
     * @return
     */
    public static String join(Iterable iterable, final String separator) {
        return join(iterable.iterator(), separator);
    }
    /**
     * 将{@code iterator}使用分隔符{@code separator}进行拼接
     * @param iterator
     * @param separator
     * @return
     */
    public static String join(Iterator iterator, final String separator) {
        // handle null, zero and one elements before building a buffer
        if (iterator == null) {
            return null;
        }
        if (!iterator.hasNext()) {
            return EMPTY;
        }
        final Object first = iterator.next();
        if (!iterator.hasNext()) {
            return Objects.toString(first, "");
        }

        // two or more elements
        final StringBuilder buf = new StringBuilder(STRING_BUILDER_SIZE); // Java default is 16, probably too small
        if (first != null) {
            buf.append(first);
        }

        while (iterator.hasNext()) {
            if (separator != null) {
                buf.append(separator);
            }
            final Object obj = iterator.next();
            if (obj != null) {
                buf.append(obj);
            }
        }
        return buf.toString();
    }
}
