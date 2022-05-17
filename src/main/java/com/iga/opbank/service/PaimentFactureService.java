package com.iga.opbank.service;

import com.iga.opbank.service.dto.PaimentFactureDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.iga.opbank.domain.PaimentFacture}.
 */
public interface PaimentFactureService {
    /**
     * Save a paimentFacture.
     *
     * @param paimentFactureDTO the entity to save.
     * @return the persisted entity.
     */
    PaimentFactureDTO save(PaimentFactureDTO paimentFactureDTO);

    /**
     * Updates a paimentFacture.
     *
     * @param paimentFactureDTO the entity to update.
     * @return the persisted entity.
     */
    PaimentFactureDTO update(PaimentFactureDTO paimentFactureDTO);

    /**
     * Partially updates a paimentFacture.
     *
     * @param paimentFactureDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PaimentFactureDTO> partialUpdate(PaimentFactureDTO paimentFactureDTO);

    /**
     * Get all the paimentFactures.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PaimentFactureDTO> findAll(Pageable pageable);
    /**
     * Get all the PaimentFactureDTO where Operation is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<PaimentFactureDTO> findAllWhereOperationIsNull();

    /**
     * Get all the paimentFactures with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PaimentFactureDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" paimentFacture.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PaimentFactureDTO> findOne(Long id);

    /**
     * Delete the "id" paimentFacture.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
