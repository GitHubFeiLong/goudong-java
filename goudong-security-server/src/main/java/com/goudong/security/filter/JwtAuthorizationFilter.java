package com.goudong.security.filter;

import com.goudong.commons.dto.AuthorityRoleDTO;
import com.goudong.commons.dto.AuthorityUserDTO;
import com.goudong.commons.entity.AuthorityRoleDO;
import com.goudong.commons.utils.JwtTokenUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

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

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {

        String tokenHeader = request.getHeader(JwtTokenUtil.TOKEN_HEADER);
        // 如果请求头中没有Authorization信息则直接放行了
        if (tokenHeader == null || !tokenHeader.startsWith(JwtTokenUtil.TOKEN_BEARER_PREFIX) || !tokenHeader.startsWith(JwtTokenUtil.TOKEN_BASIC_PREFIX)) {
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

    private UsernamePasswordAuthenticationToken getBasicAuthenticationToken(String base64) throws UnsupportedEncodingException {

        // 解码
        String decode = new String(Base64.getDecoder().decode(base64), "UTF-8");
        // 为空 || 格式错误
        boolean isTrue = !StringUtils.hasText(decode) || !(decode.indexOf(":") > 0 && decode.length() < decode.length() - 1);
        if (isTrue) {
            return null;
        }
        // arr[0] 用户名； arr[1] 密码
        String[] arr = decode.split(":");

        // 调用登录接口，返回token信息。


        // 解析token为对象
        AuthorityUserDTO authorityUserDTO = AuthorityUserDTO.builder().build();

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
