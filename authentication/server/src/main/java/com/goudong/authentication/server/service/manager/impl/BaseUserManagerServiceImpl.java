package com.goudong.authentication.server.service.manager.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.goudong.authentication.common.core.*;
import com.goudong.authentication.common.util.HttpRequestUtil;
import com.goudong.authentication.server.domain.BaseApp;
import com.goudong.authentication.server.domain.BaseRole;
import com.goudong.authentication.server.domain.BaseUser;
import com.goudong.authentication.server.rest.req.BaseUserPageReq;
import com.goudong.authentication.server.rest.req.BaseUserSimpleCreateReq;
import com.goudong.authentication.server.rest.req.BaseUserSimpleUpdateReq;
import com.goudong.authentication.server.rest.req.RefreshToken;
import com.goudong.authentication.server.rest.req.search.BaseUserDropDownReq;
import com.goudong.authentication.server.rest.resp.BaseUserDropDownResp;
import com.goudong.authentication.server.rest.resp.BaseUserPageResp;
import com.goudong.authentication.server.service.BaseAppService;
import com.goudong.authentication.server.service.BaseRoleService;
import com.goudong.authentication.server.service.BaseUserService;
import com.goudong.authentication.server.service.dto.BaseUserDTO;
import com.goudong.authentication.server.service.dto.MyAuthentication;
import com.goudong.authentication.server.service.manager.BaseUserManagerService;
import com.goudong.authentication.server.util.SecurityContextUtil;
import com.goudong.boot.web.core.ClientException;
import com.goudong.core.lang.PageResult;
import com.goudong.core.util.AssertUtil;
import com.goudong.core.util.CollectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 类描述：
 * 用户管理服务层接口实现类
 * @author chenf
 * @version 1.0
 */
@Slf4j
@Service
public class BaseUserManagerServiceImpl implements BaseUserManagerService {
    //~fields
    //==================================================================================================================
    @Resource
    private BaseUserService baseUserService;

    @Resource
    private BaseAppService baseAppService;

    @Resource
    private BaseRoleService baseRoleService;

    @Resource
    private PasswordEncoder passwordEncoder;

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
    public BaseUser findOneByAppIdAndUsername(Long appId, String username) {
        return baseUserService.findOneByAppIdAndUsername(appId, username);
    }

    /**
     * 获取登录成功信息
     * @param myAuthentication 用户认证成功对象
     * @return 用户基本信息和token
     */
    @Override
    public LoginResp login(MyAuthentication myAuthentication) {
        log.info("认证成功，响应用户登录信息:{}", myAuthentication);
        LoginResp loginResp = new LoginResp();
        // 设置角色
        List<String> roles = myAuthentication.getRoles().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());

        loginResp.setId(myAuthentication.getId());
        loginResp.setUsername(myAuthentication.getUsername());
        loginResp.setAppId(myAuthentication.getAppId());
        loginResp.setRealAppId(myAuthentication.getRealAppId());
        loginResp.setRoles(roles);

        BaseApp app = baseAppService.findById(myAuthentication.getAppId());

        // 创建token
        Jwt jwt = new Jwt(1, TimeUnit.DAYS, app.getSecret());
        UserSimple userSimple = new UserSimple(myAuthentication.getId(), myAuthentication.getAppId(), myAuthentication.getRealAppId(), myAuthentication.getUsername(), roles);
        Token token = jwt.generateToken(userSimple);
        loginResp.setToken(token);
        // 设置应用首页地址
        loginResp.setHomePage(app.getHomePage());

        return loginResp;
    }

    /**
     * 刷新token
     *
     * @param token refreshToken
     * @return token
     */
    @Override
    public Token refreshToken(RefreshToken token) {
        Long xAppId = HttpRequestUtil.getXAppId();
        BaseApp app = baseAppService.findById(xAppId);
        Jwt jwt = new Jwt(1, TimeUnit.DAYS, app.getSecret());
        UserSimple userSimple = jwt.parseToken(token.getRefreshToken());
        return jwt.generateToken(userSimple);
    }

    /**
     * 根据{@code token}获取用户信息
     *
     * @param token token
     * @return 用户信息
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetail getUserDetailByToken(String token) {
        BaseApp baseApp = baseAppService.findByHeader();
        Jwt jwt = new Jwt(baseApp.getSecret());
        UserSimple userSimple = jwt.parseToken(token);

        // 查询用户角色菜单
        BaseUser baseUser = baseUserService.findDetailById(userSimple.getId());

        UserDetail userDetail = new UserDetail();
        userDetail.setId(baseUser.getId());
        userDetail.setAppId(baseUser.getAppId());
        userDetail.setRealAppId(baseUser.getRealAppId());
        userDetail.setUsername(baseUser.getUsername());

        List<BaseRole> roles = baseUser.getRoles();
        List<String> roleNames = new ArrayList<>(roles.size());
        Set<Menu> menuHashSet = new HashSet<>(roles.size());
        roles.forEach(p -> {
            roleNames.add(p.getName());

            p.getMenus().forEach(p2 -> {
                menuHashSet.add(BeanUtil.copyProperties(p2, Menu.class));
            });

        });

        List<Menu> menuArrayList = new ArrayList<>(menuHashSet);
        menuArrayList.sort(new Comparator<Menu>() {
            @Override
            public int compare(Menu o1, Menu o2) {
                return Optional.ofNullable(o1.getSortNum()).orElseGet(() ->Integer.MAX_VALUE).compareTo(Optional.ofNullable(o2.getSortNum()).orElseGet(() ->Integer.MAX_VALUE));
            }
        });
        // 处理菜单
        userDetail.setRoles(roleNames);
        userDetail.setMenus(menuArrayList);

        return userDetail;
    }

    /**
     * 分页获取用户下拉，只返回操作人所在真实应用下的用户
     *
     * @param req 请求参数
     * @return 用户下拉列表
     */
    @Override
    public PageResult<BaseUserDropDownResp> userDropDown(BaseUserDropDownReq req) {
        return baseUserService.userDropDown(req);
    }

    /**
     * 分页查询用户
     *
     * @param req 分页参数
     * @return 用户分页对象
     */
    @Override
    public PageResult<BaseUserPageResp> page(BaseUserPageReq req) {
        return baseUserService.page(req);
    }

    /**
     * 简单方式创建用户
     *
     * @param req 用户信息
     * @return 用户对象
     */
    @Override
    public BaseUserDTO simpleCreateUser(BaseUserSimpleCreateReq req) {
        MyAuthentication myAuthentication = SecurityContextUtil.get();
        Long realAppId = myAuthentication.getRealAppId();

        BaseUser user = new BaseUser();
        user.setAppId(realAppId);
        user.setRealAppId(realAppId);
        user.setUsername(req.getUsername());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setEnabled(true);
        user.setLocked(false);
        user.setValidTime(DateUtil.parse("2099-12-31 00:00:00", DatePattern.NORM_DATETIME_FORMATTER));
        user.setRemark(req.getRemark());
        user.setRoles(baseRoleService.listByIds(req.getRoleIds())); // 设置角色

        return baseUserService.save(user);
    }

    /**
     * 简单方式修改用户
     *
     * @param req
     * @return
     */
    @Override
    public BaseUserDTO simpleUpdateUser(BaseUserSimpleUpdateReq req) {
        MyAuthentication myAuthentication = SecurityContextUtil.get();
        Long realAppId = myAuthentication.getRealAppId();
        BaseUser user = baseUserService.findById(req.getId());
        AssertUtil.isEquals(realAppId, user.getRealAppId(), () -> ClientException.clientByForbidden().serverMessage("不能修改其它应用下的用户"));
        if (CollectionUtil.isNotEmpty(req.getRoleIds())) {
            List<BaseRole> baseRoles = baseRoleService.listByIds(req.getRoleIds());
            baseRoles.forEach(p -> AssertUtil.isEquals(realAppId, p.getAppId(), () -> ClientException.clientByForbidden().serverMessage("不能使用其它应用下的角色")));
            user.setRoles(baseRoles);
        }

        user.setEnabled(Optional.ofNullable(req.getEnabled()).orElseGet(() -> user.getLocked()));
        user.setLocked(Optional.ofNullable(req.getLocked()).orElseGet(() -> user.getLocked()));
        user.setValidTime(Optional.ofNullable(req.getValidTime()).orElseGet(() -> user.getValidTime()));
        user.setRemark(Optional.ofNullable(req.getRemark()).orElseGet(() -> user.getRemark()));

        return baseUserService.save(user);
    }

    /**
     * 批量删除用户
     *
     * @param ids 被删除的用户id集合
     * @return true删除成功；false删除失败
     */
    @Override
    public Boolean deleteByIds(List<Long> ids) {
        return baseUserService.deleteByIds(ids);
    }

    /**
     * 重置用户密码
     *
     * @param userId 用户id
     * @return
     */
    @Override
    public Boolean resetPassword(Long userId) {
        return baseUserService.resetPassword(userId);
    }

    /**
     * 修改用户激活状态
     *
     * @param userId 用户id
     * @return
     */
    @Override
    public Boolean changeEnabled(Long userId) {
        return baseUserService.changeEnabled(userId);
    }

    /**
     * 修改用户锁定状态
     *
     * @param userId 用户id
     * @return
     */
    @Override
    public Boolean changeLocked(Long userId) {
        return baseUserService.changeLocked(userId);
    }
}