package com.goudong.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.goudong.commons.dto.user.BaseWhitelist2CreateDTO;
import com.goudong.commons.dto.user.BaseWhitelistDTO;
import com.goudong.user.mapper.BaseWhitelistMapper;
import com.goudong.user.po.BaseWhitelistPO;
import com.goudong.user.repository.BaseWhitelistRepository;
import com.goudong.user.service.BaseWhitelistService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 类描述：
 * 白名单业务层接口
 * @author msi
 * @version 1.0
 * @date 2022/1/9 3:14
 */
@Service
public class BaseWhitelistServiceImpl implements BaseWhitelistService {

    private final BaseWhitelistMapper baseWhitelistMapper;
    private final BaseWhitelistRepository baseWhitelistRepository;

    public BaseWhitelistServiceImpl(BaseWhitelistMapper baseWhitelistMapper,
                                   BaseWhitelistRepository baseWhitelistRepository) {
        this.baseWhitelistMapper = baseWhitelistMapper;
        this.baseWhitelistRepository = baseWhitelistRepository;
    }


    /**
     * 添加白名单
     *
     * @param createDTOS 新增白名单接口
     */
    @Override
    @Transactional
    public List<BaseWhitelistDTO> addWhitelist(List<BaseWhitelist2CreateDTO> createDTOS) {
        List<BaseWhitelistPO> baseWhitelistPOS = baseWhitelistRepository.findAll();

        List<String> pattern = baseWhitelistPOS.stream().map(BaseWhitelistPO::getPattern).collect(Collectors.toList());

        List<BaseWhitelistPO> copyToList = baseWhitelistMapper.createDTOS2POS(createDTOS);

        // pattern相等，就需要更新其它树形
        baseWhitelistPOS.stream().forEach(p1->{
            copyToList.stream().forEach(p2->{
                // 相等，那么就更新
                if (Objects.equals(p1.getPattern(), p2.getPattern())) {
                    p1.setMethods(p2.getMethods());
                    p1.setRemark(p2.getRemark());
                    p1.setIsSystem(p2.getIsSystem());
                }
            });
        });

        // 剩下的是数据库中不存在的。
        List<BaseWhitelistPO> saveBaseWhitelistPOList = copyToList.stream()
                .filter(f -> !pattern.contains(f.getPattern()))
                .collect(Collectors.toList());

        if (CollectionUtils.isNotEmpty(saveBaseWhitelistPOList)) {
            baseWhitelistRepository.saveAll(saveBaseWhitelistPOList);
        }

        return BeanUtil.copyToList(saveBaseWhitelistPOList, BaseWhitelistDTO.class, CopyOptions.create());
    }
}
