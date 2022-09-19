package com.goudong.user.service.impl;

import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.lang.Assert;
import com.goudong.commons.constant.user.RoleConst;
import com.goudong.commons.core.context.UserContext;
import com.goudong.commons.dto.core.BasePageResult;
import com.goudong.commons.dto.oauth2.BaseMenuDTO;
import com.goudong.commons.enumerate.core.ClientExceptionEnum;
import com.goudong.commons.enumerate.core.ServerExceptionEnum;
import com.goudong.commons.exception.ClientException;
import com.goudong.commons.exception.user.RoleException;
import com.goudong.commons.framework.redis.RedisTool;
import com.goudong.commons.tree.v2.Tree;
import com.goudong.commons.utils.JPAPageResultConvert;
import com.goudong.commons.utils.core.BeanUtil;
import com.goudong.user.dto.AddRoleReq;
import com.goudong.user.dto.BaseRole2QueryPageDTO;
import com.goudong.user.dto.BaseRoleDTO;
import com.goudong.user.dto.ModifyRoleReq;
import com.goudong.user.po.BaseMenuPO;
import com.goudong.user.po.BaseRolePO;
import com.goudong.user.repository.BaseMenuRepository;
import com.goudong.user.repository.BaseRoleRepository;
import com.goudong.user.service.BaseMenuService;
import com.goudong.user.service.BaseRoleService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 类描述：
 *
 * @Author e-Feilong.Chen
 * @Date 2022/1/7 15:34
 */
@Service
@RequiredArgsConstructor
public class BaseRoleServiceImpl implements BaseRoleService {
    private final BaseRoleRepository baseRoleRepository;

    private final BaseMenuRepository baseMenuRepository;

    private final BaseMenuService baseMenuService;

    private final RedisTool redisTool;


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
    @Transactional
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
        rolePO.setRoleName("ROLE_" + req.getRoleNameCn());
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
        return BeanUtil.copyProperties(rolePO, BaseRoleDTO.class);
    }

    /**
     * 根据id查询角色
     *
     * @param id
     * @return
     */
    @Override
    @Transactional
    public BaseRoleDTO getById(Long id) {
        BaseRolePO rolePO = baseRoleRepository.findById(id)
                .orElseThrow(() -> ClientException.clientException(ClientExceptionEnum.NOT_FOUND, "角色不存在"));

        // 当前用户所拥有的菜单权限，不能越级设置权限
        List<com.goudong.commons.dto.oauth2.BaseRoleDTO> roles = UserContext.get().getRoles();
        List<String> roleNames = roles.stream().map(m -> m.getRoleName()).collect(Collectors.toList());
        List<BaseMenuDTO> permissions = baseMenuService.findAllByRoleNames(roleNames);

        BaseRoleDTO baseRoleDTO = BeanUtil.copyProperties(rolePO, BaseRoleDTO.class);
        List<Long> menuIds = baseRoleDTO.getMenus().stream().map(BaseMenuDTO::getId).collect(Collectors.toList());

        // 拥有的权限
        permissions.stream().forEach(p -> {
            if (menuIds.contains(p.getId())) {
                p.setChecked(true);
            }
        });

        // 转换成Tree
        baseRoleDTO.setPermission(Tree.getInstance().toTree(permissions));

        return baseRoleDTO;
    }

    /**
     * 修改角色的权限
     *
     * @param id      角色id
     * @param menuIds 菜单
     */
    @Override
    @Transactional
    public BaseRoleDTO updatePermissions(Long id, List<Long> menuIds) {
        BaseRolePO rolePO = baseRoleRepository.findById(id).orElseThrow(() -> ClientException.clientException(ClientExceptionEnum.NOT_FOUND, "角色不存在"));

        // 校验数据
        List<com.goudong.commons.dto.oauth2.BaseRoleDTO> roles = UserContext.get().getRoles();
        List<String> roleNames = roles.stream().map(m -> m.getRoleName()).collect(Collectors.toList());
        List<BaseMenuDTO> permissions = baseMenuService.findAllByRoleNames(roleNames);
        List<Long> hasMenuIds = permissions.stream().map(BaseMenuDTO::getId).collect(Collectors.toList());

        Assert.isTrue(hasMenuIds.containsAll(menuIds), ()->ClientException.clientException(ClientExceptionEnum.FORBIDDEN, "暂无权限", "当前用户没权限没有权限设置的部分权限"));

        List<BaseMenuPO> menus = baseMenuRepository.findAllById(menuIds);
        if (menus.size() == menuIds.size()) {
            rolePO.setMenus(menus);
            return BeanUtil.copyProperties(rolePO, BaseRoleDTO.class);
        }

        throw ClientException.clientException(ClientExceptionEnum.BAD_REQUEST, "菜单无效");
    }


}
