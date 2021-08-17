package com.goudong.oauth2;
import cn.hutool.core.bean.BeanUtil;
import com.google.common.collect.Lists;
import java.time.LocalDateTime;

import com.goudong.commons.dto.AuthorityRoleDTO;
import com.goudong.commons.dto.AuthorityUserDTO;
import com.goudong.commons.pojo.ResourceAntMatcher;
import com.goudong.commons.utils.RedisOperationsUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
}
