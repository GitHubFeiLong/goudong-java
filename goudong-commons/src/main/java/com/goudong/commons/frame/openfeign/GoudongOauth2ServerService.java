package com.goudong.commons.frame.openfeign;

import com.goudong.commons.dto.oauth2.BaseUserDTO;
import com.goudong.commons.dto.oauth2.BaseWhitelist2CreateDTO;
import com.goudong.commons.dto.oauth2.BaseWhitelistDTO;
import com.goudong.commons.frame.core.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 类描述：
 * 认证服务接口
 * @author msi
 * @date 2022/1/20 22:26
 * @version 1.0
 */
@FeignClient(name="goudong-oauth2-server", path = "/api/oauth2")
@ResponseBody
public interface GoudongOauth2ServerService {
    //~白名单
    //==================================================================================================================
    /**
     * 新增白名单
     * @param createDTOS
     * @return
     */
    @PostMapping("/whitelist/whitelist")
    Result<List<BaseWhitelistDTO>> addWhitelist(@RequestBody List<BaseWhitelist2CreateDTO> createDTOS);

    /**
     * 查询所有白名单
     * @return
     */
    @GetMapping("/whitelist/whitelist")
    Result<List<BaseWhitelistDTO>> listWhitelist();

    /**
     * 初始化白名单到Redis中去
     * @return
     */
    @GetMapping("/init/whitelist-2-redis")
    Result<List<BaseWhitelistDTO>> initWhitelist2Redis();

    //~认证
    //==================================================================================================================

    //~鉴权
    //==================================================================================================================
    /**
     * 获取当前用户
     * @return
     */
    @GetMapping("/authentication/current-user-info")
    Result<BaseUserDTO> currentUser();

    /**
     * 鉴权 将令牌放在请求头中。
     * @param uri 请求地址
     * @param method 请求方式
     * @param token 令牌
     * @return
     */
    @GetMapping("/authentication/authorize")
    Result<BaseUserDTO> authorize(@RequestParam("uri") @NotBlank String uri
            , @RequestParam("method") @NotBlank String method
            , @RequestHeader(HttpHeaders.AUTHORIZATION) String token) ;
}
