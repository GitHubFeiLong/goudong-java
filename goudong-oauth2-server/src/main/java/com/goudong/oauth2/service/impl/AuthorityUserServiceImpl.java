package com.goudong.oauth2.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.goudong.commons.dto.AuthorityUserDTO;
import com.goudong.commons.enumerate.ClientExceptionEnum;
import com.goudong.commons.exception.ClientException;
import com.goudong.commons.openfeign.MessageService;
import com.goudong.commons.po.AuthorityRolePO;
import com.goudong.commons.po.AuthorityUserPO;
import com.goudong.commons.po.AuthorityUserRolePO;
import com.goudong.commons.pojo.Result;
import com.goudong.commons.utils.AssertUtil;
import com.goudong.commons.utils.BeanUtil;
import com.goudong.oauth2.enumerate.OtherUserTypeEnum;
import com.goudong.oauth2.mapper.AuthorityRoleMapper;
import com.goudong.oauth2.mapper.AuthorityUserMapper;
import com.goudong.oauth2.mapper.AuthorityUserRoleMapper;
import com.goudong.oauth2.service.AuthorityUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * 类描述：
 * 用户服务层
 * @Author msi
 * @Date 2021-05-02 13:53
 * @Version 1.0
 */
@Slf4j
@Service
public class AuthorityUserServiceImpl extends ServiceImpl<AuthorityUserMapper, AuthorityUserPO> implements AuthorityUserService {

    @Resource
    private AuthorityRoleMapper authorityRoleMapper;
    @Resource
    private AuthorityUserMapper authorityUserMapper;
    @Resource
    private AuthorityUserRoleMapper authorityUserRoleMapper;
    // @Resource
    // private SelfAuthorityUserMapper selfAuthorityUserMapper;
    @Resource
    private MessageService messageService;


    /**
     * 根据指定的用户名，生成3个可以未被注册的用户名
     * 当返回结果为空集合时，表示账号可以使用
     * @param username
     * @return
     */
    @Override
    public List<String> generateUserName(String username) {
        AssertUtil.hasText(username, "根据用户名查询用户时，用户名不能为空");
        List<String> result = new ArrayList<>();
        // 查询用户名是否存在
        LambdaQueryWrapper<AuthorityUserPO> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(AuthorityUserPO::getUsername, username);

        AuthorityUserPO authorityUserPO = super.getOne(lambdaQueryWrapper);

        if (authorityUserPO == null) {
            return result;
        }

        lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.like(AuthorityUserPO::getUsername, username);

        List<String> names = super.list(lambdaQueryWrapper).stream().map(AuthorityUserPO::getUsername).collect(Collectors.toList());

        Random random = new Random();
        do {
            int i = random.nextInt(10000);
            String item = username + i;
            // 本次生成的用户名不存在
            if(!names.contains(item)){
                names.add(item);
                result.add(item);
            }
        } while (result.size() < 3);

        return result;
    }

    /**
     * 新增用户
     *
     * @param authorityUserDTO
     * @return
     */
    @Transactional
    @Override
    public AuthorityUserDTO createUser(AuthorityUserDTO authorityUserDTO) {
        // 查询填写的基本信息是否已存在
        LambdaQueryWrapper<AuthorityUserPO> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(AuthorityUserPO::getUsername, authorityUserDTO.getUsername())
                .or()
                .eq(AuthorityUserPO::getPhone, authorityUserDTO.getPhone())
                .or()
                .eq(AuthorityUserPO::getEmail, authorityUserDTO.getEmail());

        List<AuthorityUserPO> userPOList = super.list(lambdaQueryWrapper);

        if (userPOList.size() > 1) {
            // 有多条，表示提交的数据有问题
            // 1. 使用postman 类似工具，提交未经校验的内容
            // 2. 注册时间过长，账号被别人注册了
            throw ClientException.clientException(ClientExceptionEnum.BAD_REQUEST);
        }
        String accountRadio = authorityUserDTO.getAccountRadio();
        AuthorityUserPO userPO = BeanUtil.copyProperties(authorityUserDTO, AuthorityUserPO.class);
        // 为空，插入
        if (userPOList.isEmpty() && "".equals(accountRadio)) {

            userPO.setPassword(BCrypt.hashpw(userPO.getPassword(), BCrypt.gensalt()));
            super.save(userPO);

            LambdaQueryWrapper<AuthorityRolePO> queryWrapper = new LambdaQueryWrapper();
            queryWrapper.eq(AuthorityRolePO::getRoleName, "ROLE_ORDINARY");

            AuthorityRolePO rolePO = authorityRoleMapper.selectOne(queryWrapper);
            // 绑定角色
            AuthorityUserRolePO userRolePO = new AuthorityUserRolePO();
            userRolePO.setRoleId(rolePO.getId());
            userRolePO.setUserId(userPO.getId());

            authorityUserRoleMapper.insert(userRolePO);

            return BeanUtil.copyProperties(userPO, AuthorityUserDTO.class);
        }

        if ("MY_SELF".equals(accountRadio) || "NOT_MY_SELF".equals(accountRadio)) {
            // 数据库没有相关用户有一条数据
            userPO.setId(userPOList.get(0).getId());

            // 加密
            userPO.setPassword(BCrypt.hashpw(userPO.getPassword(), BCrypt.gensalt()));
            authorityUserMapper.updateInsert(userPO);

            return BeanUtil.copyProperties(userPO, AuthorityUserDTO.class);
        }

        throw ClientException.clientException(ClientExceptionEnum.BAD_REQUEST);
    }

    /**
     * 根据 登录账号，查询用户基本信息
     *
     * @param loginName 登录账号
     * @return
     */
    @Override
    @Deprecated
    public AuthorityUserDTO getUserByLoginName(String loginName) {
        // 查询填写的基本信息是否已存在
        LambdaQueryWrapper<AuthorityUserPO> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(AuthorityUserPO::getUsername, loginName)
                .or()
                .eq(AuthorityUserPO::getPhone, loginName)
                .or()
                .eq(AuthorityUserPO::getEmail, loginName);

        AuthorityUserPO userPO = super.getOne(lambdaQueryWrapper);

        return BeanUtil.copyProperties(userPO, AuthorityUserDTO.class);
    }

    /**
     * 绑定opendId
     *
     * @param userDTO
     */
    @Override
    public AuthorityUserDTO updateOpenId(AuthorityUserDTO userDTO) {
        AssertUtil.isEnum(userDTO.getUserType(), OtherUserTypeEnum.class, "绑定账号openId的类型无效");
        // 判断用户名和密码是否匹配
        String loginName = userDTO.getLoginName();

        LambdaQueryWrapper<AuthorityUserPO> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(AuthorityUserPO::getUsername, loginName)
                .or()
                .eq(AuthorityUserPO::getEmail, loginName)
                .or()
                .eq(AuthorityUserPO::getPhone, loginName);
        AuthorityUserPO authorityUserPO = super.getOne(queryWrapper);

        boolean error = authorityUserPO==null
                || !new BCryptPasswordEncoder().matches(userDTO.getPassword(), authorityUserPO.getPassword());
        if (error) {
            throw ClientException.clientException(ClientExceptionEnum.NOT_FOUND, "账户名与密码不匹配，请重新输入");
        }
        // 修改openId
        // qq
        if (OtherUserTypeEnum.QQ.name().equals(userDTO.getUserType())) {

            LambdaUpdateWrapper<AuthorityUserPO> updateWrapper = new LambdaUpdateWrapper();
            updateWrapper.set(AuthorityUserPO::getQqOpenId, userDTO.getQqOpenId())
                    .eq(AuthorityUserPO::getId, authorityUserPO.getId());

            super.update(updateWrapper);
        }
        // 查询返回
        authorityUserPO.setQqOpenId(userDTO.getQqOpenId());
        return BeanUtil.copyProperties(authorityUserPO, AuthorityUserDTO.class);
    }

    /**
     * 根据用户名或者电话号或者邮箱 查询用户的详细信息。包含用户信息，角色信息，权限信息
     *
     * @param loginName
     * @return
     */
    @Override
    public AuthorityUserDTO getUserDetailByLoginName(String loginName) {
        AuthorityUserDTO authorityUserDTO = authorityUserMapper.selectUserDetailByUsername(loginName);
        return authorityUserDTO;
    }

    /**
     * 更新密码
     *
     * @param authorityUserDTO
     * @return
     */
    @Override
    public AuthorityUserDTO updatePassword(AuthorityUserDTO authorityUserDTO) {
        AuthorityUserPO byId = super.getById(authorityUserDTO.getId());
        if (byId == null) {
            throw ClientException.clientException(ClientExceptionEnum.NOT_FOUND, "账号不存在");
        }

        // 判断验证码是否正确
        Result<Boolean> booleanResult = messageService.checkCode(byId.getPhone(), authorityUserDTO.getCode());

        if (booleanResult.getData()) {
            Long userId = authorityUserDTO.getId();

            LambdaUpdateWrapper<AuthorityUserPO> updateWrapper = new LambdaUpdateWrapper<>();
            String hashPw = BCrypt.hashpw(authorityUserDTO.getPassword(), BCrypt.gensalt());
            updateWrapper.set(AuthorityUserPO::getPassword, hashPw)
                    .eq(AuthorityUserPO::getId, userId);

            boolean update = super.update(updateWrapper);
            if (update) {
                return BeanUtil.copyProperties(super.getById(authorityUserDTO.getId()), AuthorityUserDTO.class);
            }
        }

        // 验证码错误，或更新失败
        throw ClientException.clientException(ClientExceptionEnum.NOT_FOUND, "验证码失效");
    }
}
