package com.zuopei.oauth2.config;

import com.zuopei.commons.enumerate.RedisKeyExpirationEnum;
import com.zuopei.commons.utils.RedisValueUtil;
import com.zuopei.oauth2.dao.AuthorityMenuDao;
import com.zuopei.oauth2.entity.AuthorityMenuDO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

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
    private AuthorityMenuDao authorityMenuDao;
    @Resource
    private RedisValueUtil redisValueUtil;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("启动服务了");
        List<AuthorityMenuDO> authorityMenuDOS = authorityMenuDao.listMenuAndRole();
        log.info("authorityMenuDOS:{}", authorityMenuDOS);
        redisValueUtil.setValue(RedisKeyExpirationEnum.OAUTH2_ROLE_VISIT_URL, authorityMenuDOS);
    }
}
