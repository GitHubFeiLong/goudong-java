package com.goudong.oauth2.controller.qq;

import com.goudong.commons.entity.AuthorityUserDO;
import com.goudong.oauth2.config.QQApplicationValue;
import com.goudong.oauth2.entity.OtherUserInfoBean;
import com.goudong.oauth2.enumerate.OtherUserTypeEnum;
import com.goudong.oauth2.service.UserService;
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
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;

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
@RequestMapping("/oauth/qq")
public class QQController {
    @Resource
    private QQApplicationValue qqApplicationValue;

    @Resource
    private UserService userService;

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
     * QQ登录成功后的回调接口
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
            AuthorityUserDO userByQQOpenId = userService.getUserByQQOpenId(openID);
            // 需要注册
            if (userByQQOpenId == null) {
                OtherUserInfoBean otherUserInfoBean = new OtherUserInfoBean(openID, userInfoBean.getNickname(), userInfoBean.getAvatar().getAvatarURL30(), OtherUserTypeEnum.QQ.name());
                String qqBindRedirectUriFull = qqApplicationValue.getQqBindRedirectUriFull(otherUserInfoBean);
                log.info("qqBindRedirectUriFull:{}", qqBindRedirectUriFull);
                response.sendRedirect(qqBindRedirectUriFull);
            }
        }

    }
}
