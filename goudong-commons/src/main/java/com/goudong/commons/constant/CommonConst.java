package com.goudong.commons.constant;

/**
 * 类描述：
 * 字符串常量
 * @Author e-Feilong.Chen
 * @Date 2021/8/13 17:33
 */
public class CommonConst {
    /**
     * *
     */
    public static final String ASTERISK = "*";

    /**
     * 正斜杠
     */
    public static final String FORWARD_SLASH = "/";

    /**
     * 空字符串
     */
    public static final String NULL_STRING = "";

    /**
     * API接口前缀固定（防止使用路径传参骗过服务器白名单）
     */
    public static final String API_PREFIX = "/api/*";

    /**
     * 增强swagger的文档地址模式pattern
     */
    public static final String KNIFE4J_DOC_PATTERN = "/api/*/doc.html";

    /**
     * 所有字节码文件的路径 antPath模式
     */
    public static final String ALL_CLASSES_ANT_PATH = "/**/*.class";

    /**
     * 超级管理员角色
     */
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    /**
     * 普通用户角色
     */
    public static final String ROLE_ORDINARY = "ROLE_ORDINARY";

}
