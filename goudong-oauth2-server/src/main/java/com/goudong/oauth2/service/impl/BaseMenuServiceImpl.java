package com.goudong.oauth2.service.impl;

import com.goudong.oauth2.repository.BaseMenuRepository;
import com.goudong.oauth2.service.BaseMenuService;
import org.springframework.stereotype.Service;

/**
 * 类描述：
 * 菜单接口的实现类
 * @author msi
 * @version 1.0
 * @date 2022/1/23 9:12
 */
@Service
public class BaseMenuServiceImpl implements BaseMenuService {

    //~fields
    //==================================================================================================================
    private final BaseMenuRepository baseMenuRepository;


    //~methods
    //==================================================================================================================

    public BaseMenuServiceImpl(BaseMenuRepository baseMenuRepository) {
        this.baseMenuRepository = baseMenuRepository;
    }


}