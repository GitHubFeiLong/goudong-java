package com.goudong.core.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class PrimitiveTypeUtilTest {

    @Test
    void isBasicType() {
        // String
        assertTrue(PrimitiveTypeUtil.isBasicType("123123"));

        // 基本类型
        assertTrue(PrimitiveTypeUtil.isBasicType((byte)1));
        assertTrue(PrimitiveTypeUtil.isBasicType('c'));
        assertTrue(PrimitiveTypeUtil.isBasicType((short)1));
        assertTrue(PrimitiveTypeUtil.isBasicType(1));
        assertTrue(PrimitiveTypeUtil.isBasicType(1L));
        assertTrue(PrimitiveTypeUtil.isBasicType(1.1F));
        assertTrue(PrimitiveTypeUtil.isBasicType(1.1D));
        assertTrue(PrimitiveTypeUtil.isBasicType(true));

        // 包装
        assertTrue(PrimitiveTypeUtil.isBasicType(new Byte("1")));
        assertTrue(PrimitiveTypeUtil.isBasicType(new Character('a')));
        assertTrue(PrimitiveTypeUtil.isBasicType(new Short((short)128)));
        assertTrue(PrimitiveTypeUtil.isBasicType(new Integer(1)));
        assertTrue(PrimitiveTypeUtil.isBasicType(new Long("1")));
        assertTrue(PrimitiveTypeUtil.isBasicType(new Float("1.1")));
        assertTrue(PrimitiveTypeUtil.isBasicType(new Double("1")));
        assertTrue(PrimitiveTypeUtil.isBasicType(new Boolean(true)));

    }
}
