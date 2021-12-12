package com.goudong.oauth2;
import cn.hutool.core.bean.BeanUtil;
import com.google.common.collect.Lists;
import com.goudong.commons.dto.AuthorityRoleDTO;
import com.goudong.commons.dto.AuthorityUserDTO;
import com.goudong.commons.utils.JwtTokenUtil;
import com.goudong.commons.core.redis.RedisOperationsUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 类描述：
 *
 * @Author e-Feilong.Chen
 * @Date 2021/8/16 15:14
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class RedisOperationUtilTest {

    @Autowired
    private RedisOperationsUtil redisOperationsUtil;
    @Test
    public void test() throws IOException, ClassNotFoundException {
        AuthorityUserDTO authorityUserDTO = new AuthorityUserDTO();
        authorityUserDTO.setUsername("setUsername");
        authorityUserDTO.setPassword("setPassword");
        authorityUserDTO.setEmail("setEmail");
        authorityUserDTO.setPhone("setPhone");
        authorityUserDTO.setValidTime(LocalDateTime.now());
        AuthorityRoleDTO authorityRoleDTO = new AuthorityRoleDTO();
        authorityRoleDTO.setRoleName("roleName1");
        AuthorityRoleDTO authorityRoleDTO1 = new AuthorityRoleDTO();
        authorityRoleDTO1.setRoleName("roleName2");
        authorityRoleDTO1.setId(10L);

        authorityUserDTO.setAuthorityRoleDTOS(Lists.newArrayList(authorityRoleDTO, authorityRoleDTO1));
        authorityUserDTO.setAuthorityMenuDTOS(Lists.newArrayList());
        authorityUserDTO.setDeleted(true);
        authorityUserDTO.setUpdateTime(LocalDateTime.now());
        authorityUserDTO.setCreateTime(LocalDateTime.now());

        Map<String, Object> stringObjectMap = BeanUtil.beanToMap(authorityUserDTO);

        redisOperationsUtil.opsForHash().putAll("key", stringObjectMap);

        Map key = redisOperationsUtil.opsForHash().entries("key");
        AuthorityUserDTO authorityUserDTO1 = BeanUtil.toBean(key, AuthorityUserDTO.class);
        System.out.println("1 = " + 1);
    }

    @Test
    public void test1 () {
        String token =
                "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJ7XCJhdXRob3JpdHlNZW51RFRPU1wiOltdLFwiYXV0aG9yaXR5Um9sZURUT1NcIjpbe1wicmVtYXJrXCI6XCJcIixcInJvbGVOYW1lXCI6XCJST0xFX0FETUlOXCIsXCJyb2xlTmFtZUNuXCI6XCLotoXnuqfnrqHnkIblkZhcIn1dLFwiZW1haWxcIjpcIkBcIixcImlkXCI6MSxcInBhc3N3b3JkXCI6XCIkMmEkMTAkOHB0dGVWMXhQNTFBak9TL3U2TkFsZS8xUHcyQlVzUy5EL21iR3VoYXVmLnFjLm9UaWtlQXlcIixcInBob25lXCI6XCIxXCIsXCJyZW1hcmtcIjpcIui2hee6p-euoeeQhuWRmFwiLFwidXNlcm5hbWVcIjpcImFkbWluXCJ9Iiwic3ViIjoi54uX5LicIiwiaXNzIjoiY2ZsIiwiZXhwIjoxNjI5Nzg5MzcwLCJpYXQiOjE2MjkxODQ1NzAsImp0aSI6IjkzYmE0Y2U3LTA3MmMtNGQxMC04N2ZmLTBkM2U1NmI4OTkxZiJ9.DfCPPDxENk8kSMBfCbfCPcXhZcqRaocjKOyT-B6upc4"
                ;
        String key = JwtTokenUtil.generateRedisKey(token);

        AuthorityUserDTO authorityUserDTO = JwtTokenUtil.resolveToken(token);
        redisOperationsUtil.opsForHash().putAll(key, BeanUtil.beanToMap(authorityUserDTO));

        authorityUserDTO.setUsername(null);
        redisOperationsUtil.opsForHash().putAll(key, BeanUtil.beanToMap(authorityUserDTO));

        System.out.println(1);
    }

    @Test
    public void test2() {
        redisOperationsUtil.opsForValue().set("key", "value", -1, TimeUnit.HOURS);

    }
}
