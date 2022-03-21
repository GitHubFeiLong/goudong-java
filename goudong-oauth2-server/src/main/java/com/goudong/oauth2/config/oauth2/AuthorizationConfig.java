package com.goudong.oauth2.config.oauth2;

import com.goudong.oauth2.service.BaseUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.InMemoryAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;

/**
 * 类描述：
 * 认证服务器配置
 * @author msi
 * @version 1.0
 * @date 2021/8/29 9:44
 */
@Configuration
@EnableAuthorizationServer
@EnableResourceServer
public class AuthorizationConfig extends AuthorizationServerConfigurerAdapter {


    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private BaseUserService userService;

    @Autowired
    private TokenStore redisTokenStore;

    /**
     * 授权模式的service，使用授权码模式 authorization_code必须注入
     * @return
     */
    @Bean
    public AuthorizationCodeServices authorizationCodeServices() {
        return new InMemoryAuthorizationCodeServices();
    }

    /**
     * 令牌管理服务配置
     * @return
     */
    @Bean
    public AuthorizationServerTokenServices tokenServices() {
        DefaultTokenServices services = new DefaultTokenServices();
        // 客户端配置策略
        // services.setClientDetailsService(null);
        // 支持令牌的刷新
        services.setSupportRefreshToken(true);
        // 令牌服务
        services.setTokenStore(redisTokenStore);
        // access_token 的过期时间
        services.setAccessTokenValiditySeconds(60*60*2);
        // refresh_token 的过期时间
        services.setRefreshTokenValiditySeconds(60*60*24*3);

        return services;
    }

    /**
     * 令牌访问端点的配置
     * spring Security框架默认的访问端点有如下6个：
     * /oauth/authorize：获取授权码的端点
     * /oauth/token：获取令牌端点。
     * /oauth/confifirm_access：用户确认授权提交端点。
     * /oauth/error：授权服务错误信息端点。
     * /oauth/check_token：用于资源服务访问的令牌解析端点。
     * /oauth/token_key：提供公有密匙的端点，如果你使用JWT令牌的话。
     *
     * 当然如果业务要求需要改变这些默认的端点的url，也是可以修改的，AuthorizationServerEndpointsConfigurer有一个方法，如下：
     * public AuthorizationServerEndpointsConfigurer pathMapping(String defaultPath, String customPath)
     * 第一个参数：需要替换的默认端点url
     * 第二个参数：自定义的端点url
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints
                // 配置了授权码模式所需要的服务，AuthorizationCodeServices
                .authorizationCodeServices(authorizationCodeServices())
                // 密码模式所需要的authenticationManager
                .authenticationManager(authenticationManager)
                // 令牌管理模式，无论哪种模式都需要
                .tokenServices(tokenServices())
                // 只允许POST提交访问令牌，uri: /oauth/token
                .allowedTokenEndpointRequestMethods(HttpMethod.POST);
        // endpoints.authenticationManager(authenticationManager)
        //         .userDetailsService(userService)
        //         .tokenStore(redisTokenStore);
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
        /*
            也可以实现 ClientDetailsService接口，自定义数据来源
         */

        clients.inMemory()
                .withClient("admin")//配置client_id
                .secret(passwordEncoder.encode("admin123456"))//配置client_secret
                .accessTokenValiditySeconds(3600)//配置访问token的有效期
                .refreshTokenValiditySeconds(864000)//配置刷新token的有效期
                // 配置redirect_uri，用于授权成功后跳转
                .redirectUris("http://www.baidu.com")
                // 定义客户端的权限，这里只是一个标识，资源服务可以根据这个权限进行鉴权。
                .scopes("all")
                // 给客户端分配的资源权限，对应的是资源服务，比如订单这个微服务就可以看成一个资源，作为客户端肯定不是所有资源都能访问。
                .resourceIds("res1")
                // 是否需要授权，设置为true则不需要用户点击确认授权直接返回授权码
                .autoApprove(true)
                // 定义认证中心支持的授权类型，总共支持五种
                // 授权码模式：authorization_code、密码模式：password、客户端模式：client_credentials、简化模式：implicit、令牌刷新：refresh_token，这并不是OAuth2的模式，定义这个表示认证中心支持令牌刷新
                .authorizedGrantTypes("refresh_token", "authorization_code","password");//配置grant_type，表示授权类型
    }

    /**
     * 令牌访问安全约束配置
     * 这个方法限制客户端访问认证接口的权限。
     * @param security
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security
                // 开启/oauth/token_key验证端口权限访问
                .tokenKeyAccess("permitAll()")
                // 开启/oauth/check_token验证端口认证权限访问
                .checkTokenAccess("permitAll")
                // 表示支持client_id和client_secret 做登录认证，否则请求 token 会返回 401。
                .allowFormAuthenticationForClients();

        // // 允许客户端访问 OAuth2 授权接口，否则请求 token 会返回 401。
        // security.allowFormAuthenticationForClients();
        // // 第二行和第三行分别是允许已授权用户访问 checkToken 接口和获取 token 接口。
        // security.checkTokenAccess("isAuthenticated()");
        // security.tokenKeyAccess("isAuthenticated()");
    }

}
