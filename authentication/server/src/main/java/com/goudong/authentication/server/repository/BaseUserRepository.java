package com.goudong.authentication.server.repository;

import com.goudong.authentication.server.domain.BaseUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the BaseUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BaseUserRepository extends JpaRepository<BaseUser, Long>, JpaSpecificationExecutor<BaseUser> {

    @Query(value = "from BaseUser where appId=?1 and username=?2")
    BaseUser findByLogin(Long appId, String login);

    /**
     * 删除应用下的所有用户
     * @param appId
     * @return
     */
    @Modifying
    @Query(nativeQuery = true, value = "delete from base_user where app_id = ?1 or real_app_id = ?1")
    int deleteByAppId(Long appId);
}
