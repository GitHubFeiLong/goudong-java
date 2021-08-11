package com.goudong.security.scheduler;

import com.goudong.commons.enumerate.RedisKeyEnum;
import com.goudong.commons.po.AuthorityIgnoreResourcePO;
import com.goudong.commons.pojo.IgnoreResourceAntMatchers;
import com.goudong.commons.utils.RedisOperationsUtil;
import com.goudong.security.dao.SelfAuthorityIgnoreResourceDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * 类描述：
 * 定时的扫描忽略资源，将新添加的加入到拦截器白名单中去
 * @Author msi
 * @Date 2021-08-10 21:04
 * @Version 1.0
 */
@Slf4j
@Component
public class IgnoreResourceScheduler {

    @Resource
    private SelfAuthorityIgnoreResourceDao selfAuthorityIgnoreResourceDao;

    @Resource
    private RedisOperationsUtil redisOperationsUtil;

    //每隔2秒执行一次
    @Scheduled(fixedRate = 20000)
    public void testTasks() {
        List<IgnoreResourceAntMatchers> ignoreResourceAntMatchers = getIgnoreResourceAntMatchers();
        log.info("定时器：{}", ignoreResourceAntMatchers);
        redisOperationsUtil.setListValue(RedisKeyEnum.OAUTH2_IGNORE_RESOURCE, ignoreResourceAntMatchers);
    }
    /**
     * 获取后台设置的忽略资源
     * @return
     */
    public List<IgnoreResourceAntMatchers> getIgnoreResourceAntMatchers() {
        // * 通配符
        final String asterisk = "*";
        // 返回结果
        List<IgnoreResourceAntMatchers> antMatchersList = new ArrayList<>();

        List<AuthorityIgnoreResourcePO> authorityIgnoreResourcePOS = selfAuthorityIgnoreResourceDao.selectAll();
        if (!authorityIgnoreResourcePOS.isEmpty()) {
            authorityIgnoreResourcePOS.stream()
                    .forEach(p1->{
                        String[] methods;
                        if (asterisk.equals(p1.getMethod())) {
                            methods = new String[]{"GET", "POST", "PUT", "DELETE"};
                        } else {
                            // 数据库设置的该路径的请求方式用逗号进行分割
                            methods = p1.getMethod().split(",");
                            Assert.isTrue(methods != null && methods.length > 0, "请求方式为空");
                        }
                        Stream.of(methods).forEach(p2->{
                            // 放入集合中。
                            antMatchersList.add(new IgnoreResourceAntMatchers(HttpMethod.resolve(p2.toUpperCase()), p1.getUrl()));
                        });
                    });
        }

        return antMatchersList;
    }

}
