package com.goudong.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.lang.Assert;
import com.goudong.boot.redis.context.UserContext;
import com.goudong.boot.redis.core.RedisTool;
import com.goudong.boot.web.core.ClientException;
import com.goudong.boot.web.enumerate.ClientExceptionEnum;
import com.goudong.boot.web.enumerate.ServerExceptionEnum;
import com.goudong.boot.web.util.PageResultConvert;
import com.goudong.commons.constant.user.RoleConst;
import com.goudong.commons.dto.oauth2.BaseMenuDTO;
import com.goudong.commons.dto.oauth2.BaseUserDTO;
import com.goudong.core.lang.PageResult;
import com.goudong.core.util.CollectionUtil;
import com.goudong.core.util.StringUtil;
import com.goudong.core.util.tree.v2.Tree;
import com.goudong.user.dto.AddRoleReq;
import com.goudong.user.dto.BaseRole2QueryPageDTO;
import com.goudong.user.dto.BaseRoleDTO;
import com.goudong.user.dto.ModifyRoleReq;
import com.goudong.user.enumerate.RedisKeyProviderEnum;
import com.goudong.user.exception.RoleException;
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

import javax.persistence.criteria.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
    public PageResult<BaseRoleDTO> page(BaseRole2QueryPageDTO page) {
        PageRequest pageRequest = PageRequest.of(page.getJPAPage(),
                page.getSize(),
                Sort.sort(BaseRolePO.class).by(BaseRolePO::getCreateTime).descending());

        Specification<BaseRolePO> specification = (root, query, criteriaBuilder) -> {
            if (StringUtils.isNotBlank(page.getRoleNameCn())) {
                query.where(criteriaBuilder.like(root.get("roleNameCn"), page.getRoleNameCn() + "%"));
            }
            if (StringUtils.isNotBlank(page.getRoleName())) {
                query.where(criteriaBuilder.like(root.get("roleName"),  "%" + page.getRoleName().toUpperCase() + "%"));
            }
            if (StringUtils.isNotBlank(page.getRemark())) {
                query.where(criteriaBuilder.like(root.get("remark"), page.getRemark() + "%"));
            }
            return query.getRestriction();
        };

        Page<BaseRolePO> all = baseRoleRepository.findAll(specification, pageRequest);

        PageResult<BaseRoleDTO> convert = PageResultConvert.convert(all, BaseRoleDTO.class);

        // 用户数量 （key 角色id， value 用户数量）
        Map<Long, Integer> longIntegerMap = all.stream().collect(Collectors.toMap(BaseRolePO::getId, p -> p.getUsers().size(), (k1, k2) -> k1));

        // 设置用户数量
        convert.getContent().stream().forEach(p -> {
            p.setUserNumbers(Optional.ofNullable(longIntegerMap.get(p.getId())).orElseGet(() -> 0));
        });
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
        BaseRolePO rolePO = baseRoleRepository.findById(req.getId()).orElseThrow(() -> ClientException.client(ClientExceptionEnum.NOT_FOUND, "角色不存在"));
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
    @Transactional
    public BaseRoleDTO removeRole(Long id) {
        BaseRolePO rolePO = baseRoleRepository.findById(id).orElseThrow(() -> ClientException.client(ClientExceptionEnum.NOT_FOUND, "角色不存在"));
        if (CollectionUtil.isNotEmpty(rolePO.getUsers())) {
            throw RoleException.client("角色删除失败", String.format("角色%s不能被删除,该角色拥有%d个用户", rolePO.getRoleNameCn(), rolePO.getUsers().size()));
        }
        baseRoleRepository.delete(rolePO);
        return BeanUtil.copyProperties(rolePO, BaseRoleDTO.class);
    }

    /**
     * 删除角色
     *
     * @param ids
     * @return
     */
    @Override
    @Transactional
    public boolean removeRoles(List<Long> ids) {
        List<BaseRolePO> baseRolePOS = baseRoleRepository.findAllById(ids);
        // 查询是否有用户在使用角色
        baseRolePOS.stream().forEach(p -> {
            if (CollectionUtil.isNotEmpty(p.getUsers())) {
                throw RoleException.client("角色删除失败", String.format("角色%s不能被删除,该角色拥有%d个用户", p.getRoleNameCn(), p.getUsers().size()));
            }
        });
        baseRoleRepository.deleteAll(baseRolePOS);
        return true;
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
                .orElseThrow(() -> ClientException.client(ClientExceptionEnum.NOT_FOUND, "角色不存在"));

        // 当前用户所拥有的菜单权限，不能越级设置权限
        List<com.goudong.commons.dto.oauth2.BaseRoleDTO> roles = ((BaseUserDTO)(UserContext.get())).getRoles();
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
        BaseRolePO rolePO = baseRoleRepository.findById(id).orElseThrow(() -> ClientException.client(ClientExceptionEnum.NOT_FOUND, "角色不存在"));

        // 校验数据
        List<com.goudong.commons.dto.oauth2.BaseRoleDTO> roles = ((BaseUserDTO)(UserContext.get())).getRoles();
        List<String> roleNames = roles.stream().map(m -> m.getRoleName()).collect(Collectors.toList());
        List<BaseMenuDTO> permissions = baseMenuService.findAllByRoleNames(roleNames);
        List<Long> hasMenuIds = permissions.stream().map(BaseMenuDTO::getId).collect(Collectors.toList());

        Assert.isTrue(hasMenuIds.containsAll(menuIds), ()->ClientException.client(ClientExceptionEnum.FORBIDDEN, "暂无权限", "当前用户没权限没有权限设置的部分权限"));

        List<BaseMenuPO> menus = baseMenuRepository.findAllById(menuIds);
        if (menus.size() == menuIds.size()) {
            rolePO.setMenus(menus);

            // 删除redis中的当前角色
            redisTool.deleteKey(RedisKeyProviderEnum.MENU_ROLE, rolePO.getRoleName());
            return BeanUtil.copyProperties(rolePO, BaseRoleDTO.class);
        }

        throw ClientException.client(ClientExceptionEnum.BAD_REQUEST, "菜单无效");
    }

    /**
     * 角色名模糊分页查询
     *
     * @param page
     * @return
     */
    @Override
    @Transactional
    public PageResult<BaseRoleDTO> pageRoleName(BaseRole2QueryPageDTO page) {
        Specification<BaseRolePO> specification = new Specification<BaseRolePO>() {
            @Override
            public Predicate toPredicate(Root<BaseRolePO> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                Predicate predicate = null;
                if (StringUtil.isNotBlank(page.getRoleNameCn())) {
                    Path<String> username = root.get("role_name_CN");
                    predicate = criteriaBuilder.like(username, page.getRoleNameCn() + "%");
                }
                return predicate;
            }
        };

        PageRequest pageRequest = PageRequest.of(page.getJPAPage(), page.getSize(), Sort.sort(BaseRolePO.class).by(BaseRolePO::getCreateTime).descending());

        Page<BaseRolePO> all = baseRoleRepository.findAll(specification, pageRequest);

        PageResult<BaseRoleDTO> convert = PageResultConvert.convert(all, BaseRoleDTO.class);

        return convert;
    }


}
