package com.goudong.oauth2.config.security;import com.fasterxml.jackson.core.JsonProcessingException;import com.fasterxml.jackson.databind.ObjectMapper;import com.goudong.oauth2.filter.MySecurityContextPersistenceFilter;import com.goudong.oauth2.po.BaseWhitelistPO;import com.goudong.oauth2.repository.BaseWhitelistRepository;import com.goudong.oauth2.service.BaseAppService;import com.goudong.oauth2.service.BaseUserService;import lombok.extern.slf4j.Slf4j;import org.springframework.context.annotation.Bean;import org.springframework.http.HttpMethod;import org.springframework.security.authentication.AuthenticationManager;import org.springframework.security.config.annotation.ObjectPostProcessor;import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;import org.springframework.security.config.annotation.web.builders.HttpSecurity;import org.springframework.security.config.annotation.web.builders.WebSecurity;import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;import org.springframework.security.config.http.SessionCreationPolicy;import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;import org.springframework.security.crypto.password.PasswordEncoder;import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;import org.springframework.security.web.context.SecurityContextPersistenceFilter;import java.util.List;import java.util.stream.Stream;/** * SpringSecurity配置 * Created by macro on 2019/10/8. */@Slf4j@EnableWebSecurity@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true) // 开启方法注解权限配置public class SecurityConfig extends WebSecurityConfigurerAdapter {    /**     * 自定义权限不足处理器：返回状态码403     */    private final AccessDeniedHandlerImpl accessDeniedHandler;    /**     * 自定义未登录时：返回状态码401     */    private final AuthenticationEntryPointImpl authenticationEntryPoint;    /**     * 自定义登录失败处理器：返回状态码402     */    private final AuthenticationFailureHandlerImpl authenticationFailureHandler;    /**     * 自定义登录成功处理器：返回状态码200     */    private final AuthenticationSuccessHandlerImpl authenticationSuccessHandler;    /**     * 自定义注销成功处理器：返回状态码200     */    private final LogoutSuccessHandlerImpl logoutSuccessHandler;    /**     * 动态获取url权限配置     */    private final FilterInvocationSecurityMetadataSourceImpl filterInvocationSecurityMetadataSource;    /**     * 自定义权限判断管理器     */    private final AccessDecisionManagerImpl accessDecisionManager;    /**     * 认证时详细信息     */    private final AuthenticationDetailsSourceImpl authenticationDetailsSource;    /**     * 白名单持久层     */    private final BaseWhitelistRepository baseWhitelistRepository;    /**     * 用户服务层接口     */    private final BaseUserService baseUserService;    /**     * 应用服务层     */    private final BaseAppService baseAppService;    public SecurityConfig(AccessDeniedHandlerImpl accessDeniedHandler,                          AuthenticationEntryPointImpl authenticationEntryPoint,                          AuthenticationFailureHandlerImpl authenticationFailureHandler,                          AuthenticationSuccessHandlerImpl authenticationSuccessHandler,                          LogoutSuccessHandlerImpl logoutSuccessHandler,                          FilterInvocationSecurityMetadataSourceImpl filterInvocationSecurityMetadataSource,                          AccessDecisionManagerImpl accessDecisionManager,                          AuthenticationDetailsSourceImpl authenticationDetailsSource,                          BaseWhitelistRepository baseWhitelistRepository,                          BaseUserService baseUserService,                          BaseAppService baseAppService) {        this.accessDeniedHandler = accessDeniedHandler;        this.authenticationEntryPoint = authenticationEntryPoint;        this.authenticationFailureHandler = authenticationFailureHandler;        this.authenticationSuccessHandler = authenticationSuccessHandler;        this.logoutSuccessHandler = logoutSuccessHandler;        this.filterInvocationSecurityMetadataSource = filterInvocationSecurityMetadataSource;        this.accessDecisionManager = accessDecisionManager;        this.authenticationDetailsSource = authenticationDetailsSource;        this.baseWhitelistRepository = baseWhitelistRepository;        this.baseUserService = baseUserService;        this.baseAppService = baseAppService;    }    @Bean    public PasswordEncoder passwordEncoder() {        return new BCryptPasswordEncoder();    }    @Bean    @Override    public AuthenticationManager authenticationManagerBean() throws Exception {        return super.authenticationManagerBean();    }    /**     * 扫描系统的白名单     * @param web     * @throws Exception     */    @Override    public void configure(WebSecurity web) throws Exception {        super.configure(web);        WebSecurity.IgnoredRequestConfigurer ignoring = web.ignoring();        List<BaseWhitelistPO> whitelistPOS = baseWhitelistRepository.findAll();        ObjectMapper objectMapper = new ObjectMapper();        whitelistPOS.forEach(whitelist->{            try {                Stream.of(objectMapper.readValue(whitelist.getMethods(), String[].class)).forEach(method->{                    ignoring.antMatchers(HttpMethod.valueOf(method), whitelist.getPattern());                });            } catch (JsonProcessingException e) {                e.printStackTrace();            }        });    }    @Override    public void configure(HttpSecurity http) throws Exception {        // 未认证时：返回状态码401        http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint);        // 无权访问时：返回状态码403        http.exceptionHandling().accessDeniedHandler(accessDeniedHandler);        // url权限认证处理        http                // 支持EL表达式                // https://docs.spring.io/spring-security/site/docs/5.3.13.RELEASE/reference/html5/#el-access                .authorizeRequests()                //所有请求都需要认证                .anyRequest()                .authenticated()                // 使用自定义的 accessDecisionManager ⭐                /// .accessDecisionManager(accessDecisionManager)                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {                    @Override                    public <O extends FilterSecurityInterceptor> O postProcess(O o) {                        o.setSecurityMetadataSource(filterInvocationSecurityMetadataSource); //动态获取url权限配置                        o.setAccessDecisionManager(accessDecisionManager); //权限判断                        return o;                    }                }) ;        // 将session策略设置为无状态的,通过token进行权限认证,并关闭默认的SecurityContextPersistenceFilter        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)                .and()                // 关闭SecurityContextPersistenceFilter                .securityContext().disable();        // 开启自动配置的登录功能        http.formLogin() //开启登录                //自定义登录请求路径(post)                .loginProcessingUrl("/authentication/login")                //自定义登录用户名密码属性名,默认为username和password                .usernameParameter("username").passwordParameter("password")                //验证成功处理器(前后端分离)：返回状态码200                // 也可以使用下面这种方式定时认证成功和认证失败处理                // https://docs.spring.io/spring-security/site/docs/5.3.13.RELEASE/reference/html5/#servlet-events                .successHandler(authenticationSuccessHandler)                //验证失败处理器(前后端分离)：返回状态码402                .failureHandler(authenticationFailureHandler)                //身份验证详细信息源(登录验证中增加额外字段)                .authenticationDetailsSource(authenticationDetailsSource)                .permitAll();        // 开启自动配置的注销功能        http.logout() //用户注销, 清空session                //自定义注销请求路径                .logoutUrl("/authentication/logout")                //注销成功处理器(前后端分离)：返回状态码200                .logoutSuccessHandler(logoutSuccessHandler);        // 添加Jwt过滤器        http.addFilterAfter(new MySecurityContextPersistenceFilter(baseAppService, baseUserService), SecurityContextPersistenceFilter.class);        // 禁用csrf防御机制(跨域请求伪造)，这么做在测试和开发会比较方便。        http.csrf().disable();    }}