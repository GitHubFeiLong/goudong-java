package com.goudong.authentication.server.service.manager;

import com.goudong.authentication.common.core.LoginResp;
import com.goudong.authentication.common.core.Token;
import com.goudong.authentication.common.core.UserDetail;
import com.goudong.authentication.server.domain.BaseUser;
import com.goudong.authentication.server.rest.req.BaseUserPageReq;
import com.goudong.authentication.server.rest.req.RefreshToken;
import com.goudong.authentication.server.rest.req.search.BaseUserDropDown;
import com.goudong.authentication.server.rest.req.search.BaseUserPageSearchReq;
import com.goudong.authentication.server.rest.resp.BaseUserDropDownResp;
import com.goudong.authentication.server.service.dto.MyAuthentication;
import com.goudong.core.lang.PageResult;

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

    /**
     * 刷新token
     * @param token refreshToken
     * @return token
     */
    Token refreshToken(RefreshToken token);

    /**
     * 根据{@code token}获取用户信息
     * @param token token
     * @return 用户信息
     */
    UserDetail getUserDetailByToken(String token);

    /**
     * 分页获取用户下拉，只返回操作人所在真实应用下的用户
     * @param req 请求参数
     * @return 用户下拉列表
     */
    PageResult<BaseUserDropDownResp> userDropDown(BaseUserDropDown req);

    /**
     * 分页查询用户
     * @param req 分页参数
     * @return 用户分页对象
     */
    PageResult<BaseUserPageSearchReq> page(BaseUserPageReq req);
}
