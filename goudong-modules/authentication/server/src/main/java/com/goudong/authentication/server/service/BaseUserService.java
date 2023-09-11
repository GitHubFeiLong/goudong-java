package com.goudong.authentication.server.service;

import com.goudong.authentication.server.domain.BaseUser;
import com.goudong.authentication.server.rest.req.BaseUserCreate;
import com.goudong.authentication.server.rest.req.BaseUserPageReq;
import com.goudong.authentication.server.rest.req.BaseUserSimpleUpdateReq;
import com.goudong.authentication.server.rest.req.search.BaseUserDropDownReq;
import com.goudong.authentication.server.rest.resp.BaseUserDropDownResp;
import com.goudong.authentication.server.rest.resp.BaseUserPageResp;
import com.goudong.authentication.server.service.dto.BaseUserDTO;
import com.goudong.core.lang.PageResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link BaseUser}.
 */
public interface BaseUserService {
    //~fields
    //==================================================================================================================

    //~methods
    //==================================================================================================================

    /**
     * 根据应用Id和用户名查询用户
     * @param appId 应用Id
     * @param username 用户名
     * @return 返回用户
     */
    BaseUser findOneByAppIdAndUsername(Long appId, String username);

    /**
     * 根据id查询用户
     * @param id 用户id
     * @return 用户对象
     */
    BaseUser findById(Long id);

    /**
     * 根据id查询用户
     * @param id 用户id
     * @return 用户对象详细信息（保留角色菜单）
     */
    BaseUser findDetailById(Long id);

    /**
     * 分页获取用户下拉，只返回操作人所在真实应用下的用户
     *
     * @param req 请求参数
     * @return 用户下拉列表
     */
    PageResult<BaseUserDropDownResp> userDropDown(BaseUserDropDownReq req);

    /**
     * 分页查询用户
     *
     * @param req 分页参数
     * @return 用户分页对象
     */
    PageResult<BaseUserPageResp> page(BaseUserPageReq req);

    /**
     * 新增/修改用户
     * @param user
     * @return
     */
    BaseUserDTO save(BaseUser user);

    /**
     * 批量删除用户
     *
     * @param ids 被删除的用户id集合
     * @return true删除成功；false删除失败
     */
    Boolean deleteByIds(List<Long> ids);

    /**
     * 重置用户密码
     * @param userId 用户id
     * @return
     */
    Boolean resetPassword(Long userId);

    /**
     * 修改用户激活状态
     *
     * @param userId 用户id
     * @return
     */
    Boolean changeEnabled(Long userId);

    /**
     * 修改用户锁定状态
     *
     * @param userId 用户id
     * @return
     */
    Boolean changeLocked(Long userId);
}
