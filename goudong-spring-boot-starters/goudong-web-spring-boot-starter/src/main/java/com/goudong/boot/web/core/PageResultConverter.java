package com.goudong.boot.web.core;

/**
 * 接口描述：
 *
 * @author cfl
 * @version 1.0
 * @date 2022/10/26 17:31
 */
public interface PageResultConverter<S, T, E> {

    /**
     * 将{@code Page}转成{@code PageResult}
     * @param source
     * @param tClazz
     * @return
     */
    T basicConvert(S source, Class<E> tClazz);

    /**
     * 将{@code Object}转成{@code PageResult}，门面方法，接收客户端的参数传递
     * @param source
     * @param tClazz
     * @return
     */
    T convert(Object source, Class<E> tClazz);
}
