package com.goudong.security.dao;




import com.goudong.commons.entity.AuthorityIgnoreResourceDO;

import java.util.List;

/**
 * 类描述：
 * 不需要权限就能访问的资源处理
 * @Author msi
 * @Date 2021-04-03 20:18
 * @Version 1.0
 */
public interface AuthorityIgnoreResourceDao {
    /**
     * 查询全部
     * @return
     */
    List<AuthorityIgnoreResourceDO> selectAll();
}
