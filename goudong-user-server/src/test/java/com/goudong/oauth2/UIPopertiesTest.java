package com.goudong.oauth2;

import com.goudong.user.config.UIProperties;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 类描述：
 *
 * @Author e-Feilong.Chen
 * @Date 2021/8/25 17:14
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class UIPopertiesTest {

    @Autowired
    private UIProperties uiProperties;
    @Test
    public void test1() {
        System.out.println("uiProperties = " + uiProperties);
    }
}
