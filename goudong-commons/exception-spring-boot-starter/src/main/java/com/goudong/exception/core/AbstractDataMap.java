package com.goudong.exception.core;

/**
 * 类描述：
 * 扩展接口的返回数据,可以继承该类进行扩充
 * @author msi
 * @version 1.0
 * @date 2022/9/22 14:55
 */
public class AbstractDataMap {

    //~fields
    //==================================================================================================================
    /**
     * 不处理错误信息
     * 接口返回异常时，前端根据该字段进行判断是否处理错误信息，比如弹窗提示信息
     */
    private Boolean doNotHandleErrorMessage;

    //~methods
    //==================================================================================================================

}