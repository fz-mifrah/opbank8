package com.iga.opbank.service;

import com.iga.opbank.service.dto.CompteDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.iga.opbank.domain.Compte}.
 */
public interface CompteService {
    /**
     * Save a compte.
     *
     * @param compteDTO the entity to save.
     * @return the persisted entity.
     */
    CompteDTO save(CompteDTO compteDTO);

    /**
     * Updates a compte.
     *
     * @param compteDTO the entity to update.
     * @return the persisted entity.
     */
    CompteDTO update(CompteDTO compteDTO);

    /**
     * Partially updates a compte.
     *
     * @param compteDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CompteDTO> partialUpdate(CompteDTO compteDTO);

    /**
     * Get all the comptes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CompteDTO> findAll(Pageable pageable);
    /**
     * Get all the CompteDTO where Client is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<CompteDTO> findAllWhereClientIsNull();

    /**
     * Get all the comptes with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CompteDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" compte.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CompteDTO> findOne(Long id);

    /**
     * Delete the "id" compte.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
