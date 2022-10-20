package com.goudong.modules.redis.master.slave;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;

import java.util.HashSet;
import java.util.Set;

/**
 * 类描述：
 *
 * @author cfl
 * @version 1.0
 * @date 2022/10/20 9:20
 */
@ExtendWith(SpringExtension.class)
public class JedisSentinelTest {

    @Test
    void test1() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(20);
        config.setMaxIdle(10);
        config.setMinIdle(5);

        String masterName = "mymaster";
        Set<String> sentinels = new HashSet<String>();
        sentinels.add(new HostAndPort("192.168.0.138",26379).toString());
        sentinels.add(new HostAndPort("192.168.0.138",26380).toString());
        sentinels.add(new HostAndPort("192.168.0.138",26381).toString());
        //JedisSentinelPool其实本质跟JedisPool类似，都是与redis主节点建立的连接池
        //JedisSentinelPool并不是说与sentinel建立的连接池，而是通过sentinel发现redis主节点并与其建立连接
        JedisSentinelPool jedisSentinelPool = new JedisSentinelPool(masterName, sentinels, config, 3000, "soft01");
        Jedis jedis = null;
        try {
            jedis = jedisSentinelPool.getResource();
            System.out.println(jedis.set("sentinel666", "zhuge"));
            System.out.println(jedis.get("sentinel666"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //注意这里不是关闭连接，在JedisPool模式下，Jedis会被归还给资源池。
            if (jedis != null)
                jedis.close();
        }
    }
}
