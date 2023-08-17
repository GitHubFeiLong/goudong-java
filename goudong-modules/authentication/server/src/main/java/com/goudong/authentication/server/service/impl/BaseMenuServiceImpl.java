package com.goudong.authentication.server.service.impl;

import com.goudong.authentication.server.domain.BaseMenu;
import com.goudong.authentication.server.repository.BaseMenuRepository;
import com.goudong.authentication.server.service.BaseMenuService;
import com.goudong.authentication.server.service.dto.BaseMenuDTO;
import com.goudong.authentication.server.service.mapper.BaseMenuMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link BaseMenu}.
 */
@Service
@Transactional
public class BaseMenuServiceImpl implements BaseMenuService {

    private final Logger log = LoggerFactory.getLogger(BaseMenuServiceImpl.class);

    private final BaseMenuRepository baseMenuRepository;

    private final BaseMenuMapper baseMenuMapper;

    public BaseMenuServiceImpl(BaseMenuRepository baseMenuRepository, BaseMenuMapper baseMenuMapper) {
        this.baseMenuRepository = baseMenuRepository;
        this.baseMenuMapper = baseMenuMapper;
    }

    /**
     * Save a baseMenu.
     *
     * @param baseMenuDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public BaseMenuDTO save(BaseMenuDTO baseMenuDTO) {
        log.debug("Request to save BaseMenu : {}", baseMenuDTO);
        BaseMenu baseMenu = baseMenuMapper.toEntity(baseMenuDTO);
        baseMenu = baseMenuRepository.save(baseMenu);
        return baseMenuMapper.toDto(baseMenu);
    }

    /**
     * Get all the baseMenus.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<BaseMenuDTO> findAll(Pageable pageable) {
        log.debug("Request to get all BaseMenus");
        return baseMenuRepository.findAll(pageable)
            .map(baseMenuMapper::toDto);
    }


    /**
     * Get one baseMenu by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<BaseMenuDTO> findOne(Long id) {
        log.debug("Request to get BaseMenu : {}", id);
        return baseMenuRepository.findById(id)
            .map(baseMenuMapper::toDto);
    }

    /**
     * Delete the baseMenu by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete BaseMenu : {}", id);
        baseMenuRepository.deleteById(id);
    }
}
