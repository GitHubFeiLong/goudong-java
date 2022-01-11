package com.goudong.commons.frame.redis;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.DataType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;

@SpringBootTest
@RunWith(SpringRunner.class)
class RedisToolTest {

    @Resource
    private RedisTool redisTool;

    /**
     * 设置String
     */
    @Test
    void setString() {
        boolean boo1 = redisTool.set(SimpleRedisKey.builder()
                        .key("goudong:goudong-user-server:test:string1")
                        .redisType(DataType.STRING)
                        .javaType(String.class).build(),
                "as");
        Assert.isTrue(boo1, "\"as\"设置到redis中失败");

        boolean boo2 = redisTool.set(SimpleRedisKey.builder()
                        .key("goudong:goudong-user-server:test:string2")
                        .redisType(DataType.STRING)
                        .javaType(String.class).build(),
                null);

        Assert.isTrue(boo2, "设置 null 到redis失败");
        boolean boo3 = redisTool.set(SimpleRedisKey.builder()
                        .key("goudong:goudong-user-server:test:string3")
                        .redisType(DataType.STRING)
                        .javaType(String.class).build(),
                "");

        Assert.isTrue(boo3, "设置\"\"到redis失败");
    }

    /**
     * 设置Hash
     */
    @Test
    void setHash() {
        // 设置HASH
        User user1 = new User();
        user1.setUsername("张三");
        user1.setPassword("面膜123123123");
        boolean boo1 = redisTool.set(SimpleRedisKey.builder()
                        .key("goudong:goudong-user-server:test:hash1")
                        .redisType(DataType.HASH)
                        .javaType(User.class).build(),
                user1);
        Assert.isTrue(boo1, "设置"+user1+"到redis失败");

        User user2 = new User();
        boolean boo2 = redisTool.set(SimpleRedisKey.builder()
                        .key("goudong:goudong-user-server:test:hash2")
                        .redisType(DataType.HASH)
                        .javaType(User.class).build(),
                user2);
        Assert.isTrue(boo2, "设置"+user2+"到redis失败");
        boolean boo3 = redisTool.set(SimpleRedisKey.builder()
                        .key("goudong:goudong-user-server:test:hash3")
                        .redisType(DataType.HASH)
                        .javaType(User.class).build(),
                null);

        Assert.isTrue(boo3, "设置 null 到redis失败");
    }

    @Test
    void setList() {

        // null设置到redis中
        boolean boo1 = redisTool.set(
                SimpleRedisKey.builder()
                        .key("goudong:goudong-user-server:test:list1")
                        .redisType(DataType.LIST)
                        .javaType(User.class).build(),
                null);
        Assert.isTrue(boo1, "设置null到redis失败");

        // 空集合会设置到redis中
        ArrayList<User> list2 = Lists.newArrayList();
        boolean boo2 = redisTool.set(
                SimpleRedisKey.builder()
                        .key("goudong:goudong-user-server:test:list2")
                        .redisType(DataType.LIST)
                        .javaType(User.class).build(),
                list2);
        Assert.isTrue(boo2, "设置"+list2+"到redis失败");

        // 设置集合
        ArrayList<User> user3 = Lists.newArrayList(User.builder().username("user1").password("123456").build(),
                User.builder().username("user2").password("123456").build(),
                User.builder().username("user3").age(18).build());
        boolean boo3 = redisTool.set(
                SimpleRedisKey.builder()
                        .key("goudong:goudong-user-server:test:list3")
                        .redisType(DataType.LIST)
                        .javaType(User.class).build(),
                user3);
        Assert.isTrue(boo3, "设置"+user3+"到redis失败");
    }

    @Test
    void setRedisData() {
        // 设置Set
        redisTool.set(
                SimpleRedisKey.builder()
                        .key("goudong:goudong-user-server:set1")
                        .redisType(DataType.SET)
                        .javaType(String.class).build(),
                null);
        redisTool.set(
                SimpleRedisKey.builder()
                        .key("goudong:goudong-user-server:set2")
                        .redisType(DataType.SET)
                        .javaType(String.class).build(),
                new HashSet<>());

        HashSet<Object> hashSet = new HashSet<>();
        hashSet.addAll(Lists.newArrayList("a", "b", "c"));
        redisTool.set(
                SimpleRedisKey.builder()
                        .key("goudong:goudong-user-server:set3")
                        .redisType(DataType.SET)
                        .javaType(String.class).build(),
                hashSet);

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    static class User {
        private String username;
        private String password;
        private Long id;
        private int age;
    }
}