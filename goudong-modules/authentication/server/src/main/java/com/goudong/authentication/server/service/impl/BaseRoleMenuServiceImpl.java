package com.goudong.authentication.server.service.impl;

import com.goudong.authentication.server.domain.BaseRoleMenu;
import com.goudong.authentication.server.repository.BaseRoleMenuRepository;
import com.goudong.authentication.server.service.BaseRoleMenuService;
import com.goudong.authentication.server.service.dto.BaseRoleMenuDTO;
import com.goudong.authentication.server.service.mapper.BaseRoleMenuMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link BaseRoleMenu}.
 */
@Service
@Transactional
public class BaseRoleMenuServiceImpl implements BaseRoleMenuService {

    private final Logger log = LoggerFactory.getLogger(BaseRoleMenuServiceImpl.class);

    private final BaseRoleMenuRepository baseRoleMenuRepository;

    private final BaseRoleMenuMapper baseRoleMenuMapper;

    public BaseRoleMenuServiceImpl(BaseRoleMenuRepository baseRoleMenuRepository, BaseRoleMenuMapper baseRoleMenuMapper) {
        this.baseRoleMenuRepository = baseRoleMenuRepository;
        this.baseRoleMenuMapper = baseRoleMenuMapper;
    }

    /**
     * Save a baseRoleMenu.
     *
     * @param baseRoleMenuDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public BaseRoleMenuDTO save(BaseRoleMenuDTO baseRoleMenuDTO) {
        log.debug("Request to save BaseRoleMenu : {}", baseRoleMenuDTO);
        BaseRoleMenu baseRoleMenu = baseRoleMenuMapper.toEntity(baseRoleMenuDTO);
        baseRoleMenu = baseRoleMenuRepository.save(baseRoleMenu);
        return baseRoleMenuMapper.toDto(baseRoleMenu);
    }

    /**
     * Get all the baseRoleMenus.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<BaseRoleMenuDTO> findAll(Pageable pageable) {
        log.debug("Request to get all BaseRoleMenus");
        return baseRoleMenuRepository.findAll(pageable)
            .map(baseRoleMenuMapper::toDto);
    }


    /**
     * Get one baseRoleMenu by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<BaseRoleMenuDTO> findOne(Long id) {
        log.debug("Request to get BaseRoleMenu : {}", id);
        return baseRoleMenuRepository.findById(id)
            .map(baseRoleMenuMapper::toDto);
    }

    /**
     * Delete the baseRoleMenu by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete BaseRoleMenu : {}", id);
        baseRoleMenuRepository.deleteById(id);
    }
}
