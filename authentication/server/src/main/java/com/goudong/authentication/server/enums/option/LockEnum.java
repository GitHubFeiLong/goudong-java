package com.goudong.authentication.server.enums.option;

import com.goudong.core.lang.IEnum;
import lombok.Getter;

/**
 * 类描述：
 * 锁定分类枚举
 * @author chenf
 * @version 1.0
 */
@Getter
public enum LockEnum implements IEnum<Boolean, LockEnum> {
    LOCKED(true, "已锁定"),
    UNLOCKED(false, "未锁定"),
    ;
    /**
     * 值
     */
    private final Boolean value;
    /**
     * 显示标签
     */
    private final String label;

    LockEnum(Boolean value, String label) {
        this.value = value;
        this.label = label;
    }

    /**
     * 获取枚举成员的唯一标识
     *
     * @return 枚举成员的唯一标识
     */
    @Override
    public Boolean getId() {
        return this.value;
    }
}
