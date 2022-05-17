package com.iga.opbank.service;

import com.iga.opbank.service.dto.BanqueDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.iga.opbank.domain.Banque}.
 */
public interface BanqueService {
    /**
     * Save a banque.
     *
     * @param banqueDTO the entity to save.
     * @return the persisted entity.
     */
    BanqueDTO save(BanqueDTO banqueDTO);

    /**
     * Updates a banque.
     *
     * @param banqueDTO the entity to update.
     * @return the persisted entity.
     */
    BanqueDTO update(BanqueDTO banqueDTO);

    /**
     * Partially updates a banque.
     *
     * @param banqueDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<BanqueDTO> partialUpdate(BanqueDTO banqueDTO);

    /**
     * Get all the banques.
     *
     * @return the list of entities.
     */
    List<BanqueDTO> findAll();

    /**
     * Get the "id" banque.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<BanqueDTO> findOne(Long id);

    /**
     * Delete the "id" banque.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
