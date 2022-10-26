package com.goudong.boot.exception.core;

import org.springframework.lang.Nullable;

/**
 * 接口描述：
 *
 * @author cfl
 * @version 1.0
 * @date 2022/10/26 17:31
 */
@FunctionalInterface
public interface PageResultConverter<S, T, E> {

    @Nullable
    T convert(S source, Class<E> tClazz);
}
