package com.goudong.oauth2.controller.open;

import cn.hutool.core.util.IdUtil;
import com.goudong.commons.dto.AuthorityUserDTO;
import com.goudong.commons.pojo.Result;
import com.goudong.commons.utils.AssertUtil;
import com.goudong.commons.utils.BeanUtil;
import com.goudong.commons.utils.JwtTokenUtil;
import com.goudong.commons.vo.AuthorityUser2CreateVO;
import com.goudong.commons.vo.AuthorityUserVO;
import com.goudong.oauth2.service.AuthorityUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 类描述：
 * 用户控制器
 * @Author msi
 * @Date 2021-05-02 13:33
 * @Version 1.0
 */
@Api(tags = "登录后操作用户")
@Slf4j
@Validated
@RestController
@RequestMapping("/api/oauth2/user")
public class AuthorityUserController {

}
