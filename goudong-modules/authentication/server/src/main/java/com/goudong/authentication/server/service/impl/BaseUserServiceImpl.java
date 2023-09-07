package com.goudong.authentication.server.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.zhxu.bs.BeanSearcher;
import cn.zhxu.bs.SearchResult;
import cn.zhxu.bs.operator.Between;
import cn.zhxu.bs.operator.InList;
import cn.zhxu.bs.util.MapBuilder;
import cn.zhxu.bs.util.MapUtils;
import com.goudong.authentication.common.core.LoginResp;
import com.goudong.authentication.server.domain.BaseRole;
import com.goudong.authentication.server.domain.BaseUser;
import com.goudong.authentication.server.repository.BaseUserRepository;
import com.goudong.authentication.server.rest.req.BaseUserCreate;
import com.goudong.authentication.server.rest.req.BaseUserPageReq;
import com.goudong.authentication.server.rest.req.BaseUserUpdate;
import com.goudong.authentication.server.rest.req.search.BaseUserDropDown;
import com.goudong.authentication.server.rest.req.search.BaseUserPageSearchReq;
import com.goudong.authentication.server.rest.req.search.SelectUsersRoleNames;
import com.goudong.authentication.server.rest.resp.BaseUserDropDownResp;
import com.goudong.authentication.server.service.BaseUserService;
import com.goudong.authentication.server.service.dto.BaseUserDTO;
import com.goudong.authentication.server.service.dto.MyAuthentication;
import com.goudong.authentication.server.service.mapper.BaseUserMapper;
import com.goudong.authentication.server.util.BeanSearcherUtil;
import com.goudong.authentication.server.util.PageResultUtil;
import com.goudong.authentication.server.util.SecurityContextUtil;
import com.goudong.boot.redis.core.RedisTool;
import com.goudong.boot.web.core.ClientException;
import com.goudong.core.lang.PageResult;
import com.goudong.core.util.AssertUtil;
import com.goudong.core.util.CollectionUtil;
import com.goudong.core.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.goudong.authentication.server.enums.RedisKeyTemplateProviderEnum.APP_DROP_DOWN;

/**
 * Service Implementation for managing {@link BaseUser}.
 */
@Slf4j
@Service
public class BaseUserServiceImpl implements BaseUserService {

    //~fields
    //==================================================================================================================
    @Resource
    private BaseUserRepository baseUserRepository;

    @Resource
    private BaseUserMapper baseUserMapper;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private BeanSearcher beanSearcher;

    @Resource
    private RedisTool redisTool;

    //~methods
    //==================================================================================================================
    /**
     * 根据应用Id和用户名查询用户
     *
     * @param appId    应用Id
     * @param username 用户名
     * @return 返回用户
     */
    @Override
    @Transactional
    public BaseUser findOneByAppIdAndUsername(Long appId, String username) {
        BaseUser baseUser = baseUserRepository.findByLogin(appId, username);
        // 懒加载,必须使用才能加载
        if (baseUser !=null) {
            List<String> roleNames = baseUser.getRoles().stream().map(BaseRole::getName).collect(Collectors.toList());
        }
        return baseUser;
    }

    /**
     * 根据id查询用户
     *
     * @param id 用户id
     * @return 用户对象
     */
    @Override
    public BaseUser findById(Long id) {
        // 查询角色，权限使用
        BaseUser baseUser = baseUserRepository.findById(id).orElseThrow(() -> ClientException.client("用户不存在"));
        baseUser.getRoles().stream().map(BaseRole::getMenus).collect(Collectors.toList());
        return baseUser;
    }

    /**
     * 根据id查询用户
     * @param id 用户id
     * @return 用户对象详细信息（保留角色菜单）
     */
    @Override
    public BaseUser findDetailById(Long id) {
        // 查询角色，权限使用
        BaseUser baseUser = baseUserRepository.findById(id).orElseThrow(() -> ClientException.client("用户不存在"));
        // 简单获取角色和菜单，即可进行查询
        List<String> roleNames = new ArrayList<>();
        List<String> menuNames = new ArrayList<>();
        baseUser.getRoles().forEach(role -> {
            roleNames.add(role.getName());
            role.getMenus().forEach(menu -> {
                menuNames.add(menu.getName());
            });
        });
        return baseUser;
    }

    /**
     * 分页获取用户下拉，只返回操作人所在真实应用下的用户
     *
     * @param req 请求参数
     * @return 用户下拉列表
     */
    @Override
    public PageResult<BaseUserDropDownResp> userDropDown(BaseUserDropDown req) {
        MyAuthentication authentication = SecurityContextUtil.get();
        req.setRealAppId(authentication.getRealAppId());
        SearchResult<BaseUserDropDown> search = beanSearcher.search(BaseUserDropDown.class, BeanSearcherUtil.getParaMap(req));
        return PageResultUtil.convert(search, req, BaseUserDropDownResp.class);
    }

    /**
     * 分页查询用户
     *
     * @param req 分页参数
     * @return 用户分页对象
     */
    @Override
    public PageResult<BaseUserPageSearchReq> page(BaseUserPageReq req) {
        MyAuthentication myAuthentication = SecurityContextUtil.get();
        MapBuilder builder = MapUtils.builder();
        builder.page(req.getPage(), req.getSize());
        builder.field(BaseUserPageSearchReq::getAppId, myAuthentication.getRealAppId());
        // 其它查询参数
        if (req.getId() != null) {
            builder.field(BaseUserPageSearchReq::getId, req.getId());
        }
        if (StringUtil.isNotBlank(req.getUsername())) {
            builder.field(BaseUserPageSearchReq::getUsername, req.getUsername());
        }
        if (req.getStartValidTime() != null && req.getEndValidTime() != null) {
            builder.field(BaseUserPageSearchReq::getValidTime, req.getStartValidTime(), req.getEndValidTime()).op(Between.class);
        }

        Map<String, Object> build = builder.build();
        SearchResult<BaseUserPageSearchReq> search = beanSearcher.search(BaseUserPageSearchReq.class,  build);

        if (search.getTotalCount().longValue() > 0) {
            // 用户id集合
            List<Long> userIds = search.getDataList().stream().map(BaseUserPageSearchReq::getId).collect(Collectors.toList());

            // 查询角色
            List<SelectUsersRoleNames> selectUsersRoleNames = beanSearcher.searchAll(SelectUsersRoleNames.class, MapUtils.builder()
                    .field(SelectUsersRoleNames::getUserId, userIds).op(InList.class)
                    .build());

            Map<Long, List<SelectUsersRoleNames>> map = selectUsersRoleNames.stream().collect(Collectors.groupingBy(SelectUsersRoleNames::getUserId));

            search.getDataList().stream().forEach(p -> {
                List<SelectUsersRoleNames> roles = map.get(p.getId());
                if (CollectionUtil.isNotEmpty(roles)) {
                    p.setRoles(roles);
                }
            });
        }

        return PageResultUtil.convert(search, req, BaseUserPageSearchReq.class);
    }












    // ===========


    /**
     * 新增用户
     * @param req
     * @return
     */
    @Override
    public BaseUserDTO save(BaseUserCreate req) {
        MyAuthentication authentication = (MyAuthentication)SecurityContextHolder.getContext().getAuthentication();
        // 不是超级管理员不能新增其它app用户只能新增自己app用户
        if (!authentication.superAdmin()) {
            AssertUtil.isEquals(authentication.getAppId(), req.getAppId(), () -> ClientException.clientByForbidden("无权添加应用用户"));
        }

        BaseUser baseUser = BeanUtil.copyProperties(req, BaseUser.class);
        // 密码加密
        baseUser.setPassword(passwordEncoder.encode(req.getPassword()));
        baseUser.setEnabled(true);
        baseUser.setLocked(false);
        baseUserRepository.save(baseUser);
        redisTool.deleteKey(APP_DROP_DOWN, req.getAppId());

        return baseUserMapper.toDto(baseUser);
    }

    /**
     * 修改用户
     *
     * @param req
     * @return
     */
    @Override
    public BaseUserDTO save(BaseUserUpdate req) {
        MyAuthentication authentication = (MyAuthentication)SecurityContextHolder.getContext().getAuthentication();

        BaseUser baseUser = baseUserRepository.findById(req.getId()).orElseThrow(() -> ClientException.client("用户不存在"));

        // 不是超级管理员不能新增其它app用户只能修改自己app用户
        if (!authentication.superAdmin()) {
            AssertUtil.isEquals(authentication.getAppId(), baseUser.getAppId(), () -> ClientException.clientByForbidden("无权修改应用用户"));
        }
        baseUser.setRemark(req.getRemark());
        if (StringUtils.isNotBlank(req.getPassword())) {
            baseUser.setPassword(passwordEncoder.encode(req.getPassword()));
        }

        baseUser.setEnabled(Optional.ofNullable(req.getEnabled()).orElseGet(() -> baseUser.getLocked()));
        baseUser.setLocked(Optional.ofNullable(req.getLocked()).orElseGet(() -> baseUser.getLocked()));
        baseUser.setValidTime(Optional.ofNullable(req.getValidTime()).orElseGet(() -> baseUser.getValidTime()));

        baseUserRepository.save(baseUser);
        return baseUserMapper.toDto(baseUser);
    }

    /**
     * Get all the baseUsers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<BaseUserDTO> findAll(Pageable pageable) {
        log.debug("Request to get all BaseUsers");
        return baseUserRepository.findAll(pageable)
            .map(baseUserMapper::toDto);
    }





    /**
     * Get one baseUser by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<BaseUserDTO> findOne(Long id) {
        log.debug("Request to get BaseUser : {}", id);
        return baseUserRepository.findById(id)
            .map(baseUserMapper::toDto);
    }

    /**
     * Delete the baseUser by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public Boolean delete(Long id) {
        log.debug("Request to delete BaseUser : {}", id);
        MyAuthentication authentication = (MyAuthentication)SecurityContextHolder.getContext().getAuthentication();

        BaseUser baseUser = baseUserRepository.findById(id).orElseThrow(() -> ClientException.client("用户不存在"));

        // 不是超级管理员不能新增其它app用户只能删除自己app用户
        if (!authentication.superAdmin()) {
            AssertUtil.isEquals(authentication.getAppId(), baseUser.getAppId(), () -> ClientException.clientByForbidden("无权删除应用用户"));
        }

        baseUserRepository.deleteById(id);
        redisTool.deleteKey(APP_DROP_DOWN, baseUser.getAppId());
        return true;
    }

    /**
     * 登录信息
     *
     * @param myAuthentication
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public LoginResp login(MyAuthentication myAuthentication) {
//        log.info("认证成功，添加角色权限");
//        LoginResp loginResp = new LoginResp();
//
//        // 查询角色，权限使用
//        BaseUser baseUser = baseUserRepository.findById(myAuthentication.getId()).get();
//        BaseAppDTO baseAppDTO = baseAppService.findOne(myAuthentication.getAppId()).get();
//
//        // 设置角色
//        List<String> roles = baseUser.getRoles().stream().map(BaseRole::getName).collect(Collectors.toList());
//
//        loginResp.setId(myAuthentication.getId());
//        loginResp.setUsername(myAuthentication.getUsername());
//        loginResp.setAppId(myAuthentication.getAppId());
//        loginResp.setRealAppId(baseUser.getRealAppId());
//        loginResp.setRoles(roles);
//
//        // 创建token
//        Jwt jwt = new Jwt(1, TimeUnit.DAYS, baseAppDTO.getSecret());
//        UserSimple userSimple = new UserSimple(myAuthentication.getId(), myAuthentication.getAppId(), myAuthentication.getRealAppId(), myAuthentication.getUsername(), roles);
//        Token token = jwt.generateToken(userSimple);
//        loginResp.setToken(token);
//        // 设置应用首页地址
//        loginResp.setHomePage(baseAppDTO.getHomePage());
//
//        return loginResp;
        return null;
    }



    /**
     * 查询用户id详情
     *
     * @param id
     * @return
     */
    @Override
    public BaseUserDTO getById(Long id) {
        BaseUser baseUser = baseUserRepository.findById(id).orElseThrow(() -> ClientException.client("用户不存在"));
        MyAuthentication authentication = (MyAuthentication)SecurityContextHolder.getContext().getAuthentication();
        // 不是超级管理员
        if (!authentication.superAdmin()) {
            AssertUtil.isEquals(authentication.getAppId(), baseUser.getAppId(), () -> ClientException.client("用户不存在"));
        }

        BaseUserDTO baseUserDTO = baseUserMapper.toDto(baseUser);

        return baseUserDTO;
    }



}
