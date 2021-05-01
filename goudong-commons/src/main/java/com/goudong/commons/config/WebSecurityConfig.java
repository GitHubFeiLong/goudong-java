package com.goudong.commons.config;

import com.goudong.commons.dao.AuthorityIgnoreResourceDao;
import com.goudong.commons.entity.AuthorityIgnoreResourceDO;
import com.goudong.commons.filter.JWTAuthorizationFilter;
import com.goudong.commons.handler.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Stream;

/**
 * 类描述：
 * spring security 的配置
 * EnableGlobalMethodSecurity注解 开启方法资源拦截
 * @ClassName WebSecurityConfig
 * @Author msi
 * @Date 2020/12/29 22:40
 * @Version 1.0
 */
@Slf4j
@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    private UrlAuthenticationEntryPoint authenticationEntryPoint; //自定义未登录时：返回状态码401

    @Resource
    private UrlAuthenticationSuccessHandler authenticationSuccessHandler; //自定义登录成功处理器：返回状态码200

    @Resource
    private UrlAuthenticationFailureHandler authenticationFailureHandler; //自定义登录失败处理器：返回状态码402

    @Resource
    private UrlAccessDeniedHandler accessDeniedHandler; //自定义权限不足处理器：返回状态码403

    @Resource
    private UrlLogoutSuccessHandler logoutSuccessHandler; //自定义注销成功处理器：返回状态码200

    @Resource
    private SelfAuthenticationProvider authenticationProvider; //自定义登录认证

    @Resource
    private SelfFilterInvocationSecurityMetadataSource filterInvocationSecurityMetadataSource; //动态获取url权限配置
    @Resource
    private SelfAccessDecisionManager accessDecisionManager; //自定义权限判断管理器

    @Resource
    private AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails> authenticationDetailsSource; //身份验证详细信息源

    @Resource
    private AuthorityIgnoreResourceDao authorityIgnoreResourceDao;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /**
     * 放行资源
     * @param web
     */
    @Override
    public void configure(WebSecurity web) {
        List<AuthorityIgnoreResourceDO> authorityIgnoreResourceDOS = authorityIgnoreResourceDao.selectAll();
        if (!authorityIgnoreResourceDOS.isEmpty()) {
            authorityIgnoreResourceDOS.stream()
                    .filter(f->!f.getIsDelete())
                    .forEach(p1->{
                        // 数据库设置的该路径的请求方式用逗号进行分割
                        String[] methods = p1.getMethod().split(",");
                        Assert.isTrue(methods != null && methods.length > 0, "请求方式为空");
                        Stream.of(methods).forEach(p2->{
                            web.ignoring().antMatchers(HttpMethod.resolve(p2.toUpperCase()), p1.getUrl());
                        });
                    });
        }
        log.info("ignore url >> {}", web.toString());
    }

    /**
     * 自定义登录认证
     * @param auth
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authenticationProvider);
    }


    /**
     * 配置安全拦截机制（最重要）
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 关闭csrf验证(防止跨站请求伪造攻击)
        http.csrf().disable();

        // 未登录时：返回状态码401
        http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint);

        // 无权访问时：返回状态码403
        http.exceptionHandling().accessDeniedHandler(accessDeniedHandler);

        // url权限认证处理
        http
            .authorizeRequests()
            .anyRequest().authenticated()//所有请求都需要认证
            .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                @Override
                public <O extends FilterSecurityInterceptor> O postProcess(O o) {
                    o.setSecurityMetadataSource(filterInvocationSecurityMetadataSource); //动态获取url权限配置
                    o.setAccessDecisionManager(accessDecisionManager); //权限判断
                    return o;
                }
            });

        // 将session策略设置为无状态的,通过token进行权限认证
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // 开启自动配置的登录功能
        http.formLogin() //开启登录
                .loginProcessingUrl("/api/user/nonceLogin") //自定义登录请求路径(post)
                .usernameParameter("username").passwordParameter("password") //自定义登录用户名密码属性名,默认为username和password
                .successHandler(authenticationSuccessHandler) //验证成功处理器(前后端分离)：返回状态码200
                .failureHandler(authenticationFailureHandler) //验证失败处理器(前后端分离)：返回状态码402
                .authenticationDetailsSource(authenticationDetailsSource) //身份验证详细信息源(登录验证中增加额外字段)
                .permitAll();
        // 开启自动配置的注销功能
        http.logout() //用户注销, 清空session
                .logoutUrl("/nonceLogout") //自定义注销请求路径
                .logoutSuccessHandler(logoutSuccessHandler); //注销成功处理器(前后端分离)：返回状态码200

        // 添加Jwt过滤器
        http.addFilter(new JWTAuthorizationFilter(authenticationManager()));
    }


}
