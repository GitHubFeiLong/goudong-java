package com.goudong.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.goudong.boot.redis.core.RedisTool;
import com.goudong.boot.web.core.ClientException;
import com.goudong.boot.web.enumerate.ClientExceptionEnum;
import com.goudong.commons.constant.user.RoleConst;
import com.goudong.commons.framework.jpa.MyIdentifierGenerator;
import com.goudong.core.context.GoudongContext;
import com.goudong.core.context.UserContext;
import com.goudong.core.util.CollectionUtil;
import com.goudong.core.util.StringUtil;
import com.goudong.core.util.tree.v2.Tree;
import com.goudong.user.dto.AddMenuReq;
import com.goudong.user.dto.BaseMenuDTO;
import com.goudong.user.dto.BaseMenuPageReq;
import com.goudong.user.dto.InitMenuReq;
import com.goudong.user.enumerate.RedisKeyProviderEnum;
import com.goudong.user.po.BaseMenuPO;
import com.goudong.user.po.BaseRolePO;
import com.goudong.user.repository.BaseMenuRepository;
import com.goudong.user.repository.BaseRoleRepository;
import com.goudong.user.service.BaseMenuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Order;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
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

    private final BaseRoleRepository baseRoleRepository;

    /**
     * 查询所有菜单
     *
     * @return
     */
    public List<BaseMenuDTO> findAll() {
        // 查询redis是否存在，不存在再加锁查询数据库
        Long appId = GoudongContext.get().getAppId();
        List<BaseMenuDTO> menuDTOS = redisTool.getList(RedisKeyProviderEnum.MENU_ALL, BaseMenuDTO.class, appId);
        if (CollectionUtil.isNotEmpty(menuDTOS)) {
            return BeanUtil.copyToList(menuDTOS, BaseMenuDTO.class, CopyOptions.create());
        }
        synchronized (this) {
            menuDTOS = redisTool.getList(RedisKeyProviderEnum.MENU_ALL, BaseMenuDTO.class, appId);
            if (CollectionUtil.isNotEmpty(menuDTOS)) {
                return BeanUtil.copyToList(menuDTOS, BaseMenuDTO.class, CopyOptions.create());
            }
            // 查询数据库
            List<BaseMenuPO> menus = baseMenuRepository.findAllByAppId(appId);
            if (CollectionUtil.isNotEmpty(menus)) {
                menuDTOS = BeanUtil.copyToList(menus, BaseMenuDTO.class, CopyOptions.create());
                redisTool.set(RedisKeyProviderEnum.MENU_ALL, menuDTOS, appId);
                return BeanUtil.copyToList(menuDTOS, BaseMenuDTO.class, CopyOptions.create());
            }
        }

        return new ArrayList<>(0);
    }

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

        Example<BaseMenuPO> of = Example.of(baseMenuPO);
        List<BaseMenuPO> sysMenus = baseMenuRepository.findAll(of);

        // 将菜单转成map，
        Map<String, BaseMenuPO> map = sysMenus.stream()
                // 转成map
                .collect(Collectors.toMap(p -> key(p), p -> p, (k1, k2) -> k1));


        // 将前端传递的菜单进行转成一维，并填充id和parentId属性建立父子关系
        List<BaseMenuPO> pos = new ArrayList<>();
        AtomicInteger sortNumAtomic = new AtomicInteger(1);
        req.stream().forEach(p -> {
            convert(sortNumAtomic, p, null, map, pos);
        });

        // 多余的菜单id（需要删除）
        List<Long> needlessMenuIds = new ArrayList<>();
        Date now = new Date();
        Long userId = UserContext.get().getId();

        pos.stream().forEach(p->{
            // 获取原始菜单
            String key = key(p);
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

        // 查询admin拥有的角色
        BaseRolePO baseRolePO = baseRoleRepository.findById(1L).orElseThrow(()-> ClientException.client(ClientExceptionEnum.NOT_FOUND, "管理员角色不存在"));
        // 修改角色
        List<BaseMenuPO> addMenus = CollectionUtil.subtract(pos, baseRolePO.getMenus()).stream().collect(Collectors.toList());
        baseRolePO.setMenus(addMenus);

        // 删除redis中的所有菜单，和redis中角色的菜单权限。
        redisTool.deleteKeysByPattern(RedisKeyProviderEnum.MENU_ROLE);

        return BeanUtil.copyToList(pos, BaseMenuDTO.class, CopyOptions.create());
    }

    /**
     * 创建唯一标识
     * @param po
     * @return
     */
    private String key(BaseMenuPO po) {
        if (StringUtil.isNotBlank(po.getPermissionId())) {
            return po.getPermissionId();
        }
        StringBuilder sb = new StringBuilder()
                .append(Optional.ofNullable(po.getType()).orElseGet(() -> -1))
                .append("-").append(Optional.ofNullable(po.getOpenModel()).orElseGet(() -> -1))
                .append("-").append(Optional.ofNullable(po.getPath()).orElseGet(() -> ""))
                .append("-").append(Optional.ofNullable(po.getComponentPath()).orElseGet(() -> ""))
                .append("-").append(Optional.ofNullable(po.getMethod()).orElseGet(() -> "").toUpperCase())
                ;

        return sb.toString();
    }

    /**
     * 创建唯一标识
     * @param type
     * @param openModel
     * @param path
     * @param componentPath
     * @param method
     * @param permissionId
     * @return
     */
    private String key(Integer type, Integer openModel, String path, String componentPath, String method, String permissionId) {
        if (StringUtil.isNotBlank(permissionId)) {
            return permissionId;
        }
        StringBuilder sb = new StringBuilder()
                .append(Optional.ofNullable(type).orElseGet(() -> -1))
                .append("-").append(Optional.ofNullable(openModel).orElseGet(() -> -1))
                .append("-").append(Optional.ofNullable(path).orElseGet(() -> ""))
                .append("-").append(Optional.ofNullable(componentPath).orElseGet(() -> ""))
                .append("-").append(Optional.ofNullable(method).orElseGet(() -> "").toUpperCase())
                ;

        return sb.toString();
    }


    public List<BaseMenuDTO> findAllByRoleName(String role) {
        // 匿名角色，不需要查询权限
        if(RoleConst.ROLE_ANONYMOUS.equals(role)) {
            return new ArrayList<>(0);
        }
        // 查询redis是否存在，不存在再加锁查询数据库
        List<BaseMenuDTO> menuDTOS = redisTool.getList(RedisKeyProviderEnum.MENU_ROLE, BaseMenuDTO.class, GoudongContext.get().getAppId(),role);
        if (CollectionUtil.isNotEmpty(menuDTOS)) {
            return menuDTOS;
        }
        synchronized (this) {
            menuDTOS = redisTool.getList(RedisKeyProviderEnum.MENU_ROLE, BaseMenuDTO.class, GoudongContext.get().getAppId(), role);
            if (CollectionUtil.isNotEmpty(menuDTOS)) {
                return menuDTOS;
            }
            // 查询数据库
            BaseRolePO baseRolePO = baseRoleRepository.findByAppIdAndRoleName(GoudongContext.get().getAppId(), role)
                    .orElseThrow(() -> ClientException.client(ClientExceptionEnum.BAD_REQUEST, "参数错误，角色不存在"));
            List<BaseMenuPO> menus = baseRolePO.getMenus();

            if (CollectionUtil.isNotEmpty(menus)) {
                menuDTOS = BeanUtil.copyToList(menus, BaseMenuDTO.class, CopyOptions.create());

                redisTool.set(RedisKeyProviderEnum.MENU_ROLE, menuDTOS, GoudongContext.get().getAppId(), role);

                return menuDTOS;
            }
        }

        return new ArrayList<>(0);
    }

    /**
     * 查询指定role的菜单资源
     *
     * @param roles
     * @return
     */
    // @Override
    @Transactional
    public List<BaseMenuDTO> findAllByRoleNames(final List<String> roles) {
        List<BaseMenuDTO> all = new ArrayList<>();
        roles.stream().forEach(role -> {
            all.addAll(findAllByRoleName(role));
        });

        // 进行去重
        List<BaseMenuDTO> values = all.stream()
                .collect(Collectors.toMap(k -> k.getId(), p -> p, (k1, k2) -> k1))
                .values()
                .stream()
                // 排序下
                .sorted(new Comparator<BaseMenuDTO>() {
                    @Override
                    public int compare(BaseMenuDTO o1, BaseMenuDTO o2) {
                        // 返回正数：升序，返回负数 降序
                        return o1.getId() > o2.getId() ? 1 : -1 ;
                    }
                })
                .collect(Collectors.toList());

        return values;
    }

    /**
     * 分页查询菜单
     *
     * @param req
     * @return
     */
    @Override
    public List<BaseMenuDTO> listByTree(BaseMenuPageReq req) {
        Specification<BaseMenuPO> specification = (root, query, criteriaBuilder) -> {
            Order weightOrder = criteriaBuilder.asc(root.get("sortNum"));
            return query.orderBy(weightOrder).getRestriction();
        };
        List<BaseMenuPO> all = baseMenuRepository.findAll(specification);
        List<BaseMenuDTO> menuDTOS = BeanUtil.copyToList(all, BaseMenuDTO.class, CopyOptions.create());
        List<BaseMenuDTO> tree = Tree.getInstance().id(BaseMenuDTO::getId)
                .parentId(BaseMenuDTO::getParentId)
                .children(BaseMenuDTO::getChildren)
                .toTree(menuDTOS);
        return tree;
    }

    /**
     * 新增菜单
     *
     * @param req
     * @return
     */
    @Override
    public BaseMenuDTO addMenu(AddMenuReq req) {
        // 参数校验
        req.check();
        BaseMenuPO po = BeanUtil.copyProperties(req, BaseMenuPO.class);
        baseMenuRepository.save(po);
        return BeanUtil.copyProperties(po, BaseMenuDTO.class);
    }

    /**
     * 将 InitMenuReq 对象转换成 BaseMenuPO 并放入集合中
     * 如果原始菜单存在相同数据，那么不创建新的id，只是修改
     *
     * @param sortNumAtomic  排序值
     * @param req 前端传递的菜单参数
     * @param parentId 该菜单的父id
     * @param map 数据库中原始系统菜单
     * @param pos 转换后对象放入的容器
     */
    private void convert(AtomicInteger sortNumAtomic, InitMenuReq req, Long parentId, Map<String, BaseMenuPO> map, List<BaseMenuPO> pos) {
        BaseMenuPO po = new BaseMenuPO();

        String key = key(req.getType(), req.getOpenModel(), req.getPath(), req.getComponentPath(), req.getMethod(), req.getPermissionId());
        BaseMenuPO poByMap = map.get(key);
        po.setHide(false);
        po.setSortNum(sortNumAtomic.getAndIncrement());
        po.setAppId(GoudongContext.get().getAppId());
        if (poByMap != null) {  // 原始菜单中有时，就需要修改
            // 拷贝其他属性
            BeanUtil.copyProperties(poByMap, po);
        } else {
            BeanUtil.copyProperties(req, po);
            po.setId(MyIdentifierGenerator.ID.nextId());
        }

        po.setParentId(parentId);
        // 添加到集合
        pos.add(po);

        if (CollectionUtil.isNotEmpty(req.getChildren())) {
            req.getChildren().stream().forEach(p->{
                // 递归添加
                convert(sortNumAtomic, p, po.getId(), map, pos);
            });
        }
    }


    /**
     * 获取匹配的所有key，使用scan避免阻塞
     *
     * @param patten 匹配keys的规则
     * @return 返回获取到的keys
     */
    public static Set<String> getKeysByScan(RedisTemplate redisTemplate, String patten) {
        return (Set<String>) redisTemplate.execute(connect -> {
            Set<String> binaryKeys = new HashSet<>();
            Cursor<byte[]> cursor = connect.scan(new ScanOptions.ScanOptionsBuilder().match(patten).count(200000).build());
            while (cursor.hasNext() && binaryKeys.size() < 200000) {
                binaryKeys.add(new String(cursor.next()));
            }
            return binaryKeys;
        }, true);
    }
}
