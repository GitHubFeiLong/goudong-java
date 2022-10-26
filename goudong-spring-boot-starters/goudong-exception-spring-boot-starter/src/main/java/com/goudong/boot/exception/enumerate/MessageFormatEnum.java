package com.goudong.boot.exception.enumerate;

import com.goudong.boot.exception.util.MessageFormatUtil;

/**
 * 枚举描述：
 * 信息格式化的枚举
 * @see MessageFormatUtil
 * @author msi
 * @version 1.0
 * @date 2022/9/22 16:37
 */
public enum MessageFormatEnum {

    //~fields
    //==================================================================================================================
    /**
     * 日志格式,模板格式：{}
     */
    LOG_FORMAT("\\{[^\\}]*\\}"),

    /**
     * 占位符，模板格式：${}
     */
    PLACEHOLDER_FORMAT("\\$\\{[^\\}]*\\}"),


    ;

    //~methods
    //==================================================================================================================
    private String formatRegex;

    MessageFormatEnum(String formatRegex) {
        this.formatRegex = formatRegex;
    }

    //~getter
    //==================================================================================================================
    public String getFormatRegex() {
        return formatRegex;
    }
}
