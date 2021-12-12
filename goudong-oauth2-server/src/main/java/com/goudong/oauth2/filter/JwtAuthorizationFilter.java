package com.goudong.oauth2.filter;

import com.goudong.commons.config.SpringBeanConfig;
import com.goudong.commons.dto.AuthorityRoleDTO;
import com.goudong.commons.dto.AuthorityUserDTO;
import com.goudong.commons.enumerate.ClientExceptionEnum;
import com.goudong.commons.enumerate.RedisKeyEnum;
import com.goudong.commons.exception.ClientException;
import com.goudong.commons.pojo.IgnoreResourceAntMatcher;
import com.goudong.commons.utils.JwtTokenUtil;
import com.goudong.commons.core.redis.RedisOperationsUtil;
import com.goudong.commons.utils.StringUtil;
import com.goudong.oauth2.mapper.SelfAuthorityUserMapper;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.AntPathMatcher;

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
    private RedisOperationsUtil redisOperationsUtil = (RedisOperationsUtil) SpringBeanConfig.getBean("redisOperationsUtil");

    /**
     * 用户dao
     */
    private SelfAuthorityUserMapper selfAuthorityUserMapper = (SelfAuthorityUserMapper) SpringBeanConfig.getBean("selfAuthorityUserMapper");

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {

        List<IgnoreResourceAntMatcher> listValue = redisOperationsUtil.getListValue(RedisKeyEnum.OAUTH2_IGNORE_RESOURCE, IgnoreResourceAntMatcher.class);
        // 如果当前请求是白名单，就放过
        if (!listValue.isEmpty()) {
            long count = listValue.stream()
                    .filter(f -> {
                        // 请求方式
                        if (HttpMethod.resolve(request.getMethod()).equals(f.getHttpMethod())) {
                            String requestPath = request.getRequestURI();
                            String patternPath = f.getPattern();

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
        }
        if (tokenHeader.startsWith(JwtTokenUtil.TOKEN_BASIC_PREFIX)) {
            return getBasicAuthenticationToken(tokenHeader);
        }

        String message = StringUtil.format("请求头 {} 的值格式错误，需要以 {} 或 {} 开头。", JwtTokenUtil.TOKEN_HEADER, JwtTokenUtil.TOKEN_BEARER_PREFIX, JwtTokenUtil.TOKEN_BASIC_PREFIX);
        throw ClientException.clientException(ClientExceptionEnum.UNAUTHORIZED, message);
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
            AuthorityUserDTO authorityUserDTO = selfAuthorityUserMapper.selectUserDetailByUsername(arr[0]);
            // 用户不存在
            if (authorityUserDTO == null) {
                throw ClientException.clientException(ClientExceptionEnum.NOT_FOUND, "请求携带用户不存在");
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

        String message = StringUtil.format("请求头 {} 的值不是正确的 base64编码类型", JwtTokenUtil.TOKEN_HEADER);
        throw ClientException.clientException(ClientExceptionEnum.UNAUTHORIZED, message);
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
