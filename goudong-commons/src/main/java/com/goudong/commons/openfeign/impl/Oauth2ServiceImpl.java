package com.goudong.commons.openfeign.impl;

import com.goudong.commons.dto.AuthorityMenuDTO;
import com.goudong.commons.dto.AuthorityUserDTO;
import com.goudong.commons.dto.BaseIgnoreResourceDTO;
import com.goudong.commons.openfeign.Oauth2Service;
import com.goudong.commons.pojo.Result;
import com.goudong.commons.vo.AuthorityMenu2InsertVO;
import com.goudong.commons.vo.BaseIgnoreResourceVO;

import java.util.List;

/**
 * 类描述：
 * 熔断,限流,异常回调
 * @Author e-Feilong.Chen
 * @Date 2021/8/13 10:49
 */
public class Oauth2ServiceImpl implements Oauth2Service {
    /**
     * 查询用户的详细信息,包括角色权限
     *
     * @param loginName
     * @return
     */
    @Override
    public Result<AuthorityUserDTO> getUserDetailByLoginName(String loginName) {
        return Result.ofFail();
    }

    /**
     * 添加多条白名单
     *
     * @param insertVOList 白名单集合
     * @return
     */
    @Override
    public Result<List<BaseIgnoreResourceDTO>> addIgnoreResources(List<BaseIgnoreResourceVO> insertVOList) {
        return null;
    }

    /**
     * 批量添加菜单
     *
     * @param insertVOList
     * @return
     */
    @Override
    public Result<List<AuthorityMenuDTO>> addMenus(List<AuthorityMenu2InsertVO> insertVOList) {
        return null;
    }

}
