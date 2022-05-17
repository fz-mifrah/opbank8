package com.iga.opbank.service;

import com.iga.opbank.service.dto.CarteBancaireDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.iga.opbank.domain.CarteBancaire}.
 */
public interface CarteBancaireService {
    /**
     * Save a carteBancaire.
     *
     * @param carteBancaireDTO the entity to save.
     * @return the persisted entity.
     */
    CarteBancaireDTO save(CarteBancaireDTO carteBancaireDTO);

    /**
     * Updates a carteBancaire.
     *
     * @param carteBancaireDTO the entity to update.
     * @return the persisted entity.
     */
    CarteBancaireDTO update(CarteBancaireDTO carteBancaireDTO);

    /**
     * Partially updates a carteBancaire.
     *
     * @param carteBancaireDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CarteBancaireDTO> partialUpdate(CarteBancaireDTO carteBancaireDTO);

    /**
     * Get all the carteBancaires.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CarteBancaireDTO> findAll(Pageable pageable);
    /**
     * Get all the CarteBancaireDTO where Compte is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<CarteBancaireDTO> findAllWhereCompteIsNull();

    /**
     * Get the "id" carteBancaire.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CarteBancaireDTO> findOne(Long id);

    /**
     * Delete the "id" carteBancaire.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
