package com.goudong.security.config;

import com.goudong.security.dao.SelfAuthorityUserDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 类描述：
 * 动态获取url权限配置
 * @Author msi
 * @Date 2021-04-03 17:58
 * @Version 1.0
 */
@Slf4j
@Component
public class SelfFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    @Resource
    private SelfAuthorityUserDao selfAuthorityUserDao;

    @Override
    public Collection<ConfigAttribute> getAttributes(Object o) throws IllegalArgumentException {
        // 获取请求地址
        String requestUrI = ((FilterInvocation) o).getHttpRequest().getRequestURI();

        // 获取上下文路径
        String contextPath = ((FilterInvocation) o).getHttpRequest().getContextPath();
        // mvc 错误接口
        if (requestUrI.equals(contextPath + "/error")) {
            return null;
        }

        Set<ConfigAttribute> set = new HashSet<>();

        // 获取请求的方法
        String requestMethod = ((FilterInvocation) o).getHttpRequest().getMethod();
        log.info("requestUrI >> {}，requestMethod >> {}", requestUrI, requestMethod);
        // 查询 请求方式的url 需要哪些权限
        List<String> roleNames = selfAuthorityUserDao.selectRoleNameByMenu(requestUrI, requestMethod);
        // 没有角色匹配
        if (roleNames.isEmpty()) {
            return SecurityConfig.createList("ROLE_ANONYMOUS");
        }

        // 将能访问地址的角色添加到集合
        roleNames.forEach(roleName -> {
            SecurityConfig securityConfig = new SecurityConfig(roleName);
            set.add(securityConfig);
        });

        return set;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return FilterInvocation.class.isAssignableFrom(aClass);
    }
}
