package com.goudong.redis.lua;

import com.google.common.collect.Lists;
import com.goudong.commons.framework.redis.RedisTool;
import com.goudong.user.GoudongUserServer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

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
public class LuaTest3 {

    //~fields
    //==================================================================================================================
    @Autowired
    RedisTool redisTemplate;

    //~methods
    //==================================================================================================================
    @Test
    public void test1() {
        // 执行 lua 脚本
        DefaultRedisScript redisScript = new DefaultRedisScript<>();
        // 指定 lua 脚本
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("lua/demo3.lua")));
        // // 指定返回类型
        redisScript.setResultType(List.class);
        // 参数一：redisScript，参数二：key列表，参数三：arg（可多个）
        Object result =  redisTemplate.execute(redisScript, Lists.newArrayList(1, 2));
        System.out.println(result);
    }

}