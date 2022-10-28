package com.goudong.boot.web.util;

import com.goudong.core.lang.PageResult;
import com.goudong.core.util.CollectionUtil;

/**
 * 类描述：
 * 分页结果对象转换器
 * @author cfl
 * @version 1.0
 * @date 2022/10/26 17:11
 */
public class PageResultConvert<E> {

    /**
     * 使用系统定义的默认转换
     *
     * @param source 框架原分页结果对象
     * @param tClazz 转换后的结果类型
     * @return
     * @param <E>
     */
    public static <E> PageResult<E> convert(Object source, Class<E> tClazz) {
        // 如果客户端有集成定义的持久层，那么就取其中一个进行转换
        if (CollectionUtil.isNotEmpty(PageTypeEnum.CLIENT_TYPES)) {
            return PageTypeEnum.CLIENT_TYPES.get(0).convert(source, tClazz);
        }
        throw new RuntimeException("持久层没有定义，请更新" + PageTypeEnum.class);
    }

    /**
     * 如果客户端集成了多个ORM框架，客户端可以自定义类型转换
     * @param source 框架原分页结果对象
     * @param tClazz 转换后的结果类型
     * @param typeEnum 指定ORM类型
     * @return
     * @param <E>
     */
    public static <E> PageResult<E> convert(Object source, Class<E> tClazz, PageTypeEnum typeEnum) {
        return typeEnum.convert(source, tClazz);
    }

}
