package com.goudong.authentication.server.repository;

import com.goudong.authentication.server.domain.BaseAppCert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data  repository for the BaseAppCert entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BaseAppCertRepository extends JpaRepository<BaseAppCert, Long>, JpaSpecificationExecutor<BaseAppCert> {

    /**
     * 查询应用所有证书
     * @param appId 应用id
     * @return 应用所有证书
     */
    List<BaseAppCert> findAllByAppIdOrderByIdDesc(Long appId);
}
