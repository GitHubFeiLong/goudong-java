package com.goudong.oauth2;

import com.goudong.commons.pojo.ResourceAntMatcher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;

/**
 * 类描述：
 *
 * @Author e-Feilong.Chen
 * @Date 2021/8/16 15:14
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class ResourceUtil {

    @Test
    public void test() throws IOException, ClassNotFoundException {
        List<ResourceAntMatcher> resourceAntMatchers = com.goudong.commons.utils.ResourceUtil.scanMenu("/api/demo");
        System.out.println("resourceAntMatchers = " + resourceAntMatchers);
    }
}
