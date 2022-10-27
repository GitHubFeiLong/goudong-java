package com.goudong.modules.jpa.spring.boot.repository;

import com.goudong.modules.jpa.spring.boot.po.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 类描述：
 *
 * @author cfl
 * @version 1.0
 * @date 2022/10/27 14:11
 */
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
}
