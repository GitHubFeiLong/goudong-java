package com.goudong.user.dao;

import com.goudong.user.entity.AuthorityIgnoreResourceDO;
import com.goudong.user.entity.AuthorityUserDO;

import java.util.List;
import java.util.Map;

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
