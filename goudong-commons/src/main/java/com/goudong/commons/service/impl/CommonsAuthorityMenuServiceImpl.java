package com.goudong.commons.service.impl;

import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.goudong.commons.dto.AuthorityMenuDTO;
import com.goudong.commons.enumerate.core.RedisKeyEnum;
import com.goudong.commons.po.AuthorityMenuPO;
import com.goudong.commons.mapper.CommonsAuthorityMenuMapper;
import com.goudong.commons.service.CommonsAuthorityMenuService;
import com.goudong.commons.utils.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.BatchUpdateException;
import java.util.ArrayList;
import java.util.List;

/**
 * 类描述：
 *
 * @Author e-Feilong.Chen
 * @Date 2021/8/16 16:16
 */
@Slf4j
@Service
public class CommonsAuthorityMenuServiceImpl extends ServiceImpl<CommonsAuthorityMenuMapper, AuthorityMenuPO> implements CommonsAuthorityMenuService {

    @Autowired
    private RedissonClient redissonClient;

    /**
     * 批量添加菜单,已添加分布式锁，防止重复添加数据
     *
     * @param insetDTOS
     * @return
     */
    @Override
    public List<AuthorityMenuDTO> addList(List<AuthorityMenuDTO> insetDTOS) {

        RedisKeyEnum oauth2RedissonAddMenu = RedisKeyEnum.OAUTH2_REDISSON_ADD_MENU;
        // 创建分布式锁
        RLock lock = redissonClient.getLock(oauth2RedissonAddMenu.getKey());
        // 加锁，并且设置锁过期时间，防止死锁的产生
        lock.lock(oauth2RedissonAddMenu.getTime(), oauth2RedissonAddMenu.getTimeUnit());

        // 先查询菜单
        List<AuthorityMenuPO> list = super.list(null);

        List<AuthorityMenuPO> authorityMenuPOS = BeanUtil.copyToList(insetDTOS, AuthorityMenuPO.class, CopyOptions.create());

        // 过滤已存在的菜单
        authorityMenuPOS.removeAll(list);

        // 集合为空，直接返回
        if (authorityMenuPOS.isEmpty()) {
            return new ArrayList<>();
        }

        // 添加进菜单
        try {
            super.saveBatch(authorityMenuPOS);
        } catch (Exception e) {
            if (e instanceof BatchUpdateException) {
                log.info("更新失败");
            }
        }

        // 释放锁
        lock.unlock();

        return BeanUtil.copyToList(authorityMenuPOS, AuthorityMenuDTO.class, CopyOptions.create());
    }

}
