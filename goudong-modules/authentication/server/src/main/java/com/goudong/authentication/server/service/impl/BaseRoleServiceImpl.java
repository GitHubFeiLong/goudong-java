package com.goudong.authentication.server.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.lang.Assert;
import cn.zhxu.bs.BeanSearcher;
import cn.zhxu.bs.SearchResult;
import cn.zhxu.bs.util.MapUtils;
import com.goudong.authentication.server.constant.RoleConst;
import com.goudong.authentication.server.domain.BaseMenu;
import com.goudong.authentication.server.domain.BaseRole;
import com.goudong.authentication.server.domain.BaseUser;
import com.goudong.authentication.server.repository.BaseRoleRepository;
import com.goudong.authentication.server.repository.BaseUserRoleRepository;
import com.goudong.authentication.server.rest.req.BaseRoleCreateReq;
import com.goudong.authentication.server.rest.req.BaseRolePageReq;
import com.goudong.authentication.server.rest.req.BaseRoleUpdateReq;
import com.goudong.authentication.server.rest.req.search.BaseRoleDropDownReq;
import com.goudong.authentication.server.rest.req.search.BaseRolePage;
import com.goudong.authentication.server.rest.resp.BaseRoleDropDownResp;
import com.goudong.authentication.server.rest.resp.BaseRolePageResp;
import com.goudong.authentication.server.rest.resp.BaseUserDropDownResp;
import com.goudong.authentication.server.service.BaseRoleService;
import com.goudong.authentication.server.service.dto.BaseMenuDTO;
import com.goudong.authentication.server.service.dto.BaseRoleDTO;
import com.goudong.authentication.server.service.dto.MyAuthentication;
import com.goudong.authentication.server.service.mapper.BaseRoleMapper;
import com.goudong.authentication.server.util.BeanSearcherUtil;
import com.goudong.authentication.server.util.PageResultUtil;
import com.goudong.authentication.server.util.SecurityContextUtil;
import com.goudong.boot.redis.core.RedisTool;
import com.goudong.boot.web.core.ClientException;
import com.goudong.core.lang.PageResult;
import com.goudong.core.util.AssertUtil;
import com.goudong.core.util.CollectionUtil;
import com.goudong.core.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.goudong.authentication.server.enums.RedisKeyTemplateProviderEnum.ROLE_DROP_DOWN;

/**
 * Service Implementation for managing {@link BaseRole}.
 */
@Service
@Transactional
public class BaseRoleServiceImpl implements BaseRoleService {
    //~fields
    //==================================================================================================================
    private final Logger log = LoggerFactory.getLogger(BaseRoleServiceImpl.class);
    @Resource
    private BaseRoleRepository baseRoleRepository;

    @Resource
    private BaseRoleMapper baseRoleMapper;

    @Resource
    private BaseUserRoleRepository baseUserRoleRepository;

    @Resource
    private RedisTool redisTool;

    @Resource
    private BeanSearcher beanSearcher;

    //~methods
    //==================================================================================================================

    /**
     * 用户所在应用下的角色下拉
     *
     * @param req 条件查询参数
     * @return 角色下拉分页对象
     */
    @Override
    public PageResult<BaseRoleDropDownResp> roleDropDown(BaseRoleDropDownReq req) {
        MyAuthentication myAuthentication = SecurityContextUtil.get();
        req.setAppId(myAuthentication.getRealAppId());
        SearchResult<BaseRoleDropDownReq> search = beanSearcher.search(BaseRoleDropDownReq.class, BeanSearcherUtil.getParaMap(req));

        return PageResultUtil.convert(search, req, BaseRoleDropDownResp.class);
    }

    /**
     * 根据角色id集合查询角色
     *
     * @param ids 需要查询的角色id集合
     * @return 角色集合
     */
    @Override
    public List<BaseRole> listByIds(List<Long> ids) {
        return baseRoleRepository.findAllById(ids);
    }

    /**
     * 分页查询角色列表
     *
     * @param req 条件查询参数
     * @return 角色分页列表
     */
    @Override
    @Transactional(readOnly = true)
    public PageResult<BaseRolePageResp> page(BaseRolePageReq req) {
        MyAuthentication myAuthentication = SecurityContextUtil.get();

        Specification<BaseRole> specification = new Specification<BaseRole>() {
            @Override
            public Predicate toPredicate(Root<BaseRole> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> andPredicateList = new ArrayList<>();
                andPredicateList.add(criteriaBuilder.equal(root.get("appId"), myAuthentication.getRealAppId()));

                if (CollectionUtil.isNotEmpty(req.getIds())) {
                    CriteriaBuilder.In<Object> ids = criteriaBuilder.in(root.get("id"));
                    req.getIds().forEach(id -> ids.value(id));
                    andPredicateList.add(ids);
                }
                if (StringUtil.isNotBlank(req.getName())) {
                    Predicate name = criteriaBuilder.like(root.get("name").as(String.class), "%" + req.getName() + "%");
                    andPredicateList.add(name);
                }
                if (StringUtil.isNotBlank(req.getRemark())) {
                    Predicate remark = criteriaBuilder.like(root.get("remark").as(String.class), "%" + req.getRemark() + "%");
                    andPredicateList.add(remark);
                }

                return criteriaBuilder.and(andPredicateList.toArray(new Predicate[andPredicateList.size()]));
            }
        };

        Pageable pageable = PageRequest.of(req.getPage(), req.getSize(), Sort.by("createdDate").descending());

        Page<BaseRole> rolePage = baseRoleRepository.findAll(specification, pageable);

        List<BaseRolePageResp> contents = new ArrayList<>(rolePage.getContent().size());
        AtomicLong serialNumber = new AtomicLong(req.getStartSerialNumber());
        rolePage.getContent().forEach(p -> {
            BaseRolePageResp baseRolePageResp = new BaseRolePageResp();
            baseRolePageResp.setSerialNumber(serialNumber.getAndIncrement());
            baseRolePageResp.setAppId(p.getAppId());
            baseRolePageResp.setId(p.getId());
            baseRolePageResp.setName(p.getName());
            baseRolePageResp.setRemark(p.getRemark());
            baseRolePageResp.setCreatedDate(p.getCreatedDate());
            List<BaseUserDropDownResp> users = new ArrayList<>(p.getUsers().size());
            p.getUsers().forEach(user -> {
                users.add(new BaseUserDropDownResp(user.getId(), user.getUsername()));
            });
            baseRolePageResp.setUsers(users);

            contents.add(baseRolePageResp);
        });

        return new PageResult<BaseRolePageResp>(rolePage.getTotalElements(),
                (long)rolePage.getTotalPages(),
                rolePage.getPageable().getPageNumber() + 1L,
                (long)rolePage.getPageable().getPageSize(),
                contents
        );
    }

    /**
     * 新增角色
     *
     * @param req
     * @return
     */
    @Override
    @Transactional
    public BaseRoleDTO save(BaseRoleCreateReq req) {
        Assert.isFalse(Objects.equals(RoleConst.ROLE_APP_SUPER_ADMIN, req.getName()), () -> ClientException.client("添加角色失败"));
        Assert.isFalse(Objects.equals(RoleConst.ROLE_APP_ADMIN, req.getName()), () -> ClientException.client("添加角色失败"));

        MyAuthentication myAuthentication = SecurityContextUtil.get();

        BaseRole baseRole = BeanUtil.copyProperties(req, BaseRole.class);
        baseRole.setAppId(myAuthentication.getRealAppId());
        baseRoleRepository.save(baseRole);

        return baseRoleMapper.toDto(baseRole);
    }

    /**
     * 修改角色
     *
     * @param req 需要修改的角色信息
     * @return 修改后角色信息
     */
    @Override
    @Transactional
    public BaseRoleDTO update(BaseRoleUpdateReq req) {
        MyAuthentication myAuthentication = SecurityContextUtil.get();
        BaseRole baseRole = baseRoleRepository.findById(req.getId()).orElseThrow(() -> ClientException.client("角色不存在"));
        AssertUtil.isEquals(myAuthentication.getRealAppId(), baseRole.getAppId(), () -> ClientException.client("角色不存在").serverMessage("不能操作其它应用下的角色"));

        baseRole.setName(req.getName());
        baseRole.setRemark(req.getRemark());
        baseRole.setLastModifiedBy(myAuthentication.getUsername());
        baseRole.setLastModifiedDate(new Date());

        return baseRoleMapper.toDto(baseRole);
    }

    /**
     * 批量删除角色
     *
     * @param ids 删除的id集合
     * @return true删除成功；false删除失败
     */
    @Override
    public Boolean deleteByIds(List<Long> ids) {
        MyAuthentication myAuthentication = SecurityContextUtil.get();
        Long realAppId = myAuthentication.getRealAppId();
        List<BaseRole> allById = baseRoleRepository.findAllById(ids);
        allById.forEach(p -> AssertUtil.isEquals(realAppId, p.getAppId(), () -> ClientException.clientByForbidden().serverMessage("不能删除其它应用下的角色")));
        baseRoleRepository.deleteAll(allById);
        return true;
    }

    /**
     * 根据id查询用户
     *
     * @param id 用户id
     * @return 用户对象
     */
    @Override
    public BaseRole findById(Long id) {
        BaseRole baseRole = baseRoleRepository.findById(id).orElseThrow(() -> ClientException.client("角色不存在"));
        return baseRole;
    }

    /**
     * 查询登录用户所拥有的权限
     * @return 权限集合
     */
    @Override
    @Transactional(readOnly = true)
    public List<BaseMenuDTO> listPermissionsByLoginUser() {
        MyAuthentication myAuthentication = SecurityContextUtil.get();
        List<String> roleNames = myAuthentication.getRoles().stream().map(SimpleGrantedAuthority::getAuthority).collect(Collectors.toList());
        List<BaseRole> baseRoles = baseRoleRepository.findAll((root, query, criteriaBuilder) -> {
            List<Predicate> andPredicateList = new ArrayList<>();
            andPredicateList.add(criteriaBuilder.equal(root.get("appId"), myAuthentication.getRealAppId()));
            if (CollectionUtil.isNotEmpty(roleNames)) {
                CriteriaBuilder.In<Object> names = criteriaBuilder.in(root.get("name"));
                roleNames.forEach(name -> names.value(name));
                andPredicateList.add(names);
            }
            return criteriaBuilder.and(andPredicateList.toArray(new Predicate[andPredicateList.size()]));
        });

        List<BaseMenu> all = baseRoles.stream().map(BaseRole::getMenus).flatMap(List::stream).collect(Collectors.toList());

        // 进行去重
        List<BaseMenu> values = all.stream()
                .collect(Collectors.toMap(k -> k.getId(), p -> p, (k1, k2) -> k1))
                .values()
                .stream()
                // 排序下
                .sorted(new Comparator<BaseMenu>() {
                    @Override
                    public int compare(BaseMenu o1, BaseMenu o2) {
                        // 返回正数：升序，返回负数 降序
                        return o1.getId() > o2.getId() ? 1 : -1 ;
                    }
                })
                .collect(Collectors.toList());

        return BeanUtil.copyToList(values, BaseMenuDTO.class, CopyOptions.create());
    }

    //==待删除


    /**
     * 修改角色
     *
     * @param req
     * @return
     */
    @Override
    public BaseRoleDTO save(BaseRoleUpdateReq req) {
        BaseRole baseRole = baseRoleRepository.findById(req.getId()).orElseThrow(() -> ClientException.client("角色不存在"));

        MyAuthentication authentication = (MyAuthentication) SecurityContextHolder.getContext().getAuthentication();
        // 不是超级管理员
        if (!authentication.superAdmin()) {
            AssertUtil.isEquals(authentication.getAppId(), baseRole.getAppId(), () -> ClientException.clientByForbidden("无权修改应用角色"));
        }

        baseRole.setRemark(req.getRemark());

        baseRoleRepository.save(baseRole);

        return baseRoleMapper.toDto(baseRole);
    }

    /**
     * Get all the baseRoles.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<BaseRoleDTO> findAll(Pageable pageable) {
        log.debug("Request to get all BaseRoles");
        return baseRoleRepository.findAll(pageable)
            .map(baseRoleMapper::toDto);
    }

    /**
     * Get one baseRole by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<BaseRoleDTO> findOne(Long id) {
        log.debug("Request to get BaseRole : {}", id);
        return baseRoleRepository.findById(id)
            .map(baseRoleMapper::toDto);
    }

    /**
     * 删除角色
     * @param id
     * @return
     */
    @Override
    public boolean delete(Long id) {
        log.debug("Request to delete BaseRole : {}", id);
        BaseRole baseRole = baseRoleRepository.findById(id).orElseThrow(() -> ClientException.client("角色不存在"));
        MyAuthentication authentication = (MyAuthentication) SecurityContextHolder.getContext().getAuthentication();
        // 不是超级管理员
        if (!authentication.superAdmin()) {
            AssertUtil.isEquals(authentication.getAppId(), baseRole.getAppId(), () -> ClientException.clientByForbidden("无权删除应用角色"));
        }

        // 查询角色下的用户
        // int count = baseUserRoleRepository.countByRoleId(id);
        int count = 0;

        AssertUtil.isTrue(count == 0, () -> ClientException.client("删除角色失败"));
        baseRoleRepository.deleteById(id);

        return true;
    }

    @Override
    public PageResult page(BaseRolePage req) {
        MyAuthentication authentication = (MyAuthentication) SecurityContextHolder.getContext().getAuthentication();
        // 不是超级管理员
        if (!authentication.superAdmin()) {
            AssertUtil.isEquals(authentication.getAppId(), req.getAppId(), () -> ClientException.clientByForbidden("无权访问该应用"));
        }

        Map<String, Object> build = MapUtils.builder()
                .page(req.getPage(), req.getSize())
                .field(BaseRolePage::getAppId, req.getAppId())
                .build();
        SearchResult<BaseRolePage> search = beanSearcher.search(BaseRolePage.class,  build);

        return PageResultUtil.convert(search, req);
    }



    /**
     * 角色下拉
     * @param req
     * @return
     */
    // @Override
    public List<BaseRoleDropDownReq> dropDown(BaseRoleDropDownReq req) {
        MyAuthentication authentication = (MyAuthentication)SecurityContextHolder.getContext().getAuthentication();
        Long appId = Optional.ofNullable(req.getAppId()).orElseGet(() -> authentication.getAppId());

        // redis key
        String key = ROLE_DROP_DOWN.getFullKey(appId);
        if (redisTool.hasKey(key)) {
            return redisTool.getList(ROLE_DROP_DOWN, BaseRoleDropDownReq.class, appId);
        }
        synchronized (this) {
            if (redisTool.hasKey(key)) {
                return redisTool.getList(ROLE_DROP_DOWN, BaseRoleDropDownReq.class, appId);
            }

            Map<String, Object> build = MapUtils.builder()
                    .field(BaseRoleDropDownReq::getAppId, appId)
                    .build();
            List<BaseRoleDropDownReq> list = beanSearcher.searchAll(BaseRoleDropDownReq.class, build);

            redisTool.set(ROLE_DROP_DOWN, list);

            return list;
        }
    }


}
