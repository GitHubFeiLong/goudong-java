// package com.goudong.oauth2.config.oauth2;
//
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.data.redis.connection.RedisConnectionFactory;
// import org.springframework.security.oauth2.provider.token.TokenStore;
// import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
//
// /**
//  * 类描述：
//  *
//  * @Author e-Feilong.Chen
//  * @Date 2021/8/30 14:42
//  */
// @Configuration
// public class RedisTokenStoreConfig {
//     @Autowired
//     private RedisConnectionFactory redisConnectionFactory;
//
//     @Bean
//     public TokenStore redisTokenStore() {
//         return new RedisTokenStore(redisConnectionFactory);
//     }
// }
