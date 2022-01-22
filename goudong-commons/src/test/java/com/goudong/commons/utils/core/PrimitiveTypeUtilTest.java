package com.goudong.commons.utils.core;

import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import static org.junit.jupiter.api.Assertions.*;

class PrimitiveTypeUtilTest {

    @Test
    void isBasicType() {
        // String
        Assert.isTrue(PrimitiveTypeUtil.isBasicType("123123"));

        // 基本类型
        Assert.isTrue(PrimitiveTypeUtil.isBasicType((byte)1));
        Assert.isTrue(PrimitiveTypeUtil.isBasicType('c'));
        Assert.isTrue(PrimitiveTypeUtil.isBasicType((short)1));
        Assert.isTrue(PrimitiveTypeUtil.isBasicType(1));
        Assert.isTrue(PrimitiveTypeUtil.isBasicType(1L));
        Assert.isTrue(PrimitiveTypeUtil.isBasicType(1.1F));
        Assert.isTrue(PrimitiveTypeUtil.isBasicType(1.1D));
        Assert.isTrue(PrimitiveTypeUtil.isBasicType(true));

        // 包装
        Assert.isTrue(PrimitiveTypeUtil.isBasicType(new Byte("1")));
        Assert.isTrue(PrimitiveTypeUtil.isBasicType(new Character('a')));
        Assert.isTrue(PrimitiveTypeUtil.isBasicType(new Short((short)128)));
        Assert.isTrue(PrimitiveTypeUtil.isBasicType(new Integer(1)));
        Assert.isTrue(PrimitiveTypeUtil.isBasicType(new Long("1")));
        Assert.isTrue(PrimitiveTypeUtil.isBasicType(new Float("1.1")));
        Assert.isTrue(PrimitiveTypeUtil.isBasicType(new Double("1")));
        Assert.isTrue(PrimitiveTypeUtil.isBasicType(new Boolean(true)));

    }
}