package com.goudong.boot.redis.core;

import cn.hutool.core.bean.BeanUtil;
import com.goudong.core.util.CollectionUtil;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

@SpringBootTest
// @RunWith(SpringRunner.class)
@ExtendWith(SpringExtension.class)
@Slf4j
class RedisToolTest {

    //~ 常量
    //==================================================================================================================
    public static final SimpleRedisKey DELETE_KEY = new SimpleRedisKey("test:delete-key:string", DataType.STRING, String.class);
    public static final SimpleRedisKey DELETE_KEYS_1 =
            new SimpleRedisKey("test:delete-keys:string1", DataType.STRING, String.class);
    public static final SimpleRedisKey DELETE_KEYS_2 =
            new SimpleRedisKey("test:delete-keys:string2", DataType.STRING, String.class);
    public static final SimpleRedisKey HAS_KEY =
            new SimpleRedisKey("test:has-key:string1", DataType.STRING, String.class);
    public static final SimpleRedisKey REFRESH_KEY =
            new SimpleRedisKey("test:refresh-key:string2", DataType.STRING, String.class, 10, TimeUnit.SECONDS);
    public static final SimpleRedisKey EXPIRE_KEY =
            new SimpleRedisKey("test:expire-key:string1", DataType.STRING, String.class, 10, TimeUnit.SECONDS);
    public static final SimpleRedisKey GET_EXPIRE1 =
            new SimpleRedisKey("test:get-expire:string1:${id}", DataType.HASH, String.class, 10, TimeUnit.SECONDS);
    public static final SimpleRedisKey GET_EXPIRE2 =
            new SimpleRedisKey("test:get-expire:string2:${id}", DataType.HASH, String.class, 10, TimeUnit.SECONDS);
    public static final SimpleRedisKey GET_EXPIRE3 =
            new SimpleRedisKey("test:get-expire:string3:${id}", DataType.HASH, String.class, 10, TimeUnit.SECONDS);
    @Resource
    private RedisTool redisTool;


    @Test
    void deleteKey() {
        boolean boo1 = redisTool.deleteKey(DELETE_KEY);
        Assert.isTrue(!boo1, "删除的key不存在时，应该返回false");
        String key = DELETE_KEY.getFullKey();
        redisTool.opsForValue().set(key, 123456, 30, TimeUnit.SECONDS);
        boolean boo = redisTool.deleteKey(DELETE_KEY);
        Boolean hasKey = redisTool.hasKey(key);
        Assert.isTrue(!hasKey, "删除key失败");
    }

    @Test
    void deleteKeys() {
        redisTool.opsForValue().set(DELETE_KEYS_1.getKey(), 123, 30, TimeUnit.SECONDS);
        redisTool.opsForValue().set(DELETE_KEYS_2.getKey(), 456, 30, TimeUnit.SECONDS);
        redisTool.deleteKeys(Lists.newArrayList(DELETE_KEYS_1, DELETE_KEYS_2), new Object[][]{{},{}});
        boolean fail = redisTool.hasKey(DELETE_KEYS_1.getKey()) || redisTool.hasKey(DELETE_KEYS_2.getKey());
        Assert.isTrue(!fail, "删除失败");
    }

    @Test
    void hasKey() {
        Boolean hasKey = redisTool.existKey(HAS_KEY);
        Assert.isTrue(!hasKey, "此时还未设置该key，不应该存在key");
        redisTool.opsForValue().set(HAS_KEY.getKey(), "123123", 30, TimeUnit.SECONDS);
        hasKey = redisTool.existKey(HAS_KEY);
        Assert.isTrue(hasKey, "该key已经设置了，应该存在在Redis中");
    }

    @Test
    void refresh() throws InterruptedException {
        // 设置key
        redisTool.set(REFRESH_KEY, 1);
        // 等待一段时间在刷新key
        Thread.sleep(5000L);
        // 查看key的失效时间是否正确
        redisTool.refresh(REFRESH_KEY);
        Long expire = redisTool.getExpire(REFRESH_KEY.getKey());
        Assert.isTrue(REFRESH_KEY.getTime() - expire < 5, "过期时间未刷新");
    }

    @Test
    void expire() {
        // 设置key
        String key = EXPIRE_KEY.getKey();
        redisTool.opsForValue().set(key, "123", 10, TimeUnit.SECONDS);
        redisTool.expireByCustom(EXPIRE_KEY, 60, TimeUnit.SECONDS);
        Long expire = redisTool.getExpire(key);
        Assert.isTrue(expire > 10, "设置失效时间失败");
    }

    @Test
    void getExpire() {
        Object[] param = {1};
        String key = GET_EXPIRE1.getFullKey(param);
        BasePO basePO = new BasePO();
        basePO.setId(0L);
        basePO.setDeleted(false);
        basePO.setCreateUserId(10L);
        basePO.setUpdateUserId(20L);
        basePO.setUpdateTime(new Date());
        basePO.setCreateTime(new Date());

        redisTool.opsForHash().putAll(key, BeanUtil.beanToMap(basePO));
        redisTool.expire(key, 30, TimeUnit.SECONDS);

        long expire = redisTool.getExpire(GET_EXPIRE1, param);

        Assert.isTrue(expire > 0, "获取的值错误：" + expire);

        long expire1 = redisTool.getExpire(GET_EXPIRE2, param);
        Assert.isTrue(expire1 == -2, "该key不存在");

        String key2 = GET_EXPIRE3.getFullKey(param);
        redisTool.opsForHash().putAll(key2, BeanUtil.beanToMap(basePO));
        long expire2 = redisTool.getExpire(GET_EXPIRE3, param);

        Assert.isTrue(expire2 == -1, "该key的ttl值应该是-1, expire2=" + expire2);

    }

    @Test
    void set() {
        setString();
        setHash();
        setList();
        setSet();
    }

    @Test
    void setString() {
        boolean boo1 = redisTool.set(
                new SimpleRedisKey("goudong:goudong-user-server:test:string1", DataType.STRING, String.class),
                "as");
        Assert.isTrue(boo1, "\"as\"设置到redis中失败");

        boolean boo2 = redisTool.set(
                new SimpleRedisKey("goudong:goudong-user-server:test:string2", DataType.STRING, String.class),
                null);

        Assert.isTrue(boo2, "设置 null 到redis失败");
        boolean boo3 = redisTool.set(
                new SimpleRedisKey("goudong:goudong-user-server:test:string3", DataType.STRING, String.class),
                "");

        Assert.isTrue(boo3, "设置\"\"到redis失败");
    }


    void setHash() {
        // 设置HASH
        User user1 = new User();
        user1.setUsername("张三");
        user1.setPassword("面膜123123123");
        boolean boo1 = redisTool.set(
                new SimpleRedisKey("goudong:goudong-user-server:test:hash1", DataType.HASH, String.class),
                user1);
        Assert.isTrue(boo1, "设置"+user1+"到redis失败");

        User user2 = new User();
        boolean boo2 = redisTool.set(
                new SimpleRedisKey("goudong:goudong-user-server:test:hash2", DataType.HASH, String.class),
                user2);
        Assert.isTrue(boo2, "设置"+user2+"到redis失败");
        boolean boo3 = redisTool.set(
                new SimpleRedisKey("goudong:goudong-user-server:test:hash3", DataType.HASH, String.class),
                null);

        Assert.isTrue(!boo3, "设置 null 到redis失败");
    }

    void setList() {

        // null设置到redis中
        boolean boo1 = redisTool.set(
                new SimpleRedisKey("goudong:goudong-user-server:test:list1", DataType.LIST, User.class),
                null);
        Assert.isTrue(!boo1, "设置null到redis失败");

        // 空集合会设置到redis中
        ArrayList<User> list2 = Lists.newArrayList();
        boolean boo2 = redisTool.set(
                new SimpleRedisKey("goudong:goudong-user-server:test:list2", DataType.LIST, User.class),
                list2);
        Assert.isTrue(!boo2, "设置"+list2+"到redis失败");

        // 设置集合
        ArrayList<User> user3 = Lists.newArrayList(User.builder().username("user1").password("123456").build(),
                User.builder().username("user2").password("123456").build(),
                User.builder().username("user3").age(18).build());
        boolean boo3 = redisTool.set(
                new SimpleRedisKey("goudong:goudong-user-server:test:list3", DataType.LIST, User.class),
                user3);
        Assert.isTrue(boo3, "设置"+user3+"到redis失败");
    }

    @Test
    void setSet() {
        // 设置Set
        boolean boo1 = redisTool.set(
                new SimpleRedisKey("goudong:goudong-user-server:test:set1", DataType.SET, String.class),
                CollectionUtil.newHashSet(1, 2, 3, 4));
        Assert.isTrue(boo1, "设置 null 到 Set失败");

        boolean boo2 = redisTool.set(
                new SimpleRedisKey("goudong:goudong-user-server:test:set2", DataType.SET, String.class),
                CollectionUtil.newHashSet("1", "2", "3", "4"));
        Assert.isTrue(boo2, "设置 空集合 到 Set失败");

        HashSet<Object> hashSet = new HashSet<>();
        hashSet.addAll(Lists.newArrayList("a", "b", "c"));
        boolean boo3 = redisTool.set(
                new SimpleRedisKey("goudong:goudong-user-server:test:set3", DataType.SET, String.class),
                hashSet);
        Assert.isTrue(boo3, String.format("设置 %s 到 Set失败", hashSet));
    }

    @Test
    void getSet() {
        // 设置Set
        Set<String> set = (Set<String>)redisTool.get(
                new SimpleRedisKey("goudong:goudong-user-server:test:set1", DataType.SET, String.class));

        set = (Set<String>)redisTool.get(
                new SimpleRedisKey("goudong:goudong-user-server:test:set2", DataType.SET, String.class));
        System.out.println(set);
    }

    @Test
    void getString(){
        String str1 = redisTool.getString(
                new SimpleRedisKey("goudong:goudong-user-server:test:string1", DataType.STRING, String.class)
                );
        Assert.isTrue(Objects.equals("as", str1), "错误：" + str1);

        String str2 = redisTool.getString(
                new SimpleRedisKey("goudong:goudong-user-server:test:string2", DataType.STRING, String.class)
               );
        Assert.isTrue(Objects.equals(null, str2), "错误：" + str1);

        String str3 = redisTool.getString(
                new SimpleRedisKey("goudong:goudong-user-server:test:string3", DataType.STRING, String.class)
                );
        Assert.isTrue(Objects.equals("", str3), "错误：" + str1);

    }

    @Test
    void getHash() {
        // 设置HASH
        User user1 = new User();
        user1.setUsername("张三");
        user1.setPassword("面膜123123123");
        redisTool.opsForHash().putAll("goudong:goudong-user-server:test:hash1", BeanUtil.beanToMap(user1));
        User user = redisTool.getHash(
                new SimpleRedisKey("goudong:goudong-user-server:test:hash1", DataType.HASH, User.class),
                User.class);
        Assert.isTrue(user1.equals(user), "获取对象不相等");

        User user3 = redisTool.getHash(
                new SimpleRedisKey("goudong:goudong-user-server:test:hash2", DataType.HASH, User.class),User.class);
        Assert.isTrue(user3 == null, "获取无效key错误");
    }

    public static final SimpleRedisKey ZSET_TEMPLATE =
            new SimpleRedisKey("test:zset:${id}", DataType.ZSET, DefaultTypedTuple.class, 100, TimeUnit.SECONDS);

    @Test
    void testZSet(){
        Set<DefaultTypedTuple> zset = new HashSet();
        zset.add(new DefaultTypedTuple("cfl", 100D));
        zset.add(new DefaultTypedTuple("zs", 90D));
        zset.add(new DefaultTypedTuple("wmz", 15D));
        redisTool.set(ZSET_TEMPLATE, zset, 1);

        Set<DefaultTypedTuple> o = (Set<DefaultTypedTuple>)redisTool.get(ZSET_TEMPLATE, 1);
        Iterator<DefaultTypedTuple> iterator = o.iterator();
        while (iterator.hasNext()) {
            DefaultTypedTuple next = iterator.next();
            System.out.format("score:%s,value:%s", next.getScore(), next.getValue());
        }

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