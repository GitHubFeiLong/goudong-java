package com.goudong.file.repository.user;

import com.goudong.file.po.user.BaseUserPO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 接口描述：
 * 用户持久层
 * @author msi
 * @version 1.0
 * @date 2022/1/7 21:03
 */
public interface BaseUserRepository extends JpaRepository<BaseUserPO, Long>, JpaSpecificationExecutor<BaseUserPO> {

}
