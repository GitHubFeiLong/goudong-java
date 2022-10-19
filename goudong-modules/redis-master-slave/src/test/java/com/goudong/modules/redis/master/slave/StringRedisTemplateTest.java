package com.goudong.modules.redis.master.slave;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * 类描述：
 *
 * @author cfl
 * @version 1.0
 * @date 2022/10/19 15:47
 */
@SpringBootTest
@ExtendWith(SpringExtension.class)
public class StringRedisTemplateTest {

    @SpyBean
    private StringRedisTemplate redisTemplate;

    @Test
    void testSet() {
        redisTemplate.opsForValue().set("age", "18");
    }
}
