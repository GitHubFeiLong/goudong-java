package com.goudong.user.service.impl;

import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.IdUtil;
import com.goudong.commons.core.context.UserContext;
import com.goudong.commons.dto.oauth2.BaseMenuDTO;
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
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    // private final BaseRoleMenuRepository baseRoleMenuRepository;

    /**
     * 初始化
     * @// TODO: 2022/9/18 需要给管理员加上权限
     * @param req
     * @return
     */
    @Override
    @Transactional
    public List<BaseMenuDTO> init(List<InitMenuReq> req) {

        // 查询所有系统菜单
        BaseMenuPO baseMenuPO = new BaseMenuPO();
        baseMenuPO.setSys(true);
        Example<BaseMenuPO> of = Example.of(baseMenuPO);
        List<BaseMenuPO> sysMenus = baseMenuRepository.findAll(of);

        // 将菜单转成map，
        Map<String, BaseMenuPO> map = sysMenus.stream()
                // 转成map
                .collect(Collectors.toMap(p -> p.getPath() + "#" + String.valueOf(p.getMethod()).toUpperCase(), p -> p, (k1, k2) -> k1));


        // 将前端传递的菜单进行转成一维，并填充id和parentId属性建立父子关系
        List<BaseMenuPO> pos = new ArrayList<>();
        req.stream().forEach(p -> {
            convert(p, null, map, pos);
        });

        // 设置成系统菜单
        pos.stream().forEach(m->m.setSys(true));

        // 多余的菜单id（需要删除）
        List<Long> needlessMenuIds = new ArrayList<>();
        Date now = new Date();
        Long userId = UserContext.get().getId();

        pos.stream().forEach(p->{
            // 获取原始菜单
            String key = p.getPath() + "#" + String.valueOf(p.getMethod()).toUpperCase();
            BaseMenuPO poByMap = map.get(key);
            if (poByMap != null) { // 原始菜单中有时，就需要修改
                // 其他子节点也需要修改父节点id
                p.setUpdateTime(now);
                p.setUpdateUserId(userId);
                // 使用过了就删除,最后map中剩下的就是数据库中多余的，
                map.remove(key);
            }
        });

        // 原始中多余的菜单进行删除
        if (!map.isEmpty()) {
            map.forEach((k,v)->{
                needlessMenuIds.add(v.getId());
            });

            // 删除角色菜单中间表
            baseMenuRepository.deleteRoleMenu(needlessMenuIds);
            // 删除菜单表
            baseMenuRepository.deleteByIdIn(needlessMenuIds);
        }

        // 批量保存或更新
        baseMenuRepository.saveAll(pos);

        redisTool.delete("goudong-oauth2-server:menu:ALL");
        return BeanUtil.copyToList(pos, BaseMenuDTO.class, CopyOptions.create());
    }

    /**
     * 将 InitMenuReq 对象转换成 BaseMenuPO 并放入集合中
     * 如果原始菜单存在相同数据，那么不创建新的id，只是修改
     *
     * @param req 前端传递的菜单参数
     * @param parentId 该菜单的父id
     * @param map 数据库中原始系统菜单
     * @param pos 转换后对象放入的容器
     */
    private void convert(InitMenuReq req, Long parentId, Map<String, BaseMenuPO> map, List<BaseMenuPO> pos) {
        BaseMenuPO po = new BaseMenuPO();

        String key = req.getPath() + "#" + String.valueOf(req.getMethod()).toUpperCase();
        BaseMenuPO poByMap = map.get(key);
        if (poByMap != null) {  // 原始菜单中有时，就需要修改
            // 拷贝其他属性
            BeanUtil.copyProperties(poByMap, po, "name", "path", "api", "method");
        } else {
            po.setId(IdUtil.getSnowflake(1,1).nextId());
        }
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
                convert(p, po.getId(), map, pos);
            });
        }
    }

}
