package com.goudong.boot.exception.util;

import com.goudong.boot.exception.core.PageResult;
import com.goudong.boot.exception.enumerate.PageTypeEnum;

import java.util.ArrayList;
import java.util.List;

/**
 * 类描述：
 *
 * @author cfl
 * @version 1.0
 * @date 2022/10/26 17:11
 */
public class PageResultConvert<E> {

    /**
     * 客户系统支持的分页类型
     */
    public static List<PageTypeEnum> CLIENT_TYPES = new ArrayList<>();

    public PageResult<E> convert(Object source, Class<E> tClazz) {
        if (CLIENT_TYPES.contains(PageTypeEnum.JPA)) {

        }

        if (CLIENT_TYPES.contains(PageTypeEnum.MYBATIS_PLUS)) {


        }
    }
}
