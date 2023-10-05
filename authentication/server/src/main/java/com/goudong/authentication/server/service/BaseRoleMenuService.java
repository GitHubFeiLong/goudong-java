package com.goudong.authentication.server.service;

import com.goudong.authentication.server.domain.BaseRoleMenu;
import com.goudong.authentication.server.service.dto.BaseRoleMenuDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link BaseRoleMenu}.
 */
public interface BaseRoleMenuService {

    /**
     * Save a baseRoleMenu.
     *
     * @param baseRoleMenuDTO the entity to save.
     * @return the persisted entity.
     */
    BaseRoleMenuDTO save(BaseRoleMenuDTO baseRoleMenuDTO);

    /**
     * Get all the baseRoleMenus.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<BaseRoleMenuDTO> findAll(Pageable pageable);


    /**
     * Get the "id" baseRoleMenu.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<BaseRoleMenuDTO> findOne(Long id);

    /**
     * Delete the "id" baseRoleMenu.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
