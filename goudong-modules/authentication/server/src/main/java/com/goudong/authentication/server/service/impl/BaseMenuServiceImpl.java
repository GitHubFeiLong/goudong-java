package com.goudong.authentication.server.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.goudong.authentication.server.constant.CommonConst;
import com.goudong.authentication.server.domain.BaseMenu;
import com.goudong.authentication.server.repository.BaseMenuRepository;
import com.goudong.authentication.server.rest.req.BaseMenuChangeSortNumReq;
import com.goudong.authentication.server.rest.req.BaseMenuCreateReq;
import com.goudong.authentication.server.rest.req.BaseMenuGetAllReq;
import com.goudong.authentication.server.rest.req.BaseMenuUpdateReq;
import com.goudong.authentication.server.service.BaseMenuService;
import com.goudong.authentication.server.service.dto.BaseMenuDTO;
import com.goudong.authentication.server.service.dto.MyAuthentication;
import com.goudong.authentication.server.service.mapper.BaseMenuMapper;
import com.goudong.authentication.server.util.SecurityContextUtil;
import com.goudong.boot.web.core.ClientException;
import com.goudong.core.util.AssertUtil;
import com.goudong.core.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link BaseMenu}.
 */
@Slf4j
@Service
@Transactional
public class BaseMenuServiceImpl implements BaseMenuService {

    //~fields
    //==================================================================================================================
    @Resource
    private BaseMenuRepository baseMenuRepository;
    @Resource
    private BaseMenuMapper baseMenuMapper;
    @Resource
    private ObjectMapper objectMapper;
    @Resource
    private TransactionTemplate transactionTemplate;
    @Resource
    private JdbcTemplate jdbcTemplate;
    //~methods
    //==================================================================================================================

    /**
     * 查询应用下所有菜单
     *
     * @param appId 应用id
     * @return 菜单集合
     */
    @Override
    public List<BaseMenuDTO> findAllByAppId(Long appId) {
        return baseMenuMapper.toDto(baseMenuRepository.findAllByAppId(appId));
    }

    /**
     * 查询菜单
     *
     * @param id 菜单id
     * @return 菜单
     */
    @Override
    public BaseMenu findById(Long id) {
        assert id!=null;
        MyAuthentication myAuthentication = SecurityContextUtil.get();

        Specification<BaseMenu> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> andPredicateList = new ArrayList<>();
            andPredicateList.add(criteriaBuilder.equal(root.get("appId"), myAuthentication.getRealAppId()));
            andPredicateList.add(criteriaBuilder.equal(root.get("id"), id));
            return criteriaBuilder.and(andPredicateList.toArray(new Predicate[andPredicateList.size()]));
        };
        return baseMenuRepository.findOne(specification).orElseThrow(() -> ClientException.client("菜单不存在:" + id));
    }

    /**
     * 查询菜单
     *
     * @param ids 菜单id集合
     * @return 菜单集合
     */
    @Override
    public List<BaseMenu> findAllById(List<Long> ids) {
        return baseMenuRepository.findAllById(ids);
    }

    /**
     * 查询指定条件下的菜单
     *
     * @param req 条件
     * @return 菜单集合
     */
    @Override
    public List<BaseMenuDTO> findAll(BaseMenuGetAllReq req) {
        Specification<BaseMenu> specification = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> andPredicateList = new ArrayList<>();
            andPredicateList.add(criteriaBuilder.equal(root.get("appId"), req.getAppId()));
            if (StringUtil.isNotBlank(req.getName())) {
                andPredicateList.add(criteriaBuilder.like(root.get("name").as(String.class), "%" + req.getName() + "%"));
            }
            if (StringUtil.isNotBlank(req.getPath())) {
                andPredicateList.add(criteriaBuilder.like(root.get("path").as(String.class), "%" + req.getPath() + "%"));
            }
            if (req.getType() != null) {
                andPredicateList.add(criteriaBuilder.equal(root.get("type"), req.getType()));
            }

            if (StringUtil.isNotBlank(req.getPermissionId())) {
                andPredicateList.add(criteriaBuilder.like(root.get("permissionId").as(String.class), "%" + req.getPermissionId() + "%"));
            }
            return criteriaBuilder.and(andPredicateList.toArray(new Predicate[andPredicateList.size()]));
        };

        return baseMenuMapper.toDto(baseMenuRepository.findAll(specification));
    }

    /**
     * 新增菜单
     *
     * @param req
     * @return
     */
    @Override
    @Transactional
    public BaseMenuDTO save(BaseMenuCreateReq req) {
        BaseMenu baseMenu = BeanUtil.copyProperties(req, BaseMenu.class);
        MyAuthentication myAuthentication = SecurityContextUtil.get();
        baseMenu.setAppId(myAuthentication.getRealAppId());
        // 校验参数
        checkBaseMenuBySave(baseMenu);
        baseMenuRepository.save(baseMenu);
        return baseMenuMapper.toDto(baseMenu);
    }

    /**
     * 更新菜单
     *
     * @param req
     * @return
     */
    @Override
    public BaseMenuDTO update(BaseMenuUpdateReq req) {
        BaseMenu baseMenu = this.findById(req.getId());
        BeanUtil.copyProperties(req, baseMenu);
        // 校验参数
        checkBaseMenuBySave(baseMenu);
        baseMenuRepository.save(baseMenu);
        return baseMenuMapper.toDto(baseMenu);
    }

    /**
     * 删除菜单，如果菜单是父节点，就会删除它及它下面的所有子节点
     *
     * @param id
     * @return
     */
    @Override
    public Boolean deleteById(Long id) {
        // 检查应用
        BaseMenu baseMenu = this.findById(id);

        String sql = "SELECT t3.* FROM (SELECT t1.*, IF ( FIND_IN_SET( parent_id, @pids ) > 0, @pids := CONCAT( @pids, ',', id ), '0' ) AS ischild FROM ( SELECT t.id, t.parent_id, t.NAME FROM base_menu AS t ORDER BY t.id ASC ) t1, ( SELECT @pids := '%s' ) t2 ) t3 WHERE ischild != '0'";
        String format = String.format(sql, id);

        List<Map<String, Object>> maps = jdbcTemplate.queryForList(format);
        List<Long> ids = maps.stream().map(m -> (long) m.get("id")).collect(Collectors.toList());
        ids.add(id);

        return transactionTemplate.execute(status -> {
            try {
                ids.forEach(p -> {
                    baseMenuRepository.deleteById(p);
                });
                return true;
            } catch(Exception e) {
                status.setRollbackOnly();
                log.error("删除菜单时，执行事物异常：{}", e.getMessage());
                throw e;
            }
        });
    }

    /**
     * 修改菜单排序
     *
     * @param req
     * @return
     */
    @Override
    @Transactional
    public Boolean changeSortNum(BaseMenuChangeSortNumReq req) {
        MyAuthentication myAuthentication = SecurityContextUtil.get();
        Long realAppId = myAuthentication.getRealAppId();
        if (req.getBeforeId() != req.getAfterId()) {
            BaseMenu beforeBaseMenu = this.findById(req.getBeforeId());
            BaseMenu afterBaseMenu = this.findById(req.getAfterId());
            Integer minSortNum;
            Integer maxSortNum;
            if (beforeBaseMenu.getSortNum() > afterBaseMenu.getSortNum()) { // 向前拖拽
                minSortNum = afterBaseMenu.getSortNum();
                maxSortNum = beforeBaseMenu.getSortNum();
                baseMenuRepository.updateSortNum(realAppId, beforeBaseMenu.getParentId(), minSortNum, maxSortNum, 1);
            } else { // 向后拖拽
                minSortNum = beforeBaseMenu.getSortNum();
                maxSortNum = afterBaseMenu.getSortNum();
                baseMenuRepository.updateSortNum(realAppId, beforeBaseMenu.getParentId(), minSortNum, maxSortNum, -1);
            }

            beforeBaseMenu.setSortNum(afterBaseMenu.getSortNum());
            return true;
        }
        return true;
    }

    /**
     * 检查菜单
     * @param baseMenu
     */
    private void checkBaseMenuBySave(BaseMenu baseMenu) {
        // 参数校验
        switch (baseMenu.getType()) {
            case 2: // 按钮
                AssertUtil.isNotBlank(baseMenu.getPermissionId(), () -> "权限标识必填");
                baseMenu.setMethod(checkMethod(baseMenu.getMethod()));
                baseMenu.setSortNum(0);
                AssertUtil.isNotNull(baseMenu.getHide(), () -> "菜单是否隐藏必填");
                baseMenu.setMeta(checkMeta(baseMenu.getMeta()));
                break;
            case 1: // 菜单
                AssertUtil.isNotBlank(baseMenu.getPermissionId(), () -> "权限标识必填");
                AssertUtil.isNotBlank(baseMenu.getPath(), () -> "路由地址必填");
                baseMenu.setMethod(null);    // 菜单直接设置null
                AssertUtil.isNotNull(baseMenu.getSortNum(), () -> "菜单排序号码必填");
                AssertUtil.isNotNull(baseMenu.getHide(), () -> "菜单是否隐藏必填");
                baseMenu.setMeta(checkMeta(baseMenu.getMeta()));
                break;
            default: // 接口
                AssertUtil.isNotBlank(baseMenu.getPath(), () -> "路由地址必填");
                AssertUtil.isNotBlank(baseMenu.getMethod(), () -> "请求方式必填");
                baseMenu.setMethod(checkMethod(baseMenu.getMethod()));
                baseMenu.setSortNum(0);
                baseMenu.setHide(false);
                baseMenu.setMethod(checkMeta(baseMenu.getMeta()));
        }

        // 父级校验
        if (baseMenu.getParentId() != null) {
            BaseMenu parentMenu = this.findById(baseMenu.getParentId());
        }

//        List<RoleMember> membersList =  objectMapper.readValue(members, javaType);
//        mapper.getTypeFactory().constructParametricType(HashMap.class,String.class, Bean.class);
//        ObjectMapper mapper = new ObjectMapper();
//        List<Bean> beanList = mapper.readValue(jsonString, new TypeReference<List<Bean>>() {});


    }
    /**
     * 检查method
     * @param method
     * @return  method
     */
    private String checkMethod(String method) {
        if (StringUtil.isNotBlank(method)) {
            try {
                JavaType javaType = objectMapper.getTypeFactory().constructParametricType(List.class, String.class);
                List<String> methods = objectMapper.readValue(method, javaType);
                AssertUtil.isTrue(Arrays.asList(CommonConst.HTTP_METHODS).containsAll(methods), () -> ClientException.client("请求方式配置错误"));
                return method;
            } catch (JsonProcessingException e) {
                throw new RuntimeException("method参数错误", e);
            }
        }

        return "[]";
    }

    /**
     * 检查meta 是否格式是json，并返回meta
     * @param meta
     * @return  meta
     */
    private String checkMeta(String meta) {
        if (StringUtil.isNotBlank(meta)) {
            try {
                objectMapper.readTree(meta);
                return meta;
            } catch (JsonProcessingException e) {
                throw new RuntimeException("菜单元数据meta必须是json", e);
            }
        }
        return "{}";
    }

}
