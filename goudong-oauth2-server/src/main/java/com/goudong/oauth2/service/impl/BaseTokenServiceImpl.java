package com.goudong.oauth2.service.impl;

import com.goudong.commons.utils.BeanUtil;
import com.goudong.oauth2.dto.BaseTokenDTO;
import com.goudong.oauth2.po.BaseTokenPO;
import com.goudong.oauth2.repository.BaseTokenRepository;
import com.goudong.oauth2.service.BaseTokenService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 接口描述：
 * 令牌服务实现
 * @author msi
 * @version 1.0
 * @date 2022/1/19 20:28
 */
@Service
public class BaseTokenServiceImpl implements BaseTokenService {

    //~fields
    //==================================================================================================================
    /**
     * 令牌持久层
     */
    private final BaseTokenRepository baseTokenRepository;

    //~methods
    //==================================================================================================================
    public BaseTokenServiceImpl(BaseTokenRepository baseTokenRepository) {
        this.baseTokenRepository = baseTokenRepository;
    }

    /**
     * 保存令牌到数据库
     *
     * @param dto
     * @return
     */
    @Override
    @Transactional
    public BaseTokenDTO save(BaseTokenDTO dto) {
        // 根据用户id,和客户端类型 查询数据
        BaseTokenPO byUserIdAndClientType = baseTokenRepository.findByUserIdAndClientType(dto.getUserId(), dto.getClientType());
        if (byUserIdAndClientType != null) {
            baseTokenRepository.delete(byUserIdAndClientType);
        }
        // Hibernate 在实际执行SQL语句时并没有按照代码的顺序执行，而是按照 INSERT, UPDATE, DELETE的顺序执行的
        // 先执行flush()
        baseTokenRepository.flush();
        // 新建
        BaseTokenPO baseTokenPO = BeanUtil.copyProperties(dto, BaseTokenPO.class);
        this.baseTokenRepository.save(baseTokenPO);
        return BeanUtil.copyProperties(baseTokenPO, BaseTokenDTO.class);
    }
}
