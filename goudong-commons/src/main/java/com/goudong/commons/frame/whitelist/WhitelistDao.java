package com.goudong.commons.frame.whitelist;

import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * 类描述：
 * 白名单持久层
 * @author msi
 * @version 1.0
 * @date 2022/1/8 16:54
 */
@Deprecated
public class WhitelistDao {
    /**
     * 插入sql语句
     */
    public static final String INSERT_WHITELIST_SQL = "insert into base_whitelist (`pattern`,`method`,`remark`,`is_system`) " +
            "values(?, ?, ?, ?)";

    /**
     * 查询sql语句
     */
    public static final String SELECT_WHITELIST_SQL = "select " +
            "`id`, `pattern`, `method`, `remark`, `is_system`, `deleted`, `update_time`, `create_time`, `create_user_id`, `update_user_id` " +
            "from `base_whitelist`";

    private final JdbcTemplate jdbcTemplate;

    public WhitelistDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * 查询所有白名单
     * @return
     */
    public List<BaseWhitelistPO> findAll() {
        List<BaseWhitelistPO> list = jdbcTemplate.query(WhitelistDao.SELECT_WHITELIST_SQL, new BaseWhitelistPO());

        return list;
    }

    /**
     * 插入
     * @return
     */
    public boolean insert(List<BaseWhitelistPO> whitelistPOS) {
        List<Object[]> batchArgs=new ArrayList<Object[]>(whitelistPOS.size());
        whitelistPOS.forEach(p->{
            batchArgs.add(new Object[]{p.getPattern(), p.getMethod(), p.getRemark(), p.getIsSystem()});
        });

        jdbcTemplate.batchUpdate(WhitelistDao.INSERT_WHITELIST_SQL, batchArgs);
        return true;
    }

}
