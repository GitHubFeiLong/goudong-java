package com.goudong.oauth2.service.impl;

import cn.hutool.core.bean.copier.CopyOptions;
import com.goudong.commons.dto.oauth2.BaseApiResource2CreateDTO;
import com.goudong.commons.dto.oauth2.BaseApiResourceDTO;
import com.goudong.commons.utils.core.BeanUtil;
import com.goudong.oauth2.po.BaseApiResourcePO;
import com.goudong.oauth2.repository.BaseApiResourceRepository;
import com.goudong.oauth2.service.BaseApiResourceService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 类描述：
 * 保存系统中所有api接口资源服务层接口实现类
 * @author cfl
 * @date 2022/8/2 22:48
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class BaseApiResourceServiceImpl implements BaseApiResourceService {

    /**
     * 持久层接口
     */
    private final BaseApiResourceRepository baseApiResourceRepository;


    /**
     * 保存服务api资源
     *
     * @param createDTOS
     * @return
     */
    @Override
    public List<BaseApiResourceDTO> addApiResources(@NotEmpty List<BaseApiResource2CreateDTO> createDTOS) {
        // 查询该服务下的
        List<BaseApiResourcePO> existsApiResourcePOS = baseApiResourceRepository.findAllByApplicationName(createDTOS.get(0).getApplicationName());

        List<BaseApiResourcePO> saveApiResourcePOS = BeanUtil.copyToList(createDTOS, BaseApiResourcePO.class, CopyOptions.create());

        // 数据库不存在数据，直接保存
        if (CollectionUtils.isEmpty(existsApiResourcePOS)) {
            baseApiResourceRepository.saveAll(saveApiResourcePOS);
            return BeanUtil.copyToList(saveApiResourcePOS, BaseApiResourceDTO.class, CopyOptions.create());
        }

        List<BaseApiResourcePO> needSaveApiResourcePOS = new ArrayList<>();

        // 首先是修改已存在数据
        existsApiResourcePOS.stream().forEach(p1->{
            saveApiResourcePOS.stream().forEach(p2->{
                // 相同，修改
                if (Objects.equals(p1.getPattern() + p1.getMethod(), p2.getPattern()+p2.getMethod())) {
                    p1.setRemark(p2.getRemark());
                } else {
                    needSaveApiResourcePOS.add(p2);
                }
            });
        });

        // 其次保存新增的数据
        if (CollectionUtils.isNotEmpty(needSaveApiResourcePOS)) {
            baseApiResourceRepository.saveAll(needSaveApiResourcePOS);
        }

        return BeanUtil.copyToList(saveApiResourcePOS, BaseApiResourceDTO.class, CopyOptions.create());
    }
}
