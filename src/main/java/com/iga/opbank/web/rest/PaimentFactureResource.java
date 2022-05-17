package com.iga.opbank.web.rest;

import com.iga.opbank.repository.PaimentFactureRepository;
import com.iga.opbank.service.PaimentFactureService;
import com.iga.opbank.service.dto.PaimentFactureDTO;
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
 * REST controller for managing {@link com.iga.opbank.domain.PaimentFacture}.
 */
@RestController
@RequestMapping("/api")
public class PaimentFactureResource {

    private final Logger log = LoggerFactory.getLogger(PaimentFactureResource.class);

    private static final String ENTITY_NAME = "paimentFacture";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PaimentFactureService paimentFactureService;

    private final PaimentFactureRepository paimentFactureRepository;

    public PaimentFactureResource(PaimentFactureService paimentFactureService, PaimentFactureRepository paimentFactureRepository) {
        this.paimentFactureService = paimentFactureService;
        this.paimentFactureRepository = paimentFactureRepository;
    }

    /**
     * {@code POST  /paiment-factures} : Create a new paimentFacture.
     *
     * @param paimentFactureDTO the paimentFactureDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new paimentFactureDTO, or with status {@code 400 (Bad Request)} if the paimentFacture has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/paiment-factures")
    public ResponseEntity<PaimentFactureDTO> createPaimentFacture(@Valid @RequestBody PaimentFactureDTO paimentFactureDTO)
        throws URISyntaxException {
        log.debug("REST request to save PaimentFacture : {}", paimentFactureDTO);
        if (paimentFactureDTO.getId() != null) {
            throw new BadRequestAlertException("A new paimentFacture cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PaimentFactureDTO result = paimentFactureService.save(paimentFactureDTO);
        return ResponseEntity
            .created(new URI("/api/paiment-factures/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /paiment-factures/:id} : Updates an existing paimentFacture.
     *
     * @param id the id of the paimentFactureDTO to save.
     * @param paimentFactureDTO the paimentFactureDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paimentFactureDTO,
     * or with status {@code 400 (Bad Request)} if the paimentFactureDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the paimentFactureDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/paiment-factures/{id}")
    public ResponseEntity<PaimentFactureDTO> updatePaimentFacture(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PaimentFactureDTO paimentFactureDTO
    ) throws URISyntaxException {
        log.debug("REST request to update PaimentFacture : {}, {}", id, paimentFactureDTO);
        if (paimentFactureDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paimentFactureDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paimentFactureRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PaimentFactureDTO result = paimentFactureService.update(paimentFactureDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paimentFactureDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /paiment-factures/:id} : Partial updates given fields of an existing paimentFacture, field will ignore if it is null
     *
     * @param id the id of the paimentFactureDTO to save.
     * @param paimentFactureDTO the paimentFactureDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paimentFactureDTO,
     * or with status {@code 400 (Bad Request)} if the paimentFactureDTO is not valid,
     * or with status {@code 404 (Not Found)} if the paimentFactureDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the paimentFactureDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/paiment-factures/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PaimentFactureDTO> partialUpdatePaimentFacture(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PaimentFactureDTO paimentFactureDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update PaimentFacture partially : {}, {}", id, paimentFactureDTO);
        if (paimentFactureDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paimentFactureDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paimentFactureRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PaimentFactureDTO> result = paimentFactureService.partialUpdate(paimentFactureDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paimentFactureDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /paiment-factures} : get all the paimentFactures.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of paimentFactures in body.
     */
    @GetMapping("/paiment-factures")
    public ResponseEntity<List<PaimentFactureDTO>> getAllPaimentFactures(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false) String filter,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        if ("operation-is-null".equals(filter)) {
            log.debug("REST request to get all PaimentFactures where operation is null");
            return new ResponseEntity<>(paimentFactureService.findAllWhereOperationIsNull(), HttpStatus.OK);
        }
        log.debug("REST request to get a page of PaimentFactures");
        Page<PaimentFactureDTO> page;
        if (eagerload) {
            page = paimentFactureService.findAllWithEagerRelationships(pageable);
        } else {
            page = paimentFactureService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /paiment-factures/:id} : get the "id" paimentFacture.
     *
     * @param id the id of the paimentFactureDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the paimentFactureDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/paiment-factures/{id}")
    public ResponseEntity<PaimentFactureDTO> getPaimentFacture(@PathVariable Long id) {
        log.debug("REST request to get PaimentFacture : {}", id);
        Optional<PaimentFactureDTO> paimentFactureDTO = paimentFactureService.findOne(id);
        return ResponseUtil.wrapOrNotFound(paimentFactureDTO);
    }

    /**
     * {@code DELETE  /paiment-factures/:id} : delete the "id" paimentFacture.
     *
     * @param id the id of the paimentFactureDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/paiment-factures/{id}")
    public ResponseEntity<Void> deletePaimentFacture(@PathVariable Long id) {
        log.debug("REST request to delete PaimentFacture : {}", id);
        paimentFactureService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
