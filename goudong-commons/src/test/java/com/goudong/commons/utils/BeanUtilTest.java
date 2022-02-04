package com.goudong.commons.utils;

import cn.hutool.core.bean.copier.CopyOptions;
import com.google.common.collect.Lists;
import com.goudong.commons.po.core.BasePO;
import com.goudong.commons.utils.core.BeanUtil;
import org.junit.jupiter.api.Test;

import java.util.Date;
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
        basePO.setUpdateTime(new Date());
        basePO.setCreateTime(new Date());

        BasePO basePO2 = new BasePO();
        basePO2.setId(1L);
        basePO2.setDeleted(false);
        basePO2.setCreateUserId(1L);
        basePO2.setUpdateUserId(1L);
        basePO2.setUpdateTime(new Date());
        basePO2.setCreateTime(new Date());

        List<BasePO> list = Lists.newArrayList(basePO, basePO2);
        List<BasePO> strings = BeanUtil.copyToList(list, BasePO.class, CopyOptions.create());
        list.get(0).setId(10L);
        System.out.println("strings = " + strings);
    }
}