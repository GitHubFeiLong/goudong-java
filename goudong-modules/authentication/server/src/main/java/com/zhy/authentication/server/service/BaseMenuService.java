package com.zhy.authentication.server.service;

import com.zhy.authentication.server.service.dto.BaseMenuDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.zhy.authentication.server.domain.BaseMenu}.
 */
public interface BaseMenuService {

    /**
     * Save a baseMenu.
     *
     * @param baseMenuDTO the entity to save.
     * @return the persisted entity.
     */
    BaseMenuDTO save(BaseMenuDTO baseMenuDTO);

    /**
     * Get all the baseMenus.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<BaseMenuDTO> findAll(Pageable pageable);


    /**
     * Get the "id" baseMenu.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<BaseMenuDTO> findOne(Long id);

    /**
     * Delete the "id" baseMenu.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
