package com.goudong.oauth2.qq;

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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 类描述：
 * qq登录
 * @ClassName QQController
 * @Author msi
 * @Date 2021/2/25 17:21
 * @Version 1.0
 */
@Api(tags = "QQ")
@Slf4j
@Controller
@RequestMapping("/oauth/qq")
public class QQController {

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
            log.info("openIDObj:{}, openID:{}", openIDObj, openID);
            UserInfo qzoneUserInfo = new UserInfo(accessToken, openID);
            log.info("qzoneUserInfo:{}", qzoneUserInfo);
            UserInfoBean userInfoBean = qzoneUserInfo.getUserInfo();
            log.info("UserInfoBean:{}", userInfoBean);

            HttpSession session=request.getSession();
            session.setAttribute("name", userInfoBean.getNickname());
            session.setAttribute("avatar", userInfoBean.getAvatar().getAvatarURL30());
            response.sendRedirect("index.jsp");
        }

    }
}
