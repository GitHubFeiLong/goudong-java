package com.goudong.oauth2.log;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 类描述：
 *
 * @author msi
 * @version 1.0
 * @date 2021/9/4 17:29
 */
@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class Log {

    @Test
    public void test1 () {

        while(true) {
            log.debug("测试debug日志，测试删除策略");
        }
    }
}
