package com.goudong.repository;

import com.goudong.po.BaseTokenPO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 接口描述：
 *
 * @author msi
 * @version 1.0
 * @date 2022/1/19 20:27
 */
public interface BaseTokenRepository extends JpaRepository<BaseTokenPO, Long>, JpaSpecificationExecutor<BaseTokenPO> {

}
