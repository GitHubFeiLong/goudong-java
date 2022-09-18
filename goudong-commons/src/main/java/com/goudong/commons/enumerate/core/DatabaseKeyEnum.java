package com.goudong.commons.enumerate.core;

import com.goudong.commons.utils.core.LogUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * 类描述：
 * 数据库中的索引名枚举，异常时根据名字判断，全局抛出异常
 * 注意：枚举名一定是数据库索引名大写，数据库索引名修改，这里需要同步修改
 * @Author e-Feilong.Chen
 * @Date 2021/9/24 13:51
 */
@Slf4j
@Getter
public enum DatabaseKeyEnum {
    //~ 用户表 base_user
    //==================================================================================================================
    /**
     * 用户表 邮箱唯一索引
     */
    UQ_BASE_USER_EMAIL("保存用户失败，邮箱已存在"),
    /**
     * 用户表 用户名唯一索引
     */
    UQ_BASE_USER_USERNAME("保存用户失败，用户名已存在"),
    /**
     * 用户表手机唯一索引
     */
    UQ_BASE_USER_PHONE("保存用户失败，手机号已存在"),

    //~ 角色表 base_role
    //==================================================================================================================
    UQ_ROLE_ROLE_NAME("保存失败，角色已存在"),

    /**
     * 用户角色表用户id和角色id唯一索引
     */
    UK_BASE_USER_ROLE__BASE_USER_ID_BASE_ROLE_ID("保存用户角色失败，用户已拥有该角色"),
    /**
     * 角色菜单表角色id和菜单id唯一索引
     */
    UK_BASE_ROLE_MENU__BASE_ROLE_ID_BASE_MENU_ID("保存角色菜单失败，角色已拥有菜单"),

    /**
     * api接口资源表的唯一索引
     */
    UQ_BASE_API_RESOURCE_PATTERN_METHOD_APPLICATION_NAME("保存api_resource失败，数据已存在"),

    //~ 菜单表 base_menu
    //==================================================================================================================
    UK_BASE_MENU_PATH_METHOD("请勿重复添加相同菜单"),
    ;

    /**
     * 错误时，用户提示信息
     */
    private String clientMessage;

    DatabaseKeyEnum(String clientMessage) {
        this.clientMessage = clientMessage;
    }

    /**
     * 根据数据库报错的 key找到提示对象
     * @param keyName 索引名(唯一索引名)
     * @return
     */
    public static String getClientMessage (@NotNull String keyName) {
        if (StringUtils.isBlank(keyName)) {
            return null;
        }
        DatabaseKeyEnum[] values = DatabaseKeyEnum.values();
        for (int i = 0; i < values.length; i++) {
            if (Objects.equals(keyName, values[i].name().toLowerCase())) {
                return values[i].clientMessage;
            }
        }
        // 如果系统报错是数据库约束（非空、唯一等），但是没有在该枚举中进行配置，那么就直接返回null对象，使用数据库的提示
        LogUtil.error(log, "请新增成员变量：{}", keyName.toUpperCase());
        return null;
    }
}
