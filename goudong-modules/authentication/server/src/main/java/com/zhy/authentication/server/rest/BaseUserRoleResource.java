// package com.zhy.authentication.server.rest;
//
// import com.zhy.authentication.server.service.BaseUserRoleService;
// import com.zhy.authentication.server.service.dto.BaseUserRoleDTO;
// import com.zhy.authentication.server.web.rest.errors.BadRequestAlertException;
// import io.github.jhipster.web.util.HeaderUtil;
// import io.github.jhipster.web.util.PaginationUtil;
// import io.github.jhipster.web.util.ResponseUtil;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.data.domain.Page;
// import org.springframework.data.domain.Pageable;
// import org.springframework.http.HttpHeaders;
// import org.springframework.http.ResponseEntity;
// import org.springframework.util.MultiValueMap;
// import org.springframework.web.bind.annotation.*;
// import org.springframework.web.util.UriComponentsBuilder;
//
// import java.net.URI;
// import java.net.URISyntaxException;
// import java.util.List;
// import java.util.Optional;
//
// /**
//  * REST controller for managing {@link com.zhy.authentication.server.domain.BaseUserRole}.
//  */
// @RestController
// @RequestMapping("/api")
// public class BaseUserRoleResource {
//
//     private final Logger log = LoggerFactory.getLogger(BaseUserRoleResource.class);
//
//     private static final String ENTITY_NAME = "baseUserRole";
//
//     @Value("${jhipster.clientApp.name}")
//     private String applicationName;
//
//     private final BaseUserRoleService baseUserRoleService;
//
//     public BaseUserRoleResource(BaseUserRoleService baseUserRoleService) {
//         this.baseUserRoleService = baseUserRoleService;
//     }
//
//     /**
//      * {@code POST  /base-user-roles} : Create a new baseUserRole.
//      *
//      * @param baseUserRoleDTO the baseUserRoleDTO to create.
//      * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new baseUserRoleDTO, or with status {@code 400 (Bad Request)} if the baseUserRole has already an ID.
//      * @throws URISyntaxException if the Location URI syntax is incorrect.
//      */
//     @PostMapping("/base-user-roles")
//     public ResponseEntity<BaseUserRoleDTO> createBaseUserRole(@RequestBody BaseUserRoleDTO baseUserRoleDTO) throws URISyntaxException {
//         log.debug("REST request to save BaseUserRole : {}", baseUserRoleDTO);
//         if (baseUserRoleDTO.getId() != null) {
//             throw new BadRequestAlertException("A new baseUserRole cannot already have an ID", ENTITY_NAME, "idexists");
//         }
//         BaseUserRoleDTO result = baseUserRoleService.save(baseUserRoleDTO);
//         return ResponseEntity.created(new URI("/api/base-user-roles/" + result.getId()))
//             .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
//             .body(result);
//     }
//
//     /**
//      * {@code PUT  /base-user-roles} : Updates an existing baseUserRole.
//      *
//      * @param baseUserRoleDTO the baseUserRoleDTO to update.
//      * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated baseUserRoleDTO,
//      * or with status {@code 400 (Bad Request)} if the baseUserRoleDTO is not valid,
//      * or with status {@code 500 (Internal Server Error)} if the baseUserRoleDTO couldn't be updated.
//      * @throws URISyntaxException if the Location URI syntax is incorrect.
//      */
//     @PutMapping("/base-user-roles")
//     public ResponseEntity<BaseUserRoleDTO> updateBaseUserRole(@RequestBody BaseUserRoleDTO baseUserRoleDTO) throws URISyntaxException {
//         log.debug("REST request to update BaseUserRole : {}", baseUserRoleDTO);
//         if (baseUserRoleDTO.getId() == null) {
//             throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
//         }
//         BaseUserRoleDTO result = baseUserRoleService.save(baseUserRoleDTO);
//         return ResponseEntity.ok()
//             .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, baseUserRoleDTO.getId().toString()))
//             .body(result);
//     }
//
//     /**
//      * {@code GET  /base-user-roles} : get all the baseUserRoles.
//      *
//      * @param pageable the pagination information.
//      * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of baseUserRoles in body.
//      */
//     @GetMapping("/base-user-roles")
//     public ResponseEntity<List<BaseUserRoleDTO>> getAllBaseUserRoles(Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
//         log.debug("REST request to get a page of BaseUserRoles");
//         Page<BaseUserRoleDTO> page = baseUserRoleService.findAll(pageable);
//         HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
//         return ResponseEntity.ok().headers(headers).body(page.getContent());
//     }
//
//     /**
//      * {@code GET  /base-user-roles/:id} : get the "id" baseUserRole.
//      *
//      * @param id the id of the baseUserRoleDTO to retrieve.
//      * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the baseUserRoleDTO, or with status {@code 404 (Not Found)}.
//      */
//     @GetMapping("/base-user-roles/{id}")
//     public ResponseEntity<BaseUserRoleDTO> getBaseUserRole(@PathVariable Long id) {
//         log.debug("REST request to get BaseUserRole : {}", id);
//         Optional<BaseUserRoleDTO> baseUserRoleDTO = baseUserRoleService.findOne(id);
//         return ResponseUtil.wrapOrNotFound(baseUserRoleDTO);
//     }
//
//     /**
//      * {@code DELETE  /base-user-roles/:id} : delete the "id" baseUserRole.
//      *
//      * @param id the id of the baseUserRoleDTO to delete.
//      * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
//      */
//     @DeleteMapping("/base-user-roles/{id}")
//     public ResponseEntity<Void> deleteBaseUserRole(@PathVariable Long id) {
//         log.debug("REST request to delete BaseUserRole : {}", id);
//         baseUserRoleService.delete(id);
//         return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
//     }
// }
