package com.goudong.oauth2.service.impl;

import cn.hutool.core.bean.copier.CopyOptions;
import com.goudong.commons.dto.user.BaseWhitelist2CreateDTO;
import com.goudong.commons.dto.user.BaseWhitelistDTO;
import com.goudong.commons.frame.redis.RedisTool;
import com.goudong.commons.utils.BeanUtil;
import com.goudong.oauth2.mapper.BaseWhitelistMapper;
import com.goudong.oauth2.po.BaseWhitelistPO;
import com.goudong.oauth2.repository.BaseWhitelistRepository;
import com.goudong.oauth2.service.BaseWhitelistService;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@Service
public class BaseWhitelistServiceImpl implements BaseWhitelistService {

    private final BaseWhitelistMapper baseWhitelistMapper;
    private final BaseWhitelistRepository baseWhitelistRepository;
    private final RedisTool redisTool;

    public BaseWhitelistServiceImpl(BaseWhitelistMapper baseWhitelistMapper,
                                   BaseWhitelistRepository baseWhitelistRepository,
                                    RedisTool redisTool
    ) {
        this.baseWhitelistMapper = baseWhitelistMapper;
        this.baseWhitelistRepository = baseWhitelistRepository;
        this.redisTool = redisTool;
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

        // pattern相等，就需要更新其它属性
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

        // List<WhitelistRedisValue> redisValues = com.goudong.commons.utils.BeanUtil
        //         .copyToList(baseWhitelistRepository.findAll(), WhitelistRedisValue.class, CopyOptions.create());
        // // 更新redis中的值
        // boolean succeed = redisTool.set(RedisKeyProviderEnum.WHITELIST, redisValues);
        //
        // if (!succeed) {
        //     LogUtil.error(log, "更新redis中白名单失败");
        // }

        return BeanUtil.copyToList(saveBaseWhitelistPOList, BaseWhitelistDTO.class, CopyOptions.create());
    }

    /**
     * 查询所有白名单
     *
     * @return
     */
    @Override
    public List<BaseWhitelistDTO> findAll() {
        List<BaseWhitelistPO> all = baseWhitelistRepository.findAll();
        return BeanUtil.copyToList(all, BaseWhitelistDTO.class, CopyOptions.create());
    }
}
