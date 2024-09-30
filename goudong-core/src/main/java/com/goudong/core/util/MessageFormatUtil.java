package com.goudong.core.util;


import com.goudong.core.lang.RegexConst;

/**
 * 类描述：
 * 格式化信息
 * @author msi
 */
public final class MessageFormatUtil {

    //~fields
    //==================================================================================================================
    public static final String REPLACE_STRING = "--CUSTOMER-STRING--";
    //~methods
    //==================================================================================================================

    /**
     * 默认转换
     * @see MessageFormatEnum#LOG_FORMAT
     * @param messagePattern    pattern
     * @param args  pattern参数
     * @return  格式化后字符串
     */
    public static String format(String messagePattern, Object... args) {
        return format(MessageFormatEnum.LOG_FORMAT, messagePattern, args);
    }

    /**
     * 消息转换
     * @param formatterEnum    格式化枚举
     * @param messagePattern   消息模板
     * @param args             模板参数
     * @return  转换后字符串
     */
    public static String format(MessageFormatEnum formatterEnum, String messagePattern, Object... args) {
        if (formatterEnum == null) {
            throw new IllegalArgumentException();
        }
        if (args != null && args.length > 0 && StringUtil.isNotBlank(messagePattern)) {
            for (Object arg : args) {
                /*
                   防止 替换的字符串中有$导致方法执行报错 {@code java.lang.IllegalArgumentException: Illegal group reference}
                    先将$替换成一个固定字符串，最后再将结果替换回来
                 */
                String after = String.valueOf(arg).replaceAll("\\$", REPLACE_STRING);
                messagePattern = messagePattern.replaceFirst(formatterEnum.getFormatRegex(), after);
            }

            return messagePattern.replaceAll(REPLACE_STRING, "\\$");
        }

        return messagePattern;
    }

    /**
     * 枚举描述：
     * 信息格式化的枚举
     * @see MessageFormatUtil
     * @author cfl
     */
    public enum MessageFormatEnum {

        //~fields
        //==================================================================================================================
        /**
         * 日志格式,模板格式：{@code {}}
         */
        LOG_FORMAT(RegexConst.PLACEHOLDER_1),

        /**
         * 占位符，模板格式：{@code ${}}
         */
        PLACEHOLDER_FORMAT(RegexConst.PLACEHOLDER_2),
        ;

        //~methods
        //==================================================================================================================
        private final String formatRegex;

        MessageFormatEnum(String formatRegex) {
            this.formatRegex = formatRegex;
        }

        //~getter
        //==================================================================================================================
        public String getFormatRegex() {
            return formatRegex;
        }
    }
}
