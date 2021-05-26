package com.goudong.oauth2;

import com.goudong.commons.pojo.Transition;
import com.goudong.oauth2.config.UIProperties;
import com.goudong.oauth2.controller.qq.QQController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @Author msi
 * @Date 2021-05-26 17:12
 * @Version 1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class BootTest {
    @Resource
    private UIProperties uiProperties;

    @Test
    public void test1 () {
        String token = "token123";

        Transition transition = Transition.builder()
                .token(token)
                .redirectUrl(uiProperties.getIndexPage())
                .build();
        // 重定向首页，并带上token参数
        System.out.println(uiProperties.getTransitionPageUrl(transition));
    }
}
