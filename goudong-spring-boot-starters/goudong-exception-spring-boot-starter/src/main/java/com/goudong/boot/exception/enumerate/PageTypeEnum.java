package com.goudong.boot.exception.enumerate;

import java.util.ArrayList;
import java.util.List;

/**
 * 枚举描述：
 * 分页结果的类型
 * @author cfl
 * @version 1.0
 * @date 2022/10/26 15:51
 */
public enum PageTypeEnum {

    JPA,
    MYBATIS_PLUS,
    ;

    /**
     * 客户系统支持的分页类型
     */
    public static List<PageTypeEnum> CLIENT_TYPES = new ArrayList<>();

    /**
     * 根据classpath上出现的某些特定类（jpa的page，或mybatis plus的page等），将其添加到可用集合
     * @param typeEnum
     */
    public static void addClientType(PageTypeEnum typeEnum) {
        PageTypeEnum.CLIENT_TYPES.add(typeEnum);
    }
}
