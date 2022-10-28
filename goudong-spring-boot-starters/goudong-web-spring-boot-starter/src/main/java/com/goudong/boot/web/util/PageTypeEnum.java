package com.goudong.boot.web.util;

import com.goudong.core.lang.PageResult;
import com.goudong.core.util.AssertUtil;
import com.goudong.core.util.MessageFormatUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 枚举描述：
 * Page的类型, 有优先级，如果client既配了{@code SPRING_DATA}，又配了{@code MYBATIS_PLUS}，那么默认使用前面的成员。
 * @author cfl
 * @version 1.0
 * @date 2022/10/26 15:51
 */
public enum PageTypeEnum {

    JPA{
        @Override
        public PageResult convert(Object source, Class tClazz) {
            return SpringDataPage2PageResultConverter.getInstance().convert(source, tClazz);
        }

        @Override
        public int getPage(int page) {
            return page - 1;
        }
    },

    MYBATIS_PLUS{
        @Override
        public PageResult convert(Object source, Class tClazz) {
            return MybatisPlusPage2PageResultConverter.getInstance().convert(source, tClazz);
        }
        @Override
        public int getPage(int page) {
            return page;
        }
    },
    ;

    /**
     * 客户系统支持的ORM类型,一般来说，客户端只会引入一个ORM框架。但是有可能会引入多个，所以需要注意
     */
    public static List<PageTypeEnum> CLIENT_TYPES = new ArrayList<>();

    /**
     * 获取最优的类型
     * @return
     */
    public static PageTypeEnum getClientPriority() {
        AssertUtil.isNotEmpty(CLIENT_TYPES, () -> MessageFormatUtil.format("client 未使用 {}", PageTypeEnum.values()));
        return  CLIENT_TYPES.stream().sorted((o1, o2) -> o1.ordinal() - o2.ordinal()).findFirst().get();
    }

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
    public int getPage(int page) {
        throw new RuntimeException();
    }
}
