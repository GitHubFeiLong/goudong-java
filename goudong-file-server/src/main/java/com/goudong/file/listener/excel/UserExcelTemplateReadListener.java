package com.goudong.file.listener.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ListUtils;
import com.goudong.core.util.ListUtil;
import com.goudong.core.util.StringUtil;
import com.goudong.file.dto.UserExcelTemplateDTO;
import com.goudong.file.enumerate.SexEnum;
import com.goudong.file.po.user.BaseRolePO;
import com.goudong.file.po.user.BaseUserPO;
import com.goudong.file.service.BaseRoleService;
import com.goudong.file.service.BaseUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 类描述：
 * 用户导入读的监听器
 * @author cfl
 * @version 1.0
 * @date 2022/12/1 13:54
 */
@Slf4j
public class UserExcelTemplateReadListener implements ReadListener<UserExcelTemplateDTO> {
    //~fields
    //==================================================================================================================
    /**
     * 单次缓存的数据量
     */
    public static final int BATCH_COUNT = 100;
    /**
     *临时存储
     */
    private List<BaseUserPO> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);

    private BaseUserService baseUserService;
    private BaseRoleService baseRoleService;
    //~methods
    //==================================================================================================================
    public UserExcelTemplateReadListener(BaseUserService baseUserService, BaseRoleService baseRoleService) {
        this.baseUserService = baseUserService;
        this.baseRoleService = baseRoleService;
    }
    /**
     * 所有角色
     * <pre>
     *     key      ->  角色中文名
     *     value    ->  角色对象
     * </pre>
     */
    private Map<String, BaseRolePO> rolePOMap = baseRoleService.findAll()
            .stream().collect(Collectors.toMap(BaseRolePO::getRoleNameCn, p->p, (k1, k2) -> k1));

    /**
     * When analysis one row trigger invoke function.
     *
     * @param data    one row value. Is is same as {@link AnalysisContext#readRowHolder()}
     * @param context analysis context
     */
    @Override
    public void invoke(UserExcelTemplateDTO data, AnalysisContext context) {
        BaseUserPO baseUserPO = new BaseUserPO();
        baseUserPO.setUsername(data.getUsername());
        String password = StringUtil.isNotBlank(data.getPassword())? data.getPassword() : "123456";
        baseUserPO.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
        baseUserPO.setEmail(data.getEmail());
        baseUserPO.setPhone(data.getPhone());
        baseUserPO.setNickname(data.getNickname());
        baseUserPO.setRemark(data.getRemark());
        baseUserPO.setValidTime(data.getValidTime());
        baseUserPO.setSex(SexEnum.getBySex(data.getSex()).getId());
        baseUserPO.setRoles(ListUtil.newArrayList(rolePOMap.get(data.getRoleNameCn())));
        baseUserPO.setEnabled(true);
        baseUserPO.setLocked(false);

        cachedDataList.add(baseUserPO);
        if (cachedDataList.size() >= BATCH_COUNT) {
            saveData();
        }
    }

    /**
     * 所有数据解析完
     *
     * @param context
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        saveData();
    }
    /**
     * 保存数据库
     */
    private void saveData() {
        log.info("{}条数据，开始存储数据库！", cachedDataList.size());
        baseUserService.saveAll(cachedDataList);
        // 存储完成清理 list
        cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
    }
}
