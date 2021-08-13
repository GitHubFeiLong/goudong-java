package com.goudong.commons.openfeign;

import com.goudong.commons.dto.AuthorityUserDTO;
import com.goudong.commons.pojo.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Oauth2接口
 */
@FeignClient(name="goudong-oauth2-server", path = "/api/oauth2")
public interface Oauth2Service {

    /**
     * 查询用户的详细信息,包括角色权限
     * @param loginName
     * @return
     */
    @GetMapping("/open/user/detail-info/{login-name}")
    @ResponseBody
    Result<AuthorityUserDTO> getUserDetailByLoginName (@PathVariable("login-name") String loginName);
}
