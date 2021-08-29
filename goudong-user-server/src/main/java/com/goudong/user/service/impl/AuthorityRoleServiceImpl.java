package com.goudong.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.goudong.commons.dto.AuthorityRoleDTO;
import com.goudong.commons.po.AuthorityRolePO;
import com.goudong.commons.pojo.PageParam;
import com.goudong.commons.utils.AuthorityUserUtil;
import com.goudong.commons.utils.BeanUtil;
import com.goudong.user.mapper.AuthorityRoleMapper;
import com.goudong.user.service.AuthorityRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 接口描述：
 * 角色服务层实现
 * @Author msi
 * @Date 2021-08-19 20:46
 * @Version 1.0
 */
@Service
public class AuthorityRoleServiceImpl extends ServiceImpl<AuthorityRoleMapper, AuthorityRolePO> implements AuthorityRoleService {


    private AuthorityUserUtil authorityUserUtil;

    @Autowired
    public void setAuthorityUserUtil(AuthorityUserUtil authorityUserUtil) {
        this.authorityUserUtil = authorityUserUtil;
    }

    /**
     * 批量添加角色
     *
     * @param authorityRoleDTOS
     * @return
     */
    @Override
    @Transactional
    public List<AuthorityRoleDTO> addRoles(List<AuthorityRoleDTO> authorityRoleDTOS) {
        List<AuthorityRolePO> authorityRolePOS = BeanUtil.copyList(authorityRoleDTOS, AuthorityRolePO.class);
        // todo 唯一
        super.saveBatch(authorityRolePOS);
        return BeanUtil.copyList(authorityRolePOS, AuthorityRoleDTO.class);
    }

    /**
     * 新增角色
     *
     * @param authorityRoleDTO
     * @return
     */
    @Override
    public AuthorityRoleDTO addRole(AuthorityRoleDTO authorityRoleDTO) {
        AuthorityRolePO authorityRolePO = BeanUtil.copyProperties(authorityRoleDTO, AuthorityRolePO.class);
        super.save(authorityRolePO);
        return BeanUtil.copyProperties(authorityRolePO, AuthorityRoleDTO.class);
    }

    /**
     * 更新角色信息
     *
     * @param authorityRoleDTO
     * @return
     */
    @Override
    public AuthorityRoleDTO updateRole(AuthorityRoleDTO authorityRoleDTO) {
        LambdaUpdateWrapper<AuthorityRolePO> updateWrapper = new LambdaUpdateWrapper();
        updateWrapper
                .set(AuthorityRolePO::getRoleName, authorityRoleDTO.getRoleName())
                .set(AuthorityRolePO::getRoleNameCn, authorityRoleDTO.getRoleNameCn())
                .set(AuthorityRolePO::getRemark, authorityRoleDTO.getRemark())
                .eq(AuthorityRolePO::getId, authorityRoleDTO.getId());

        super.update(updateWrapper);

        return BeanUtil.copyProperties(super.getById(authorityRoleDTO.getId()), AuthorityRoleDTO.class);
    }

    /**
     * 根据id删除
     *
     * @param id
     * @return
     */
    @Override
    public AuthorityRoleDTO deleteRole(Long id) {
        // 手动查询
        AuthorityRolePO byId = super.getById(id);
        boolean removed = super.removeById(id);
        // 手动查询
        if (removed && byId != null) {
            byId.setUpdateTime(LocalDateTime.now());
            byId.setUpdateUserId(authorityUserUtil.getUserId());
            byId.setDeleted(false);
        }
        return BeanUtil.copyProperties(byId, AuthorityRoleDTO.class);
    }

    @Override
    public Page<AuthorityRolePO> pageRoles(PageParam pageParam) {
        //创建Page对象
        Page<AuthorityRolePO> rolePOPage = new Page<>(pageParam.getCurrent(),pageParam.getSize());

        //构建条件
        QueryWrapper<AuthorityRolePO> wrapper = new QueryWrapper<>();
        //直接传wrapper，或不传wrapper传一个实体或Map,wrapper在Service中定义
        Page<AuthorityRolePO> page = super.page(rolePOPage);
        return page;
    }
}
