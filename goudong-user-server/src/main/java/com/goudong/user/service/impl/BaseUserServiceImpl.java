package com.goudong.user.service.impl;

import cn.hutool.core.date.DateTime;
import com.goudong.commons.dto.user.BaseUserDTO;
import com.goudong.commons.enumerate.core.ClientExceptionEnum;
import com.goudong.commons.enumerate.user.AccountRadioEnum;
import com.goudong.commons.exception.ClientException;
import com.goudong.commons.frame.core.Result;
import com.goudong.commons.frame.openfeign.GoudongMessageServerService;
import com.goudong.commons.utils.core.BeanUtil;
import com.goudong.commons.utils.core.AssertUtil;
import com.goudong.user.po.BaseRolePO;
import com.goudong.user.po.BaseUserPO;
import com.goudong.user.repository.BaseUserRepository;
import com.goudong.user.service.BaseRoleService;
import com.goudong.user.service.BaseUserService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 类描述：
 * 用户服务层
 * @Author e-Feilong.Chen
 * @Date 2022/1/7 15:34
 */
@Service
public class BaseUserServiceImpl implements BaseUserService {

    /**
     * 用户持久层接口
     */
    private final BaseUserRepository baseUserRepository;

    /**
     * 角色服务层接口
     */
    private final BaseRoleService baseRoleService;

    private final GoudongMessageServerService goudongMessageServerService;

    public BaseUserServiceImpl(BaseUserRepository baseUserRepository,
                               BaseRoleService baseRoleService,
                               GoudongMessageServerService goudongMessageServerService) {
        this.baseUserRepository = baseUserRepository;
        this.baseRoleService = baseRoleService;
        this.goudongMessageServerService = goudongMessageServerService;
    }

    /**
     * 根据指定的用户名，生成3个可以未被注册的用户名
     * 当返回结果为空集合时，表示账号可以使用
     *
     * @param username
     * @return
     */
    @Override
    public List<String> generateUserName(String username) {
        AssertUtil.hasText(username, "根据用户名查询用户时，用户名不能为空");
        List<String> result = new ArrayList<>();

        // 查询用户名是否存在
        BaseUserPO byUsername = baseUserRepository.findByUsername(username);

        // 不存在用户，直接使用
        if (byUsername == null) {
            return result;
        }

        // 模糊查询
        List<BaseUserPO> baseUserPOS = baseUserRepository.findAllByUsernameIsLike(username);

        List<String> names = baseUserPOS.stream().map(BaseUserPO::getUsername).collect(Collectors.toList());

        // 生成随机用户名
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
     * 根据{@link AccountRadioEnum}值，判断是否是直接新增一个用户信息，还是先将已有的用户信息删除，再新增用户信息
     * @param baseUserDTO
     * @return
     */
    @Override
    @Transactional
    public BaseUserDTO createUser(BaseUserDTO baseUserDTO) {
        AccountRadioEnum accountRadioEnum = AccountRadioEnum.valueOf(baseUserDTO.getAccountRadio());
        BaseUserPO userPO = BeanUtil.copyProperties(baseUserDTO, BaseUserPO.class);
        switch (accountRadioEnum) {
            case BLANK:
                // 查询填写的基本信息是否已存在
                List<BaseUserPO> baseUserPOS = ListUsersByLoginName(baseUserDTO.getUsername(), baseUserDTO.getPhone(), baseUserDTO.getEmail());
                if (CollectionUtils.isNotEmpty(baseUserPOS)) {
                    throw ClientException.clientException(ClientExceptionEnum.BAD_REQUEST, "注册的用户已存在");
                }
                return createBaseUser(userPO);
            case MY_SELF:
            case NOT_MY_SELF:
                BaseUserPO byPhone = baseUserRepository.findByPhone(baseUserDTO.getPhone());
                if (byPhone != null) {
                    // 先删除，再新增
                    baseUserRepository.delete(byPhone);
                    baseUserRepository.flush();
                }
                return createBaseUser(userPO);
            default:
                return null;
        }
    }

    /**
     * 新增一个用户信息
     * @param userPO
     * @return
     */
    private BaseUserDTO createBaseUser(BaseUserPO userPO) {
        // 为空，插入
        userPO.setPassword(BCrypt.hashpw(userPO.getPassword(), BCrypt.gensalt()));
        // 设置非空属性
        userPO.setValidTime(new DateTime("9999-12-31 23:59:59"));
        // 设置角色
        BaseRolePO roleUser = baseRoleService.findByRoleUser();
        userPO.getRoles().add(roleUser);
        baseUserRepository.save(userPO);

        return BeanUtil.copyProperties(userPO, BaseUserDTO.class);
    }

    /**
     * 根据登录名（用户名，手机号，邮箱）查询用户信息
     * @param username
     * @param phone
     * @param email
     * @return
     */
    private List<BaseUserPO> ListUsersByLoginName(String username, String phone, String email) {
        List<BaseUserPO> baseUserPOS = baseUserRepository.findAll(new Specification<BaseUserPO>() {
            @Override
            public Predicate toPredicate(Root<BaseUserPO> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.or(
                        criteriaBuilder.equal(root.get("username").as(String.class), username),
                        criteriaBuilder.equal(root.get("phone").as(String.class), phone),
                        criteriaBuilder.equal(root.get("email").as(String.class), email)
                );
            }
        });
        return baseUserPOS;
    }

    /**
     * 根据 用户名、手机号、邮箱 查询用户基本信息
     *
     * @param loginName 用户名、手机号、邮箱
     * @return
     */
    @Override
    public List<BaseUserPO> getUserByLoginName(String loginName) {
        // 查询填写的基本信息是否已存在
        List<BaseUserPO> baseUserPOS = this.ListUsersByLoginName(loginName, loginName, loginName);

        return baseUserPOS;
    }

    /**
     * 更新密码
     *
     * @param baseUserDTO
     * @return
     */
    @Override
    @Transactional
    public BaseUserDTO updatePassword(BaseUserDTO baseUserDTO) {
        Optional<BaseUserPO> byId = baseUserRepository.findById(baseUserDTO.getId());
        if (byId.isPresent()) {
            BaseUserPO baseUserPO = byId.get();
            // 判断验证码是否正确
            Result<Boolean> booleanResult = goudongMessageServerService.checkPhoneCode(baseUserPO.getPhone(), baseUserDTO.getCode());
            if(Objects.equals(booleanResult.getData(), Boolean.FALSE)) {
                // 验证码错误，或更新失败
                throw ClientException.clientException(ClientExceptionEnum.NOT_FOUND, "验证码失效");
            }

            baseUserPO.setPassword(new BCryptPasswordEncoder().encode(baseUserDTO.getPassword()));
            return BeanUtil.copyProperties(baseUserPO, BaseUserDTO.class);
        }

        throw ClientException.clientException(ClientExceptionEnum.NOT_FOUND, "账号不存在");
    }

    /**
     * 绑定opendId
     *
     * @param userDTO
     */
    @Override
    public BaseUserDTO updateOpenId(BaseUserDTO userDTO) {

        // 判断用户名和密码是否匹配
        String loginName = userDTO.getLoginName();

        BaseUserPO byLogin = baseUserRepository.findByLogin(loginName);


        boolean error = byLogin ==null
                || !new BCryptPasswordEncoder().matches(userDTO.getPassword(), byLogin.getPassword());
        if (error) {
            throw ClientException.clientException(ClientExceptionEnum.NOT_FOUND, "账户名与密码不匹配，请重新输入");
        }
        // 修改openId
        byLogin.setQqOpenId(userDTO.getQqOpenId());
        return BeanUtil.copyProperties(byLogin, BaseUserDTO.class);
    }

    /**
     * 根据用户名或者电话号或者邮箱 查询用户的详细信息。包含用户信息，角色信息，权限信息
     *
     * @param loginName
     * @return
     */
    @Override
    public BaseUserDTO getUserDetailByLoginName(String loginName) {
        return BeanUtil.copyProperties(baseUserRepository.findByLogin(loginName), BaseUserDTO.class);
    }
}
