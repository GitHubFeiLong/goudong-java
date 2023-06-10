package com.goudong.oauth2.enumerate;

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

    //~ 用户表 base_app
    //==================================================================================================================
    UK_APP_NAME_BASE_APP("uk_app_name", "应用已存在"),

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
