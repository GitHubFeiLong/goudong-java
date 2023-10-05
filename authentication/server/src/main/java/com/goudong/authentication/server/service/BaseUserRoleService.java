package com.goudong.authentication.server.service;

import com.goudong.authentication.server.domain.BaseUserRole;
import com.goudong.authentication.server.service.dto.BaseUserRoleDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link BaseUserRole}.
 */
public interface BaseUserRoleService {

    /**
     * Save a baseUserRole.
     *
     * @param baseUserRoleDTO the entity to save.
     * @return the persisted entity.
     */
    BaseUserRoleDTO save(BaseUserRoleDTO baseUserRoleDTO);

    /**
     * Get all the baseUserRoles.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<BaseUserRoleDTO> findAll(Pageable pageable);

    /**
     * 查询用户id的所有角色
     * @param userId
     * @return
     */
    List<BaseUserRole> findAllByUserId(Long userId);

    /**
     * Get the "id" baseUserRole.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<BaseUserRoleDTO> findOne(Long id);

    /**
     * Delete the "id" baseUserRole.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
