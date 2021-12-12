package com.goudong.commons.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 类描述：
 * swagger 允许访问
 * @Author e-Feilong.Chen
 * @Date 2021/8/12 9:46
 */
@Configuration
@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer {

    // @Resource
    // private UserInterceptor userInterceptor;

    // /**
    //  * 注册拦截器
    //  * @param registry
    //  */
    // @Override
    // public void addInterceptors(InterceptorRegistry registry) {
    //     WebMvcConfigurer.super.addInterceptors(registry);
    //     // api前缀的请求都进行拦截处理
    //     // 注意： 这里拦截不到在配置文件添加前缀
    //     registry.addInterceptor(userInterceptor).addPathPatterns("/**");
    // }

    /**
     * mvc 添加静态资源
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 增强swagger 的静态资源放行
        registry.addResourceHandler("/doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    /**
     * 不根据url后缀匹配返回指定的数据类型
     * @param configurer
     */
    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.favorPathExtension(false);
    }

    /**
     * 跨域
     * @return
     */
    @Bean
    public CorsFilter corsFilter() {
        //1.添加CORS配置信息
        CorsConfiguration config = new CorsConfiguration();
        //1) 允许的域,不要写*，否则cookie就无法使用了
        config.addAllowedOrigin("*");
        //3) 允许的请求方式
        config.addAllowedMethod(HttpMethod.OPTIONS.name());
        config.addAllowedMethod(HttpMethod.POST.name());
        config.addAllowedMethod(HttpMethod.PUT.name());
        config.addAllowedMethod(HttpMethod.GET.name());
        config.addAllowedMethod(HttpMethod.PATCH.name());
        config.addAllowedMethod(HttpMethod.DELETE.name());
        // 4）允许的头信息
        config.addAllowedHeader("*");

        //初始化Cors配置源
        UrlBasedCorsConfigurationSource configSource = new UrlBasedCorsConfigurationSource();
        //2.添加映射路径，我们拦截一切请求
        configSource.registerCorsConfiguration("/**", config);

        //3.返回CorsFilter实例.参数:cors配置源
        return new CorsFilter(configSource);
    }
}
