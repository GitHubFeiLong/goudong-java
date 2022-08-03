package com.goudong;

import com.goudong.commons.framework.redis.RedisKeyTemplateProviderEnum;
import com.goudong.commons.framework.redis.RedisTool;
import com.goudong.user.GoudongUserServer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * 类描述：
 * 测试AOP是否能清除ThreadLocal
 * @author cfl
 * @version 1.0
 * @date 2022/8/3 23:33
 */
@SpringBootTest(classes = GoudongUserServer.class)
@RunWith(SpringRunner.class)
public class RedisTest {
    //~fields
    //==================================================================================================================
    @Resource
    private RedisTool redisTool;

    //~methods
    //==================================================================================================================
    @Test
    public void test1() {
        // EXPIRE_KEY("test:expire-key:string1", DataType.STRING, String.class, 10, TimeUnit.SECONDS),
        boolean value = redisTool.enableRandom()
                .set(RedisKeyTemplateProviderEnum.EXPIRE_KEY, "value");

    }
}
