// package com.zhy.authentication.server.rest;
//
// import com.zhy.authentication.server.service.BaseRoleMenuService;
// import com.zhy.authentication.server.service.dto.BaseRoleMenuDTO;
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
//  * REST controller for managing {@link com.zhy.authentication.server.domain.BaseRoleMenu}.
//  */
// @RestController
// @RequestMapping("/api")
// public class BaseRoleMenuResource {
//
//     private final Logger log = LoggerFactory.getLogger(BaseRoleMenuResource.class);
//
//     private static final String ENTITY_NAME = "baseRoleMenu";
//
//     @Value("${jhipster.clientApp.name}")
//     private String applicationName;
//
//     private final BaseRoleMenuService baseRoleMenuService;
//
//     public BaseRoleMenuResource(BaseRoleMenuService baseRoleMenuService) {
//         this.baseRoleMenuService = baseRoleMenuService;
//     }
//
//     /**
//      * {@code POST  /base-role-menus} : Create a new baseRoleMenu.
//      *
//      * @param baseRoleMenuDTO the baseRoleMenuDTO to create.
//      * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new baseRoleMenuDTO, or with status {@code 400 (Bad Request)} if the baseRoleMenu has already an ID.
//      * @throws URISyntaxException if the Location URI syntax is incorrect.
//      */
//     @PostMapping("/base-role-menus")
//     public ResponseEntity<BaseRoleMenuDTO> createBaseRoleMenu(@RequestBody BaseRoleMenuDTO baseRoleMenuDTO) throws URISyntaxException {
//         log.debug("REST request to save BaseRoleMenu : {}", baseRoleMenuDTO);
//         if (baseRoleMenuDTO.getId() != null) {
//             throw new BadRequestAlertException("A new baseRoleMenu cannot already have an ID", ENTITY_NAME, "idexists");
//         }
//         BaseRoleMenuDTO result = baseRoleMenuService.save(baseRoleMenuDTO);
//         return ResponseEntity.created(new URI("/api/base-role-menus/" + result.getId()))
//             .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
//             .body(result);
//     }
//
//     /**
//      * {@code PUT  /base-role-menus} : Updates an existing baseRoleMenu.
//      *
//      * @param baseRoleMenuDTO the baseRoleMenuDTO to update.
//      * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated baseRoleMenuDTO,
//      * or with status {@code 400 (Bad Request)} if the baseRoleMenuDTO is not valid,
//      * or with status {@code 500 (Internal Server Error)} if the baseRoleMenuDTO couldn't be updated.
//      * @throws URISyntaxException if the Location URI syntax is incorrect.
//      */
//     @PutMapping("/base-role-menus")
//     public ResponseEntity<BaseRoleMenuDTO> updateBaseRoleMenu(@RequestBody BaseRoleMenuDTO baseRoleMenuDTO) throws URISyntaxException {
//         log.debug("REST request to update BaseRoleMenu : {}", baseRoleMenuDTO);
//         if (baseRoleMenuDTO.getId() == null) {
//             throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
//         }
//         BaseRoleMenuDTO result = baseRoleMenuService.save(baseRoleMenuDTO);
//         return ResponseEntity.ok()
//             .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, baseRoleMenuDTO.getId().toString()))
//             .body(result);
//     }
//
//     /**
//      * {@code GET  /base-role-menus} : get all the baseRoleMenus.
//      *
//      * @param pageable the pagination information.
//      * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of baseRoleMenus in body.
//      */
//     @GetMapping("/base-role-menus")
//     public ResponseEntity<List<BaseRoleMenuDTO>> getAllBaseRoleMenus(Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
//         log.debug("REST request to get a page of BaseRoleMenus");
//         Page<BaseRoleMenuDTO> page = baseRoleMenuService.findAll(pageable);
//         HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
//         return ResponseEntity.ok().headers(headers).body(page.getContent());
//     }
//
//     /**
//      * {@code GET  /base-role-menus/:id} : get the "id" baseRoleMenu.
//      *
//      * @param id the id of the baseRoleMenuDTO to retrieve.
//      * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the baseRoleMenuDTO, or with status {@code 404 (Not Found)}.
//      */
//     @GetMapping("/base-role-menus/{id}")
//     public ResponseEntity<BaseRoleMenuDTO> getBaseRoleMenu(@PathVariable Long id) {
//         log.debug("REST request to get BaseRoleMenu : {}", id);
//         Optional<BaseRoleMenuDTO> baseRoleMenuDTO = baseRoleMenuService.findOne(id);
//         return ResponseUtil.wrapOrNotFound(baseRoleMenuDTO);
//     }
//
//     /**
//      * {@code DELETE  /base-role-menus/:id} : delete the "id" baseRoleMenu.
//      *
//      * @param id the id of the baseRoleMenuDTO to delete.
//      * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
//      */
//     @DeleteMapping("/base-role-menus/{id}")
//     public ResponseEntity<Void> deleteBaseRoleMenu(@PathVariable Long id) {
//         log.debug("REST request to delete BaseRoleMenu : {}", id);
//         baseRoleMenuService.delete(id);
//         return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
//     }
// }
