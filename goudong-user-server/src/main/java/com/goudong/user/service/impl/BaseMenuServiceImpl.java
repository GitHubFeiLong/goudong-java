package com.goudong.user.service.impl;

import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.IdUtil;
import com.goudong.commons.dto.oauth2.BaseMenuDTO;
import com.goudong.commons.enumerate.core.ClientExceptionEnum;
import com.goudong.commons.exception.ClientException;
import com.goudong.commons.framework.redis.RedisTool;
import com.goudong.commons.utils.core.BeanUtil;
import com.goudong.user.dto.InitMenuReq;
import com.goudong.user.po.BaseMenuPO;
import com.goudong.user.repository.BaseMenuRepository;
import com.goudong.user.service.BaseMenuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 类描述：
 * 菜单服务层实现类
 * @Author e-Feilong.Chen
 * @Date 2022/1/7 15:34
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BaseMenuServiceImpl implements BaseMenuService {

    private final BaseMenuRepository baseMenuRepository;

    private final RedisTool redisTool;

    /**
     * 初始化
     *
     * @param req
     * @return
     */
    @Override
    public List<BaseMenuDTO> init(List<InitMenuReq> req) {

        long count = baseMenuRepository.countBySys(true);
        if (count > 1) {
            throw ClientException.clientException(ClientExceptionEnum.FORBIDDEN, "请勿重复初始菜单");
        }

        List<BaseMenuPO> pos = new ArrayList<>();
        req.stream().forEach(p -> {
            convert(p, null, pos);
        });

        log.info(pos.toString());

        // 设置成系统菜单
        pos.stream().forEach(m->m.setSys(true));

        baseMenuRepository.saveAll(pos);

        redisTool.delete("goudong-oauth2-server:menu:ALL");
        return BeanUtil.copyToList(pos, BaseMenuDTO.class, CopyOptions.create());
    }

    /**
     * 将 InitMenuReq 对象转换成 BaseMenuPO 并放入集合中
     * @param req 前端传递的菜单参数
     * @param parentId 该菜单的父id
     * @param pos 转换后对象放入的容器
     */
    private void convert(InitMenuReq req, Long parentId, List<BaseMenuPO> pos) {
        BaseMenuPO po = new BaseMenuPO();
        po.setId(IdUtil.getSnowflake(1,1).nextId());
        po.setName(req.getName());
        po.setApi(req.getApi());
        po.setPath(req.getPath());
        if (StringUtils.isNotBlank(req.getMethod())) {
            po.setMethod(req.getMethod().toUpperCase());
        }
        po.setParentId(parentId);
        // 添加到集合
        pos.add(po);

        if (CollectionUtils.isNotEmpty(req.getChildren())) {
            req.getChildren().stream().forEach(p->{
                // 递归添加
                convert(p, po.getId(), pos);
            });
        }
    }
}
