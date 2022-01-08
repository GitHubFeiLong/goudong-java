// package com.goudong.oauth2.config;
//
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
// import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
// import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
// import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
//
// /**
//  * 类描述：
//  * 资源服务器，可以单独创建一个服务和oauth认证服务分离
//  * @auther msi
//  * @date 2021/12/6 16:59
//  * @version 1.0
//  */
// @Configuration
// @EnableResourceServer
// public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
//
//     /**
//      * 配置令牌校验服务，客户端携带令牌访问资源，作为资源端，必须校验令牌的真伪
//      * @return
//      */
//     // @Bean
//     public RemoteTokenServices tokenServices() {
//         // 远程调用授权服务的check_token进行令牌的校验
//         RemoteTokenServices services = new RemoteTokenServices();
//         // /oauth/check_token 这个url是认证中心校验的token的端点
//         services.setCheckTokenEndpointUrl("http://localhost:8080/auth-server/oauth/check_token");
//         // 客户端的唯一id
//         services.setClientId("id");
//         // 客户端的密钥
//         services.setClientSecret("124");
//
//         return services;
//     }
//
//     /**
//      * 配置资源id和令牌校验服务
//      * @param resources
//      * @throws Exception
//      */
//     @Override
//     public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
//         // super.configure(resources);
//         resources
//                 // 配置唯一的资源id
//                 .resourceId("res1")
//                 // 配置令牌校验服务
//                 .tokenServices(tokenServices());
//     }
//
//     /**
//      * 配置security的安全机制
//      * @param http
//      * @throws Exception
//      */
//     @Override
//     public void configure(HttpSecurity http) throws Exception {
//         //#oauth2.hasScope() 校验客户端的权限，这个all是在客户端中的scope
//         http.authorizeRequests()
//                 .antMatchers("/**")
//                 .access("#oauth2.hasScope('all')")
//                 .anyRequest()
//                 .authenticated();
//     }
// }
//
