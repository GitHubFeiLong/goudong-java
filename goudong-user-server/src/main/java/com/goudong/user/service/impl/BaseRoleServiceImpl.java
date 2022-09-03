package com.goudong.user.service.impl;

import cn.hutool.core.bean.copier.CopyOptions;
import com.goudong.commons.constant.user.RoleConst;
import com.goudong.commons.dto.core.BasePageResult;
import com.goudong.commons.enumerate.core.ClientExceptionEnum;
import com.goudong.commons.enumerate.core.ServerExceptionEnum;
import com.goudong.commons.exception.ClientException;
import com.goudong.commons.exception.user.RoleException;
import com.goudong.commons.utils.JPAPageResultConvert;
import com.goudong.commons.utils.core.BeanUtil;
import com.goudong.user.dto.AddRoleReq;
import com.goudong.user.dto.BaseRole2QueryPageDTO;
import com.goudong.user.dto.BaseRoleDTO;
import com.goudong.user.dto.ModifyRoleReq;
import com.goudong.user.po.BaseRolePO;
import com.goudong.user.repository.BaseRoleRepository;
import com.goudong.user.service.BaseRoleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 类描述：
 *
 * @Author e-Feilong.Chen
 * @Date 2022/1/7 15:34
 */
@Service
public class BaseRoleServiceImpl implements BaseRoleService {
    private final BaseRoleRepository baseRoleRepository;

    public BaseRoleServiceImpl(BaseRoleRepository baseRoleRepository) {
        this.baseRoleRepository = baseRoleRepository;
    }

    /**
     * 查询预置的普通角色
     *
     * @return
     */
    @Override
    public BaseRolePO findByRoleUser() {
        String serverMessage = String.format("预置的角色：%s不存在", RoleConst.ROLE_USER);
        return baseRoleRepository.findByRoleName(RoleConst.ROLE_USER)
                .orElseThrow(()-> new RoleException(ServerExceptionEnum.SERVER_ERROR, serverMessage));
    }

    /**
     * 分页查询角色
     *
     * @param page
     * @return
     */
    @Override
    public BasePageResult<BaseRoleDTO> page(BaseRole2QueryPageDTO page) {
        PageRequest pageRequest = PageRequest.of(page.getJPAPage(),
                page.getJPASize(),
                Sort.sort(BaseRolePO.class).by(BaseRolePO::getCreateTime).descending());

        Specification<BaseRolePO> specification = (root, query, criteriaBuilder) -> {
            if (StringUtils.isNotBlank(page.getRoleNameCn())) {
                query.where(criteriaBuilder.like(root.get("roleNameCn"), page.getRoleNameCn() + "%"));
            }
            return query.getRestriction();
        };

        Page<BaseRolePO> all = baseRoleRepository.findAll(specification, pageRequest);

        BasePageResult<BaseRoleDTO> convert = JPAPageResultConvert.convert(all, BaseRoleDTO.class);

        return convert;
    }

    /**
     * 根据角色id集合查询角色
     *
     * @param roleIds
     * @return
     */
    @Override
    public List<BaseRoleDTO> listByIds(List<Long> roleIds) {
        Specification<BaseRolePO> specification = ((root, query, criteriaBuilder) -> query.where(root.get("id").in(roleIds)).getRestriction());

        List<BaseRolePO> roles = baseRoleRepository.findAll(specification);
        return BeanUtil.copyToList(roles, BaseRoleDTO.class, CopyOptions.create());
    }

    /**
     * 新增角色
     *
     * @param req
     * @return
     */
    @Override
    public BaseRoleDTO addRole(AddRoleReq req) {
        BaseRolePO rolePO = BeanUtil.copyProperties(req, BaseRolePO.class);
        rolePO.setRoleName("ROLE_" + req.getRoleNameCn());
        baseRoleRepository.save(rolePO);
        return BeanUtil.copyProperties(rolePO, BaseRoleDTO.class);
    }

    /**
     * 修改角色
     *
     * @param req
     * @return
     */
    @Override
    @Transactional
    public BaseRoleDTO modifyRole(ModifyRoleReq req) {
        BaseRolePO rolePO = baseRoleRepository.findById(req.getId()).orElseThrow(() -> ClientException.clientException(ClientExceptionEnum.NOT_FOUND, "角色不存在"));
        rolePO.setRoleNameCn(req.getRoleNameCn());
        rolePO.setRemark(req.getRemark());

        return BeanUtil.copyProperties(rolePO, BaseRoleDTO.class);
    }

    /**
     * 删除角色
     *
     * @param id
     * @return
     */
    @Override
    public BaseRoleDTO removeRole(Long id) {
        BaseRolePO rolePO = baseRoleRepository.findById(id).orElseThrow(() -> ClientException.clientException(ClientExceptionEnum.NOT_FOUND, "角色不存在"));
        baseRoleRepository.delete(rolePO);
        rolePO.setDeleted(true);
        return BeanUtil.copyProperties(rolePO, BaseRoleDTO.class);
    }
}
