package com.goudong.boot.web.util;

import com.goudong.core.lang.PageResult;
import com.goudong.core.util.CollectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 类描述：
 * 分页结果对象转换器
 * @author cfl
 * @version 1.0
 * @date 2022/10/26 17:11
 */
public class PageResultConvert<E> {

    private static final Logger log = LoggerFactory.getLogger(PageResultConvert.class);

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
            for (int i = 0, size = PageTypeEnum.CLIENT_TYPES.size(); i < size; i++) {
                PageTypeEnum pageTypeEnum = PageTypeEnum.CLIENT_TYPES.get(i);
                try {
                    return pageTypeEnum.convert(source, tClazz);
                } catch (IllegalArgumentException e) {
                    if (log.isDebugEnabled()) {
                        log.debug("{} 转换分页结果异常", pageTypeEnum.name());
                    }
                }
            }

            // 当 source 不是 PageTypeEnum 内定义的类型，会转换失败。
            throw new IllegalArgumentException("未定义类型的分页对象：" + source);
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
