package com.goudong.boot.exception.util;

import com.goudong.boot.exception.core.PageResult;

import java.util.ArrayList;
import java.util.List;

/**
 * 枚举描述：
 * ORM的类型
 * @author cfl
 * @version 1.0
 * @date 2022/10/26 15:51
 */
public enum ORMTypeEnum {

    JPA{
        @Override
        public PageResult convert(Object source, Class tClazz) {
            return JpaPage2PageResultConverter.getInstance().convert(source, tClazz);
        }

        @Override
        public long getPage(long page) {
            return page - 1;
        }
    },

    MYBATIS_PLUS{
        @Override
        public PageResult convert(Object source, Class tClazz) {
            return MybatisPlusPage2PageResultConverter.getInstance().convert(source, tClazz);
        }
        @Override
        public long getPage(long page) {
            return page;
        }
    },
    ;

    /**
     * 客户系统支持的ORM类型,一般来说，客户端只会引入一个ORM框架。但是有可能会引入多个，所以需要注意
     */
    public static List<ORMTypeEnum> CLIENT_TYPES = new ArrayList<>();

    /**
     * 分页结果转换
     * @param source 原分页结果对象
     * @param tClazz 目标类型
     * @return
     */
    public PageResult convert(Object source, Class tClazz){
        throw new RuntimeException();
    };

    /**
     * 返回{@code page}，jpa是从0开始，mybatis是1开始
     * @param page
     * @return
     */
    public long getPage(long page) {
        throw new RuntimeException();
    }
}
