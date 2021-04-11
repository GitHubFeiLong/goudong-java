package com.goudong.oauth2.service.impl;

import com.goudong.commons.enumerate.ClientExceptionEnum;
import com.goudong.commons.enumerate.RedisKeyExpirationEnum;
import com.goudong.commons.exception.BasicException;
import com.goudong.commons.pojo.Result;
import com.goudong.commons.utils.RedisValueUtil;
import com.goudong.oauth2.entity.AuthorityMenuDO;
import com.goudong.oauth2.entity.AuthorityRoleDO;
import com.goudong.oauth2.entity.AuthorityUserDO;
import com.goudong.oauth2.service.AuthenticationService;
import com.goudong.oauth2.util.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Iterator;
import java.util.List;

/**
 * @Author msi
 * @Date 2021-04-09 9:07
 * @Version 1.0
 */
@Slf4j
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    @Resource
    private RedisValueUtil redisValueUtil;

    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    /**
     * 鉴权
     *
     * @param request
     * @param httpPath
     * @param httpMethod
     * @return
     */
    @Override
    public Result authentication(HttpServletRequest request, String httpPath, String httpMethod) {
        // 判断 url + httpMethod 是否需要权限
        List<AuthorityMenuDO> authorityMenuDOS = redisValueUtil.getListValue(RedisKeyExpirationEnum.OAUTH2_ROLE_VISIT_URL, AuthorityMenuDO.class);
        // 为空不需要验证
        if (authorityMenuDOS.isEmpty()) {
            return Result.ofSuccess();
        }

        // 有值，检查请求头
        String tokenHeader = request.getHeader(JwtTokenUtil.TOKEN_HEADER);
        // 如果请求头中没有Authorization信息，需要登录
        if (tokenHeader == null || !tokenHeader.startsWith(JwtTokenUtil.TOKEN_PREFIX)) {
            return Result.ofFail(new BasicException.ClientException(ClientExceptionEnum.NOT_AUTHENTICATION));
        }

        // 正常情况
        AuthorityUserDO authorityUserDO = JwtTokenUtil.resolveToken(tokenHeader);

        // 获取登录状态
        AuthorityUserDO redisAuthorityUserDO = redisValueUtil.getValue(RedisKeyExpirationEnum.OAUTH2_USER_ROLE, AuthorityUserDO.class, authorityUserDO.getUuid());
        // redis中不存在key，表示登录过期
        if (redisAuthorityUserDO == null) {
//            return Result.ofFail("登录过期");
            return Result.ofFail(new BasicException.ClientException(ClientExceptionEnum.AUTHENTICATION_EXPIRES));
        }
        // 角色
        List<AuthorityRoleDO> authorityRoleDOS = redisAuthorityUserDO.getAuthorityRoleDOS();
        // 角色为空就表示普通用户，也不能访问
        if (authorityRoleDOS == null || authorityRoleDOS.isEmpty()) {
            // 无权访问
            return Result.ofFail(new BasicException.ClientException(ClientExceptionEnum.NOT_AUTHORIZATION));
        }

        // 比较访问用户的角色是否包含该菜单需要的角色
        Iterator<AuthorityMenuDO> iterator = authorityMenuDOS.iterator();
        while(iterator.hasNext()){
            AuthorityMenuDO menuDO = iterator.next();
            // 匹配url && 匹配角色
            boolean flag = matchUrl(menuDO.getUrl(), menuDO.getMethod(), httpPath, httpMethod) && matchRole(menuDO.getAuthorityRoleDOS(), authorityRoleDOS);
            if (flag) {
                return Result.ofSuccess();
            }
        }

        // 无权访问
        return Result.ofFail(new BasicException.ClientException(ClientExceptionEnum.NOT_AUTHORIZATION));
    }


    /**
     * 判断本次请求的url是否可以访问
     * @param menuUrl 菜单设置的路径
     * @param menuMethod 菜单路径设置的请求方法（GET,POST,DELETE,PUT,*）
     * @param httpPath 本次请求的url
     * @param httpMethod 本次请求的method
     * @return
     */
    private boolean matchUrl (String menuUrl, String menuMethod, String httpPath, String httpMethod) {
        boolean urlMatch = antPathMatcher.match(menuUrl, httpPath);
        if (urlMatch) {
            // * 表示所有请求方式都ok
            if (menuMethod.indexOf("*") != -1) {
                return true;
            }

            // 小写
            httpMethod = httpMethod.toLowerCase();

            // 存在就匹配
            if (menuMethod.toLowerCase().indexOf(httpMethod) != -1) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断角色是否匹配
     * @param menuRoleDOS 菜单需要的角色
     * @param userRoleDOS 用户拥有的角色
     * @return
     */
    private boolean matchRole (List<AuthorityRoleDO> menuRoleDOS, List<AuthorityRoleDO> userRoleDOS) {
        if (menuRoleDOS == null ||menuRoleDOS.isEmpty()) {
            log.warn("菜单需要的角色为空");
            return true;
        }

        // 外小内多
        for (AuthorityRoleDO m : userRoleDOS) {
            for (AuthorityRoleDO m2 : menuRoleDOS) {
                if (m.getRoleName().equalsIgnoreCase(m2.getRoleName())) {
                    return true;
                }
            }
        }

        return false;
    }


}
