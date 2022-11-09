package com.goudong.oauth2.controller.authentication;

import com.goudong.boot.redis.core.RedisTool;
import com.goudong.commons.dto.oauth2.BaseUserDTO;
import com.goudong.commons.enumerate.user.OtherUserTypeEnum;
import com.goudong.commons.pojo.Transition;
import com.goudong.commons.utils.core.BeanUtil;
import com.goudong.commons.utils.core.LogUtil;
import com.goudong.oauth2.core.OtherUserInfoBean;
import com.goudong.oauth2.dto.BaseAuthenticationLogDTO;
import com.goudong.oauth2.dto.BaseTokenDTO;
import com.goudong.oauth2.enumerate.AuthenticationLogTypeEnum;
import com.goudong.oauth2.po.BaseUserPO;
import com.goudong.oauth2.properties.RedirectPageProperties;
import com.goudong.oauth2.service.BaseAuthenticationLogService;
import com.goudong.oauth2.service.BaseTokenService;
import com.goudong.oauth2.service.BaseUserService;
import com.qq.connect.QQConnectException;
import com.qq.connect.api.OpenID;
import com.qq.connect.api.qzone.UserInfo;
import com.qq.connect.javabeans.AccessToken;
import com.qq.connect.javabeans.qzone.UserInfoBean;
import com.qq.connect.oauth.Oauth;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 类描述：
 * qq登录
 * @ClassName QQController
 * @Author msi
 * @Date 2021/2/25 17:21
 * @Version 1.0
 */
@SuppressWarnings("ALL")
@Api(tags = "QQ")
@Slf4j
@Controller
@RequestMapping("/qq")
public class QQController {

    /**
     * 用户服务接口
     */
    private final BaseUserService baseUserService;
    /**
     * redis tool 工具
     */
    private final RedisTool redisTool;
    /**
     * 重定向前端配置的页面信息
     */
    private final RedirectPageProperties redirectPageProperties;

    /**
     * token服务层接口
     */
    private final BaseTokenService baseTokenService;

    /**
     * 认证日志服务层接口
     */
    private final BaseAuthenticationLogService baseAuthenticationLogService;

    public QQController(BaseUserService baseUserService,
                        RedisTool redisTool,
                        RedirectPageProperties redirectPageProperties,
                        BaseTokenService baseTokenService,
                        BaseAuthenticationLogService baseAuthenticationLogService) {
        this.baseUserService = baseUserService;
        this.redisTool = redisTool;
        this.redirectPageProperties = redirectPageProperties;
        this.baseTokenService = baseTokenService;
        this.baseAuthenticationLogService = baseAuthenticationLogService;
    }

    /**
     * qq登录
     * @param request
     * @param response
     * @throws QQConnectException
     * @throws IOException
     */
    @ApiOperation(value = "QQ互联登录", notes = "")
    @GetMapping("/login")
    public void login (HttpServletRequest request, HttpServletResponse response) throws QQConnectException, IOException {
        // 重定向认证界面
        response.sendRedirect(new Oauth().getAuthorizeURL(request));
    }

    /**
     * QQ登录成功后的回调接口,跳转页面
     * @param request
     * @param response
     * @throws QQConnectException
     * @throws IOException
     */
    @ApiOperation(value = "QQ登录回调", notes = "QQ登录成功后，QQ互联的回调地址")
    @GetMapping("/fallback")
    public void fallback(HttpServletRequest request, HttpServletResponse response) throws QQConnectException, IOException {
        AccessToken accessTokenObj = (new Oauth()).getAccessTokenByRequest(request);
        String accessToken = null, openID = null;
        long tokenExpireIn = 0L;
        // login()方法请求和回调必须是同一个服务处理，不然会进入if，导致失败
        if (StringUtils.isEmpty(accessTokenObj.getAccessToken())) {
            log.error("没有获取到响应参数：{}", accessTokenObj.getAccessToken());
        }else{
            accessToken = accessTokenObj.getAccessToken();
            tokenExpireIn = accessTokenObj.getExpireIn();
            OpenID openIDObj =  new OpenID(accessToken);
            openID = openIDObj.getUserOpenID();
            UserInfo qzoneUserInfo = new UserInfo(accessToken, openID);
            log.info("qzoneUserInfo:{}", qzoneUserInfo.toString());
            UserInfoBean userInfoBean = qzoneUserInfo.getUserInfo();
            log.info("UserInfoBean:{}", userInfoBean.toString());

            // 根据openId查询用户
            BaseUserDTO byOpenId = baseUserService.findByOpenId(openID);

            // openId未使用，可以绑定
            if (byOpenId == null) {
                OtherUserInfoBean otherUserInfoBean = OtherUserInfoBean.builder()
                        .openId(openID)
                        .nickname(userInfoBean.getNickname())
                        .userType(OtherUserTypeEnum.QQ.name())
                        .headPortrait30(userInfoBean.getAvatar().getAvatarURL30())
                        .build();
                String qqBindRedirectUriFull = redirectPageProperties.getBindPageUrl(otherUserInfoBean);
                LogUtil.debug(log, "绑定qq的前端URL地址:{}", qqBindRedirectUriFull);
                // 重定向进行绑定
                response.sendRedirect(qqBindRedirectUriFull);
            } else {

                // 持久化token到Mysql
                BaseTokenDTO tokenDTO = baseTokenService.loginHandler(byOpenId.getId());

                // 将认证信息存储到redis中
                BaseUserPO baseUserPO = BeanUtil.copyProperties(byOpenId, BaseUserPO.class);
                baseUserService.saveAccessToken2Redis(baseUserPO, tokenDTO.getAccessToken());

                Transition transition = Transition.builder()
                        .accessToken(tokenDTO.getAccessToken())
                        .refreshToken(tokenDTO.getRefreshToken())
                        .accessExpires(tokenDTO.getAccessExpires())
                        .refreshExpires(tokenDTO.getRefreshExpires())
                        .redirectUrl(redirectPageProperties.getIndexPage())
                        .build();

                // 重定向首页，并带上token参数
                response.sendRedirect(redirectPageProperties.getTransitionPageUrl(transition));
            }

            // 保存认证日志
            BaseAuthenticationLogDTO baseAuthenticationLogDTO = new BaseAuthenticationLogDTO(
                    openID,
                    true,
                    AuthenticationLogTypeEnum.QQ.name(),
                    "认证成功");
            baseAuthenticationLogService.create(baseAuthenticationLogDTO);
        }

    }
}
