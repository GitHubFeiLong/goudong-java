package com.goudong.authentication.server.service.impl;

import com.goudong.authentication.server.domain.BaseUserRole;
import com.goudong.authentication.server.repository.BaseUserRoleRepository;
import com.goudong.authentication.server.service.BaseUserRoleService;
import com.goudong.authentication.server.service.dto.BaseUserRoleDTO;
import com.goudong.authentication.server.service.mapper.BaseUserRoleMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link BaseUserRole}.
 */
@Service
@Transactional
public class BaseUserRoleServiceImpl implements BaseUserRoleService {

    private final Logger log = LoggerFactory.getLogger(BaseUserRoleServiceImpl.class);

    private final BaseUserRoleRepository baseUserRoleRepository;

    private final BaseUserRoleMapper baseUserRoleMapper;

    public BaseUserRoleServiceImpl(BaseUserRoleRepository baseUserRoleRepository, BaseUserRoleMapper baseUserRoleMapper) {
        this.baseUserRoleRepository = baseUserRoleRepository;
        this.baseUserRoleMapper = baseUserRoleMapper;
    }

    /**
     * Save a baseUserRole.
     *
     * @param baseUserRoleDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public BaseUserRoleDTO save(BaseUserRoleDTO baseUserRoleDTO) {
        log.debug("Request to save BaseUserRole : {}", baseUserRoleDTO);
        BaseUserRole baseUserRole = baseUserRoleMapper.toEntity(baseUserRoleDTO);
        baseUserRole = baseUserRoleRepository.save(baseUserRole);
        return baseUserRoleMapper.toDto(baseUserRole);
    }

    /**
     * Get all the baseUserRoles.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<BaseUserRoleDTO> findAll(Pageable pageable) {
        log.debug("Request to get all BaseUserRoles");
        return baseUserRoleRepository.findAll(pageable)
            .map(baseUserRoleMapper::toDto);
    }

    /**
     * 查询用户id的所有角色
     *
     * @param userId
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public List<BaseUserRole> findAllByUserId(Long userId) {
        log.info("=================");
        // List<BaseUserRole> all = baseUserRoleRepository.findAllByUserId(userId);
        List<BaseUserRole> all = null;
        log.info("=================");
        return all;
    }


    /**
     * Get one baseUserRole by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<BaseUserRoleDTO> findOne(Long id) {
        log.debug("Request to get BaseUserRole : {}", id);
        return baseUserRoleRepository.findById(id)
            .map(baseUserRoleMapper::toDto);
    }

    /**
     * Delete the baseUserRole by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete BaseUserRole : {}", id);
        baseUserRoleRepository.deleteById(id);
    }
}
