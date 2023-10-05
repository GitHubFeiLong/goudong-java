package com.goudong.authentication.server.repository;

import com.goudong.authentication.server.domain.BaseApp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the BaseApp entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BaseAppRepository extends JpaRepository<BaseApp, Long>, JpaSpecificationExecutor<BaseApp> {

}
