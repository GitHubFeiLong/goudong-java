package com.goudong.commons.openfeign.impl;

import com.goudong.commons.dto.AuthorityUserDTO;
import com.goudong.commons.dto.BaseTokenDTO;
import com.goudong.commons.openfeign.UserService;
import com.goudong.commons.frame.core.Result;
import com.goudong.commons.vo.BaseToken2CreateVO;

import java.util.List;
import java.util.Optional;

/**
 * 类描述：
 * 熔断,限流,异常回调
 * @Author e-Feilong.Chen
 * @Date 2021/8/13 10:49
 */
public class UserServiceImpl implements UserService {
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
     * 批量添加token到数据库
     *
     * @param token2CreateVOS
     * @return
     */
    @Override
    public Result<List<BaseTokenDTO>> createTokens(List<BaseToken2CreateVO> token2CreateVOS) {
        return null;
    }

    /**
     * 根据token-md5查询
     *
     * @param tokenMd5
     * @return
     */
    @Override
    public Result<Optional<BaseTokenDTO>> getTokenByTokenMd5(String tokenMd5) {
        return null;
    }

}
