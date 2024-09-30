package com.goudong.boot.web.bean;

/**
 * 接口描述：
 * 数据库索引约束返回提示信息
 * @author cfl
 */
public interface DatabaseKeyInterface {

    /**
     * 数据库中的索引
     *
     * @return 索引名称
     */
    String getKey();

    /**
     * 根据数据库中的{@code key}获取对应的提示信息
     *
     * @param key 数据库索引名
     * @return  客户端提示信息
     */
    String getClientMessage(String key);
}
