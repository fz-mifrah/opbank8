package com.iga.opbank.service;

import com.iga.opbank.service.dto.OperationDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.iga.opbank.domain.Operation}.
 */
public interface OperationService {
    /**
     * Save a operation.
     *
     * @param operationDTO the entity to save.
     * @return the persisted entity.
     */
    OperationDTO save(OperationDTO operationDTO);

    /**
     * Updates a operation.
     *
     * @param operationDTO the entity to update.
     * @return the persisted entity.
     */
    OperationDTO update(OperationDTO operationDTO);

    /**
     * Partially updates a operation.
     *
     * @param operationDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<OperationDTO> partialUpdate(OperationDTO operationDTO);

    /**
     * Get all the operations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<OperationDTO> findAll(Pageable pageable);

    /**
     * Get all the operations with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<OperationDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" operation.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OperationDTO> findOne(Long id);

    /**
     * Delete the "id" operation.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
