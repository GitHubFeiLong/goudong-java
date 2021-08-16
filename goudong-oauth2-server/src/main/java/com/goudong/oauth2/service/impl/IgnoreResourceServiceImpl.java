package com.goudong.oauth2.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.goudong.commons.dto.BaseIgnoreResourceDTO;
import com.goudong.commons.po.BaseIgnoreResourcePO;
import com.goudong.commons.utils.BeanUtil;
import com.goudong.oauth2.mapper.IgnoreResourceMapper;
import com.goudong.oauth2.service.IgnoreResourceService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 类描述：
 * 白名单服务层实现
 * @Author msi
 * @Date 2021-08-14 11:50
 * @Version 1.0
 */
@Service
public class IgnoreResourceServiceImpl extends ServiceImpl<IgnoreResourceMapper, BaseIgnoreResourcePO> implements IgnoreResourceService {

    /**
     * 集合添加到白名单
     *
     * @param insertDTOList
     * @return
     */
    @Override
    public List<BaseIgnoreResourceDTO> addList(List<BaseIgnoreResourceDTO> insertDTOList) {
        List<BaseIgnoreResourcePO> newList = BeanUtil.copyList(insertDTOList, BaseIgnoreResourcePO.class);
        // 先查询数据
        LambdaQueryWrapper<BaseIgnoreResourcePO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BaseIgnoreResourcePO::getIsSystem, false);
        List<BaseIgnoreResourcePO> rawList = super.list(queryWrapper);

        // 比较不同数据
        List<BaseIgnoreResourcePO> insertList = absent(rawList, newList);

        // 不同数据插入数据库
        super.saveBatch(insertList);

        return BeanUtil.copyList(insertList, BaseIgnoreResourceDTO.class);
    }

    /**
     * 比较差集，将rawList和newList进行比较，返回rawList中不存在的数据
     * @param rawList 原数据库白名单有的数据
     * @param newList 现要添加的白名单数据
     * @return
     */
    private List<BaseIgnoreResourcePO> absent(List<BaseIgnoreResourcePO> rawList, List<BaseIgnoreResourcePO> newList){
        List<BaseIgnoreResourcePO> result = new ArrayList<>();
        newList.stream().forEach(p->{
            // url不同且方法也不同。表示新添加的接口
            long count = rawList.stream().filter(f -> f.getPattern().equals(p.getPattern()) && f.getMethod().equals(p.getMethod())).count();
            if (count == 0) {
                result.add(p);
            }
        });
        return result;
    }
}
