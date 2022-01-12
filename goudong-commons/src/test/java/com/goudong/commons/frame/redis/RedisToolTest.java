package com.goudong.commons.frame.redis;
import java.time.LocalDateTime;

import cn.hutool.core.bean.BeanUtil;
import com.google.common.collect.Lists;
import com.goudong.commons.po.core.BasePO;
import lombok.*;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.DataType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@SpringBootTest
@RunWith(SpringRunner.class)
class RedisToolTest {

    //~ 常量
    //==================================================================================================================

    @Resource
    private RedisTool redisTool;

    @Test
    void deleteKey() {
        boolean boo1 = redisTool.deleteKey(RedisKeyTemplateProviderEnum.DELETE_KEY);
        Assert.isTrue(!boo1, "删除的key不存在时，应该返回false");
        String key = GenerateRedisKeyUtil.generateByClever(RedisKeyTemplateProviderEnum.DELETE_KEY);
        redisTool.opsForValue().set(key, 123456, 30, TimeUnit.SECONDS);
        boolean boo = redisTool.deleteKey(RedisKeyTemplateProviderEnum.DELETE_KEY);
        Boolean hasKey = redisTool.hasKey(key);
        Assert.isTrue(!hasKey, "删除key失败");
    }

    @Test
    void deleteKeys() {
        redisTool.opsForValue().set(RedisKeyTemplateProviderEnum.DELETE_KEYS_1.getKey(), 123, 30, TimeUnit.SECONDS);
        redisTool.opsForValue().set(RedisKeyTemplateProviderEnum.DELETE_KEYS_2.getKey(), 456, 30, TimeUnit.SECONDS);
        redisTool.deleteKeys(Lists.newArrayList(RedisKeyTemplateProviderEnum.DELETE_KEYS_1, RedisKeyTemplateProviderEnum.DELETE_KEYS_2), new Object[][]{{},{}});
        boolean fail = redisTool.hasKey(RedisKeyTemplateProviderEnum.DELETE_KEYS_1.getKey()) || redisTool.hasKey(RedisKeyTemplateProviderEnum.DELETE_KEYS_2.getKey());
        Assert.isTrue(!fail, "删除失败");
    }

    @Test
    void hasKey() {
        Boolean hasKey = redisTool.existKey(RedisKeyTemplateProviderEnum.HAS_KEY);
        Assert.isTrue(!hasKey, "此时还未设置该key，不应该存在key");
        redisTool.opsForValue().set(RedisKeyTemplateProviderEnum.HAS_KEY.getKey(), "123123", 30, TimeUnit.SECONDS);
        hasKey = redisTool.existKey(RedisKeyTemplateProviderEnum.HAS_KEY);
        Assert.isTrue(hasKey, "该key已经设置了，应该存在在Redis中");
    }

    @Test
    void refresh() throws InterruptedException {
        // 设置key
        redisTool.set(RedisKeyTemplateProviderEnum.REFRESH_KEY, 1);
        // 等待一段时间在刷新key
        Thread.sleep(5000L);
        // 查看key的失效时间是否正确
        redisTool.refresh(RedisKeyTemplateProviderEnum.REFRESH_KEY);
        Long expire = redisTool.getExpire(RedisKeyTemplateProviderEnum.REFRESH_KEY.getKey());
        Assert.isTrue(RedisKeyTemplateProviderEnum.REFRESH_KEY.getTime() - expire < 5, "过期时间未刷新");
    }

    @Test
    void expire() {
        // 设置key
        String key = RedisKeyTemplateProviderEnum.EXPIRE_KEY.getKey();
        redisTool.opsForValue().set(key, "123", 10, TimeUnit.SECONDS);
        redisTool.expireByCustom(RedisKeyTemplateProviderEnum.EXPIRE_KEY, 60, TimeUnit.SECONDS);
        Long expire = redisTool.getExpire(key);
        Assert.isTrue(expire > 10, "设置失效时间失败");
    }

    @Test
    void getExpire() {
        Object[] param = {1};
        String key = GenerateRedisKeyUtil.generateByClever(RedisKeyTemplateProviderEnum.GET_EXPIRE1.getKey(), param);
        BasePO basePO = new BasePO();
        basePO.setId(0L);
        basePO.setDeleted(false);
        basePO.setCreateUserId(10L);
        basePO.setUpdateUserId(20L);
        basePO.setUpdateTime(LocalDateTime.now());
        basePO.setCreateTime(LocalDateTime.now());

        redisTool.opsForHash().putAll(key, BeanUtil.beanToMap(basePO));
        redisTool.expire(key, 30, TimeUnit.SECONDS);

        long expire = redisTool.getExpire(RedisKeyTemplateProviderEnum.GET_EXPIRE1, param);

        Assert.isTrue(expire > 0, "获取的值错误：" + expire);

        long expire1 = redisTool.getExpire(RedisKeyTemplateProviderEnum.GET_EXPIRE2, param);
        Assert.isTrue(expire1 == -2, "该key不存在");

        String key2 = GenerateRedisKeyUtil.generateByClever(RedisKeyTemplateProviderEnum.GET_EXPIRE3, param);
        redisTool.opsForHash().putAll(key2, BeanUtil.beanToMap(basePO));
        long expire2 = redisTool.getExpire(RedisKeyTemplateProviderEnum.GET_EXPIRE3, param);

        Assert.isTrue(expire2 == -1, "该key的ttl值应该是-1, expire2=" + expire2);

    }

    @Test
    void set() {
        setString();
        setHash();
        setList();
        setSet();
    }

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

        Assert.isTrue(!boo3, "设置 null 到redis失败");
    }

    void setList() {

        // null设置到redis中
        boolean boo1 = redisTool.set(
                SimpleRedisKey.builder()
                        .key("goudong:goudong-user-server:test:list1")
                        .redisType(DataType.LIST)
                        .javaType(User.class).build(),
                null);
        Assert.isTrue(!boo1, "设置null到redis失败");

        // 空集合会设置到redis中
        ArrayList<User> list2 = Lists.newArrayList();
        boolean boo2 = redisTool.set(
                SimpleRedisKey.builder()
                        .key("goudong:goudong-user-server:test:list2")
                        .redisType(DataType.LIST)
                        .javaType(User.class).build(),
                list2);
        Assert.isTrue(!boo2, "设置"+list2+"到redis失败");

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

    void setSet() {
        // 设置Set
        boolean boo1 = redisTool.set(
                SimpleRedisKey.builder()
                        .key("goudong:goudong-user-server:test:set1")
                        .redisType(DataType.SET)
                        .javaType(String.class).build(),
                null);
        Assert.isTrue(boo1, "设置 null 到 Set失败");

        boolean boo2 = redisTool.set(
                SimpleRedisKey.builder()
                        .key("goudong:goudong-user-server:test:set2")
                        .redisType(DataType.SET)
                        .javaType(String.class).build(),
                new HashSet<>());
        Assert.isTrue(boo2, "设置 空集合 到 Set失败");

        HashSet<Object> hashSet = new HashSet<>();
        hashSet.addAll(Lists.newArrayList("a", "b", "c"));
        boolean boo3 = redisTool.set(
                SimpleRedisKey.builder()
                        .key("goudong:goudong-user-server:test:set3")
                        .redisType(DataType.SET)
                        .javaType(String.class).build(),
                hashSet);
        Assert.isTrue(boo3, String.format("设置 %s 到 Set失败", hashSet));
    }

    @Test
    void getString(){
        String str1 = redisTool.getString(SimpleRedisKey.builder()
                .key("goudong:goudong-user-server:test:string1")
                .redisType(DataType.STRING)
                .javaType(String.class).build());
        Assert.isTrue(Objects.equals("as", str1), "错误：" + str1);

        String str2 = redisTool.getString(SimpleRedisKey.builder()
                .key("goudong:goudong-user-server:test:string2")
                .redisType(DataType.STRING)
                .javaType(String.class).build());
        Assert.isTrue(Objects.equals(null, str2), "错误：" + str1);

        String str3 = redisTool.getString(SimpleRedisKey.builder()
                .key("goudong:goudong-user-server:test:string3")
                .redisType(DataType.STRING)
                .javaType(String.class).build());
        Assert.isTrue(Objects.equals("", str3), "错误：" + str1);

    }

    @Test
    void getHash() {
        // 设置HASH
        User user1 = new User();
        user1.setUsername("张三");
        user1.setPassword("面膜123123123");
        User user = redisTool.getHash(SimpleRedisKey.builder()
                        .key("goudong:goudong-user-server:test:hash1")
                        .redisType(DataType.HASH)
                        .javaType(User.class).build(),
                User.class);
        Assert.isTrue(user1.equals(user), "获取对象不相等");

        User user2 = new User();
        User user3 = redisTool.getHash(SimpleRedisKey.builder()
                .key("goudong:goudong-user-server:test:hash2")
                .redisType(DataType.HASH)
                .javaType(User.class).build(),User.class);
        Assert.isTrue(Objects.equals(user2, user3), "错误对象不相等");

        User user4 = redisTool.getHash(SimpleRedisKey.builder()
                        .key("goudong:goudong-user-server:test:hash3")
                        .redisType(DataType.HASH)
                        .javaType(User.class).build(),
                User.class);

        Assert.isTrue(user4 == null, "获取无效key错误");
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @EqualsAndHashCode
    static class User {
        private String username;
        private String password;
        private Long id;
        private Integer age;
    }
}