package com.goudong.commons.openfeign;

import com.goudong.commons.dto.AuthorityUserDTO;
import com.goudong.commons.dto.BaseIgnoreResourceDTO;
import com.goudong.commons.pojo.Result;
import com.goudong.commons.vo.BaseIgnoreResourceVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Oauth2接口
 * @Author msi
 */
@FeignClient(name="goudong-oauth2-server", path = "/api/oauth2")
@ResponseBody
public interface Oauth2Service {

    /**
     * 查询用户的详细信息,包括角色权限
     * @param loginName
     * @return
     */
    @GetMapping("/open/user/detail-info/{login-name}")
    @ResponseBody
    Result<AuthorityUserDTO> getUserDetailByLoginName (@PathVariable("login-name") String loginName);

    /**
     * 添加多条白名单
     * @param insertVOList 白名单集合
     * @return
     */
    @PostMapping("/ignore-resource/list")
    Result<List<BaseIgnoreResourceDTO>> addList (@RequestBody List<BaseIgnoreResourceVO> insertVOList);
}
