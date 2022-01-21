package com.goudong.commons.openfeign;

import com.goudong.commons.dto.oauth2.BaseWhitelist2CreateDTO;
import com.goudong.commons.dto.oauth2.BaseWhitelistDTO;
import com.goudong.commons.frame.core.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

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
}
