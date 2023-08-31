package com.goudong.authentication.server.service.manager.impl;

import com.goudong.authentication.common.core.LoginResp;
import com.goudong.authentication.common.core.UserDetail;
import com.goudong.authentication.server.domain.BaseUser;
import com.goudong.authentication.server.rest.req.BaseUserCreate;
import com.goudong.authentication.server.rest.req.BaseUserUpdate;
import com.goudong.authentication.server.rest.req.search.BaseUserDropDown;
import com.goudong.authentication.server.rest.req.search.BaseUserPage;
import com.goudong.authentication.server.service.BaseUserService;
import com.goudong.authentication.server.service.dto.BaseUserDTO;
import com.goudong.authentication.server.service.dto.MyAuthentication;
import com.goudong.authentication.server.service.manager.BaseUserManagerService;
import com.goudong.core.lang.PageResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

/**
 * 类描述：
 * 用户管理服务层接口实现类
 * @author chenf
 * @version 1.0
 */
@Service
public class BaseUserManagerServiceImpl implements BaseUserManagerService {
    //~fields
    //==================================================================================================================
    @Resource
    private BaseUserService userService;

    //~methods
    //==================================================================================================================
}
