package com.goudong.user.service.impl;

import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.goudong.commons.constant.core.DateConst;
import com.goudong.commons.dto.core.BasePageResult;
import com.goudong.commons.dto.user.BaseUser2QueryPageDTO;
import com.goudong.commons.dto.user.BaseUserDTO;
import com.goudong.commons.dto.user.SimpleCreateUserReq;
import com.goudong.commons.enumerate.core.ClientExceptionEnum;
import com.goudong.commons.enumerate.user.AccountRadioEnum;
import com.goudong.commons.exception.ClientException;
import com.goudong.commons.framework.core.Result;
import com.goudong.commons.framework.openfeign.GoudongMessageServerService;
import com.goudong.commons.utils.JPAPageResultConvert;
import com.goudong.commons.utils.core.AssertUtil;
import com.goudong.commons.utils.core.BeanUtil;
import com.goudong.user.dto.AdminEditUserReq;
import com.goudong.user.dto.BaseRoleDTO;
import com.goudong.user.po.BaseRolePO;
import com.goudong.user.po.BaseUserPO;
import com.goudong.user.repository.BaseUserRepository;
import com.goudong.user.service.BaseRoleService;
import com.goudong.user.service.BaseUserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 类描述：
 * 用户服务层
 * @Author e-Feilong.Chen
 * @Date 2022/1/7 15:34
 */
@Service
@RequiredArgsConstructor
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

    private final EntityManager entityManager;

    //public BaseUserServiceImpl(BaseUserRepository baseUserRepository,
    //                           BaseRoleService baseRoleService,
    //                           GoudongMessageServerService goudongMessageServerService) {
    //    this.baseUserRepository = baseUserRepository;
    //    this.baseRoleService = baseRoleService;
    //    this.goudongMessageServerService = goudongMessageServerService;
    //}

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
     * 根据某个字段进行分页
     *
     * @param page
     * @return
     */
    @Override
    public BasePageResult<BaseUserDTO> pageByField(BaseUser2QueryPageDTO page) {
        Specification<BaseUserPO> specification = new Specification<BaseUserPO>() {
            @Override
            public Predicate toPredicate(Root<BaseUserPO> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                Predicate predicate = null;
                if (StringUtils.isNotBlank(page.getUsername())) {
                    Path<String> username = root.get("username");
                    predicate = criteriaBuilder.like(username, page.getUsername() + "%");
                } else if (StringUtils.isNotBlank(page.getPhone())) {
                    Path<String> phone = root.get("phone");
                    predicate = criteriaBuilder.like(phone, page.getPhone() + "%");
                } else if (StringUtils.isNotBlank(page.getEmail())) {
                    Path<String> email = root.get("email");
                    predicate = criteriaBuilder.like(email, page.getEmail() + "%");
                } else if (StringUtils.isNotBlank(page.getNickname())) {
                    Path<String> nickname = root.get("nickname");
                    predicate = criteriaBuilder.like(nickname, page.getNickname() + "%");
                }

                return predicate;
            }
        };

        PageRequest pageRequest = PageRequest.of(page.getJPAPage(), (int)page.getSize(), Sort.sort(BaseUserPO.class).by(BaseUserPO::getCreateTime).descending());

        Page<BaseUserPO> all = baseUserRepository.findAll(specification, pageRequest);

        BasePageResult<BaseUserDTO> convert = JPAPageResultConvert.convert(all, BaseUserDTO.class);

        // 脱敏
        convert.getContent().forEach(p->{
            p.setPassword(null);
        });
        return convert;
    }

    /**
     * 用户分页查询
     *
     * @param page
     * @return
     */
    @Transactional
    @Override
    public BasePageResult<com.goudong.commons.dto.oauth2.BaseUserDTO> page(BaseUser2QueryPageDTO page) {
        PageRequest pageRequest = PageRequest.of(page.getJPAPage(), (int)page.getSize(), Sort.sort(BaseUserPO.class).by(BaseUserPO::getCreateTime).descending());
        Specification<BaseUserPO> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> and = new ArrayList<>();
            if (StringUtils.isNotBlank(page.getUsername())) {
                and.add(criteriaBuilder.like(root.get("username"), page.getUsername() + "%"));
            }
            if (StringUtils.isNotBlank(page.getPhone())) {
                and.add(criteriaBuilder.like(root.get("phone"), page.getPhone() + "%"));
            }
            if (StringUtils.isNotBlank(page.getEmail())) {
                and.add(criteriaBuilder.like(root.get("email"), page.getEmail() + "%"));
            }
            if (StringUtils.isNotBlank(page.getNickname())) {
                and.add(criteriaBuilder.like(root.get("nickname"), page.getNickname() + "%"));
            }
            if (page.getStartValidTime() != null && page.getEndValidTime() != null) {
                and.add(criteriaBuilder.greaterThanOrEqualTo(root.get("validTime").as(String.class), page.getStartValidTime().format(DateConst.YYYY_MM_DD_HH_MM_SS)));
                and.add(criteriaBuilder.lessThanOrEqualTo(root.get("validTime").as(String.class), page.getEndValidTime().format(DateConst.YYYY_MM_DD_HH_MM_SS)));
            }
            if (page.getStartCreateTime() != null && page.getEndCreateTime() != null) {
                and.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createTime").as(String.class), page.getStartCreateTime().format(DateConst.YYYY_MM_DD_HH_MM_SS)));
                and.add(criteriaBuilder.lessThanOrEqualTo(root.get("createTime").as(String.class), page.getEndCreateTime().format(DateConst.YYYY_MM_DD_HH_MM_SS)));
            }

            if (CollectionUtils.isNotEmpty(and)) {
                return query.where(and.toArray(new Predicate[and.size()])).getRestriction();
            }

            return query.getRestriction();
        };

        Page<BaseUserPO> all = baseUserRepository.findAll(specification, pageRequest);

        BasePageResult<com.goudong.commons.dto.oauth2.BaseUserDTO> convert = JPAPageResultConvert.convert(all, com.goudong.commons.dto.oauth2.BaseUserDTO.class);

        // 脱敏
        convert.getContent().forEach(p->{
            p.setPassword(null);
        });
        return convert;
    }

    /**
     * 后台简单新增一个用户
     *
     * @param createDTO
     * @return
     */
    @Override
    public BaseUserDTO simpleCreateUser(SimpleCreateUserReq createDTO) {
        // 手机号，邮箱，用户名 等交由数据库唯一索引处理
        // 角色校验
        List<BaseRoleDTO> baseRoleDTOS = baseRoleService.listByIds(createDTO.getRoleIds());
        // 有角色不存在
        if (baseRoleDTOS.size() != createDTO.getRoleIds().size()) {
            List<Long> dbRoleIds = baseRoleDTOS.stream().map(BaseRoleDTO::getId).collect(Collectors.toList());
            Collection<Long> subtract = CollectionUtils.subtract(dbRoleIds, createDTO.getRoleIds());
            throw ClientException.clientException(ClientExceptionEnum.BAD_REQUEST, "角色无效：" + subtract);
        }

        BaseUserPO baseUserPO = BeanUtil.copyProperties(createDTO, BaseUserPO.class);
        baseUserPO.setRoles(BeanUtil.copyToList(baseRoleDTOS, BaseRolePO.class, CopyOptions.create()));
        baseUserPO.setValidTime(DateUtil.parse("9999-12-31 23:59:59"));
        baseUserRepository.save(baseUserPO);
        return BeanUtil.copyProperties(baseUserPO, BaseUserDTO.class);
    }

    /**
     * 根据id查询用户信息
     *
     * @param id
     * @return
     */
    @Override
    @Transactional
    public com.goudong.commons.dto.oauth2.BaseUserDTO getUserById(Long id) {
        BaseUserPO baseUserPO = baseUserRepository.findById(id).orElseThrow(() -> ClientException.clientException(ClientExceptionEnum.NOT_FOUND, "用户不存在"));
        return BeanUtil.copyProperties(baseUserPO, com.goudong.commons.dto.oauth2.BaseUserDTO.class);
    }

    /**
     * admin平台修改用户信息
     *
     * @param req
     * @return
     */
    @Override
    @Transactional
    public BaseUserDTO adminEditUser(AdminEditUserReq req) {
        BaseUserPO userPO = baseUserRepository.findById(req.getId()).orElseThrow(() -> ClientException.clientException(ClientExceptionEnum.NOT_FOUND, "用户不存在"));

        List<BaseRoleDTO> roles = baseRoleService.listByIds(req.getRoleIds());
        if (req.getRoleIds().size() != roles.size()) {
            throw ClientException.clientException(ClientExceptionEnum.BAD_REQUEST, "角色不正确");
        }

        userPO.setNickname(req.getNickname());
        userPO.setAvatar(req.getAvatar());
        userPO.setValidTime(req.getValidTime());
        userPO.setRemark(req.getRemark());
        userPO.setRoles(BeanUtil.copyToList(roles, BaseRolePO.class, CopyOptions.create()));

        return BeanUtil.copyProperties(userPO, BaseUserDTO.class);
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
