package com.goudong.authentication.server.service.manager.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.goudong.authentication.server.constant.RoleConst;
import com.goudong.authentication.server.domain.BaseApp;
import com.goudong.authentication.server.domain.BaseAppCert;
import com.goudong.authentication.server.domain.BaseUser;
import com.goudong.authentication.server.rest.req.BaseAppCertCreateReq;
import com.goudong.authentication.server.rest.req.BaseAppCreate;
import com.goudong.authentication.server.rest.req.BaseAppUpdate;
import com.goudong.authentication.server.rest.req.search.BaseAppDropDownReq;
import com.goudong.authentication.server.rest.req.search.BaseAppPageReq;
import com.goudong.authentication.server.rest.resp.BaseAppPageResp;
import com.goudong.authentication.server.service.BaseAppCertService;
import com.goudong.authentication.server.service.BaseAppService;
import com.goudong.authentication.server.service.BaseRoleService;
import com.goudong.authentication.server.service.BaseUserService;
import com.goudong.authentication.server.service.dto.BaseAppCertDTO;
import com.goudong.authentication.server.service.dto.BaseAppDTO;
import com.goudong.authentication.server.service.dto.MyAuthentication;
import com.goudong.authentication.server.service.manager.BaseAppManagerService;
import com.goudong.authentication.server.service.mapper.BaseAppCertMapper;
import com.goudong.authentication.server.service.mapper.BaseAppMapper;
import com.goudong.authentication.server.util.SecurityContextUtil;
import com.goudong.boot.web.core.ClientException;
import com.goudong.boot.web.core.ServerException;
import com.goudong.core.lang.PageResult;
import com.goudong.core.security.cer.CertificateUtil;
import com.goudong.core.util.AssertUtil;
import com.goudong.core.util.ListUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.security.cert.CertificateEncodingException;
import java.util.*;

/**
 * 类描述：
 *
 * @author  Administrator
 * @version 1.0
 */
@Service
public class BaseAppManagerServiceImpl implements BaseAppManagerService {
    //~fields
    //==================================================================================================================

    @Resource
    private BaseAppService baseAppService;

    @Resource
    private BaseUserService baseUserService;

    @Resource
    private BaseRoleService baseRoleService;


    @Resource
    private BaseAppCertService baseAppCertService;

    @Resource
    private TransactionTemplate transactionTemplate;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private BaseAppMapper baseAppMapper;

    @Resource
    private BaseAppCertMapper baseAppCertMapper;

    //~methods
    //==================================================================================================================
    /**
     * 根据{@code appId}查询应用
     *
     * @param appId 应用id
     * @return 应用对象
     */
    @Override
    public BaseApp findById(Long appId) {
        return baseAppService.findById(appId);
    }

    /**
     * 分页查询应用
     *
     * @param req 查询条件
     * @return 应用分页
     */
    @Override
    public PageResult<BaseAppPageResp> page(BaseAppPageReq req) {
        return baseAppService.page(req);
    }

    /**
     * 新增应用
     *
     * @param req 新增应用参数
     * @return 新增应用对象
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

        // 生成公钥私钥和证书
        DateTime validTime = DateUtil.offsetDay(new Date(), 30);
        CertificateUtil.Cer cer = CertificateUtil.create("goudong", req.getName(), validTime);
        BaseAppCert baseAppCert = new BaseAppCert();
        baseAppCert.setAppId(baseApp.getId());
        try {
            baseAppCert.setSerialNumber(cer.getCer().getSerialNumber().toString(16));
            baseAppCert.setCert(Base64.getEncoder().encodeToString(cer.getCer().getEncoded()));
        } catch (CertificateEncodingException e) {
            throw new RuntimeException(e);
        }
        baseAppCert.setPrivateKey(Base64.getEncoder().encodeToString(cer.getKeyPair().getPrivate().getEncoded()));
        baseAppCert.setPublicKey(Base64.getEncoder().encodeToString(cer.getKeyPair().getPublic().getEncoded()));
        baseAppCert.setValidTime(validTime);
        baseAppCert.setBaseApp(baseApp);

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
        baseUser.setRoles(ListUtil.newArrayList(baseRoleService.findByAppAdmin()));
        // 设置证书
        baseApp.setCerts(ListUtil.newArrayList(baseAppCert));

        transactionTemplate.execute(status -> {
            try {
                // 新增应用
                baseAppService.save(baseApp);
                // 新增管理用户
                baseUserService.save(baseUser);
                baseAppService.cleanCache(baseApp.getId());
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
     *
     * @param req 修改应用对象
     * @return 修改应用后的对象
     */
    @Override
    public BaseAppDTO update(BaseAppUpdate req) {
        return baseAppService.update(req);
    }

    /**
     * 根据id删除应用
     *
     * @param id id
     * @return 被删除的应用对象
     */
    @Override
    public BaseAppDTO deleteById(Long id) {
        return baseAppService.delete(id);
    }

    /**
     * 应用下拉，显示当前用户能查询到的应用，超级管理员查询所有，管理员只能查询本应用
     *
     * @param req 查询条件
     * @return 用户能访问的应用列表
     */
    @Override
    public List<BaseAppDropDownReq> appDropDown(BaseAppDropDownReq req) {
        return baseAppService.dropDown(req);
    }

    /**
     * 所有应用下拉
     *
     * @param req 查询条件
     * @return 所有应用
     */
    @Override
    public List<BaseAppDropDownReq> allDropDown(BaseAppDropDownReq req) {
        return baseAppService.allDropDown(req);
    }

    /**
     * 根据请求头中{@code X-App-Id}，查询应用
     *
     * @return baseApp
     */
    @Override
    public BaseApp findByHeader() {
        return baseAppService.findByHeader();
    }

    /**
     * 查询应用的所有证书
     *
     * @param appId 应用id
     * @return 应用所有证书
     */
    @Override
    public List<BaseAppCertDTO> listCertsByAppId(Long appId) {
        List<BaseAppCertDTO> certDTOS = baseAppCertService.listCertsByAppId(appId);
        certDTOS.forEach(p -> {
            // 私钥不显示
            p.setPrivateKey(null);
        });
        return certDTOS;
    }

    /**
     * 创建证书
     *
     * @param req 创建参数
     * @return 证书记录
     */
    @Override
    public BaseAppCertDTO createCert(BaseAppCertCreateReq req) {
        // 校验应用是否存在
        BaseAppCert cert = transactionTemplate.execute(status -> {
            try {
                BaseApp baseApp = baseAppService.getById(req.getAppId());
                AssertUtil.isNotNull(baseApp, () -> ClientException.client("应用无效"));
                req.setAppName(Optional.ofNullable(req.getAppName()).orElseGet(() -> baseApp.getName())); // 应用名没传就是用应用原名

                // 校验过期时间是未来
                AssertUtil.isTrue(req.getValidTime().after(new Date()), () -> ClientException.client("证书过期时间参数有误"));

                BaseAppCert baseAppCert = new BaseAppCert();

                // 创建证书
                CertificateUtil.Cer cer = CertificateUtil.create("goudong", req.getAppName(), req.getValidTime());
                baseAppCert.setAppId(baseApp.getId());
                baseAppCert.setSerialNumber(cer.getCer().getSerialNumber().toString(16)); // 16进制序列号
                baseAppCert.setCert(Base64.getEncoder().encodeToString(cer.getCer().getEncoded())); // 证书
                baseAppCert.setPrivateKey(Base64.getEncoder().encodeToString(cer.getKeyPair().getPrivate().getEncoded()));
                baseAppCert.setPublicKey(Base64.getEncoder().encodeToString(cer.getKeyPair().getPublic().getEncoded()));
                baseAppCert.setValidTime(req.getValidTime());
                List<BaseAppCert> certs = baseApp.getCerts();
                certs.add(baseAppCert);
                baseAppCert.setBaseApp(baseApp);
                baseAppService.save(baseApp);
                return baseAppCert;
            } catch (Exception e) {
                status.setRollbackOnly();
                throw new RuntimeException(e);
            }
        });

        return baseAppCertMapper.toDto(cert);
    }

}
