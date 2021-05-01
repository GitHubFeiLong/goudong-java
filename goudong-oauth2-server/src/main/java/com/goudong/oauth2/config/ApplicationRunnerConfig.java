package com.goudong.oauth2.config;

import com.goudong.commons.utils.RedisValueUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 系统启动就先加载热数据到redis中
 * @Author msi
 * @Date 2021-04-09 10:24
 * @Version 1.0
 */
@Slf4j
@Component
public class ApplicationRunnerConfig implements ApplicationRunner {
    @Resource
    private RedisValueUtil redisValueUtil;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("启动服务了");
    }
}
