package com.goudong.file.service;

import com.goudong.commons.dto.oauth2.BaseUserDTO;
import com.goudong.file.dto.BaseUser2QueryPageDTO;
import com.goudong.file.po.user.BaseUserPO;

import java.util.List;

/**
 * 接口描述：
 * 用户服务层接口
 * @Author e-Feilong.Chen
 * @Date 2022/1/7 15:34
 */
public interface BaseUserService {

    /**
     * 分页查询
     * @param page
     * @return
     */
    List<BaseUserDTO> export(BaseUser2QueryPageDTO page);
    /**
     * 根据id查询所有数据
     * @param ids
     * @return
     */
    List<com.goudong.commons.dto.oauth2.BaseUserDTO> findAllById(List<Long> ids);

    /**
     * 批量保存用户
     * @param userPOS
     */
    void saveAll(List<BaseUserPO> userPOS);
}
