package com.goudong.oauth2.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.goudong.commons.dto.AuthorityMenuDTO;
import com.goudong.commons.po.AuthorityMenuPO;
import com.goudong.commons.utils.BeanUtil;
import com.goudong.oauth2.mapper.AuthorityMenuMapper;
import com.goudong.oauth2.service.AuthorityMenuService;
import org.checkerframework.checker.units.qual.A;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 类描述：
 *
 * @Author e-Feilong.Chen
 * @Date 2021/8/16 16:16
 */
@Service
public class AuthorityMenuServiceImpl extends ServiceImpl<AuthorityMenuMapper, AuthorityMenuPO> implements AuthorityMenuService {
    
    /**
     * 批量添加菜单
     *
     * @param insetDTOS
     * @return
     */
    @Override
    public List<AuthorityMenuDTO> addList(List<AuthorityMenuDTO> insetDTOS) {
        // 先查询菜单
        List<AuthorityMenuPO> list = super.list(null);

        List<AuthorityMenuPO> authorityMenuPOS = BeanUtil.copyList(insetDTOS, AuthorityMenuPO.class);

        // 过滤已存在的菜单
        authorityMenuPOS.removeAll(list);

        // 集合为空，直接返回
        if (authorityMenuPOS.isEmpty()) {
            return new ArrayList<>();
        }

        // 添加进菜单
        super.saveBatch(authorityMenuPOS);
        
        return BeanUtil.copyList(authorityMenuPOS, AuthorityMenuDTO.class);
    }

}
