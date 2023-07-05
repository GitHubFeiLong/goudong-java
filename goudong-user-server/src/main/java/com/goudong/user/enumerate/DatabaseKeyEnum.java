package com.goudong.user.enumerate;

import com.goudong.boot.web.bean.DatabaseKeyInterface;
import com.goudong.core.util.StringUtil;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 类描述：
 * 数据库中的索引名枚举，异常时根据名字判断，全局抛出异常
 * 注意：枚举名一定是数据库索引名大写，数据库索引名修改，这里需要同步修改
 * @Author e-Feilong.Chen
 * @Date 2021/9/24 13:51
 */
@Getter
public enum DatabaseKeyEnum implements DatabaseKeyInterface {

    //~ 用户表 base_user
    //==================================================================================================================
    /**
     * 用户表 邮箱唯一索引
     */
    UK_BASE_USER_EMAIL("uk_base_user_email", "保存用户失败，邮箱已存在"),
    /**
     * 用户表 用户名唯一索引
     */
    UK_BASE_USER_USERNAME("uk_base_user_username", "保存用户失败，用户名已存在"),
    /**
     * 用户表手机唯一索引
     */
    UK_BASE_USER_PHONE("uk_base_user_phone", "保存用户失败，手机号已存在"),

    //~ 角色表 base_role
    //==================================================================================================================
    UK_ROLE_ROLE_NAME("uk_role_role_name", "保存失败，角色已存在"),

    /**
     * 用户角色表用户id和角色id唯一索引
     */
    UK_BASE_USER_ROLE__BASE_USER_ID_BASE_ROLE_ID("uk_base_user_role__base_user_id_base_role_id", "保存用户角色失败，用户已拥有该角色"),
    /**
     * 角色菜单表角色id和菜单id唯一索引
     */
    UK_BASE_ROLE_MENU__BASE_ROLE_ID_BASE_MENU_ID("uk_base_role_menu__base_role_id_base_menu_id", "保存角色菜单失败，角色已拥有菜单"),

    /**
     * api接口资源表的唯一索引
     */
    UK_BASE_API_RESOURCE_PATTERN_METHOD_APPLICATION_NAME("uk_base_api_resource_pattern_method_application_name", "保存api_resource失败，数据已存在"),

    //~ 菜单表 base_menu
    //==================================================================================================================
    UK_BASE_MENU_PERMISSION_ID("uk_base_menu_permission_id", "权限标识已存在"),
    ;

    public static final Logger log = LoggerFactory.getLogger(DatabaseKeyEnum.class);

    /**
     * 索引
     */
    private String key;

    /**
     * 错误时，用户提示信息
     */
    private String clientMessage;

    DatabaseKeyEnum(String key, String clientMessage) {
        this.key = key;
        this.clientMessage = clientMessage;
    }

    /**
     * 根据数据库中的{@code key}获取对应的提示信息
     *
     * @param key 数据库索引名
     * @return
     */
    @Override
    public String getClientMessage(String key) {
        if (StringUtil.isBlank(key)) {
            return null;
        }
        DatabaseKeyEnum[] values = DatabaseKeyEnum.values();
        for (int i = 0; i < values.length; i++) {
            if (key.equalsIgnoreCase(values[i].getKey())) {
                return values[i].clientMessage;
            }
        }
        // 如果系统报错是数据库约束（非空、唯一等），但是没有在该枚举中进行配置，那么就直接返回null对象，使用数据库的提示
        log.error("请新增成员变量：{}",  key.toUpperCase());
        return null;
    }
}
