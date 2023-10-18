package com.goudong.authentication.server.easyexcel.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ListUtils;
import com.goudong.authentication.server.domain.BaseRole;
import com.goudong.authentication.server.easyexcel.template.BaseRoleImportExcelTemplate;
import com.goudong.authentication.server.service.BaseRoleService;
import com.goudong.authentication.server.service.dto.MyAuthentication;
import com.goudong.boot.web.core.ClientException;
import com.goudong.core.util.AssertUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * 类描述：
 * 角色导入excel监听器
 * @author chenf
 * @version 1.0
 */
@Slf4j
public class BaseRoleImportExcelListener implements ReadListener<BaseRoleImportExcelTemplate> {
    //~fields
    //==================================================================================================================
    /**
     * 每隔5条存储数据库，实际使用中可以100条，然后清理list ，方便内存回收
     */
    private final int BATCH_COUNT = 100;
    /**
     * 缓存的数据
     */
    private List<BaseRoleImportExcelTemplate> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);

    /**
     * 用户信息
     */
    private MyAuthentication myAuthentication;

    /**
     * 角色服务
     */
    private BaseRoleService baseRoleService;

    /**
     * 实物
     */
    private TransactionTemplate transactionTemplate;


    //~methods
    //==================================================================================================================
    public BaseRoleImportExcelListener(MyAuthentication myAuthentication,
                                       BaseRoleService baseRoleService,
                                       TransactionTemplate transactionTemplate) {
        this.myAuthentication = myAuthentication;
        this.baseRoleService = baseRoleService;
        this.transactionTemplate = transactionTemplate;
    }

    /**
     * When analysis one row trigger invoke function.
     *
     * @param data    one row value. Is is same as {@link AnalysisContext#readRowHolder()}
     * @param context analysis context
     */
    @Override
    public void invoke(BaseRoleImportExcelTemplate data, AnalysisContext context) {
        log.debug("解析到一条数据:{}", data);
        AssertUtil.isTrue(data.getName().startsWith("ROLE_"), () -> ClientException.client("角色名称格式错误"));
        // 校验参数
        cachedDataList.add(data);
        // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
        if (cachedDataList.size() >= BATCH_COUNT) {
            updateData();
            // 存储完成清理 list
            cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
        }
    }

    /**
     * if have something to do after all analysis
     *
     * @param context
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        updateData();
        log.info("所有数据解析完成！");
    }

    /**
     * 加上存储数据库
     */
    private void updateData() {
        if (CollectionUtils.isNotEmpty(cachedDataList)) {
            log.info("{}条数据，开始存", cachedDataList.size());
            List<BaseRole> roles = new ArrayList<>(cachedDataList.size());
            cachedDataList.forEach(p -> {
                BaseRole role = new BaseRole();
                role.setAppId(myAuthentication.getRealAppId());
                role.setName(p.getName());
                role.setRemark(p.getRemark());
                roles.add(role);
            });
            transactionTemplate.execute(status -> {
                try {
                    baseRoleService.saveAll(roles);
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
