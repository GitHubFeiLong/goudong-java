package com.zhy.authentication.server.enums;

/**
 * 类描述：
 * 登录成功类型
 * @ClassName LoginTypeEnum
 * @Author Administrator
 * @Date 2023/7/30 9:49
 * @Version 1.0
 */
public enum LoginSuccessTypeEnum {
    // 超级管理员 登录 本后台 （1、在本后台选择本应用登录；2、在本后台不选择应用登录）
    // 管理员 登录 本后台 （1、在本后台选择本应用登录；2、在本后台不选择应用登录）
    ADMIN_NATIVE_APP,
    // 超级管理员 登录 其它应用后台 （1、在本后台选择其它应用登录；2、在指定应用后台进行登录）
    SUPER_ADMIN_OTHER_APP,

    // 管理员 登录 自己应用后台 （1、在本后台选择应用登录；2、在自己应用所在后台应用登录）
    ADMIN_OTHER_APP,
    // 普通用户 登录 其它应用后台（1、在本后台选择应用登录；2、在自己应用所在后台应用登录）
    USER_OTHER_APP,
}
