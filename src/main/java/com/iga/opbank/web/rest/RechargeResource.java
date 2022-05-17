package com.iga.opbank.web.rest;

import com.iga.opbank.repository.RechargeRepository;
import com.iga.opbank.service.RechargeService;
import com.iga.opbank.service.dto.RechargeDTO;
import com.iga.opbank.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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
 * REST controller for managing {@link com.iga.opbank.domain.Recharge}.
 */
@RestController
@RequestMapping("/api")
public class RechargeResource {

    private final Logger log = LoggerFactory.getLogger(RechargeResource.class);

    private static final String ENTITY_NAME = "recharge";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RechargeService rechargeService;

    private final RechargeRepository rechargeRepository;

    public RechargeResource(RechargeService rechargeService, RechargeRepository rechargeRepository) {
        this.rechargeService = rechargeService;
        this.rechargeRepository = rechargeRepository;
    }

    /**
     * {@code POST  /recharges} : Create a new recharge.
     *
     * @param rechargeDTO the rechargeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new rechargeDTO, or with status {@code 400 (Bad Request)} if the recharge has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/recharges")
    public ResponseEntity<RechargeDTO> createRecharge(@Valid @RequestBody RechargeDTO rechargeDTO) throws URISyntaxException {
        log.debug("REST request to save Recharge : {}", rechargeDTO);
        if (rechargeDTO.getId() != null) {
            throw new BadRequestAlertException("A new recharge cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RechargeDTO result = rechargeService.save(rechargeDTO);
        return ResponseEntity
            .created(new URI("/api/recharges/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /recharges/:id} : Updates an existing recharge.
     *
     * @param id the id of the rechargeDTO to save.
     * @param rechargeDTO the rechargeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rechargeDTO,
     * or with status {@code 400 (Bad Request)} if the rechargeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the rechargeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/recharges/{id}")
    public ResponseEntity<RechargeDTO> updateRecharge(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody RechargeDTO rechargeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Recharge : {}, {}", id, rechargeDTO);
        if (rechargeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rechargeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rechargeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RechargeDTO result = rechargeService.update(rechargeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rechargeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /recharges/:id} : Partial updates given fields of an existing recharge, field will ignore if it is null
     *
     * @param id the id of the rechargeDTO to save.
     * @param rechargeDTO the rechargeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rechargeDTO,
     * or with status {@code 400 (Bad Request)} if the rechargeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the rechargeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the rechargeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/recharges/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RechargeDTO> partialUpdateRecharge(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody RechargeDTO rechargeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Recharge partially : {}, {}", id, rechargeDTO);
        if (rechargeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rechargeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rechargeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RechargeDTO> result = rechargeService.partialUpdate(rechargeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rechargeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /recharges} : get all the recharges.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of recharges in body.
     */
    @GetMapping("/recharges")
    public ResponseEntity<List<RechargeDTO>> getAllRecharges(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false) String filter,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        if ("operation-is-null".equals(filter)) {
            log.debug("REST request to get all Recharges where operation is null");
            return new ResponseEntity<>(rechargeService.findAllWhereOperationIsNull(), HttpStatus.OK);
        }
        log.debug("REST request to get a page of Recharges");
        Page<RechargeDTO> page;
        if (eagerload) {
            page = rechargeService.findAllWithEagerRelationships(pageable);
        } else {
            page = rechargeService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /recharges/:id} : get the "id" recharge.
     *
     * @param id the id of the rechargeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the rechargeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/recharges/{id}")
    public ResponseEntity<RechargeDTO> getRecharge(@PathVariable Long id) {
        log.debug("REST request to get Recharge : {}", id);
        Optional<RechargeDTO> rechargeDTO = rechargeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(rechargeDTO);
    }

    /**
     * {@code DELETE  /recharges/:id} : delete the "id" recharge.
     *
     * @param id the id of the rechargeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/recharges/{id}")
    public ResponseEntity<Void> deleteRecharge(@PathVariable Long id) {
        log.debug("REST request to delete Recharge : {}", id);
        rechargeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
