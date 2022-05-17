package com.iga.opbank.web.rest;

import com.iga.opbank.repository.VirementRepository;
import com.iga.opbank.service.VirementService;
import com.iga.opbank.service.dto.VirementDTO;
import com.iga.opbank.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.iga.opbank.domain.Virement}.
 */
@RestController
@RequestMapping("/api")
public class VirementResource {

    private final Logger log = LoggerFactory.getLogger(VirementResource.class);

    private static final String ENTITY_NAME = "virement";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VirementService virementService;

    private final VirementRepository virementRepository;

    public VirementResource(VirementService virementService, VirementRepository virementRepository) {
        this.virementService = virementService;
        this.virementRepository = virementRepository;
    }

    /**
     * {@code POST  /virements} : Create a new virement.
     *
     * @param virementDTO the virementDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new virementDTO, or with status {@code 400 (Bad Request)} if the virement has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/virements")
    public ResponseEntity<VirementDTO> createVirement(@RequestBody VirementDTO virementDTO) throws URISyntaxException {
        log.debug("REST request to save Virement : {}", virementDTO);
        if (virementDTO.getId() != null) {
            throw new BadRequestAlertException("A new virement cannot already have an ID", ENTITY_NAME, "idexists");
        }
        VirementDTO result = virementService.save(virementDTO);
        return ResponseEntity
            .created(new URI("/api/virements/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /virements/:id} : Updates an existing virement.
     *
     * @param id the id of the virementDTO to save.
     * @param virementDTO the virementDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated virementDTO,
     * or with status {@code 400 (Bad Request)} if the virementDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the virementDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/virements/{id}")
    public ResponseEntity<VirementDTO> updateVirement(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody VirementDTO virementDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Virement : {}, {}", id, virementDTO);
        if (virementDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, virementDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!virementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        VirementDTO result = virementService.update(virementDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, virementDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /virements/:id} : Partial updates given fields of an existing virement, field will ignore if it is null
     *
     * @param id the id of the virementDTO to save.
     * @param virementDTO the virementDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated virementDTO,
     * or with status {@code 400 (Bad Request)} if the virementDTO is not valid,
     * or with status {@code 404 (Not Found)} if the virementDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the virementDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/virements/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<VirementDTO> partialUpdateVirement(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody VirementDTO virementDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Virement partially : {}, {}", id, virementDTO);
        if (virementDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, virementDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!virementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<VirementDTO> result = virementService.partialUpdate(virementDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, virementDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /virements} : get all the virements.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of virements in body.
     */
    @GetMapping("/virements")
    public ResponseEntity<List<VirementDTO>> getAllVirements(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false) String filter,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        if ("operation-is-null".equals(filter)) {
            log.debug("REST request to get all Virements where operation is null");
            return new ResponseEntity<>(virementService.findAllWhereOperationIsNull(), HttpStatus.OK);
        }
        log.debug("REST request to get a page of Virements");
        Page<VirementDTO> page;
        if (eagerload) {
            page = virementService.findAllWithEagerRelationships(pageable);
        } else {
            page = virementService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /virements/:id} : get the "id" virement.
     *
     * @param id the id of the virementDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the virementDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/virements/{id}")
    public ResponseEntity<VirementDTO> getVirement(@PathVariable Long id) {
        log.debug("REST request to get Virement : {}", id);
        Optional<VirementDTO> virementDTO = virementService.findOne(id);
        return ResponseUtil.wrapOrNotFound(virementDTO);
    }

    /**
     * {@code DELETE  /virements/:id} : delete the "id" virement.
     *
     * @param id the id of the virementDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/virements/{id}")
    public ResponseEntity<Void> deleteVirement(@PathVariable Long id) {
        log.debug("REST request to delete Virement : {}", id);
        virementService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
