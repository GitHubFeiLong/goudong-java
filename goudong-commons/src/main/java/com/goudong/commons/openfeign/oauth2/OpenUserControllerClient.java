package com.goudong.commons.openfeign.oauth2;

import com.goudong.commons.dto.AuthorityUserDTO;
import com.goudong.commons.pojo.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 类描述：
 *
 * @Author msi
 * @Date 2021-08-12 21:26
 * @Version 1.0
 */
@RequestMapping("/open/user")
@FeignClient(name="goudong-oauth2-server")
public interface OpenUserControllerClient {
    /**
     * 查询用户的基本信息
     * @param loginName 用户名/手机号/邮箱
     * @return
     */
    @GetMapping("/detail-info/{login-name}")
    Result<AuthorityUserDTO> getUserDetailByLoginName (@PathVariable("login-name") String loginName);
}
