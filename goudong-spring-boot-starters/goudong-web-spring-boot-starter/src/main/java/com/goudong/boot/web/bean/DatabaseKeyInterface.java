package com.goudong.boot.web.bean;

/**
 * 接口描述：
 * 数据库索引约束返回提示信息
 * @author cfl
 * @date 2022/11/2 15:19
 * @version 1.0
 */
public interface DatabaseKeyInterface {

    /**
     * 数据库中的索引
     *
     * @return
     */
    String getKey();

    /**
     * 根据数据库中的{@code key}获取对应的提示信息
     *
     * @param key 数据库索引名
     * @return
     */
    String getClientMessage(String key);
}
