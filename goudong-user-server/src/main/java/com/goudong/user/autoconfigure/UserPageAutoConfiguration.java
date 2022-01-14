package com.goudong.user.autoconfigure;

import com.goudong.commons.exception.core.ApplicationBootFailedException;
import com.goudong.commons.utils.core.StringUtil;
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

    /**
     * 创建用户配置页面Bean
     * @return
     */
    @Bean
    public UserPage userPage() {
        // 检查配置是否正确
        checkUrl(this.userPageProperties);

        UserPage userPage = new UserPage();
        userPage.setBindPage(userPageProperties.getBindPage());
        userPage.setTransitionPage(userPageProperties.getTransitionPage());
        userPage.setIndexPage(userPageProperties.getIndexPage());
        return userPage;
    }

    /**
     * 检查配置路径是否正确
     * @param userPageProperties
     */
    private void checkUrl(UserPageProperties userPageProperties) {
        if (!StringUtil.regexUrl(userPageProperties.getBindPage())) {
            throw new ApplicationBootFailedException("页面配置错误", "配置qq登陆成功需要绑定账号跳转页面地址错误(" + userPageProperties.getBindPage() + ")",
                    "修改属性:user.page.bind-page为正确的URL");
        }
        if (!StringUtil.regexUrl(userPageProperties.getTransitionPage())) {
            throw new ApplicationBootFailedException("页面配置错误", "配置重定向页面传参过渡页地址错误(" + userPageProperties.getTransitionPage() + ")",
                    "修改属性user.page.transition-page为正确的URL");
        }
        if (!StringUtil.regexUrl(userPageProperties.getIndexPage())) {
            throw new ApplicationBootFailedException("页面配置错误", "配置重定向应用首页地址错误(" + userPageProperties.getIndexPage() + ")",
                    "修改属性user.page.index-page为正确的URL");
        }

    }
}
