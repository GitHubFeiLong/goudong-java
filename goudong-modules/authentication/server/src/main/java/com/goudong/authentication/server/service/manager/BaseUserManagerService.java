package com.goudong.authentication.server.service.manager;

import com.goudong.authentication.common.core.LoginResp;
import com.goudong.authentication.common.core.UserDetail;
import com.goudong.authentication.server.domain.BaseUser;
import com.goudong.authentication.server.rest.req.BaseUserCreate;
import com.goudong.authentication.server.rest.req.BaseUserUpdate;
import com.goudong.authentication.server.rest.req.search.BaseUserDropDown;
import com.goudong.authentication.server.rest.req.search.BaseUserPage;
import com.goudong.authentication.server.service.dto.BaseUserDTO;
import com.goudong.authentication.server.service.dto.MyAuthentication;
import com.goudong.core.lang.PageResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * 类描述：
 * 用户管理服务层接口
 * @author chenf
 * @version 1.0
 */
public interface BaseUserManagerService {

    /**
     * 根据应用Id和用户名查询用户
     * @param appId 应用Id
     * @param username 用户名
     * @return 返回用户
     */
    BaseUser findOneByAppIdAndUsername(Long appId, String username);

    /**
     * 获取登录成功信息
     * @param myAuthentication 用户认证成功对象
     * @return 用户基本信息和token
     */
    LoginResp login(MyAuthentication myAuthentication);
}
