package com.goudong.core.util;

import com.goudong.core.enumerate.MessageFormatEnum;

/**
 * 类描述：
 * 格式化信息
 * @author msi
 * @version 1.0
 * @date 2022/9/22 16:32
 */
public final class MessageFormatUtil {

    //~fields
    //==================================================================================================================

    //~methods
    //==================================================================================================================

    /**
     * 默认转换
     * @see MessageFormatEnum#LOG_FORMAT
     * @param messagePattern
     * @param args
     * @return
     */
    public static String format(String messagePattern, Object... args) {
        return format(MessageFormatEnum.LOG_FORMAT, messagePattern, args);
    }

    /**
     * 消息转换
     * @param formatterEnum
     * @param messagePattern
     * @param args
     * @return
     */
    public static String format(MessageFormatEnum formatterEnum, String messagePattern, Object... args) {
        if (formatterEnum == null) {
            throw new IllegalArgumentException();
        }
        if (args != null && args.length > 0 && StringUtil.isNotBlank(messagePattern)) {
            for (int i = 0; i < args.length; i++) {
                messagePattern = messagePattern.replaceFirst(formatterEnum.getFormatRegex(), String.valueOf(args[i]));
            }

            return messagePattern;
        }

        return messagePattern;
    }

    /**
     * 消息转换
     * @param regex 指定正则
     * @param messagePattern
     * @param args
     * @return
     */
    public static String formatByRegex(String regex, String messagePattern, Object... args) {
        if (regex == null) {
            throw new IllegalArgumentException();
        }

        if (args != null && args.length > 0 && StringUtil.isNotBlank(messagePattern)) {
            for (int i = 0; i < args.length; i++) {
                messagePattern = messagePattern.replaceFirst(regex, String.valueOf(args[i]));
            }

            return messagePattern;
        }

        return messagePattern;
    }

}
