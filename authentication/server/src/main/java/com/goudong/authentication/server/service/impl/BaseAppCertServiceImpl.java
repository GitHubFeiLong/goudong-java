package com.goudong.authentication.server.service.impl;

import com.goudong.authentication.server.domain.BaseAppCert;
import com.goudong.authentication.server.repository.BaseAppCertRepository;
import com.goudong.authentication.server.rest.req.BaseAppCertCreateReq;
import com.goudong.authentication.server.service.BaseAppCertService;
import com.goudong.authentication.server.service.dto.BaseAppCertDTO;
import com.goudong.authentication.server.service.mapper.BaseAppCertMapper;
import com.goudong.core.security.cer.CertificateUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.security.cert.CertificateEncodingException;
import java.util.Base64;
import java.util.List;

/**
 * 类描述：
 *
 * @author cfl
 * @version 1.0
 */
@Service
public class BaseAppCertServiceImpl implements BaseAppCertService {

    //~fields
    //==================================================================================================================
    @Resource
    private BaseAppCertRepository baseAppCertRepository;

    @Resource
    private BaseAppCertMapper baseAppCertMapper;

    //~methods
    //==================================================================================================================
    /**
     * 查询应用的所有证书
     *
     * @param appId 应用id
     * @return 应用所有证书
     */
    @Override
    public List<BaseAppCertDTO> listCertsByAppId(Long appId) {
        return baseAppCertMapper.toDto(baseAppCertRepository.findAllByAppIdOrderByIdDesc(appId));
    }

    /**
     * 创建证书
     *
     * @param req 创建参数
     * @return 证书记录
     */
    @Override
    public BaseAppCertDTO save(BaseAppCertCreateReq req) {
        // 校验应用Id是否存在
        BaseAppCert baseAppCert = new BaseAppCert();

        // 创建证书
        CertificateUtil.Cer cer = CertificateUtil.create("goudong", req.getAppName(), req.getValidTime());

        baseAppCert.setAppId(req.getAppId());
        baseAppCert.setSerialNumber(cer.getCer().getSerialNumber().toString(16)); // 16进制序列号
        try {
            baseAppCert.setCert(Base64.getEncoder().encodeToString(cer.getCer().getEncoded())); // 证书
        } catch (CertificateEncodingException e) {
            throw new RuntimeException(e);
        }
        baseAppCert.setPrivateKey(Base64.getEncoder().encodeToString(cer.getKeyPair().getPrivate().getEncoded()));
        baseAppCert.setPublicKey(Base64.getEncoder().encodeToString(cer.getKeyPair().getPublic().getEncoded()));
        baseAppCert.setValidTime(req.getValidTime());

        baseAppCertRepository.save(baseAppCert);
        return baseAppCertMapper.toDto(baseAppCert);
    }
}
