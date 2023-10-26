package com.goudong.authentication.server.enums.option;

import com.goudong.core.lang.IEnum;
import lombok.Getter;

/**
 * 类描述：
 * 激活分类枚举
 * @author chenf
 * @version 1.0
 */
@Getter
public enum ActivateEnum implements IEnum<Boolean, ActivateEnum> {
    ACTIVATED(true, "已激活"),
    UNACTIVATED(false, "未激活"),
    ;
    /**
     * 值
     */
    private final Boolean value;
    /**
     * 显示标签
     */
    private final String label;

    ActivateEnum(Boolean value, String label) {
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

    public static void main(String[] args) {
        ActivateEnum byId = IEnum.getById(ActivateEnum.class, false);
        Boolean value1 = byId.getValue();
        System.out.println("byId = " + byId);
    }
}
