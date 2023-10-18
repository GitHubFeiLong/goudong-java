package com.goudong.authentication.server.easyexcel.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ListUtils;
import com.goudong.authentication.server.config.MyIdentifierGenerator;
import com.goudong.authentication.server.domain.BaseMenu;
import com.goudong.authentication.server.domain.BaseRole;
import com.goudong.authentication.server.easyexcel.template.BaseMenuImportExcelTemplate;
import com.goudong.authentication.server.easyexcel.template.BaseRoleImportExcelTemplate;
import com.goudong.authentication.server.service.BaseMenuService;
import com.goudong.authentication.server.service.BaseRoleService;
import com.goudong.authentication.server.service.dto.BaseMenuDTO;
import com.goudong.authentication.server.service.dto.MyAuthentication;
import com.goudong.boot.web.core.ClientException;
import com.goudong.boot.web.enumerate.ClientExceptionEnum;
import com.goudong.core.context.GoudongContext;
import com.goudong.core.util.AssertUtil;
import com.goudong.core.util.CollectionUtil;
import com.goudong.core.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 类描述：
 * 菜单导入excel监听器
 * @author chenf
 * @version 1.0
 */
@Slf4j
public class BaseMenuImportExcelListener implements ReadListener<BaseMenuImportExcelTemplate> {
    //~fields
    //==================================================================================================================
    /**
     * 缓存的数据
     */
    private List<BaseMenu> cachedDataList = new ArrayList<>();

    /**
     * 用户信息
     */
    private MyAuthentication myAuthentication;

    /**
     * 角色服务
     */
    private BaseMenuService baseMenuService;

    /**
     * 实物
     */
    private TransactionTemplate transactionTemplate;

    /**
     * 导入的菜单类型
     */
    private final Map<String, Integer> types;

    /**
     * 导入的菜单类型
     */
    private final List<String> methods = ListUtils.newArrayList("GET","POST","PUT","DELETE","HEAD","PATCH","OPTIONS","TRACE");


    //~methods
    //==================================================================================================================
    public BaseMenuImportExcelListener(MyAuthentication myAuthentication,
                                       BaseMenuService baseMenuService,
                                       TransactionTemplate transactionTemplate) {
        this.myAuthentication = myAuthentication;
        this.baseMenuService = baseMenuService;
        this.transactionTemplate = transactionTemplate;
        this.types = new HashMap<>(3);
        this.types.put("菜单", 1);
        this.types.put("按钮", 2);
        this.types.put("接口", 3);
    }

    /**
     * When analysis one row trigger invoke function.
     *
     * @param data    one row value. Is is same as {@link AnalysisContext#readRowHolder()}
     * @param context analysis context
     */
    @Override
    public void invoke(BaseMenuImportExcelTemplate data, AnalysisContext context) {
        log.debug("解析到一条数据:{}", data);
        BaseMenu baseMenu = new BaseMenu();
        // 校验参数
        AssertUtil.isNotBlank(data.getPermissionId(), () -> ClientException.client("权限标识必填"));
        AssertUtil.isNotBlank(data.getName(), () -> ClientException.client("菜单名称必填"));
        AssertUtil.isTrue(types.containsKey(data.getType()), () -> ClientException.client("未知菜单类型：" + data.getType()));
        if (StringUtil.isNotBlank(data.getMethod())) {
            AssertUtil.isTrue(methods.contains(data.getMethod()), () -> ClientException.client("未知请求方式：" + data.getMethod()));
            // 拼接成json
            baseMenu.setMethod("[\"" + data.getMethod() + "\"]");
        }

        if (StringUtil.isNotBlank(data.getMeta())) {
            AssertUtil.isTrue(Pattern.compile("^\\s*\\{.*\\}\\s*$").matcher(data.getMeta()).matches(), () -> ClientException.client("meta 格式错误"));
            // 去掉空格
            baseMenu.setMeta(data.getMeta().replaceAll("\\s+", ""));
        }


        long id = MyIdentifierGenerator.ID.nextId();
        baseMenu.setId(id);
        baseMenu.setAppId(myAuthentication.getRealAppId());
        baseMenu.setPermissionId(data.getPermissionId());
        baseMenu.setParentPermissionId(data.getParentPermissionId());
        baseMenu.setName(data.getName());
        baseMenu.setType(types.get(data.getType()));
        baseMenu.setPath(data.getPath());
        baseMenu.setRemark(data.getRemark());
        baseMenu.setHide(false);

        cachedDataList.add(baseMenu);
    }

    /**
     * if have something to do after all analysis
     *
     * @param context
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // 校验所有上级菜单标识在本应用存在
        Map<String, BaseMenu> map2 = new HashMap<>();
        List<String> parentPermissionIds = new ArrayList<>();
        for (BaseMenu baseMenu : cachedDataList) {
            map2.put(baseMenu.getPermissionId(), baseMenu);

            if (StringUtil.isNotBlank(baseMenu.getParentPermissionId())) {
                parentPermissionIds.add(baseMenu.getParentPermissionId());
            }
        }

        // 查询所有菜单
        List<BaseMenuDTO> allMenuDTOList = baseMenuService.findAllByAppId(myAuthentication.getRealAppId());
        Map<String, BaseMenuDTO> map1 = new HashMap<>();
        AtomicInteger sortNumAtomicInteger = new AtomicInteger(0);
        for (BaseMenuDTO baseMenuDTO : allMenuDTOList) {
            map1.put(baseMenuDTO.getPermissionId(), baseMenuDTO);
            // 找最大的排序号
            if (baseMenuDTO.getSortNum() > sortNumAtomicInteger.get()) {
                sortNumAtomicInteger.set(baseMenuDTO.getSortNum());
            }
        }

        // 填充id和
        map2.forEach((k,v)->{

        });

        // 将菜单转成map，
        Map<String, BaseMenu> map = cachedDataList.stream()
                // 转成map
                .collect(Collectors.toMap(BaseMenu::getPermissionId, p -> p, (k1, k2) -> k1));


        // 将前端传递的菜单进行转成一维，并填充id和parentId属性建立父子关系
        List<BaseMenuPO> pos = new ArrayList<>();
        AtomicInteger sortNumAtomic = new AtomicInteger(1);
        req.stream().forEach(p -> {
            convert(sortNumAtomic, p, null, map, pos);
        });

        // 多余的菜单id（需要删除）
        List<Long> needlessMenuIds = new ArrayList<>();
        Date now = new Date();
        Long userId = GoudongContext.get().getUserId();

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


        updateData();
        log.info("所有数据解析完成！");
    }

    /**
     * 加上存储数据库
     */
    private void updateData() {
        if (CollectionUtils.isNotEmpty(cachedDataList)) {
            transactionTemplate.execute(status -> {
                try {
                    baseMenuService.saveAll(cachedDataList);
                    return true;
                }catch (Exception e) {
                    status.setRollbackOnly();
                    // 原封不动抛出，数据库异常才能捕获自定义
                    throw e;
                }
            });
        }

    }

}
