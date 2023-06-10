package com.goudong.boot.web.bean;

import java.util.Map;

/**
 * 类描述：
 * 数据库索引，数据库索引全局异常时，会根据该Bean获取提示信息
 * @author cfl
 * @version 1.0
 * @date 2022/11/2 16:22
 */
public class DatabaseKey {

    private Map<String, String> databaseKeys;
    public DatabaseKey(Map<String, String> databaseKeys) {
        this.databaseKeys = databaseKeys;
    }

    /**
     * 根据数据库中的{@code key}获取对应的提示信息
     *
     * @param key 数据库索引名
     * @return
     */
    public String getClientMessage(String key) {
        return databaseKeys.get(key);
    }
}
