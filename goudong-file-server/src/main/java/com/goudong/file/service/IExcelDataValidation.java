package com.goudong.file.service;

/**
 * 接口描述：
 * 获取excel的数据验证下拉数据的接口
 * @author Administrator
 * @version 1.0
 * @date 2022/11/23 10:51
 */
@FunctionalInterface
public interface IExcelDataValidation {
    //~methods
    //==================================================================================================================

    /**
     * 动态获取数据校验的下拉数据
     * @return
     */
    String[] get();
}
