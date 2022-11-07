package com.goudong.file.repository.user;

import com.goudong.file.po.user.BaseRolePO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 类描述：
 * 角色持久层
 * @author msi
 * @date 2022/1/8 11:16
 * @version 1.0
 */
public interface BaseRoleRepository extends JpaRepository<BaseRolePO, Long>, JpaSpecificationExecutor<BaseRolePO> {

}
