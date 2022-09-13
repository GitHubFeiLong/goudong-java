package com.goudong.user.service;

import com.goudong.commons.dto.oauth2.BaseMenuDTO;
import com.goudong.user.dto.InitMenuReq;

import java.util.List;

/**
 * 接口描述：
 * 菜单
 * @Author e-Feilong.Chen
 * @Date 2022/1/7 15:34
 */
public interface BaseMenuService {

    /**
     * 初始化
     * @param req
     * @return
     */
    List<BaseMenuDTO> init(List<InitMenuReq> req);
}
