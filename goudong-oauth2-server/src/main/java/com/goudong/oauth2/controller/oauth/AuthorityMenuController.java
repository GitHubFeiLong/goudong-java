package com.goudong.oauth2.controller.oauth;

import com.goudong.commons.dto.AuthorityMenuDTO;
import com.goudong.commons.pojo.Result;
import com.goudong.commons.utils.BeanUtil;
import com.goudong.commons.vo.AuthorityMenu2InsertVO;
import com.goudong.oauth2.service.AuthorityMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 类描述：
 * 菜单控制器
 * @Author e-Feilong.Chen
 * @Date 2021/8/16 16:05
 */
@Api(tags = "菜单")
@Slf4j
@Validated
@RestController
@RequestMapping("/menu")
public class AuthorityMenuController {

    @Autowired
    private AuthorityMenuService authorityMenuService;

    @PostMapping("/menus")
    @ApiOperation("批量添加菜单")
    public Result<List<AuthorityMenuDTO>> addList (@RequestBody List<AuthorityMenu2InsertVO> insertVOList) {
        List<AuthorityMenuDTO> insetDTOS = BeanUtil.copyList(insertVOList, AuthorityMenuDTO.class);

        List<AuthorityMenuDTO> authorityMenuDTOS = authorityMenuService.addList(insetDTOS);
        return  Result.ofSuccess(authorityMenuDTOS);
    }



}
