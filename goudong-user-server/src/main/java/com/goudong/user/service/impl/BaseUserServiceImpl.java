package com.goudong.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.goudong.commons.dto.user.BaseUserDTO;
import com.goudong.commons.enumerate.core.ClientExceptionEnum;
import com.goudong.commons.enumerate.user.AccountRadioEnum;
import com.goudong.commons.exception.ClientException;
import com.goudong.commons.utils.core.AssertUtil;
import com.goudong.user.po.BaseRolePO;
import com.goudong.user.po.BaseUserPO;
import com.goudong.user.repository.BaseUserRepository;
import com.goudong.user.service.BaseRoleService;
import com.goudong.user.service.BaseUserService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
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

    private final BaseRoleService baseRoleService;

    public BaseUserServiceImpl(BaseUserRepository baseUserRepository, BaseRoleService baseRoleService) {
        this.baseUserRepository = baseUserRepository;
        this.baseRoleService = baseRoleService;
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
     *
     * @param baseUserDTO
     * @return
     */
    @Override
    @Transactional
    public BaseUserDTO createUser(BaseUserDTO baseUserDTO) {
        // 查询填写的基本信息是否已存在
        List<BaseUserPO> baseUserPOS = ListUsersByLoginName(baseUserDTO.getUsername(), baseUserDTO.getPassword(), baseUserDTO.getEmail());

        if (CollectionUtils.isNotEmpty(baseUserPOS)) {
            // 有多条，表示提交的数据有问题
            // 1. 使用postman 类似工具，提交未经校验的内容
            // 2. 注册时间过长，账号被别人注册了
            throw ClientException.clientException(ClientExceptionEnum.BAD_REQUEST, "注册的用户已存在");
        }
        String accountRadio = baseUserDTO.getAccountRadio();
        BaseUserPO userPO = BeanUtil.copyProperties(baseUserDTO, BaseUserPO.class);

        AccountRadioEnum accountRadioEnum = AccountRadioEnum.valueOf(accountRadio);

        if(Objects.equals(accountRadioEnum, AccountRadioEnum.BLANK)) {
            // 为空，插入
            userPO.setPassword(BCrypt.hashpw(userPO.getPassword(), BCrypt.gensalt()));
            baseUserRepository.save(userPO);

            BaseRolePO byRoleOrdinary = baseRoleService.findByRoleOrdinary();
            userPO.getRoles().add(byRoleOrdinary);

            return BeanUtil.copyProperties(userPO, BaseUserDTO.class);
        }

        // 数据库有相关用户有一条数据
        userPO.setId(baseUserPOS.get(0).getId());

        // 加密
        userPO.setPassword(BCrypt.hashpw(userPO.getPassword(), BCrypt.gensalt()));

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
}
