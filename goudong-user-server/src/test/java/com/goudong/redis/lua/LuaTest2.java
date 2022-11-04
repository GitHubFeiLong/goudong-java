package com.goudong.redis.lua;

import com.google.common.collect.Lists;
import com.goudong.boot.redis.core.RedisTool;
import com.goudong.user.GoudongUserServer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 类描述：
 * 测试RedisTemplate使用lua脚本
 * @author msi
 * @version 1.0
 * @date 2022/6/2 16:26
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("dev")
@SpringBootTest(classes = GoudongUserServer.class)
public class LuaTest2 {

    //~fields
    //==================================================================================================================
    public static final String SCRIPT = "redis.call('set', KEYS[1], ARGV[1]);redis.call('set', KEYS[2], ARGV[2]); return 1";

    @Autowired
    RedisTool redisTemplate;

    //~methods
    //==================================================================================================================
    @Test
    public void test1() {
        // 指定 lua 脚本，并且指定返回值类型
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>(SCRIPT, Long.class);
        // 参数一：redisScript，参数二：key列表，参数三：arg（可多个）
        Object result = redisTemplate.execute(redisScript, Lists.newArrayList("name", "age"), "zhangsan", "lisi");
        System.out.println(result);
    }

}
