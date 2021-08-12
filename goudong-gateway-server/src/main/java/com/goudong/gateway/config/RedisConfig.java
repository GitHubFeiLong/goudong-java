package com.goudong.gateway.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.goudong.commons.utils.RedisOperationsUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * 类描述：
 *
 * @Author msi
 * @Date 2021-08-12 20:16
 * @Version 1.0
 */
@Configuration
public class RedisConfig {
    /**
     * 配置 RedisOperationsUtil
     * 因为 RedisOperationsUtil extedns RedisTemplate 所以需要特定的配置，不能直接使用@Component注解
     * @param connectionFactory
     * @return
     */
    @Bean
    @SuppressWarnings("ALL")
    public RedisOperationsUtil redisOperationsUtil(RedisConnectionFactory connectionFactory){
        RedisOperationsUtil template = new RedisOperationsUtil();
        template.setConnectionFactory(connectionFactory);
        //使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值
        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        serializer.setObjectMapper(mapper);

        template.setValueSerializer(serializer);
        //使用StringRedisSerializer来序列化和反序列化redis的key值
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(serializer);
        template.afterPropertiesSet();
        return template;
    }
}
