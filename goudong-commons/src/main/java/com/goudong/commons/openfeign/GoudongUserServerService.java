package com.goudong.commons.openfeign;

import com.goudong.commons.dto.AuthorityUserDTO;
import com.goudong.commons.dto.BaseTokenDTO;
import com.goudong.commons.dto.user.BaseWhitelist2CreateDTO;
import com.goudong.commons.dto.user.BaseWhitelistDTO;
import com.goudong.commons.frame.core.Result;
import com.goudong.commons.vo.BaseToken2CreateVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * User接口
 * @Author msi
 */
@FeignClient(name="goudong-user-server", path = "/api/user")
@ResponseBody
public interface GoudongUserServerService {

    /**
     * 新增白名单
     * @param createDTOS
     * @return
     */
    @PostMapping("/whitelist/whitelist")
    Result<List<BaseWhitelistDTO>> addWhitelist(@RequestBody List<BaseWhitelist2CreateDTO> createDTOS);

    /**
     * 查询用户的详细信息,包括角色权限
     * @param loginName
     * @return
     */
    @GetMapping("/open/user/detail-info/{login-name}")
    Result<AuthorityUserDTO> getUserDetailByLoginName (@PathVariable("login-name") String loginName);

    /**
     * 批量添加token到数据库
     * @param token2CreateVOS
     * @return
     */
    @PostMapping("/base-token/tokens")
    Result<List<BaseTokenDTO>> createTokens (@RequestBody List<BaseToken2CreateVO> token2CreateVOS);

    /**
     * 根据token-md5查询
     * @param tokenMd5
     * @return
     */
    @GetMapping("/base-token/token/{token-md5}")
    Result<Optional<BaseTokenDTO>> getTokenByTokenMd5 (@PathVariable("token-md5") String tokenMd5);
}
