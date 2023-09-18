package com.goudong.authentication.server.service.manager.impl;

import com.goudong.authentication.server.rest.req.BaseMenuGetAllReq;
import com.goudong.authentication.server.rest.resp.BaseMenuGetAllResp;
import com.goudong.authentication.server.service.BaseMenuService;
import com.goudong.authentication.server.service.dto.BaseMenuDTO;
import com.goudong.authentication.server.service.dto.MyAuthentication;
import com.goudong.authentication.server.service.manager.BaseMenuManagerService;
import com.goudong.authentication.server.util.SecurityContextUtil;
import com.goudong.core.util.CollectionUtil;
import com.goudong.core.util.tree.v2.Tree;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;

/**
 * 类描述：
 * 应用管理服务接口实现类
 * @author cfl
 * @version 1.0
 */
@Service
public class BaseMenuManagerServiceImpl implements BaseMenuManagerService {

    //~fields
    //==================================================================================================================
    @Resource
    private BaseMenuService baseMenuService;

    //~methods
    //==================================================================================================================
    /**
     * 查询所有菜单
     * @param req 查询条件
     * @return 树形结构的菜单
     */
    @Override
    public BaseMenuGetAllResp getAll(BaseMenuGetAllReq req) {
        MyAuthentication myAuthentication = SecurityContextUtil.get();
        Long appId = myAuthentication.getRealAppId();
        List<BaseMenuDTO> allByAppId = baseMenuService.findAllByAppId(appId);
        BaseMenuGetAllResp baseMenuGetAllResp = new BaseMenuGetAllResp();
        if (CollectionUtil.isEmpty(allByAppId)) {
            return baseMenuGetAllResp;
        }
        // 排序
        allByAppId.sort(new Comparator<BaseMenuDTO>() {
            @Override
            public int compare(BaseMenuDTO o1, BaseMenuDTO o2) {
                return o1.getSortNum().compareTo(o2.getSortNum());
            }
        });
        List<BaseMenuDTO> tree = Tree.getInstance().toTree(allByAppId);
        baseMenuGetAllResp.setRecords(tree);
        return baseMenuGetAllResp;
    }
}
