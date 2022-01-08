package com.goudong.commons.frame.whitelist;

import com.goudong.commons.frame.redis.RedisOperations;
import com.goudong.commons.utils.core.LogUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import java.util.List;

/**
 * 系统启动就先加载热数据到redis中
 * @Author msi
 * @Date 2021-04-09 10:24
 * @Version 1.0
 */
@Slf4j
public class WhitelistInitialize implements ApplicationRunner {
    /**
     * 应用上下文路径
     */
    @Value("${server.servlet.context-path}")
    private String contextPath;

    /**
     * 应用名称
     */
    @Value("${spring.application.name}")
    private String applicationName;

    private final WhitelistDao whitelistDao;
    private final RedisOperations redisOperations;

    public WhitelistInitialize(WhitelistDao whitelistDao, RedisOperations redisOperations) {
        this.whitelistDao = whitelistDao;
        this.redisOperations = redisOperations;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        LogUtil.info(log, "开始处理白名单");
        List<BaseWhitelistPO> all = whitelistDao.findAll();

        // 存储到redis中。 TODO 使用feign，保存到指定库中

        LogUtil.info(log, "结束处理白名单");
    }

}
