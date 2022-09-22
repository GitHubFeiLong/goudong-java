package com.goudong.commons.framework.core;

import io.swagger.annotations.ApiModelProperty;

/**
 * 类描述：
 * 扩展接口的返回数据
 * @author msi
 * @version 1.0
 * @date 2022/9/22 14:55
 */
public class DataMap {

    //~fields
    //==================================================================================================================
    /**
     *
     */
    @ApiModelProperty(value = "不处理错误信息", notes = "接口返回异常时，前端根据该字段进行判断是否处理错误信息，比如弹窗提示信息")
    private Boolean doNotHandleErrorMessage;

    //~methods
    //==================================================================================================================

}