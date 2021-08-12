package com.goudong.security.filter;

import com.goudong.commons.config.SpringConfigTool;
import com.goudong.commons.dto.AuthorityRoleDTO;
import com.goudong.commons.dto.AuthorityUserDTO;
import com.goudong.commons.entity.AuthorityRoleDO;
import com.goudong.commons.enumerate.ClientExceptionEnum;
import com.goudong.commons.enumerate.RedisKeyEnum;
import com.goudong.commons.exception.ClientException;
import com.goudong.commons.po.AuthorityUserPO;
import com.goudong.commons.pojo.IgnoreResourceAntMatchers;
import com.goudong.commons.utils.BeanUtil;
import com.goudong.commons.utils.JwtTokenUtil;
import com.goudong.commons.utils.RedisOperationsUtil;
import com.goudong.security.dao.SelfAuthorityUserDao;
import com.goudong.security.service.impl.SelfUserDetailsService;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.ContextLoader;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 类描述：
 * 过滤器，用户请求时，获取请求头的token，并将其解析后设置到Authentication认证信息中去
 * 使用JWT token进行验证用户
 * @Author msi
 * @Date 2021-04-03 18:14
 * @Version 1.0
 */
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    /**
     * 获取 Redis操作bean
     */
    private RedisOperationsUtil redisOperationsUtil = (RedisOperationsUtil)SpringConfigTool.getBean("redisOperationsUtil");

    /**
     * 用户dao
     */
    private SelfAuthorityUserDao selfAuthorityUserDao = (SelfAuthorityUserDao)SpringConfigTool.getBean("selfAuthorityUserDao");

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {

        List<IgnoreResourceAntMatchers> listValue = redisOperationsUtil.getListValue(RedisKeyEnum.OAUTH2_IGNORE_RESOURCE);
        // 如果当前请求是白名单，就放过
        if (!listValue.isEmpty()) {
            long count = listValue.stream()
                    .filter(f -> {
                        // 请求方式
                        if (HttpMethod.resolve(request.getMethod()).equals(f.getHttpMethod())) {
                            String requestPath = request.getRequestURI();
                            String patternPath = f.getUrl();

                            return new AntPathMatcher().match(patternPath, requestPath);
                        }
                        return false;
                    }).count();

            if (count > 0) {
                chain.doFilter(request, response);
                return;
            }
        }

        String tokenHeader = request.getHeader(JwtTokenUtil.TOKEN_HEADER);

        // 如果请求头中没有Authorization信息则直接放行了
        boolean boo = tokenHeader == null
                || !(tokenHeader.startsWith(JwtTokenUtil.TOKEN_BEARER_PREFIX) || tokenHeader.startsWith(JwtTokenUtil.TOKEN_BASIC_PREFIX));
        if (boo) {
            chain.doFilter(request, response);
            return;
        }
        // 如果请求头中有token，则进行解析，并且设置认证信息
        SecurityContextHolder.getContext().setAuthentication(getAuthentication(tokenHeader));
        super.doFilterInternal(request, response, chain);
    }

    /**
     * 这里从token中获取用户信息，并将用户名和角色信息，创建一个认证对象 Authentication
     * @param tokenHeader token
     * @return
     */
    private UsernamePasswordAuthenticationToken getAuthentication(String tokenHeader) throws UnsupportedEncodingException {
        // 判断请求头是Bearer 还是 Basic 开头
        if (tokenHeader.startsWith(JwtTokenUtil.TOKEN_BEARER_PREFIX)) {
            return getBearerAuthenticationToken(tokenHeader);
        } else if (tokenHeader.startsWith(JwtTokenUtil.TOKEN_BASIC_PREFIX)) {
            return getBasicAuthenticationToken(tokenHeader);
        } else {
            return null;
        }
    }

    /**
     * Basic方式 解码 base64字符串
     * @param token
     * @return
     * @throws UnsupportedEncodingException
     */
    private UsernamePasswordAuthenticationToken getBasicAuthenticationToken(String token) throws UnsupportedEncodingException {
        // 将加工后的转换原生token（没有Bearer ，Basic 字符串）
        String base64 = JwtTokenUtil.generateNativeToken(token);
        // 解码
        String decode = new String(Base64.getDecoder().decode(base64), "UTF-8");
        // base64解码后字符串是正确的格式
        boolean isSureFormat = decode != null && decode.split(":").length == 2;
        // 正确格式，查询用户和设置权限。
        if (isSureFormat) {
            // arr[0] 用户名； arr[1] 密码
            String[] arr = decode.split(":");

            // 查询用户
            AuthorityUserDTO authorityUserDTO = selfAuthorityUserDao.selectUserDetailByUsername(arr[0]);
            // 用户不存在
            if (authorityUserDTO == null) {
                ClientException.exception(ClientExceptionEnum.TOKEN_ERROR);
            }
            // 使用 BCrypt 加密的方式进行匹配
            boolean matches = new BCryptPasswordEncoder().matches(arr[1], authorityUserDTO.getPassword());
            // 密码不正确，抛出异常
            if (!matches) {
                throw new BadCredentialsException("账户名与密码不匹配，请重新输入");
            }

            // 放置权限
            Set<SimpleGrantedAuthority> authoritiesSet = new HashSet<>();
            List<AuthorityRoleDTO> authorityRoleDOS = authorityUserDTO.getAuthorityRoleDTOS();
            if (authorityRoleDOS != null && !authorityRoleDOS.isEmpty()) {
                authorityRoleDOS.parallelStream().forEach(f1->{
                    SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(f1.getRoleName());
                    authoritiesSet.add(simpleGrantedAuthority);
                });
            }
            String username = authorityUserDTO.getUsername();
            if (username != null){
                // 用户名 密码 角色
                return new UsernamePasswordAuthenticationToken(username, null, authoritiesSet);
            }
        }

        return null;
    }

    /**
     * Bearer 方式
     * @param token
     * @return
     */
    private UsernamePasswordAuthenticationToken getBearerAuthenticationToken(String token) {
        // 解析token为对象
        AuthorityUserDTO authorityUserDTO = JwtTokenUtil.resolveToken(token);

        // 放置权限
        Set<SimpleGrantedAuthority> authoritiesSet = new HashSet<>();
        List<AuthorityRoleDTO> authorityRoleDOS = authorityUserDTO.getAuthorityRoleDTOS();
        if (authorityRoleDOS != null && !authorityRoleDOS.isEmpty()) {
            authorityRoleDOS.parallelStream().forEach(f1->{
                SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(f1.getRoleName());
                authoritiesSet.add(simpleGrantedAuthority);
            });

        }
        String username = authorityUserDTO.getUsername();
        if (username != null){
            // 用户名 密码 角色
            return new UsernamePasswordAuthenticationToken(username, null, authoritiesSet);
        }
        return null;
    }
}
