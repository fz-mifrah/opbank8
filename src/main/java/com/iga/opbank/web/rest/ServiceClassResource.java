package com.iga.opbank.web.rest;

import com.iga.opbank.repository.ServiceClassRepository;
import com.iga.opbank.service.ServiceClassService;
import com.iga.opbank.service.dto.ServiceClassDTO;
import com.iga.opbank.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.iga.opbank.domain.ServiceClass}.
 */
@RestController
@RequestMapping("/api")
public class ServiceClassResource {

    private final Logger log = LoggerFactory.getLogger(ServiceClassResource.class);

    private static final String ENTITY_NAME = "serviceClass";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ServiceClassService serviceClassService;

    private final ServiceClassRepository serviceClassRepository;

    public ServiceClassResource(ServiceClassService serviceClassService, ServiceClassRepository serviceClassRepository) {
        this.serviceClassService = serviceClassService;
        this.serviceClassRepository = serviceClassRepository;
    }

    /**
     * {@code POST  /service-classes} : Create a new serviceClass.
     *
     * @param serviceClassDTO the serviceClassDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new serviceClassDTO, or with status {@code 400 (Bad Request)} if the serviceClass has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/service-classes")
    public ResponseEntity<ServiceClassDTO> createServiceClass(@Valid @RequestBody ServiceClassDTO serviceClassDTO)
        throws URISyntaxException {
        log.debug("REST request to save ServiceClass : {}", serviceClassDTO);
        if (serviceClassDTO.getId() != null) {
            throw new BadRequestAlertException("A new serviceClass cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ServiceClassDTO result = serviceClassService.save(serviceClassDTO);
        return ResponseEntity
            .created(new URI("/api/service-classes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /service-classes/:id} : Updates an existing serviceClass.
     *
     * @param id the id of the serviceClassDTO to save.
     * @param serviceClassDTO the serviceClassDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated serviceClassDTO,
     * or with status {@code 400 (Bad Request)} if the serviceClassDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the serviceClassDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/service-classes/{id}")
    public ResponseEntity<ServiceClassDTO> updateServiceClass(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ServiceClassDTO serviceClassDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ServiceClass : {}, {}", id, serviceClassDTO);
        if (serviceClassDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, serviceClassDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!serviceClassRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ServiceClassDTO result = serviceClassService.update(serviceClassDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, serviceClassDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /service-classes/:id} : Partial updates given fields of an existing serviceClass, field will ignore if it is null
     *
     * @param id the id of the serviceClassDTO to save.
     * @param serviceClassDTO the serviceClassDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated serviceClassDTO,
     * or with status {@code 400 (Bad Request)} if the serviceClassDTO is not valid,
     * or with status {@code 404 (Not Found)} if the serviceClassDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the serviceClassDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/service-classes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ServiceClassDTO> partialUpdateServiceClass(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ServiceClassDTO serviceClassDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ServiceClass partially : {}, {}", id, serviceClassDTO);
        if (serviceClassDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, serviceClassDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!serviceClassRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ServiceClassDTO> result = serviceClassService.partialUpdate(serviceClassDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, serviceClassDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /service-classes} : get all the serviceClasses.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of serviceClasses in body.
     */
    @GetMapping("/service-classes")
    public List<ServiceClassDTO> getAllServiceClasses() {
        log.debug("REST request to get all ServiceClasses");
        return serviceClassService.findAll();
    }

    /**
     * {@code GET  /service-classes/:id} : get the "id" serviceClass.
     *
     * @param id the id of the serviceClassDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the serviceClassDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/service-classes/{id}")
    public ResponseEntity<ServiceClassDTO> getServiceClass(@PathVariable Long id) {
        log.debug("REST request to get ServiceClass : {}", id);
        Optional<ServiceClassDTO> serviceClassDTO = serviceClassService.findOne(id);
        return ResponseUtil.wrapOrNotFound(serviceClassDTO);
    }

    /**
     * {@code DELETE  /service-classes/:id} : delete the "id" serviceClass.
     *
     * @param id the id of the serviceClassDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/service-classes/{id}")
    public ResponseEntity<Void> deleteServiceClass(@PathVariable Long id) {
        log.debug("REST request to delete ServiceClass : {}", id);
        serviceClassService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
