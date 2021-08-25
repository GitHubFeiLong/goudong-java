package com.goudong.commons.scheduler;

import com.goudong.commons.enumerate.RedisKeyEnum;
import com.goudong.commons.openfeign.Oauth2Service;
import com.goudong.commons.pojo.ResourceAntMatcher;
import com.goudong.commons.utils.BeanUtil;
import com.goudong.commons.utils.RedisOperationsUtil;
import com.goudong.commons.utils.ResourceUtil;
import com.goudong.commons.vo.BaseIgnoreResourceVO;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 类描述：
 * 扫描有@IgnoreResource的控制层方法，将接口放入到白名单
 * @Author msi
 * @Date 2021-08-14 8:49
 * @Version 1.0
 */
@Slf4j
@Component
public class ScanIgnoreResourceScheduler {

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Value("${spring.application.name}")
    private String applicationName;


    private Oauth2Service oauth2Service;

    @Autowired
    public void setOauth2Service(Oauth2Service oauth2Service) {
        this.oauth2Service = oauth2Service;
    }

    private RedisOperationsUtil redisOperationsUtil;

    @Autowired
    public void setRedisOperationsUtil(RedisOperationsUtil redisOperationsUtil) {
        this.redisOperationsUtil = redisOperationsUtil;
    }

    /**
     * 定时器，扫描白名单(配置热加载)
     * 生产环境将值 scheduler.scanIgnoreResource.cron 设置成 "-"，关闭该定时器。
     */
    @SneakyThrows
    @Scheduled(cron = "${goudong.scheduler.scanIgnoreResource.cron}")
    public void scheduler() {
        long var0 = System.currentTimeMillis();
        List<ResourceAntMatcher> resourceAntMatchers = ResourceUtil.scanIgnore(contextPath);

        // 有数据，插入数据库
        List<BaseIgnoreResourceVO> baseIgnoreResourceVOS = BeanUtil.copyList(resourceAntMatchers, BaseIgnoreResourceVO.class);
        oauth2Service.addIgnoreResources(baseIgnoreResourceVOS);
        long var1 = System.currentTimeMillis();
        log.info("ScanIgnoreResourceScheduler 定时器执行花费时长: {} ms", var1 - var0);
    }


}
