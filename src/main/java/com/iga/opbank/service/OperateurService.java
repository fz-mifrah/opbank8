package com.iga.opbank.service;

import com.iga.opbank.service.dto.OperateurDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.iga.opbank.domain.Operateur}.
 */
public interface OperateurService {
    /**
     * Save a operateur.
     *
     * @param operateurDTO the entity to save.
     * @return the persisted entity.
     */
    OperateurDTO save(OperateurDTO operateurDTO);

    /**
     * Updates a operateur.
     *
     * @param operateurDTO the entity to update.
     * @return the persisted entity.
     */
    OperateurDTO update(OperateurDTO operateurDTO);

    /**
     * Partially updates a operateur.
     *
     * @param operateurDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<OperateurDTO> partialUpdate(OperateurDTO operateurDTO);

    /**
     * Get all the operateurs.
     *
     * @return the list of entities.
     */
    List<OperateurDTO> findAll();

    /**
     * Get the "id" operateur.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OperateurDTO> findOne(Long id);

    /**
     * Delete the "id" operateur.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
