package com.goudong.oauth2.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.lang.UUID;
import com.goudong.boot.web.core.BasicException;
import com.goudong.boot.web.core.ClientException;
import com.goudong.commons.framework.jpa.MyIdentifierGenerator;
import com.goudong.core.util.AssertUtil;
import com.goudong.oauth2.dto.BaseAppApplyReq;
import com.goudong.oauth2.dto.BaseAppAuditReq;
import com.goudong.oauth2.dto.BaseAppDTO;
import com.goudong.oauth2.dto.BaseAppQueryReq;
import com.goudong.oauth2.po.BaseAppPO;
import com.goudong.oauth2.po.BaseUserPO;
import com.goudong.oauth2.repository.BaseAppRepository;
import com.goudong.oauth2.service.BaseAppService;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
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
        String appId = "gd" + Long.toHexString(id);
        po.setAppId(appId);
        po.setAppSecret(UUID.randomUUID(true).toString(true));
        po.setAppName(req.getAppName());
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
    public List<BaseAppDTO> query(BaseAppQueryReq req) {
        // 获取当前用户
        BaseUserPO authentication = (BaseUserPO) SecurityContextHolder.getContext().getAuthentication();
        // 查询所有系统菜单
        BaseAppPO po = new BaseAppPO();
        po.setCreateUserId(authentication.getId());
        po.setAppName(req.getAppName());
        po.setStatus(req.getStatus());

        //实例化对象
        ExampleMatcher matching = ExampleMatcher.matching();
        //设置搜索的字段(实体的属性名)
        matching = matching.withMatcher("appName", ExampleMatcher.GenericPropertyMatchers.contains());
        //最后用这个构造获取Example
        Example<BaseAppPO> example = Example.of(po, matching);

        // 排序规则(实体的属性名)
        Sort sort = Sort.by(Sort.Direction.DESC,"createTime");

        List<BaseAppPO> pos = baseAppRepository.findAll(example, sort);

        return BeanUtil.copyToList(pos, BaseAppDTO.class, CopyOptions.create());
    }
}
