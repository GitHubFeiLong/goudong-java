package com.goudong.oauth2.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.goudong.boot.web.core.BasicException;
import com.goudong.boot.web.core.ClientException;
import com.goudong.boot.web.util.PageResultConvert;
import com.goudong.commons.constant.core.DateConst;
import com.goudong.commons.framework.jpa.MyIdentifierGenerator;
import com.goudong.core.lang.PageResult;
import com.goudong.core.util.AssertUtil;
import com.goudong.core.util.CollectionUtil;
import com.goudong.core.util.StringUtil;
import com.goudong.oauth2.dto.BaseAppApplyReq;
import com.goudong.oauth2.dto.BaseAppAuditReq;
import com.goudong.oauth2.dto.BaseAppDTO;
import com.goudong.oauth2.dto.BaseAppQueryReq;
import com.goudong.oauth2.po.BaseAppPO;
import com.goudong.oauth2.po.BaseUserPO;
import com.goudong.oauth2.repository.BaseAppRepository;
import com.goudong.oauth2.service.BaseAppService;
import com.goudong.oauth2.util.AppIdUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * 类描述：
 * 应用接口实现
 * @author cfl
 * @version 1.0
 * @date 2023/6/10 22:50
 */
@Service
public class BaseAppServiceImpl implements BaseAppService {
    //~fields
    //==================================================================================================================
    @Resource
    private BaseAppRepository baseAppRepository;
    //~methods
    //==================================================================================================================
    /**
     * 申请应用
     *
     * @param req
     * @return
     */
    @Override
    @Transactional
    public BaseAppDTO apply(BaseAppApplyReq req) {
        BaseAppPO po = new BaseAppPO();
        Long id = MyIdentifierGenerator.ID.nextId();
        po.setAppId(AppIdUtil.getAppId(id));
        po.setAppSecret(AppIdUtil.getAppSecret());
        po.setAppName(req.getAppName().trim());
        po.setStatus(BaseAppPO.StatusEnum.CHECK_PENDING.getId());
        po.setRemark(req.getRemark());
        po.setId(id);
        // 获取当前用户
        BaseUserPO authentication = (BaseUserPO) SecurityContextHolder.getContext().getAuthentication();
        Long userId = authentication.getId();
        po.setCreateUserId(userId);
        po.setUpdateUserId(userId);

        baseAppRepository.save(po);
        return BeanUtil.copyProperties(po, BaseAppDTO.class);
    }

    /**
     * 删除应用
     *
     * @param id
     * @return
     */
    @Override
    public boolean deleteById(Long id) {
        BaseAppPO po = baseAppRepository.findById(id).orElseThrow(() -> BasicException.client("应用不存在"));

        // 获取当前用户
        BaseUserPO authentication = (BaseUserPO) SecurityContextHolder.getContext().getAuthentication();

        // 只能删除自己创建的
        AssertUtil.isEquals(authentication.getId(), po.getCreateUserId(), () -> ClientException.clientByForbidden());

        baseAppRepository.delete(po);
        return true;
    }

    /**
     * 审核应用
     *
     * @param req
     * @return
     */
    @Override
    @Transactional
    public BaseAppDTO audit(BaseAppAuditReq req) {
        BaseAppPO po = baseAppRepository.findById(req.getId()).orElseThrow(() -> BasicException.client("应用不存在"));

        // 获取当前用户
        BaseUserPO authentication = (BaseUserPO) SecurityContextHolder.getContext().getAuthentication();
        po.setUpdateUserId(authentication.getId());
        po.setUpdateUserId(authentication.getId());
        po.setRemark(req.getRemark());
        po.setStatus(req.getStatus());

        return BeanUtil.copyProperties(po, BaseAppDTO.class);
    }

    /**
     * 查询应用
     *
     * @param req
     * @return
     */
    @Override
    public PageResult<BaseAppDTO> query(BaseAppQueryReq req) {
        // 获取当前用户
        BaseUserPO authentication = (BaseUserPO) SecurityContextHolder.getContext().getAuthentication();


        Specification<BaseAppPO> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> and = new ArrayList<>();

            if (!authentication.isAdmin()) {
                and.add(criteriaBuilder.equal(root.get("createUserId"), authentication.getId()));
            }

            if (StringUtil.isNotBlank(req.getAppName())) {
                and.add(criteriaBuilder.like(root.get("appName"), "%" + req.getAppName() + "%"));
            }

            if (req.getStatus() != null) {
                and.add(criteriaBuilder.equal(root.get("status"), req.getStatus()));
            }

            if (req.getStartCreateTime() != null && req.getEndCreateTime() != null) {
                and.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createTime").as(String.class), req.getStartCreateTime().format(DateConst.YYYY_MM_DD_HH_MM_SS)));
                and.add(criteriaBuilder.lessThanOrEqualTo(root.get("createTime").as(String.class), req.getEndCreateTime().format(DateConst.YYYY_MM_DD_HH_MM_SS)));
            }

            // 分页
            Order weightOrder = criteriaBuilder.desc(root.get("createTime"));
            if (CollectionUtil.isNotEmpty(and)) {
                return query.where(and.toArray(new Predicate[and.size()])).orderBy(weightOrder).getRestriction();
            }

            return query.orderBy(weightOrder).getRestriction();
        };

        PageResult<BaseAppDTO> convert = null;
        if (req.getJPAPage() != null && req.getSize() != null) {
            PageRequest pageRequest = PageRequest.of(req.getJPAPage(), req.getSize());
            Page<BaseAppPO> all = baseAppRepository.findAll(specification, pageRequest);
            convert = PageResultConvert.convert(all, BaseAppDTO.class);
        } else {
            // // 导出时
            // List<BaseUserPO> all = baseUserRepository.findAll(specification);
            // List<com.goudong.commons.dto.oauth2.BaseUserDTO> baseUserDTOS = BeanUtil.copyToList(all, com.goudong.commons.dto.oauth2.BaseUserDTO.class, CopyOptions.create());
            // convert = new PageResult<>(baseUserDTOS);
        }


        return convert;
    }
}
