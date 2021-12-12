package com.goudong.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.goudong.commons.po.BaseIgnoreResourcePO;
import com.goudong.user.mapper.IgnoreResourceMapper;
import com.goudong.user.service.IgnoreResourceService;
import org.springframework.stereotype.Service;

/**
 * 类描述：
 * 白名单服务层实现
 * @Author msi
 * @Date 2021-08-14 11:50
 * @Version 1.0
 */
@Service
public class IgnoreResourceServiceImpl extends ServiceImpl<IgnoreResourceMapper, BaseIgnoreResourcePO> implements IgnoreResourceService {
}
