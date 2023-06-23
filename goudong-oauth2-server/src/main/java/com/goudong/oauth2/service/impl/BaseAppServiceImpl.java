package com.goudong.oauth2.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.goudong.boot.web.core.BasicException;
import com.goudong.boot.web.core.ClientException;
import com.goudong.boot.web.core.ServerException;
import com.goudong.boot.web.util.PageResultConvert;
import com.goudong.commons.constant.core.DateConst;
import com.goudong.commons.constant.user.RoleConst;
import com.goudong.commons.framework.jpa.MyIdentifierGenerator;
import com.goudong.core.lang.PageResult;
import com.goudong.core.util.AssertUtil;
import com.goudong.core.util.CollectionUtil;
import com.goudong.core.util.ListUtil;
import com.goudong.core.util.StringUtil;
import com.goudong.oauth2.dto.BaseAppApplyReq;
import com.goudong.oauth2.dto.BaseAppAuditReq;
import com.goudong.oauth2.dto.BaseAppDTO;
import com.goudong.oauth2.dto.BaseAppQueryReq;
import com.goudong.oauth2.enumerate.RedisKeyProviderEnum;
import com.goudong.oauth2.po.BaseAppPO;
import com.goudong.oauth2.po.BaseRolePO;
import com.goudong.oauth2.po.BaseUserPO;
import com.goudong.oauth2.repository.BaseAppRepository;
import com.goudong.oauth2.repository.BaseRoleRepository;
import com.goudong.oauth2.repository.BaseUserRepository;
import com.goudong.oauth2.service.BaseAppService;
import com.goudong.oauth2.service.BaseMenuService;
import com.goudong.oauth2.service.BaseRoleService;
import com.goudong.oauth2.service.BaseUserService;
import com.goudong.oauth2.util.app.AppContext;
import com.goudong.oauth2.util.app.AppIdV1Strategy;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * 类描述：
 * 应用接口实现
 * @author cfl
 * @version 1.0
 * @date 2023/6/10 22:50
 */
@Service
public class BaseAppServiceImpl implements BaseAppService {
    //~fields
    //==================================================================================================================
    @Resource
    private BaseAppRepository baseAppRepository;

    @Resource
    private RedissonClient redissonClient;

    @Resource
    private BaseUserService baseUserService;

    @Resource
    private BaseUserRepository baseUserRepository;

    @Resource
    private BaseRoleRepository baseRoleRepository;

    @Resource
    private BaseRoleService baseRoleService;

    @Resource
    private BaseMenuService baseMenuService;

    @Resource
    private PasswordEncoder passwordEncoder;
    //~methods
    //==================================================================================================================

    /**
     * 根据AppId获取对象
     *
     * @param appId
     * @return
     */
    @Override
    public BaseAppPO getByAppId(String appId) {
        return baseAppRepository.findByAppId(appId).orElseThrow(() -> ClientException.client("应用不可用"));
    }

    /**
     * 申请应用
     *
     * @param req
     * @return
     */
    @Override
    @Transactional
    public BaseAppDTO apply(BaseAppApplyReq req) {
        RLock lock = redissonClient.getLock(RedisKeyProviderEnum.LOCK_BASE_APP__CREATE.getFullKey());
        lock.lock();
        try {
            // 获取当前用户
            BaseUserPO authentication = (BaseUserPO) SecurityContextHolder.getContext().getAuthentication();

            // 检查用户名是否存在
            AssertUtil.isFalse(baseUserRepository.findByUsername(req.getAppName()).isPresent(), () -> ClientException.client("应用已存在"));
            AssertUtil.isFalse(baseAppRepository.findByAppName(req.getAppName()).isPresent(), () -> ClientException.client("应用已存在"));
            // 查询用户表admin的最大id
            Long maxAdminUserId = baseUserRepository.findMaxAdminUserId();
            Long userId = maxAdminUserId + 1;

            AppIdV1Strategy appIdV1Strategy = new AppIdV1Strategy();
            AppContext appContext = new AppContext(appIdV1Strategy);

            String appSecret = appContext.getAppSecret();
            Long appId = MyIdentifierGenerator.ID.nextId();
            String appIdStr = appContext.getAppId(appId);
            Date now = new Date();

            BaseUserPO userPO = new BaseUserPO();
            userPO.setUsername(req.getAppName());
            userPO.setPassword(passwordEncoder.encode(appSecret));
            userPO.setEmail("");
            userPO.setPhone("");
            userPO.setSex(0);
            userPO.setNickname(req.getAppName());
            userPO.setValidTime(DateUtil.parse("9999-12-31 23:59:59"));
            userPO.setEnabled(false);
            userPO.setLocked(false);
            userPO.setId(userId);
            userPO.setAppId(appId);
            userPO.setCreateTime(now);
            userPO.setCreateUserId(authentication.getId());
            userPO.setUpdateTime(now);
            userPO.setUpdateUserId(authentication.getId());
            userPO.setDeleted(false);

            // 设置角色
            BaseRolePO baseRolePO = baseRoleRepository.findByRoleName(RoleConst.ROLE_ADMIN)
                    .orElseThrow(() -> ServerException.server(String.format("ROLE_ADMIN角色不存在")));
            userPO.setRoles(ListUtil.newArrayList(baseRolePO));

            baseUserRepository.save(userPO);

            BaseAppPO po = new BaseAppPO();
            po.setAppId(appIdStr);
            po.setAppSecret(appSecret);
            po.setAppName(req.getAppName());
            po.setStatus(BaseAppPO.StatusEnum.CHECK_PENDING.getId());
            po.setRemark(req.getRemark());
            po.setId(appId);
            po.setCreateUserId(authentication.getId());
            po.setUpdateUserId(authentication.getId());

            baseAppRepository.save(po);
            return BeanUtil.copyProperties(po, BaseAppDTO.class);
        } finally {
            lock.unlock();
        }

    }

    /**
     * 删除应用
     *
     * @param id
     * @return
     */
    @Override
    public boolean deleteById(Long id) {
        BaseAppPO po = baseAppRepository.findById(id).orElseThrow(() -> BasicException.client("应用不存在"));

        // 获取当前用户
        BaseUserPO authentication = (BaseUserPO) SecurityContextHolder.getContext().getAuthentication();

        // 只能删除自己创建的
        AssertUtil.isEquals(authentication.getId(), po.getCreateUserId(), () -> ClientException.clientByForbidden());

        baseAppRepository.delete(po);
        return true;
    }

    /**
     * 审核应用
     *
     * @param req
     * @return
     */
    @Override
    @Transactional
    public BaseAppDTO audit(BaseAppAuditReq req) {
        String key = RedisKeyProviderEnum.LOCK_BASE_APP__AUDIT.getFullKey();
        RLock lock = redissonClient.getLock(key);
        try {
            lock.lock();
            BaseAppPO po = baseAppRepository.findById(req.getId()).orElseThrow(() -> BasicException.client("应用不存在"));
            AssertUtil.isTrue(po.getStatus() == BaseAppPO.StatusEnum.CHECK_PENDING.getId(), () -> "请勿重复审核应用");
            // 获取当前用户
            BaseUserPO authentication = (BaseUserPO) SecurityContextHolder.getContext().getAuthentication();
            po.setRemark(req.getRemark());
            po.setStatus(req.getStatus());

            // 审核通过,将账号激活
            if (req.getStatus() == BaseAppPO.StatusEnum.PASS.getId()) {
                Optional<BaseUserPO> byUsername = baseUserRepository.findByUsername(po.getAppName());
                if (byUsername.isPresent()) {
                    BaseUserPO baseUserPO = byUsername.get();
                    baseUserPO.setEnabled(true);
                    baseUserPO.setLocked(true);
                }
            }
            return BeanUtil.copyProperties(po, BaseAppDTO.class);
        } finally {
            lock.unlock();
        }

    }

    /**
     * 查询应用
     *
     * @param req
     * @return
     */
    @Override
    public PageResult<BaseAppDTO> query(BaseAppQueryReq req) {
        // 获取当前用户
        BaseUserPO authentication = (BaseUserPO) SecurityContextHolder.getContext().getAuthentication();


        Specification<BaseAppPO> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> and = new ArrayList<>();

            if (!authentication.isAdmin()) {
                and.add(criteriaBuilder.equal(root.get("createUserId"), authentication.getId()));
            }

            if (StringUtil.isNotBlank(req.getAppName())) {
                and.add(criteriaBuilder.like(root.get("appName"), "%" + req.getAppName() + "%"));
            }

            if (req.getStatus() != null) {
                and.add(criteriaBuilder.equal(root.get("status"), req.getStatus()));
            }

            if (req.getStartCreateTime() != null && req.getEndCreateTime() != null) {
                and.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createTime").as(String.class), req.getStartCreateTime().format(DateConst.YYYY_MM_DD_HH_MM_SS)));
                and.add(criteriaBuilder.lessThanOrEqualTo(root.get("createTime").as(String.class), req.getEndCreateTime().format(DateConst.YYYY_MM_DD_HH_MM_SS)));
            }

            // 分页
            Order weightOrder = criteriaBuilder.desc(root.get("createTime"));
            if (CollectionUtil.isNotEmpty(and)) {
                return query.where(and.toArray(new Predicate[and.size()])).orderBy(weightOrder).getRestriction();
            }

            return query.orderBy(weightOrder).getRestriction();
        };

        PageResult<BaseAppDTO> convert = null;
        if (req.getJPAPage() != null && req.getSize() != null) {
            PageRequest pageRequest = PageRequest.of(req.getJPAPage(), req.getSize());
            Page<BaseAppPO> all = baseAppRepository.findAll(specification, pageRequest);
            convert = PageResultConvert.convert(all, BaseAppDTO.class);
        } else {
            // // 导出时
            // List<BaseUserPO> all = baseUserRepository.findAll(specification);
            // List<com.goudong.commons.dto.oauth2.BaseUserDTO> baseUserDTOS = BeanUtil.copyToList(all, com.goudong.commons.dto.oauth2.BaseUserDTO.class, CopyOptions.create());
            // convert = new PageResult<>(baseUserDTOS);
        }


        return convert;
    }
}
