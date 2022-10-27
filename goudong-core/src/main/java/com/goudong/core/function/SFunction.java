package com.goudong.core.function;

import java.io.Serializable;
import java.util.function.Function;

/**
 * 类描述：
 * Function获取序列化能力
 * @author msi
 * @date 2022/6/12 18:21
 * @version 1.0
 */
@FunctionalInterface
public interface SFunction<T, R> extends Function<T, R>, Serializable {}