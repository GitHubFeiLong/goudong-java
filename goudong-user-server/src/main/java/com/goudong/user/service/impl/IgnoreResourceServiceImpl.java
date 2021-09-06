package com.goudong.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.goudong.commons.dto.BaseIgnoreResourceDTO;
import com.goudong.commons.enumerate.RedisKeyEnum;
import com.goudong.commons.po.BaseIgnoreResourcePO;
import com.goudong.commons.utils.BeanUtil;
import com.goudong.commons.utils.RedisOperationsUtil;
import com.goudong.user.mapper.IgnoreResourceMapper;
import com.goudong.user.service.IgnoreResourceService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

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
