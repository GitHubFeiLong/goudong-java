package com.goudong.authentication.server.constant;

/**
 * 类描述：
 * 角色相关的常量类
 * @author msi
 * @version 1.0
 * @date 2022/1/8 11:22
 */
public class RoleConst {

    //~ 服务端预置角色
    //==================================================================================================================
    /**
     * 超级管理员角色
     */
    public static final String ROLE_APP_SUPER_ADMIN = "ROLE_APP_SUPER_ADMIN";

    /**
     * 创建应用时，创建一个应用管理员在server端
     */
    public static final String ROLE_APP_ADMIN = "ROLE_APP_ADMIN";


    //~ 客户端预置角色
    //==================================================================================================================
    /**
     * 匿名角色
     * 注意，创建角色时不能再创建该同名角色。
     */
    public static final String ROLE_ANONYMOUS = "ROLE_ANONYMOUS";
}
