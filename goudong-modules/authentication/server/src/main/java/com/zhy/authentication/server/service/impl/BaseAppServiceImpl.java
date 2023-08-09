package com.zhy.authentication.server.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.zhxu.bs.BeanSearcher;
import cn.zhxu.bs.SearchResult;
import cn.zhxu.bs.util.MapUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.goudong.boot.redis.core.RedisTool;
import com.goudong.boot.web.core.ClientException;
import com.goudong.boot.web.core.ServerException;
import com.goudong.core.lang.PageResult;
import com.goudong.core.util.AssertUtil;
import com.goudong.core.util.ListUtil;
import com.mysql.cj.xdevapi.Client;
import com.zhy.authentication.server.constant.RoleConst;
import com.zhy.authentication.server.constant.UserConst;
import com.zhy.authentication.server.domain.BaseApp;
import com.zhy.authentication.server.domain.BaseRole;
import com.zhy.authentication.server.domain.BaseUser;
import com.zhy.authentication.server.domain.BaseUserRole;
import com.zhy.authentication.server.repository.*;
import com.zhy.authentication.server.rest.req.BaseAppCreate;
import com.zhy.authentication.server.rest.req.BaseAppUpdate;
import com.zhy.authentication.server.rest.req.search.BaseAppDropDown;
import com.zhy.authentication.server.rest.req.search.BaseAppPage;
import com.zhy.authentication.server.service.BaseAppService;
import com.zhy.authentication.server.service.dto.BaseAppDTO;
import com.zhy.authentication.server.service.dto.MyAuthentication;
import com.zhy.authentication.server.service.mapper.BaseAppMapper;
import com.zhy.authentication.server.util.PageResultUtil;
import com.zhy.authentication.server.util.SecurityContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static com.zhy.authentication.server.enums.RedisKeyTemplateProviderEnum.APP_DROP_DOWN;
import static com.zhy.authentication.server.enums.RedisKeyTemplateProviderEnum.APP_ID;

/**
 * Service Implementation for managing {@link BaseApp}.
 */
@Service
public class BaseAppServiceImpl implements BaseAppService {

    private final Logger log = LoggerFactory.getLogger(BaseAppServiceImpl.class);

    @Resource
    private BaseAppRepository baseAppRepository;
    @Resource
    private BaseAppMapper baseAppMapper;

    @Resource
    private RedisTool redisTool;

    @Resource
    private ObjectMapper objectMapper;

    @Resource
    private BeanSearcher beanSearcher;

    @Resource
    private BaseRoleRepository baseRoleRepository;

    @Resource
    private BaseUserRepository baseUserRepository;

    @Resource
    private BaseMenuRepository baseMenuRepository;

    @Resource
    private TransactionTemplate transactionTemplate;

    @Resource
    private PasswordEncoder passwordEncoder;

    /**
     * 新增应用
     * @param req
     * @return
     */
    @Override
    public BaseAppDTO save(BaseAppCreate req) {
        AssertUtil.isFalse(req.getName().equals(RoleConst.ROLE_APP_SUPER_ADMIN), () -> ClientException.client("应用已存在"));
        AssertUtil.isFalse(req.getName().equals(RoleConst.ROLE_APP_ADMIN), () -> ClientException.client("应用已存在"));

        MyAuthentication authentication = SecurityContextUtil.get();

        // 新增应用
        BaseApp baseApp = new BaseApp();
        baseApp.setId(IdUtil.getSnowflake().nextId());
        baseApp.setName(req.getName());
        baseApp.setRemark(req.getRemark());
        baseApp.setHomePage(req.getHomePage());
        baseApp.setSecret(UUID.randomUUID().toString().replace("-", ""));
        baseApp.setEnabled(req.getEnabled());

        // 新增应用管理员
        BaseUser baseUser = new BaseUser();
        // 用户所在应用
        baseUser.setAppId(authentication.getRealAppId());
        // 用户真实所在应用
        baseUser.setRealAppId(baseApp.getId());
        baseUser.setUsername(req.getName());
        baseUser.setPassword(passwordEncoder.encode("123456"));
        baseUser.setEnabled(true);
        baseUser.setLocked(false);
        baseUser.setValidTime(DateUtil.parse("2099-12-31 00:00:00", DatePattern.NORM_DATETIME_FORMATTER));
        baseUser.setRemark("应用管理员");

        // 设置角色
        baseUser.setRoles(ListUtil.newArrayList(baseRoleRepository.findByAppAdmin()));

        transactionTemplate.execute(status -> {
            try {
                // 新增应用
                baseAppRepository.save(baseApp);
                // 新增管理用户
                baseUserRepository.save(baseUser);
                cleanCache(baseApp.getId());
                return true;
            } catch (Exception e) {
                status.setRollbackOnly();
                throw ServerException.server("创建应用失败", e);
            }
        });

        return baseAppMapper.toDto(baseApp);
    }

    /**
     * 修改应用
     * @param req
     * @return
     */
    @Override
    public BaseAppDTO update(BaseAppUpdate req) {
        BaseApp baseApp = baseAppRepository.findById(req.getId()).orElseThrow(() -> ClientException
                .builder()
                .clientMessageTemplate("应用id:{}不存在")
                .clientMessageParams(req.getId())
                .build()
        );

        baseApp.setName(Optional.ofNullable(req.getName()).orElseGet(() -> baseApp.getName()));
        baseApp.setRemark(Optional.ofNullable(req.getRemark()).orElseGet(() -> baseApp.getRemark()));
        baseApp.setHomePage(Optional.ofNullable(req.getHomePage()).orElseGet(() -> baseApp.getHomePage()));
        baseApp.setEnabled(Optional.ofNullable(req.getEnabled()).orElseGet(() -> baseApp.getEnabled()));
        baseAppRepository.save(baseApp);

        cleanCache(req.getId());
        return baseAppMapper.toDto(baseApp);
    }

    /**
     * 根据id查询应用
     * @param id
     * @return
     */
    @Override
    public Optional<BaseAppDTO> findOne(Long id) {
        String key = APP_ID.getFullKey(id);
        if (redisTool.hasKey(key)) {
            String appStr = (String)redisTool.get(APP_ID, id);
            try {
                BaseAppDTO baseAppDTO = objectMapper.readValue(appStr, BaseAppDTO.class);
                return Optional.of(baseAppDTO);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        synchronized (this) {
            if (redisTool.hasKey(key)) {
                String appStr = (String)redisTool.get(APP_ID, id);
                try {
                    BaseAppDTO baseAppDTO = objectMapper.readValue(appStr, BaseAppDTO.class);
                    return Optional.of(baseAppDTO);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }

            Optional<BaseAppDTO> baseAppDTO = baseAppRepository.findById(id).map(baseAppMapper::toDto);
            if (baseAppDTO.isPresent()) {
                try {
                    redisTool.set(APP_ID, objectMapper.writeValueAsString(baseAppDTO.get()), id);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }

            return baseAppDTO;
        }
    }

    /**
     * 删除应用
     * @param id
     * @return
     */
    @Override
    public void delete(Long id) {
        AssertUtil.isTrue(baseAppRepository.existsById(id), () -> ClientException.client("应用不存在"));
        transactionTemplate.execute(status -> {
            try {
                // 删除应用
                baseAppRepository.deleteById(id);
                // 删除角色
                int delRoles = baseRoleRepository.deleteByAppId(id);
                log.info("删除{}个角色", delRoles);
                int delUsers = baseUserRepository.deleteByAppId(id);
                log.info("删除{}个用户", delUsers);
                int delMenus = baseMenuRepository.deleteByAppId(id);
                log.info("删除{}个菜单", delMenus);

                return true;
            } catch (Exception e) {
                status.setRollbackOnly();
                throw new ServerException("事务异常(删除应用)：" + e.getMessage());
            }
        });
        cleanCache(id);
        log.info("删除应用成功！");
    }

    /**
     * 应用分页
     * @param req
     * @return
     */
    @Override
    public PageResult page(BaseAppPage req) {
        MyAuthentication authentication = (MyAuthentication) SecurityContextHolder.getContext().getAuthentication();
        AssertUtil.isTrue(authentication.superAdmin(), () -> ClientException.clientByForbidden());

        Map<String, Object> build = MapUtils.builder()
                .page(req.getPage(), req.getSize())
                .field(BaseAppPage::getName, req.getName())
                .orderBy(BaseAppPage::getId).asc()
                .build();

        SearchResult<BaseAppPage> search = beanSearcher.search(BaseAppPage.class,  build);
        return PageResultUtil.convert(search, req);
    }

    /**
     * 下拉
     * @param req
     * @return
     */
    @Override
    public List<BaseAppDropDown> dropDown(BaseAppDropDown req) {
        MyAuthentication authentication = (MyAuthentication) SecurityContextHolder.getContext().getAuthentication();
        // 不是超级管理员只返回自身应用
        if (!authentication.superAdmin()) {
            BaseAppDTO baseAppDTO = findOne(authentication.getAppId()).get();
            BaseAppDropDown baseAppDropDown = new BaseAppDropDown();
            baseAppDropDown.setId(baseAppDTO.getId());
            baseAppDropDown.setName(baseAppDTO.getName());
            return ListUtil.newArrayList(baseAppDropDown);
        }
        // 超级管理员返回所有应用
        return allDropDown(req);
    }

    /**
     * 下拉
     *
     * @param req
     * @return
     */
    @Override
    public List<BaseAppDropDown> allDropDown(BaseAppDropDown req) {
        // 超级管理员返回所有应用
        String key = APP_DROP_DOWN.getFullKey();
        if (redisTool.hasKey(key)) {
            return redisTool.getList(APP_DROP_DOWN, BaseAppDropDown.class);
        }
        synchronized (this) {
            if (redisTool.hasKey(key)) {
                return redisTool.getList(APP_DROP_DOWN, BaseAppDropDown.class);
            }

            List<BaseAppDropDown> list = beanSearcher.searchAll(BaseAppDropDown.class);

            redisTool.set(APP_DROP_DOWN, list);

            return list;
        }
    }

    /**
     * 删除缓存
     * @param id
     */
    private void cleanCache(Long id) {
        redisTool.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                operations.multi();
                redisTool.deleteKey(APP_ID, id);
                redisTool.deleteKey(APP_DROP_DOWN);
                operations.exec();
                return null;
            }
        });
    }
}
