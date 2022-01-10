package com.goudong.user.controller.open;
import com.google.common.collect.Lists;
import com.goudong.commons.frame.redis.RedisTool;
import com.goudong.user.core.UserServerRedisKey;
import com.goudong.user.po.BaseUserPO;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@SpringBootTest
@RunWith(SpringRunner.class)
class OpenUerControllerTest {

    @Resource
    private RedisTool redisTool;
    @Test
    void demo() {
        redisTool.set(UserServerRedisKey.STRING, "as");
        BaseUserPO user = new BaseUserPO();
        user.setUsername("张三");
        user.setPassword("面膜123123123");

        redisTool.set(UserServerRedisKey.HASH, user);
        redisTool.set(UserServerRedisKey.LIST, Lists.newArrayList("a", "b", "c"));
    }
}