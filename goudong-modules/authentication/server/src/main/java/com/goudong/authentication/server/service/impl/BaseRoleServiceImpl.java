package com.goudong.authentication.server.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import cn.zhxu.bs.BeanSearcher;
import cn.zhxu.bs.SearchResult;
import cn.zhxu.bs.util.MapUtils;
import com.goudong.authentication.server.rest.req.search.BaseRoleDropDown;
import com.goudong.authentication.server.rest.req.search.BaseRolePage;
import com.goudong.boot.redis.core.RedisTool;
import com.goudong.boot.web.core.ClientException;
import com.goudong.core.lang.PageResult;
import com.goudong.core.util.AssertUtil;
import com.goudong.authentication.server.constant.RoleConst;
import com.goudong.authentication.server.domain.BaseRole;
import com.goudong.authentication.server.repository.BaseRoleRepository;
import com.goudong.authentication.server.repository.BaseUserRoleRepository;
import com.goudong.authentication.server.rest.req.BaseRoleCreate;
import com.goudong.authentication.server.rest.req.BaseRoleUpdate;
import com.goudong.authentication.server.service.BaseRoleService;
import com.goudong.authentication.server.service.dto.BaseRoleDTO;
import com.goudong.authentication.server.service.dto.MyAuthentication;
import com.goudong.authentication.server.service.mapper.BaseRoleMapper;
import com.goudong.authentication.server.util.PageResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static com.goudong.authentication.server.enums.RedisKeyTemplateProviderEnum.ROLE_DROP_DOWN;

/**
 * Service Implementation for managing {@link BaseRole}.
 */
@Service
@Transactional
public class BaseRoleServiceImpl implements BaseRoleService {

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

    /**
     * 新增角色
     *
     * @param req
     * @return
     */
    @Override
    public BaseRoleDTO save(BaseRoleCreate req) {
        Assert.isFalse(Objects.equals(RoleConst.ROLE_APP_SUPER_ADMIN, req.getName()), () -> ClientException.client("添加角色失败"));
        MyAuthentication authentication = (MyAuthentication) SecurityContextHolder.getContext().getAuthentication();
        // 不是超级管理员
        if (!authentication.superAdmin()) {
            AssertUtil.isEquals(authentication.getAppId(), req.getAppId(), () -> ClientException.clientByForbidden("无权添加应用角色"));
        }
        BaseRole baseRole = BeanUtil.copyProperties(req, BaseRole.class);

        baseRoleRepository.save(baseRole);

        return baseRoleMapper.toDto(baseRole);
    }

    /**
     * 修改角色
     *
     * @param req
     * @return
     */
    @Override
    public BaseRoleDTO save(BaseRoleUpdate req) {
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
    @Override
    public List<BaseRoleDropDown> dropDown(BaseRoleDropDown req) {
        MyAuthentication authentication = (MyAuthentication)SecurityContextHolder.getContext().getAuthentication();
        Long appId = Optional.ofNullable(req.getAppId()).orElseGet(() -> authentication.getAppId());

        // redis key
        String key = ROLE_DROP_DOWN.getFullKey(appId);
        if (redisTool.hasKey(key)) {
            return redisTool.getList(ROLE_DROP_DOWN, BaseRoleDropDown.class, appId);
        }
        synchronized (this) {
            if (redisTool.hasKey(key)) {
                return redisTool.getList(ROLE_DROP_DOWN, BaseRoleDropDown.class, appId);
            }

            Map<String, Object> build = MapUtils.builder()
                    .field(BaseRoleDropDown::getAppId, appId)
                    .build();
            List<BaseRoleDropDown> list = beanSearcher.searchAll(BaseRoleDropDown.class, build);

            redisTool.set(ROLE_DROP_DOWN, list);

            return list;
        }
    }
}
