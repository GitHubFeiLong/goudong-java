package com.goudong.authentication.server.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.IdUtil;
import cn.zhxu.bs.BeanSearcher;
import cn.zhxu.bs.SearchResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.goudong.authentication.common.util.JsonUtil;
import com.goudong.authentication.server.constant.HttpHeaderConst;
import com.goudong.authentication.server.constant.RoleConst;
import com.goudong.authentication.server.domain.BaseApp;
import com.goudong.authentication.server.domain.BaseAppCert;
import com.goudong.authentication.server.domain.BaseUser;
import com.goudong.authentication.server.repository.BaseAppRepository;
import com.goudong.authentication.server.repository.BaseMenuRepository;
import com.goudong.authentication.server.repository.BaseRoleRepository;
import com.goudong.authentication.server.repository.BaseUserRepository;
import com.goudong.authentication.server.rest.req.BaseAppCreate;
import com.goudong.authentication.server.rest.req.BaseAppUpdate;
import com.goudong.authentication.server.rest.req.search.BaseAppDropDownReq;
import com.goudong.authentication.server.rest.req.search.BaseAppPageReq;
import com.goudong.authentication.server.rest.resp.BaseAppPageResp;
import com.goudong.authentication.server.service.BaseAppService;
import com.goudong.authentication.server.service.dto.BaseAppDTO;
import com.goudong.authentication.server.service.dto.MyAuthentication;
import com.goudong.authentication.server.service.mapper.BaseAppMapper;
import com.goudong.authentication.server.util.BeanSearcherUtil;
import com.goudong.authentication.server.util.PageResultUtil;
import com.goudong.authentication.server.util.SecurityContextUtil;
import com.goudong.boot.redis.core.RedisTool;
import com.goudong.boot.web.core.ClientException;
import com.goudong.boot.web.core.ServerException;
import com.goudong.core.lang.PageResult;
import com.goudong.core.security.cer.CertificateUtil;
import com.goudong.core.security.rsa.RSAKeySizeEnum;
import com.goudong.core.security.rsa.RSAUtil;
import com.goudong.core.util.AssertUtil;
import com.goudong.core.util.ListUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.security.KeyPair;
import java.security.cert.CertificateEncodingException;
import java.util.*;

import static com.goudong.authentication.server.enums.RedisKeyTemplateProviderEnum.APP_DROP_DOWN;
import static com.goudong.authentication.server.enums.RedisKeyTemplateProviderEnum.APP_ID;

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

    @Resource
    private HttpServletRequest httpServletRequest;

    //~methods
    //==================================================================================================================

    /**
     * 根据应用id查询应用
     *
     * @param id 应用id
     * @return 应用对象
     */
    @Override
    public BaseApp getById(Long id) {
        BaseApp baseApp = baseAppRepository.findById(id).orElseThrow(() -> ClientException.client("应用不存在"));
        return baseApp;
    }

    /**
     * 根据应用id查询应用
     * @param id 应用id
     * @return 应用对象
     */
    @Override
    public BaseApp findById(Long id) {
        String key = APP_ID.getFullKey(id);
        if (redisTool.hasKey(key)) {
            String appStr = (String)redisTool.get(APP_ID, id);
            try {
                return objectMapper.readValue(appStr, BaseApp.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        synchronized (this) {
            if (redisTool.hasKey(key)) {
                String appStr = (String)redisTool.get(APP_ID, id);
                try {
                    return objectMapper.readValue(appStr, BaseApp.class);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }

            BaseApp baseApp = baseAppRepository.findById(id).orElseThrow(() -> ClientException.client("应用不存在"));
            redisTool.set(APP_ID, JsonUtil.toJsonString(baseApp), id);
            return baseApp;
        }
    }

    /**
     * 应用分页
     * @param req 查询参数
     * @return 分页结果
     */
    @Override
    public PageResult<BaseAppPageResp> page(BaseAppPageReq req) {
        MyAuthentication authentication = SecurityContextUtil.get();
        AssertUtil.isTrue(authentication.superAdmin(), () -> ClientException.clientByForbidden());
        SearchResult<BaseAppPageReq> search = beanSearcher.search(BaseAppPageReq.class, BeanSearcherUtil.getParaMap(req));
        return PageResultUtil.convert(search, req, BaseAppPageResp.class);
    }

    /**
     * 新增应用
     *
     * @param app 新增应用参数
     * @return 应用
     */
    @Override
    public BaseAppDTO save(BaseApp app) {
        return baseAppMapper.toDto(baseAppRepository.save(app));
    }

    /**
     * 修改应用
     * @param req 修改应用对象
     * @return 修改应用后的对象
     */
    @Override
    public BaseAppDTO update(BaseAppUpdate req) {
        BaseApp baseApp = this.findById(req.getId());

        baseApp.setRemark(Optional.ofNullable(req.getRemark()).orElseGet(baseApp::getRemark));
        baseApp.setHomePage(Optional.ofNullable(req.getHomePage()).orElseGet(baseApp::getHomePage));
        baseApp.setEnabled(Optional.ofNullable(req.getEnabled()).orElseGet(baseApp::getEnabled));
        baseAppRepository.save(baseApp);

        cleanCache(req.getId());
        return baseAppMapper.toDto(baseApp);
    }

    /**
     * 删除应用
     * @param id id
     * @return 被删除应用
     */
    @Override
    public BaseAppDTO delete(Long id) {
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
        return null;
    }

    /**
     * 应用下拉
     * @param req 下拉参数
     * @return 用户能访问的应用下拉列表
     */
    @Override
    public List<BaseAppDropDownReq> dropDown(BaseAppDropDownReq req) {
        MyAuthentication authentication = SecurityContextUtil.get();
        // 不是超级管理员只返回自身应用
        if (!authentication.superAdmin()) {
            BaseApp app = findById(authentication.getRealAppId());
            BaseAppDropDownReq baseAppDropDownReq = new BaseAppDropDownReq();
            baseAppDropDownReq.setId(app.getId());
            baseAppDropDownReq.setName(app.getName());
            return ListUtil.newArrayList(baseAppDropDownReq);
        }
        // 超级管理员返回所有应用
        return allDropDown(req);
    }

    /**
     * 应用下拉
     * @param req 下拉参数
     * @return 所有应用下拉
     */
    @Override
    public List<BaseAppDropDownReq> allDropDown(BaseAppDropDownReq req) {
        // 超级管理员返回所有应用
        String key = APP_DROP_DOWN.getFullKey();
        if (redisTool.hasKey(key)) {
            return redisTool.getList(APP_DROP_DOWN, BaseAppDropDownReq.class);
        }
        synchronized (this) {
            if (redisTool.hasKey(key)) {
                return redisTool.getList(APP_DROP_DOWN, BaseAppDropDownReq.class);
            }

            List<BaseAppDropDownReq> list = beanSearcher.searchAll(BaseAppDropDownReq.class);

            redisTool.set(APP_DROP_DOWN, list);

            return list;
        }
    }

    /**
     * 根据请求头中{@code X-App-Id}，查询应用
     *
     * @return baseApp
     */
    @Override
    public BaseApp findByHeader() {
        Long appId = (Long)httpServletRequest.getAttribute(HttpHeaderConst.X_APP_ID);
        Assert.notNull(appId, () -> ClientException.client("请求头中应用id不存在"));
        return this.findById(appId);
    }

    /**
     * 判断应用是否存在
     *
     * @param appId 应用id
     * @return true 应用存在；false 应用不存在
     */
    @Override
    public boolean isExist(Long appId) {
        return baseAppRepository.existsById(appId);
    }

    /**
     * 删除缓存
     * @param id 应用id
     */
    public void cleanCache(Long id) {
        redisTool.execute(new SessionCallback<Object>() {
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
