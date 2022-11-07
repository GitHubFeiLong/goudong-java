package com.goudong.file.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.goudong.commons.constant.core.DateConst;
import com.goudong.commons.dto.oauth2.BaseUserDTO;
import com.goudong.commons.framework.openfeign.GoudongMessageServerService;
import com.goudong.core.util.CollectionUtil;
import com.goudong.file.dto.BaseUser2QueryPageDTO;
import com.goudong.file.po.user.BaseUserPO;
import com.goudong.file.repository.user.BaseUserRepository;
import com.goudong.file.service.BaseUserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * 类描述：
 * 用户服务层
 * @Author e-Feilong.Chen
 * @Date 2022/1/7 15:34
 */
@Service
@RequiredArgsConstructor
public class BaseUserServiceImpl implements BaseUserService {

    /**
     * 用户持久层接口
     */
    private final BaseUserRepository baseUserRepository;

    private final GoudongMessageServerService goudongMessageServerService;

    private final EntityManager entityManager;


    /**
     * 用户分页查询
     *
     * @param page
     * @return
     */
    @Transactional
    @Override
    public List<BaseUserDTO> export(BaseUser2QueryPageDTO page) {
        Specification<BaseUserPO> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> and = new ArrayList<>();
            if (StringUtils.isNotBlank(page.getUsername())) {
                and.add(criteriaBuilder.like(root.get("username"), page.getUsername() + "%"));
            }
            if (StringUtils.isNotBlank(page.getPhone())) {
                and.add(criteriaBuilder.like(root.get("phone"), page.getPhone() + "%"));
            }
            if (StringUtils.isNotBlank(page.getEmail())) {
                and.add(criteriaBuilder.like(root.get("email"), page.getEmail() + "%"));
            }
            if (StringUtils.isNotBlank(page.getNickname())) {
                and.add(criteriaBuilder.like(root.get("nickname"), page.getNickname() + "%"));
            }
            if (page.getStartValidTime() != null && page.getEndValidTime() != null) {
                and.add(criteriaBuilder.greaterThanOrEqualTo(root.get("validTime").as(String.class), page.getStartValidTime().format(DateConst.YYYY_MM_DD_HH_MM_SS)));
                and.add(criteriaBuilder.lessThanOrEqualTo(root.get("validTime").as(String.class), page.getEndValidTime().format(DateConst.YYYY_MM_DD_HH_MM_SS)));
            }
            if (page.getStartCreateTime() != null && page.getEndCreateTime() != null) {
                and.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createTime").as(String.class), page.getStartCreateTime().format(DateConst.YYYY_MM_DD_HH_MM_SS)));
                and.add(criteriaBuilder.lessThanOrEqualTo(root.get("createTime").as(String.class), page.getEndCreateTime().format(DateConst.YYYY_MM_DD_HH_MM_SS)));
            }

            // 分页
            Order weightOrder = criteriaBuilder.desc(root.get("createTime"));
            if (CollectionUtil.isNotEmpty(and)) {
                return query.where(and.toArray(new Predicate[and.size()])).orderBy(weightOrder).getRestriction();
            }

            return query.orderBy(weightOrder).getRestriction();
        };

        // 导出时
        List<BaseUserPO> all = baseUserRepository.findAll(specification);
        List<BaseUserDTO> baseUserDTOS = BeanUtil.copyToList(all, BaseUserDTO.class, CopyOptions.create());

        // 脱敏
        baseUserDTOS.forEach(p->{
            p.setPassword(null);
        });
        return baseUserDTOS;
    }

    /**
     * 根据id查询所有数据
     *
     * @param ids
     * @return
     */
    @Override
    @Transactional
    public List<com.goudong.commons.dto.oauth2.BaseUserDTO> findAllById(List<Long> ids) {
        List<BaseUserPO> allById = baseUserRepository.findAllById(ids);
        return BeanUtil.copyToList(allById, com.goudong.commons.dto.oauth2.BaseUserDTO.class, CopyOptions.create());
    }
}
