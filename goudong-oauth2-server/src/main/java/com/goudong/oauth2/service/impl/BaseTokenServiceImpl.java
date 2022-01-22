package com.goudong.oauth2.service.impl;

import com.goudong.commons.utils.BeanUtil;
import com.goudong.oauth2.dto.BaseTokenDTO;
import com.goudong.oauth2.po.BaseTokenPO;
import com.goudong.oauth2.repository.BaseTokenRepository;
import com.goudong.oauth2.service.BaseTokenService;
import org.springframework.data.domain.Example;
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

    /**
     * 根据访问令牌,和客户端类型获取令牌信息
     *
     * @param accessToken 访问令牌
     * @param clientType  客户端类型
     * @return
     */
    @Override
    public BaseTokenDTO findByAccessTokenAndClientType(String accessToken, String clientType) {
        return BeanUtil.copyProperties(baseTokenRepository.findByAccessTokenAndClientType(accessToken, clientType), BaseTokenDTO.class);
    }

    /**
     * 根据构建的example查询令牌
     *
     * @param example 构造查询对象
     * @return
     */
    @Override
    public BaseTokenDTO findByExample(Example<BaseTokenPO> example) {
        return BeanUtil.copyProperties(this.baseTokenRepository.findOne(example).orElseGet(()->null), BaseTokenDTO.class);
    }


}
