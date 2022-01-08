package com.goudong.commons.frame.whitelist;

import com.goudong.commons.constant.core.DateConst;
import com.goudong.commons.po.core.BasePO;
import lombok.Data;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * 类描述：
 * 白名单
 * @author msi
 * @version 1.0
 * @date 2022/1/8 16:46
 */
@Data
@Deprecated
public class BaseWhitelistPO extends BasePO implements RowMapper {

    /**
     * 匹配模式
     */
    private String pattern;
    /**
     * 请求的方法
     */
    private String method;
    /**
     * 备注
     */
    private String remark;
    /**
     * 是否是系统设置的
     */
    private Boolean isSystem;

    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        BaseWhitelistPO po = new BaseWhitelistPO();
        po.setPattern(resultSet.getString("pattern"));
        po.setMethod(resultSet.getString("method"));
        po.setRemark(resultSet.getString("remark"));
        po.setIsSystem(resultSet.getBoolean("is_system"));
        po.setId(resultSet.getLong("id"));
        po.setDeleted(resultSet.getBoolean("deleted"));
        po.setCreateUserId(resultSet.getLong("create_user_id"));
        po.setUpdateUserId(resultSet.getLong("update_user_id"));
        String updateTime = resultSet.getString("update_time");
        String createTime = resultSet.getString("create_time");
        po.setUpdateTime(LocalDateTime.parse(updateTime, DateConst.YYYY_MM_DD_HH_MM_SS));
        po.setCreateTime(LocalDateTime.parse(createTime, DateConst.YYYY_MM_DD_HH_MM_SS));
        return po;
    }
}
