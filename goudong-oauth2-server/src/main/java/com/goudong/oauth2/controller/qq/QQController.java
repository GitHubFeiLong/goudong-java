package com.goudong.oauth2.controller.qq;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.goudong.commons.dto.AuthorityUserDTO;
import com.goudong.commons.po.AuthorityUserPO;
import com.goudong.commons.pojo.Transition;
import com.goudong.commons.utils.AuthorityUserUtil;
import com.goudong.commons.utils.BeanUtil;
import com.goudong.commons.utils.JwtTokenUtil;
import com.goudong.commons.utils.RedisOperationsUtil;
import com.goudong.oauth2.config.UIProperties;
import com.goudong.oauth2.entity.OtherUserInfoBean;
import com.goudong.oauth2.enumerate.OtherUserTypeEnum;
import com.goudong.oauth2.service.AuthorityUserService;
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

import javax.annotation.Resource;
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
    @Resource
    private UIProperties uiProperties;

    @Resource
    private AuthorityUserService userService;

    @Resource
    private RedisOperationsUtil redisOperationsUtil;

    @Resource
    private AuthorityUserUtil authorityUserUtil;

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
        if (StringUtils.isEmpty(accessTokenObj.getAccessToken())) {
            log.error("没有获取到响应参数");
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
            LambdaQueryWrapper<AuthorityUserPO> lambdaQueryWrapper = new LambdaQueryWrapper();
            lambdaQueryWrapper.eq(AuthorityUserPO::getQqOpenId, openID);
            AuthorityUserPO authorityUserPO = userService.getOne(lambdaQueryWrapper);

            // openId未使用，可以绑定
            if (authorityUserPO == null) {
                OtherUserInfoBean otherUserInfoBean = new OtherUserInfoBean(openID, userInfoBean.getNickname(), userInfoBean.getAvatar().getAvatarURL30(), OtherUserTypeEnum.QQ.name());
                String qqBindRedirectUriFull = uiProperties.getBindPageUrl(otherUserInfoBean);
                log.info("qqBindRedirectUriFull:{}", qqBindRedirectUriFull);
                response.sendRedirect(qqBindRedirectUriFull);
            } else {
                AuthorityUserDTO authorityUserDTO = BeanUtil.copyProperties(authorityUserPO, AuthorityUserDTO.class);
                // 生成token
                String token = JwtTokenUtil.generateToken(authorityUserDTO, JwtTokenUtil.VALID_HOUR);
                // 设置到响应头里
                response.setHeader(JwtTokenUtil.TOKEN_HEADER, token);

                // 将用户登录信息保存到redis中
                authorityUserUtil.login(token, authorityUserDTO);

                Transition transition = Transition.builder()
                        .token(token)
                        .redirectUrl(uiProperties.getIndexPage())
                        .build();
                // 重定向首页，并带上token参数
                response.sendRedirect(uiProperties.getTransitionPageUrl(transition));
            }
        }

    }
}
