package com.goudong.core.lang;

import java.util.Map;


/**
 * 类描述：
 * 自定义异常的基类，其它模块的异常继承进行扩展
 * @author cfl
 * @date 2022/11/1 16:20
 * @version 1.0
 */
public interface BasicExceptionInterface {

    /**
     * http 响应码
     */
    int getStatus();

    /**
     * 错误代码
     */
    String getCode();

    /**
     * 客户端状态码对应信息
     */
    String getClientMessage();

    /**
     * 服务器状态码对应信息
     */
    String getServerMessage();

    /**
     * 额外信息
     * @return
     */
    Map getDataMap();

}
