package com.goudong.oauth2.config;

import com.goudong.oauth2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

/**
 * 类描述：
 * 认证服务器配置
 * @author msi
 * @version 1.0
 * @date 2021/8/29 9:44
 */
@Configuration
public class AuthorizationConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private TokenStore redisTokenStore;

    /**
     * 使用密码模式需要配置
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints.authenticationManager(authenticationManager)
                .userDetailsService(userService)
                .tokenStore(redisTokenStore);
    }

    /**
     * authorizedGrantTypes 可以包括如下几种设置中的一种或多种：
     *
     * authorization_code：授权码类型。
     * implicit：隐式授权类型。
     * password：资源所有者（即用户）密码类型。
     * client_credentials：客户端凭据（客户端ID以及Key）类型。
     * refresh_token：通过以上授权获得的刷新令牌来获取新的令牌。
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("admin")//配置client_id
                .secret(passwordEncoder.encode("admin123456"))//配置client_secret
                .accessTokenValiditySeconds(3600)//配置访问token的有效期
                .refreshTokenValiditySeconds(864000)//配置刷新token的有效期
                .redirectUris("http://www.baidu.com")//配置redirect_uri，用于授权成功后跳转
                .scopes("all")//配置申请的权限范围
                // "refresh_token"
                .authorizedGrantTypes("refresh_token", "authorization_code","password");//配置grant_type，表示授权类型
    }

    /**
     * 这个方法限制客户端访问认证接口的权限。
     * @param security
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        // 允许客户端访问 OAuth2 授权接口，否则请求 token 会返回 401。
        security.allowFormAuthenticationForClients();
        // 第二行和第三行分别是允许已授权用户访问 checkToken 接口和获取 token 接口。
        security.checkTokenAccess("isAuthenticated()");
        security.tokenKeyAccess("isAuthenticated()");
    }

}
