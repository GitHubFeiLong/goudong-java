package com.goudong.commons.openfeign;

import com.goudong.commons.dto.AuthorityMenuDTO;
import com.goudong.commons.dto.AuthorityUserDTO;
import com.goudong.commons.dto.BaseIgnoreResourceDTO;
import com.goudong.commons.dto.BaseTokenDTO;
import com.goudong.commons.pojo.Result;
import com.goudong.commons.vo.AuthorityMenu2InsertVO;
import com.goudong.commons.vo.BaseIgnoreResourceVO;
import com.goudong.commons.vo.BaseToken2CreateVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * User接口
 * @Author msi
 */
@FeignClient(name="goudong-user-server", path = "/api/user")
@ResponseBody
public interface UserService {

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
    @PostMapping("/ignore-resource/ignore-resources")
    Result<List<BaseIgnoreResourceDTO>> addIgnoreResources (@RequestBody List<BaseIgnoreResourceVO> insertVOList);

    /**
     * 批量添加菜单
     * @param insertVOList
     * @return
     */
    @PostMapping("/menu/menus")
    Result<List<AuthorityMenuDTO>> addMenus (@RequestBody List<AuthorityMenu2InsertVO> insertVOList);

    /**
     * 批量添加token到数据库
     * @param token2CreateVOS
     * @return
     */
    @PostMapping("/base-token/tokens")
    Result<List<BaseTokenDTO>> createTokens (@RequestBody List<BaseToken2CreateVO> token2CreateVOS );
}
