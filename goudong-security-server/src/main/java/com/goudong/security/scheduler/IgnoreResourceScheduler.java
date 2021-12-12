package com.goudong.security.scheduler;

import com.goudong.commons.enumerate.RedisKeyEnum;
import com.goudong.commons.po.BaseIgnoreResourcePO;
import com.goudong.commons.pojo.IgnoreResourceAntMatcher;
import com.goudong.commons.utils.GenerateRedisKeyUtil;
import com.goudong.commons.utils.IgnoreResourceAntMatcherUtil;
import com.goudong.commons.utils.redis.RedisOperationsUtil;
import com.goudong.security.mapper.BaseIgnoreResourceMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

/**
 * 类描述：
 * 定时的扫描忽略资源，将数据库的白名单 添加到 redis中。
 * @Author msi
 * @Date 2021-08-10 21:04
 * @Version 1.0
 */
@Slf4j
@Component
public class IgnoreResourceScheduler {

    @Resource
    private BaseIgnoreResourceMapper baseIgnoreResourceMapper;

    @Resource
    private RedisOperationsUtil redisOperationsUtil;

    @Resource
    private RedissonClient redissonClient;

    /**
     * 每隔2秒执行一次
     */
    @Scheduled(cron = "${goudong.scheduler.scanIgnoreResource.cron}")
    public void ignoreResourceScheduler() {
        List<IgnoreResourceAntMatcher> ignoreResourceAntMatchers = getIgnoreResourceAntMatchers();
        // 分布式锁
        String lockKey = GenerateRedisKeyUtil.generateByClever(RedisKeyEnum.OAUTH2_REDISSON_IGNORE_RESOURCE.getKey());

        RLock lock = redissonClient.getLock(lockKey);
        lock.lock();
        try {
            // 先判断是否需要存redis
            List<IgnoreResourceAntMatcher> redisValue = redisOperationsUtil.getListValue(RedisKeyEnum.OAUTH2_IGNORE_RESOURCE, IgnoreResourceAntMatcher.class);
            boolean containsAll = ignoreResourceAntMatchers.containsAll(redisValue);
            operationRedisValue(ignoreResourceAntMatchers, redisValue);
        } finally {
            lock.unlock();
        }
    }

    /**
     * 获取后台设置的忽略资源
     * @return
     */
    public List<IgnoreResourceAntMatcher> getIgnoreResourceAntMatchers() {
        // 查询表 返回白名单集合
        List<BaseIgnoreResourcePO> authorityIgnoreResourcePOS = baseIgnoreResourceMapper.selectList(null);
        // 将白名单集合转换成 使用antPathMatch时的友好对象
        return IgnoreResourceAntMatcherUtil.ignoreResource2AntMatchers(authorityIgnoreResourcePOS);
    }

    /**
     * 操作redis中的数据，多退少补策略
     * @param ignoreResourceAntMatchers
     * @param redisValue
     */
    private void operationRedisValue(List<IgnoreResourceAntMatcher> ignoreResourceAntMatchers, List<IgnoreResourceAntMatcher> redisValue) {
        // 差异，多退少补
        // 1. redis多的
        Collection<IgnoreResourceAntMatcher> redisCollection = CollectionUtils.subtract(redisValue, ignoreResourceAntMatchers);
        // 2. 数据库多的
        Collection<IgnoreResourceAntMatcher> dbCollection = CollectionUtils.subtract(ignoreResourceAntMatchers, redisValue);

        String redisKey = GenerateRedisKeyUtil.generateByClever(RedisKeyEnum.OAUTH2_IGNORE_RESOURCE.getKey());
        // 删除多余的值
        if (CollectionUtils.isNotEmpty(redisCollection)) {
            redisCollection.forEach(p->{
                redisOperationsUtil.opsForList().remove(redisKey, 0, p);
            });
        }
        // 添加新的值
        if (CollectionUtils.isNotEmpty(dbCollection)) {
            redisOperationsUtil.opsForList().leftPushAll(redisKey, dbCollection);
        }
    }



}
