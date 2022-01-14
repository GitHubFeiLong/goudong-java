package com.goudong.commons.exception.core;

import lombok.Getter;

/**
 * 类描述：
 * 应用启动失败异常
 * @author msi
 * @version 1.0
 * @date 2022/1/14 22:02
 */
@Getter
public class ApplicationBootFailedException extends RuntimeException{
    /**
     * 描述
     */
    private String description;

    /**
     * 行动
     */
    private String action;

    public ApplicationBootFailedException(String message, String description, String action) {
        super(message);
        this.description = description;
        this.action = action;
    }
}