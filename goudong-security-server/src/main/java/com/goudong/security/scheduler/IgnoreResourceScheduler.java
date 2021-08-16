package com.goudong.security.scheduler;

import com.goudong.commons.enumerate.RedisKeyEnum;
import com.goudong.commons.po.BaseIgnoreResourcePO;
import com.goudong.commons.pojo.IgnoreResourceAntMatcher;
import com.goudong.commons.utils.IgnoreResourceAntMatcherUtil;
import com.goudong.commons.utils.RedisOperationsUtil;
import com.goudong.security.mapper.BaseIgnoreResourceMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

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
    private BaseIgnoreResourceMapper baseIgnoreResourceMapper;

    @Resource
    private RedisOperationsUtil redisOperationsUtil;

    //每隔2秒执行一次
    @Scheduled(fixedRate = 10000)
    public void ignoreResourceScheduler() {
        List<IgnoreResourceAntMatcher> ignoreResourceAntMatchers = getIgnoreResourceAntMatchers();
        // 保存redis中.
        redisOperationsUtil.setListValue(RedisKeyEnum.OAUTH2_IGNORE_RESOURCE, ignoreResourceAntMatchers);
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

}
