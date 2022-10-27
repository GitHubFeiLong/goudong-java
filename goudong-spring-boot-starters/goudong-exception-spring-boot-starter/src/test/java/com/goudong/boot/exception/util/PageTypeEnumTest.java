package com.goudong.boot.exception.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class PageTypeEnumTest {

    @Test
    void getClientPriority() {
        PageTypeEnum.CLIENT_TYPES.add(PageTypeEnum.MYBATIS_PLUS);
        PageTypeEnum.CLIENT_TYPES.add(PageTypeEnum.JPA);
        PageTypeEnum clientPriority = PageTypeEnum.getClientPriority();
        Assertions.assertEquals(clientPriority, PageTypeEnum.MYBATIS_PLUS);
    }
}
