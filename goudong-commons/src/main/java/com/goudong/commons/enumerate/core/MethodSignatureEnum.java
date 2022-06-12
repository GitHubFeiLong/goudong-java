package com.goudong.commons.enumerate.core;

import lombok.Getter;

/**
 * 枚举描述：
 * 方法签名（基本类型和对应的包装类型）枚举
 * @author msi
 * @version 1.0
 * @date 2022/6/12 17:18
 */
@Getter
public enum MethodSignatureEnum {

    //~fields
    //==================================================================================================================
    BASIC_BYTE("()B"),
    BASIC_SHORT("()S"),
    BASIC_CHAR("()C"),
    BASIC_INT("()I"),
    BASIC_LONG("()J"),
    BASIC_FLOAT("()F"),
    BASIC_DOUBLE("()D"),
    BASIC_BOOLEAN("()Z"),
    PACKAGE_BYTE("()Ljava/lang/Byte;"),
    PACKAGE_SHORT("()Ljava/lang/Short;"),
    PACKAGE_CHAR("()Ljava/lang/Character;"),
    PACKAGE_INT("()Ljava/lang/Integer;"),
    PACKAGE_LONG("()Ljava/lang/Long;"),
    PACKAGE_FLOAT("()Ljava/lang/Float;"),
    PACKAGE_DOUBLE("()Ljava/lang/Double;"),
    PACKAGE_BOOLEAN("()Ljava/lang/Boolean;"),

    ;

    private String signature;

    //~methods
    //==================================================================================================================
    MethodSignatureEnum(String signature) {
        this.signature = signature;
    }

}
