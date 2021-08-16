package com.goudong.oauth2;

import com.goudong.commons.dto.AuthorityUserDTO;
import com.goudong.commons.pojo.Transition;
import com.goudong.commons.utils.JwtTokenUtil;
import com.goudong.oauth2.config.UIProperties;
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

    /**
     * 解析token
     */
    @Test
    public void testToken() {
        String token =
                "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJ7XCJhdXRob3JpdHlNZW51RFRPU1wiOlt7XCJtZXRob2RcIjpcImdldFwiLFwidXJsXCI6XCIvYXBpL3VzZXIvaGVsbG9cIixcInV1aWRcIjpcIjQwY2JkMjMyLWIxZjgtMTFlYi1iNDk2LWI0MmU5OWFlZGFjY1wifV0sXCJhdXRob3JpdHlSb2xlRFRPU1wiOlt7XCJyZW1hcmtcIjpcIlwiLFwicm9sZU5hbWVcIjpcIlJPTEVfQURNSU5cIixcInJvbGVOYW1lQ25cIjpcIueuoeeQhuWRmFwifV0sXCJjcmVhdGVUaW1lXCI6MTYxNzQ2NDkxOTAwMCxcImVtYWlsXCI6XCIxXCIsXCJwYXNzd29yZFwiOlwiJDJhJDEwJDhwdHRlVjF4UDUxQWpPUy91Nk5BbGUvMVB3MkJVc1MuRC9tYkd1aGF1Zi5xYy5vVGlrZUF5XCIsXCJwaG9uZVwiOlwiMTUyMTM1MDc3MTZcIixcInVwZGF0ZVRpbWVcIjoxNjE3NDY0OTE5MDAwLFwidXNlcm5hbWVcIjpcImFkbWluXCIsXCJ1dWlkXCI6XCJlZDdiYzI1NC1iMWY3LTExZWItYjQ5Ni1iNDJlOTlhZWRhY2NcIixcInZhbGlkVGltZVwiOjE2NDA5NjU4ODUwMDB9Iiwic3ViIjoi54uX5LicIiwiaXNzIjoiY2ZsIiwiZXhwIjoxNjI5MzYyMTQ1LCJpYXQiOjE2Mjg3NTczNDUsImp0aSI6IjA5N2Q5YzRkLWJkMzEtNDRlNy05ZDIzLWViNGU0YTY0Y2YxNCJ9.LrZkfPuh4q-nF4N7U6DgNNnPikV3MU5hj9KgmSWSge8"
                ;
        AuthorityUserDTO authorityUserDTO = JwtTokenUtil.resolveToken(token);
        System.out.println(1);
    }
}
