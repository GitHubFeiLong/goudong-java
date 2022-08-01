package com.goudong.oauth2.repository;

import com.goudong.oauth2.po.BaseWhitelistPO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * 类描述：
 * 白名单持久层
 * @author msi
 * @date 2022/1/15 12:27
 * @version 1.0
 */
public interface BaseWhitelistRepository extends JpaRepository<BaseWhitelistPO, Long>, JpaSpecificationExecutor<BaseWhitelistPO> {

    /**
     * 查询 指定isDisable的值
     * @param isDisable
     * @return
     */
    List<BaseWhitelistPO> findAllByIsDisable(boolean isDisable);

}
