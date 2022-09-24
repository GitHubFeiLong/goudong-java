package com.goudong.commons.framework.redis;

import com.goudong.user.GoudongUserServer;
import com.goudong.user.enumerate.RedisKeyProviderEnum;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashMap;

/**
 * 类描述：
 *
 * @author cfl
 * @version 1.0
 * @date 2022/9/24 20:43
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = GoudongUserServer.class)
class RedisToolTest {
    //~fields
    //==================================================================================================================

    //~methods
    //==================================================================================================================
    @SpyBean
    RedisTool redisTool;

    @Test
    void test1() {
        HashMap<Object, Object> map = new HashMap<>();
        map.put("key1", "v1");
        map.put("key2", "v2");
        map.put("key3", "v3");

        redisTool.set(RedisKeyProviderEnum.TEST, map);

        System.out.println(map);
    }
}
