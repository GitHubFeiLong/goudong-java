package com.goudong.file.enumerate;

import com.goudong.core.lang.IEnum;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * 类描述：
 * 性别
 * @author cfl
 * @version 1.0
 * @date 2022/11/20 20:59
 */
@Getter
public enum SexEnum implements IEnum<Integer, SexEnum> {
    //~fields
    //==================================================================================================================
    UNKNOWN(0, "未知"),
    MAN(1, "男"),
    WOMAN(2, "女"),
    ;
    private Integer id;
    private String sex;
    //~methods
    //==================================================================================================================
    public static SexEnum getBySex(String sex) {
        return Stream.of(values()).filter(f -> Objects.equals(f.getSex(), sex))
                .findFirst().orElseThrow(() -> new IllegalArgumentException());
    }
    SexEnum(Integer id, String sex) {
        this.id = id;
        this.sex = sex;
    }

    /**
     * 获取枚举成员的唯一标识
     *
     * @return
     */
    @Override
    public Integer getId() {
        return this.id;
    }
}
