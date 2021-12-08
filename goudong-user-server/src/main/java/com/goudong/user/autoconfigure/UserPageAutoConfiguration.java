package com.goudong.user.autoconfigure;

import com.goudong.user.core.UserPage;
import com.goudong.user.properties.UserPageProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 类描述：
 * 自动配置用户页面
 * @Author e-Feilong.Chen
 * @Date 2021/12/8 12:24
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(UserPageProperties.class)
public class UserPageAutoConfiguration {

    /**
     * 页面的一些属性
     */
    private final UserPageProperties userPageProperties;

    public UserPageAutoConfiguration(UserPageProperties userPageProperties) {
        this.userPageProperties = userPageProperties;
    }

    @Bean
    public UserPage userPage() {
        UserPage userPage = new UserPage();
        userPage.setBindPage(userPageProperties.getBindPage());
        userPage.setTransitionPage(userPageProperties.getTransitionPage());
        userPage.setIndexPage(userPageProperties.getIndexPage());

        return userPage;
    }
}
