package com.goudong.oauth2.config.security;

import cn.hutool.core.bean.BeanUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.goudong.core.lang.Result;
import com.goudong.oauth2.dto.BaseAuthenticationLogDTO;
import com.goudong.oauth2.dto.BaseTokenDTO;
import com.goudong.oauth2.dto.authentication.BaseRoleDTO;
import com.goudong.oauth2.dto.authentication.BaseUserDTO;
import com.goudong.oauth2.dto.authentication.LoginInfoDTO;
import com.goudong.oauth2.enumerate.AuthenticationLogTypeEnum;
import com.goudong.oauth2.po.BaseRolePO;
import com.goudong.oauth2.po.BaseUserPO;
import com.goudong.oauth2.service.BaseAuthenticationLogService;
import com.goudong.oauth2.service.BaseRoleService;
import com.goudong.oauth2.service.BaseTokenService;
import com.goudong.oauth2.service.BaseUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 类描述：
 * 认证成功处理器
 *
 * @author msi
 * @date 2022/1/15 21:46
 * @version 1.0
 */
@Slf4j
@Component
public class AuthenticationSuccessHandlerImpl implements AuthenticationSuccessHandler {

    /**
     * token服务层接口
     */
    private final BaseTokenService baseTokenService;

    /**
     * 用户服务
     */
    private final BaseUserService baseUserService;

    /**
     * 认证日志服务层接口
     */
    private final BaseAuthenticationLogService baseAuthenticationLogService;

    private final ObjectMapper objectMapper;

    private final BaseRoleService baseRoleService;

    public AuthenticationSuccessHandlerImpl(BaseTokenService baseTokenService,
                                            BaseUserService baseUserService,
                                            BaseAuthenticationLogService baseAuthenticationLogService,
                                            ObjectMapper objectMapper,
                                            BaseRoleService baseRoleService) {
        this.baseTokenService = baseTokenService;
        this.baseUserService = baseUserService;
        this.baseAuthenticationLogService = baseAuthenticationLogService;
        this.objectMapper = objectMapper;
        this.baseRoleService = baseRoleService;
    }

    /**
     *
     * @param httpServletRequest
     * @param httpServletResponse
     * @param authentication 保存当前用户的登录信息
     * @throws IOException
     * @throws ServletException
     */
    @Override
    @Transactional
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest,
                                        HttpServletResponse httpServletResponse,
                                        Authentication authentication) throws IOException {
        httpServletResponse.setStatus(200);
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json;charset=UTF-8");

        // 转换成自定义的Authentication对象
        BaseUserPO baseUserPO = (BaseUserPO) authentication;

        // role
        List<BaseRoleDTO> roleDTOList = new ArrayList<>(baseUserPO.getRoles().size());
        baseUserPO.getRoles().stream().forEach(p->{
            p.setMenus(null);
            roleDTOList.add(new BaseRoleDTO(p.getId(), p.getRoleName(), p.getRoleNameCn()));
        });
        // 持久化token到Mysql
        BaseTokenDTO tokenDTO = baseTokenService.loginHandler(baseUserPO.getAppId(), baseUserPO.getId());

        // 将认证信息存储到redis中
        baseUserService.saveAccessToken2Redis(baseUserPO, tokenDTO.getAccessToken());

        baseUserPO.setSessionId(tokenDTO.getSessionId());

        // 保存认证日志
        BaseAuthenticationLogDTO baseAuthenticationLogDTO = new BaseAuthenticationLogDTO(
                baseUserPO.getAppId(),
                (String)httpServletRequest.getAttribute("principal"),
                true,
                AuthenticationLogTypeEnum.SYSTEM.name(),
                "认证成功");
        baseAuthenticationLogService.create(baseAuthenticationLogDTO);

        // 响应令牌和用户信息
        LoginInfoDTO loginInfo = BeanUtil.copyProperties(tokenDTO, LoginInfoDTO.class);

        BaseUserDTO baseUser = new BaseUserDTO();
        baseUser.setId(baseUserPO.getId());
        baseUser.setUsername(baseUserPO.getUsername());
        baseUser.setEmail(baseUserPO.getEmail());
        baseUser.setPhone(baseUserPO.getPhone());
        baseUser.setNickname(baseUserPO.getNickname());

        List<Long> roleIds = baseUserPO.getRoles().stream().map(BaseRolePO::getId).collect(Collectors.toList());
        baseRoleService.fillRoleAndMenu(roleIds, baseUser);
        baseUser.setAvatar(baseUserPO.getAvatar());
        loginInfo.setUser(baseUser);
        String json = objectMapper.writeValueAsString(Result.ofSuccess(loginInfo));

        httpServletResponse.getWriter().write(json);
    }

}
