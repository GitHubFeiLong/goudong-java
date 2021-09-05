package com.goudong.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.goudong.commons.dto.BaseTokenDTO;
import com.goudong.commons.enumerate.ClientExceptionEnum;
import com.goudong.commons.exception.ClientException;
import com.goudong.commons.po.AuthorityUserPO;
import com.goudong.commons.po.BaseTokenPO;
import com.goudong.commons.utils.BeanUtil;
import com.goudong.commons.utils.StringUtil;
import com.goudong.user.mapper.BaseTokenMapper;
import com.goudong.user.service.AuthorityUserService;
import com.goudong.user.service.BaseTokenService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 类描述：
 * base_token 接口
 * @author msi
 * @version 1.0
 * @date 2021/9/5 22:01
 */
@Service
public class BaseTokenServiceImpl extends ServiceImpl<BaseTokenMapper, BaseTokenPO> implements BaseTokenService {

    private final AuthorityUserService authorityUserService;

    @Autowired
    public BaseTokenServiceImpl(
            AuthorityUserService authorityUserService
    ) {
        this.authorityUserService = authorityUserService;
    }
    /**
     * 新增多条用户token信息
     *
     * @param baseTokenDTOS
     * @return
     */
    @Override
    public List<BaseTokenDTO> createTokens(List<BaseTokenDTO> baseTokenDTOS) {
        List<BaseTokenPO> baseTokenPOS = BeanUtil.copyList(baseTokenDTOS, BaseTokenPO.class);
        // 检查，用户id
        Set<Long> userIds = baseTokenPOS.stream().map(BaseTokenPO::getUserId).collect(Collectors.toSet());
        List<AuthorityUserPO> authorityUserPOS = authorityUserService.listByIds(userIds);

        if (userIds.size() > authorityUserPOS.size()) {
            Set<Long> existsUserIds = authorityUserPOS.stream().map(AuthorityUserPO::getId).collect(Collectors.toSet());
            userIds.removeAll(existsUserIds);

            String clientMessage = StringUtil.format("以下userId数据库中不存在：{}", StringUtils.join( userIds, ","));
            throw ClientException.clientException(ClientExceptionEnum.UNPROCESSABLE_ENTITY, clientMessage);
        }

        super.saveBatch(baseTokenPOS);

        return BeanUtil.copyList(baseTokenPOS, BaseTokenDTO.class);
    }

}
