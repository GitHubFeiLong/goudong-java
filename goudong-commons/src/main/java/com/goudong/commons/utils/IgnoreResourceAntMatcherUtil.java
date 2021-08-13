package com.goudong.commons.utils;

import com.goudong.commons.constant.CommonConst;
import com.goudong.commons.dto.AuthorityMenuDTO;
import com.goudong.commons.enumerate.ClientExceptionEnum;
import com.goudong.commons.exception.ClientException;
import com.goudong.commons.po.AuthorityIgnoreResourcePO;
import com.goudong.commons.pojo.IgnoreResourceAntMatcher;
import org.springframework.http.HttpMethod;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * 类描述：
 * 请求能否访问request
 * @Author e-Feilong.Chen
 * @Date 2021/8/13 16:23
 */
public class IgnoreResourceAntMatcherUtil {

    /**
     * 断言本次请求能访问
     * @param url 请求url
     * @param method 请求方式
     * @param ignoreResources 忽略的白名单集合
     * @return
     */
    public static void assertAccess(String url, HttpMethod method, List<IgnoreResourceAntMatcher> ignoreResources) {
        boolean access = checkAccess(url, method, ignoreResources);
        // 不能访问 403
        if (!access) {
            throw ClientException.clientException(ClientExceptionEnum.NOT_AUTHORIZATION);
        }
    }

    /**
     * 检查本次请求能否正常访问
     * @param url 请求url
     * @param method 请求方式
     * @param ignoreResources 忽略的白名单集合
     * @return
     */
    public static boolean checkAccess(String url, HttpMethod method, List<IgnoreResourceAntMatcher> ignoreResources) {
        AssertUtil.notNull(url, "参数 url 不能为null");
        AssertUtil.notNull(method, "参数 method 不能为null");
        AssertUtil.notNull(ignoreResources, "参数 ignoreResources 不能为null");

        AntPathMatcher antPathMatcher = new AntPathMatcher();
        long count = ignoreResources.stream().filter(f -> antPathMatcher.match(f.getPattern(), url) && method.equals(f.getHttpMethod())).count();

        return count > 0 ? true : false;
    }

    /**
     * 将权限集合转换成 可以快速使用AntPathMatcher 过滤的集合,方便匹配
     * @param menuDTOList 用户权限集合
     * @return
     */
    public static List<IgnoreResourceAntMatcher> menu2AntMatchers(List<AuthorityMenuDTO> menuDTOList) {
        AssertUtil.notEmpty(menuDTOList, "参数 menuDTOList 不能为空");
        List<IgnoreResourceAntMatcher> result = new ArrayList<>();
        menuDTOList.stream().forEach(p->{
            List<HttpMethod> methods = new ArrayList<>();
            if (CommonConst.asterisk.equals(p.getMethod())) {
                // 默认 * 只添加四个请求方式
                methods.add(HttpMethod.POST);
                methods.add(HttpMethod.DELETE);
                methods.add(HttpMethod.PUT);
                methods.add(HttpMethod.GET);
            } else {
                String[] strs = p.getMethod().split(",");
                Stream.of(strs).forEach(p2->{
                    // 解析添加到集合
                    methods.add(HttpMethod.resolve(p2));
                });
            }
            methods.stream().forEach(p2->{
                result.add(new IgnoreResourceAntMatcher(p2, p.getUrl()));
            });
        });

        return result;
    }

    /**
     * 将白名单集合转换成 可以快速使用AntPathMatcher 过滤的集合,方便匹配
     * @param authorityIgnoreResourcePOS 白名单数据集合
     * @return
     */
    public static List<IgnoreResourceAntMatcher> ignoreResource2AntMatchers(List<AuthorityIgnoreResourcePO> authorityIgnoreResourcePOS) {
        AssertUtil.notEmpty(authorityIgnoreResourcePOS, "参数不能为空");
        // 返回结果
        List<IgnoreResourceAntMatcher> result = new ArrayList<>();

        authorityIgnoreResourcePOS.stream().forEach(p1->{

            List<HttpMethod> methods = new ArrayList<>();
            if (CommonConst.asterisk.equals(p1.getMethod())) {
                // 默认 * 只添加四个请求方式
                methods.add(HttpMethod.POST);
                methods.add(HttpMethod.DELETE);
                methods.add(HttpMethod.PUT);
                methods.add(HttpMethod.GET);
            } else {
                String[] strs = p1.getMethod().split(",");
                Stream.of(strs).forEach(p2->{
                    // 解析添加到集合
                    methods.add(HttpMethod.resolve(p2));
                });
            }
            methods.stream().forEach(p2->{
                result.add(new IgnoreResourceAntMatcher(p2, p1.getUrl()));
            });
        });

        return result;
    }

}
