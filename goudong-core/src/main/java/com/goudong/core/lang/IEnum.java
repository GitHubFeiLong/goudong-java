package com.goudong.core.lang;

import com.goudong.core.util.MessageFormatUtil;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * 接口描述：
 * 自定义枚举的父接口，方便获取
 * @author cfl
 * @version 1.0
 * @date 2022/11/9 10:13
 */
public interface IEnum<ID, T extends IEnum> {

    /**
     * 获取枚举成员的唯一标识
     * @return 枚举成员的唯一标识
     */
    ID getId();

    /**
     * 根据{@code id} 找到对应的枚举成员并返回<br>
     *
     * @param id 待找的枚举成员id
     * @return
     */
    @SuppressWarnings(value = "all")
    default T getById(ID id) {
        try {
            Method method = this.getClass().getDeclaredMethod("values");
            Object invoke = method.invoke(null);
            T[] arr = ((T[]) invoke);
            Optional<T> optional = Stream.of(arr).filter(f -> Objects.equals(f.getId(), id))
                    .findFirst();
            if (optional.isPresent()) {
                return optional.get();
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        throw new IllegalArgumentException(MessageFormatUtil.format("根据标识{}，未找到{}枚举成员", id, this.getClass().getName()));
    }

    /**
     * 根据{@code id} 找到{@code clazz}中对应的枚举成员并返回<br>
     * @param clazz 枚举类型的class对象
     * @param id 枚举成员的唯一标识
     * @return
     * @param <T>
     */
    @SuppressWarnings(value = "all")
    static <T extends IEnum> T getById(Class<T> clazz, Object id) {
        try {
            Method method = clazz.getDeclaredMethod("values");
            Object invoke = method.invoke(null);
            T[] arr = ((T[]) invoke);
            Optional<T> optional = Stream.of(arr).filter(f -> Objects.equals(f.getId(), id))
                    .findFirst();
            if (optional.isPresent()) {
                return optional.get();
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        throw new IllegalArgumentException(MessageFormatUtil.format("根据标识{}，未找到{}枚举成员", id, clazz.getName()));
    }
}

