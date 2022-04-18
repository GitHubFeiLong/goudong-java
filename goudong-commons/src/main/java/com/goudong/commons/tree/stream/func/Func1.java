package com.goudong.commons.tree.stream.func;

/**
 * 接口描述：
 *
 * @Author e-Feilong.Chen
 * @Date 2022/4/14 9:06
 */
@FunctionalInterface
public interface Func1<T, R> {

    R getId(T t);
}
