package com.goudong.modules.redis.master.slave;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * 类描述：
 * 使用jedis连接redis集群测试
 * @author cfl
 * @version 1.0
 * @date 2022/10/20 17:39
 */
@ExtendWith(SpringExtension.class)
public class JedisClusterTest {

    @Test
    void test1() throws IOException {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(20);
        config.setMaxIdle(10);
        config.setMinIdle(5);

        Set<HostAndPort> jedisClusterNode = new HashSet<HostAndPort>();
        jedisClusterNode.add(new HostAndPort("192.168.0.138", 8001));
        jedisClusterNode.add(new HostAndPort("192.168.0.138", 8002));
        jedisClusterNode.add(new HostAndPort("192.168.0.138", 8003));
        jedisClusterNode.add(new HostAndPort("192.168.0.138", 8004));
        jedisClusterNode.add(new HostAndPort("192.168.0.138", 8005));
        jedisClusterNode.add(new HostAndPort("192.168.0.138", 8006));

        JedisCluster jedisCluster = null;
        try {
            //connectionTimeout：指的是连接一个url的连接等待时间
            //soTimeout：指的是连接上一个url，获取response的返回等待时间
            jedisCluster = new JedisCluster(jedisClusterNode, 6000, 5000, 10, "soft01", config);
            System.out.println(jedisCluster.set("cluster", "zhuge"));
            System.out.println(jedisCluster.get("cluster"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedisCluster != null)
                jedisCluster.close();
        }
    }
}
