package com.goudong.commons.utils;
import cn.hutool.core.bean.copier.CopyOptions;
import com.google.common.collect.Lists;
import com.goudong.commons.po.core.BasePO;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

class BeanUtilTest {

    @Test
    void copyProperties() {
    }

    @Test
    void testCopyProperties() {
    }

    @Test
    void copyList() {
        BasePO basePO = new BasePO();
        basePO.setId(1L);
        basePO.setDeleted(false);
        basePO.setCreateUserId(1L);
        basePO.setUpdateUserId(1L);
        basePO.setUpdateTime(LocalDateTime.now());
        basePO.setCreateTime(LocalDateTime.now());

        BasePO basePO2 = new BasePO();
        basePO2.setId(1L);
        basePO2.setDeleted(false);
        basePO2.setCreateUserId(1L);
        basePO2.setUpdateUserId(1L);
        basePO2.setUpdateTime(LocalDateTime.now());
        basePO2.setCreateTime(LocalDateTime.now());

        List<BasePO> list = Lists.newArrayList(basePO, basePO2);
        List<BasePO> strings = BeanUtil.copyToList(list, BasePO.class, CopyOptions.create());
        list.get(0).setId(10L);
        System.out.println("strings = " + strings);
    }
}