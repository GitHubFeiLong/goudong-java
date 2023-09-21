package com.goudong.authentication.server.service.impl;

import com.goudong.authentication.server.domain.BaseMenu;
import com.goudong.authentication.server.domain.BaseUser;
import com.goudong.authentication.server.repository.BaseMenuRepository;
import com.goudong.authentication.server.rest.req.BaseMenuGetAllReq;
import com.goudong.authentication.server.service.BaseMenuService;
import com.goudong.authentication.server.service.dto.BaseMenuDTO;
import com.goudong.authentication.server.service.mapper.BaseMenuMapper;
import com.goudong.core.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.criteria.*;
import java.util.ArrayList;
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

    /**
     * 查询指定条件下的菜单
     *
     * @param req 条件
     * @return 菜单集合
     */
    @Override
    public List<BaseMenuDTO> findAll(BaseMenuGetAllReq req) {
        Specification<BaseMenu> specification = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> andPredicateList = new ArrayList<>();
            andPredicateList.add(criteriaBuilder.equal(root.get("appId"), req.getAppId()));
            if (StringUtil.isNotBlank(req.getName())) {
                andPredicateList.add(criteriaBuilder.like(root.get("name").as(String.class), "%" + req.getName() + "%"));
            }
            if (StringUtil.isNotBlank(req.getPath())) {
                andPredicateList.add(criteriaBuilder.like(root.get("path").as(String.class), "%" + req.getPath() + "%"));
            }
            if (req.getType() != null) {
                andPredicateList.add(criteriaBuilder.equal(root.get("type"), req.getType()));
            }

            if (StringUtil.isNotBlank(req.getPermissionId())) {
                andPredicateList.add(criteriaBuilder.like(root.get("permissionId").as(String.class), "%" + req.getPermissionId() + "%"));
            }
            return criteriaBuilder.and(andPredicateList.toArray(new Predicate[andPredicateList.size()]));
        };

        return baseMenuMapper.toDto(baseMenuRepository.findAll(specification));
    }


}
