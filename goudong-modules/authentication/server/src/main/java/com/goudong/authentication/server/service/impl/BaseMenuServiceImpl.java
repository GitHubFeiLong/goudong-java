package com.goudong.authentication.server.service.impl;

import com.goudong.authentication.server.domain.BaseMenu;
import com.goudong.authentication.server.repository.BaseMenuRepository;
import com.goudong.authentication.server.service.BaseMenuService;
import com.goudong.authentication.server.service.dto.BaseMenuDTO;
import com.goudong.authentication.server.service.mapper.BaseMenuMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * Service Implementation for managing {@link BaseMenu}.
 */
@Slf4j
@Service
@Transactional
public class BaseMenuServiceImpl implements BaseMenuService {

    //~fields
    //==================================================================================================================
    @Resource
    private BaseMenuRepository baseMenuRepository;
    @Resource
    private BaseMenuMapper baseMenuMapper;

    //~methods
    //==================================================================================================================

    /**
     * 查询应用下所有菜单
     *
     * @param appId 应用id
     * @return 菜单集合
     */
    @Override
    public List<BaseMenuDTO> findAllByAppId(Long appId) {
        return baseMenuMapper.toDto(baseMenuRepository.findAllByAppId(appId));
    }

    /**
     * 查询菜单
     *
     * @param ids 菜单id集合
     * @return 菜单集合
     */
    @Override
    public List<BaseMenu> findAllById(List<Long> ids) {
        return baseMenuRepository.findAllById(ids);
    }


}
