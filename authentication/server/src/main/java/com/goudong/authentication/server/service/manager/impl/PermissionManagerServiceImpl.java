package com.goudong.authentication.server.service.manager.impl;

import com.goudong.authentication.common.core.Jwt;
import com.goudong.authentication.common.core.UserSimple;
import com.goudong.authentication.common.util.HttpRequestUtil;
import com.goudong.authentication.server.domain.BaseApp;
import com.goudong.authentication.server.domain.BaseAppCert;
import com.goudong.authentication.server.service.BaseAppCertService;
import com.goudong.authentication.server.service.BaseAppService;
import com.goudong.authentication.server.service.BaseMenuService;
import com.goudong.authentication.server.service.dto.BaseMenuDTO;
import com.goudong.authentication.server.service.dto.PermissionDTO;
import com.goudong.authentication.server.service.manager.PermissionManagerService;
import com.goudong.boot.web.core.ClientException;
import com.goudong.core.security.cer.CertificateUtil;
import com.goudong.core.util.AssertUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 类描述：
 * 权限管理服务
 * @author chenf
 * @version 1.0
 */
@Slf4j
@Service
public class PermissionManagerServiceImpl implements PermissionManagerService {
    //~fields
    //==================================================================================================================
    @Resource
    private BaseAppService baseAppService;

    @Resource
    private BaseMenuService baseMenuService;

    @Resource
    private HttpServletRequest httpServletRequest;


    //~methods
    //==================================================================================================================
    /**
     * 查询权限列表
     *
     * @return 权限列表
     */
    @Override
    @Transactional(readOnly = true)
    public List<PermissionDTO> listPermission() {
        String authentication = Optional.ofNullable(httpServletRequest.getHeader("Authorization"))
                .orElseThrow(() -> ClientException.client("请求头Authorization丢失"));
        Long xAppId = HttpRequestUtil.getXAppId();

        // 查询应用证书
        BaseApp baseApp = Optional.ofNullable(baseAppService.getById(xAppId)).orElseThrow(() -> ClientException.client("应用不存在"));
        AssertUtil.isTrue(Boolean.TRUE.equals(baseApp.getEnabled()), () -> ClientException.client("应用未激活"));

        // 校验请求头
        if (authentication.startsWith("Bearer ")) {
            String token = authentication.substring(8);
            Jwt jwt = new Jwt(baseApp.getSecret());
            UserSimple userSimple = jwt.parseToken(token);
            return baseMenuService.findAllPermission(xAppId);
        }

        //
        if (authentication.startsWith("GOUDONG-SHA256withRSA")) {
            // 提取关键信息
            // 使用正则表达式，提取关键信息
            Pattern pattern = Pattern.compile("GOUDONG-SHA256withRSA appid=\"(\\d+)\",serial_number=\"(.*)\",timestamp=\"(\\d+)\",nonce_str=\"(.*)\" signature=\"(.*)\"");
            Matcher matcher = pattern.matcher(authentication);
            AssertUtil.isTrue(matcher.matches(), "请求头格式错误");
            Long appId =  Long.parseLong(matcher.group(1));             // 应用id
            String serialNumber =  matcher.group(2);                    // 证书序列号
            long timestamp =  Long.parseLong(matcher.group(3));         // 时间戳
            String nonceStr =  matcher.group(4);                        // 随机字符串
            String signature =  matcher.group(5);                       // 签名

            // 查询应用证书
            AssertUtil.isTrue(Objects.equals(xAppId, appId), () -> ClientException.client("应用id不匹配"));
            BaseAppCert baseAppCert = baseApp.getCerts().stream().filter(f -> f.getSerialNumber().equals(serialNumber)).findFirst().orElseThrow(() -> ClientException.client("证书不存在"));
            AssertUtil.isTrue(baseAppCert.getValidTime().after(new Date()), () -> ClientException.client("证书已过期"));

            // 验签
            // 签名校验
            String body = HttpRequestUtil.getBody(httpServletRequest);
            // 拼装消息 // 转换byte[]
            byte[] message = (appId + "\n" + serialNumber + "\n" + timestamp + "\n" + nonceStr + "\n" + body + "\n").getBytes(StandardCharsets.UTF_8);
            try {
                // 加载证书
                X509Certificate certificate = CertificateUtil.loadCertificate(baseAppCert.getCert());
                Signature sign = Signature.getInstance("SHA256withRSA");
                sign.initVerify(certificate);
                sign.update(message);
                byte[] signatureB = Base64.decodeBase64(signature); // 拉卡拉返回的签名转byte[]
                boolean verify = sign.verify(signatureB); // 验签,验拉卡拉返回的签名

                log.info("拉卡拉验证签名结束，验证结果：{}", verify);
                AssertUtil.isTrue(verify, () -> ClientException.client("验签失败"));
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException("当前java环境不支持SHA256withRSA", e);
            } catch (InvalidKeyException e) {
                throw new RuntimeException("无效的证书", e);
            } catch (SignatureException e) {
                throw new RuntimeException("签名验证过程发生了错误", e);
            }

            // 签名验证成功，查询权限
            return baseMenuService.findAllPermission(appId);

        }

        throw ClientException.client("未知的认证方式");
    }
}
