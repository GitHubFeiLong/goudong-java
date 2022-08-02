package com.goudong.bpm.config;import com.goudong.bpm.filter.BpmAuthenticationFilter;import com.goudong.commons.filter.UserContextFilter;import lombok.extern.slf4j.Slf4j;import org.springframework.context.annotation.Bean;import org.springframework.security.authentication.AuthenticationManager;import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;import org.springframework.security.config.annotation.web.builders.HttpSecurity;import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;import org.springframework.security.config.http.SessionCreationPolicy;import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;import org.springframework.security.crypto.password.PasswordEncoder;import org.springframework.security.web.context.SecurityContextPersistenceFilter;/** * SpringSecurity配置 * Created by macro on 2019/10/8. */@Slf4j@EnableWebSecurity@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true) // 开启方法注解权限配置public class SecurityConfig extends WebSecurityConfigurerAdapter {    @Bean    public PasswordEncoder passwordEncoder() {        return new BCryptPasswordEncoder();    }    @Bean    @Override    public AuthenticationManager authenticationManagerBean() throws Exception {        return super.authenticationManagerBean();    }    @Override    public void configure(HttpSecurity http) throws Exception {        // url权限认证处理        http                // 支持EL表达式                // https://docs.spring.io/spring-security/site/docs/5.3.13.RELEASE/reference/html5/#el-access                .authorizeRequests()                //所有请求都需要认证                .anyRequest()                .authenticated()                 ;        // 将session策略设置为无状态的,通过token进行权限认证,并关闭默认的SecurityContextPersistenceFilter        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)                .and()                // 关闭SecurityContextPersistenceFilter                .securityContext().disable();        // 开启自动配置的登录功能        http.formLogin() //开启登录                //自定义登录请求路径(post)                .loginProcessingUrl("/login")                .permitAll();        // 添加过滤器        http.addFilterAfter(new BpmAuthenticationFilter(), SecurityContextPersistenceFilter.class);        http.addFilterBefore(new UserContextFilter(), BpmAuthenticationFilter.class);        // 禁用csrf防御机制(跨域请求伪造)，这么做在测试和开发会比较方便。        //http.csrf().disable();    }}