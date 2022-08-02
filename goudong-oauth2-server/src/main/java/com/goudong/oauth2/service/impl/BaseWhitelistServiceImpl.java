
package com.goudong.oauth2.service.impl;

import cn.hutool.core.bean.copier.CopyOptions;
import com.goudong.commons.dto.oauth2.BaseWhitelist2CreateDTO;
import com.goudong.commons.dto.oauth2.BaseWhitelistDTO;
import com.goudong.commons.dto.oauth2.BaseWhitelistDTO2Redis;
import com.goudong.commons.framework.redis.RedisTool;
import com.goudong.commons.utils.core.BeanUtil;
import com.goudong.commons.utils.core.LogUtil;
import com.goudong.oauth2.enumerate.RedisKeyProviderEnum;
import com.goudong.oauth2.mapper.BaseWhitelistMapper;
import com.goudong.oauth2.po.BaseWhitelistPO;
import com.goudong.oauth2.repository.BaseWhitelistRepository;
import com.goudong.oauth2.service.BaseWhitelistService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
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

    /**
     * 白名单Mapper
     */
    private final BaseWhitelistMapper baseWhitelistMapper;
    /**
     * 白名单持久层
     */
    private final BaseWhitelistRepository baseWhitelistRepository;
    /**
     * redis工具
     */
    private final RedisTool redisTool;

    private final HttpServletRequest httpServletRequest;

    public BaseWhitelistServiceImpl(BaseWhitelistMapper baseWhitelistMapper,
                                   BaseWhitelistRepository baseWhitelistRepository,
                                    RedisTool redisTool,
                                    HttpServletRequest httpServletRequest
    ) {
        this.baseWhitelistMapper = baseWhitelistMapper;
        this.baseWhitelistRepository = baseWhitelistRepository;
        this.redisTool = redisTool;
        this.httpServletRequest = httpServletRequest;
    }


    /**
     * 添加白名单
     *
     * @param createDTOS 新增白名单接口
     */
    @Override
    @Transactional
    public List<BaseWhitelistDTO> addWhitelists(List<BaseWhitelist2CreateDTO> createDTOS) {
        List<BaseWhitelistPO> baseWhitelistPOS = baseWhitelistRepository.findAll();

        List<String> pattern = baseWhitelistPOS.stream().map(BaseWhitelistPO::getPattern).collect(Collectors.toList());

        List<BaseWhitelistPO> copyToList = baseWhitelistMapper.createDTOS2POS(createDTOS);

        // pattern相等，就需要更新其它属性
        baseWhitelistPOS.stream().forEach(p1->{
            copyToList.stream().forEach(p2->{
                // 相等，那么就更新
                // TODO 这里颗粒度比较大，一个pattern 对应了多个请求方式。
                if (Objects.equals(p1.getPattern(), p2.getPattern())) {
                    p1.setMethods(p2.getMethods());
                    p1.setRemark(p2.getRemark());
                    p1.setIsSystem(p2.getIsSystem());
                    p1.setIsInner(p2.getIsInner());
                    p1.setIsDisable(p2.getIsDisable());
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

        // 查询
        List<BaseWhitelistPO> whitelistPOS = baseWhitelistRepository.findAllByIsDisable(false);

        // 更新redis中白名单列表
        saveWhitelist2Redis(whitelistPOS);

        return BeanUtil.copyToList(baseWhitelistPOS, BaseWhitelistDTO.class, CopyOptions.create());
    }

    /**
     * 更新redis中白名单列表
     * @param whitelistPOS 白名单集合
     */
    private void saveWhitelist2Redis(List<BaseWhitelistPO> whitelistPOS) {
        List<BaseWhitelistDTO2Redis> redisValues = BeanUtil.copyToList(whitelistPOS, BaseWhitelistDTO2Redis.class, CopyOptions.create());
        // 更新redis中的值
        boolean succeed = redisTool.set(RedisKeyProviderEnum.WHITELIST, redisValues);

        if (!succeed) {
            LogUtil.error(log, "更新redis中白名单失败");
        }
    }

    /**
     * 查询所有白名单
     *
     * @return
     */
    @Override
    public List<BaseWhitelistDTO> findAll() {
        /*
            redis存在数据直接返回。
         */
        List<BaseWhitelistDTO2Redis> whitelistDTOS = redisTool.getList(RedisKeyProviderEnum.WHITELIST, BaseWhitelistDTO2Redis.class);
        if (CollectionUtils.isNotEmpty(whitelistDTOS)) {
            return BeanUtil.copyToList(whitelistDTOS, BaseWhitelistDTO.class, CopyOptions.create());
        }

        List<BaseWhitelistPO> whitelistPOS = baseWhitelistRepository.findAll();

        // 更新redis白名单
        saveWhitelist2Redis(whitelistPOS);

        return BeanUtil.copyToList(whitelistPOS, BaseWhitelistDTO.class, CopyOptions.create());
    }

    /**
     * 重新初始化白名单到redis中
     *
     * @return
     */
    @Override
    public List<BaseWhitelistDTO> initWhitelist2Redis() {
        List<BaseWhitelistPO> whitelistPOS = baseWhitelistRepository.findAll();
        saveWhitelist2Redis(whitelistPOS);
        return BeanUtil.copyToList(whitelistPOS, BaseWhitelistDTO.class, CopyOptions.create());
    }
}
