package com.goudong.authentication.server.easyexcel.listener;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ListUtils;
import com.goudong.authentication.server.domain.BaseUser;
import com.goudong.authentication.server.easyexcel.template.BaseUserImportExcelTemplate;
import com.goudong.authentication.server.rest.resp.BaseImportResp;
import com.goudong.authentication.server.service.BaseUserService;
import com.goudong.authentication.server.service.dto.MyAuthentication;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 类描述：
 * 用户导入excel监听器
 * @author chenf
 * @version 1.0
 */
@Slf4j
public class BaseUserImportExcelListener implements ReadListener<BaseUserImportExcelTemplate> {
    //~fields
    //==================================================================================================================
    /**
     * 每隔5条存储数据库，实际使用中可以100条，然后清理list ，方便内存回收
     */
    private final int BATCH_COUNT = 100;
    /**
     * 缓存的数据
     */
    private List<BaseUserImportExcelTemplate> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);

    /**
     * 用户信息
     */
    private MyAuthentication myAuthentication;

    /**
     * 用户服务
     */
    private BaseUserService baseUserService;

    /**
     * 实物
     */
    private TransactionTemplate transactionTemplate;

    /**
     * 加密器
     */
    private PasswordEncoder passwordEncoder;

    //~methods
    //==================================================================================================================
    public BaseUserImportExcelListener(MyAuthentication myAuthentication,
                                       BaseUserService baseUserService,
                                       TransactionTemplate transactionTemplate,
                                       PasswordEncoder passwordEncoder) {
        this.myAuthentication = myAuthentication;
        this.baseUserService = baseUserService;
        this.transactionTemplate = transactionTemplate;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * When analysis one row trigger invoke function.
     *
     * @param data    one row value. Is is same as {@link AnalysisContext#readRowHolder()}
     * @param context analysis context
     */
    @Override
    public void invoke(BaseUserImportExcelTemplate data, AnalysisContext context) {
        log.debug("解析到一条数据:{}", data);
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
            List<BaseUser> users = new ArrayList<>(cachedDataList.size());
            cachedDataList.forEach(p -> {
                BaseUser user = new BaseUser();
                user.setAppId(myAuthentication.getRealAppId());
                user.setRealAppId(myAuthentication.getRealAppId());
                user.setUsername(p.getUsername());
                user.setPassword(passwordEncoder.encode("123456"));
                user.setEnabled(Objects.equals("已激活", p.getEnabled()));
                user.setLocked(Objects.equals("已锁定", p.getLocked()));
                user.setValidTime(DateUtil.parse(p.getValidTime(), DatePattern.NORM_DATETIME_PATTERN));
                user.setRemark(p.getRemark());
                users.add(user);
            });
            transactionTemplate.execute(status -> {
                try {
                    baseUserService.saveAll(users);
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
