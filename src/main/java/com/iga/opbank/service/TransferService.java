package com.iga.opbank.service;

import com.iga.opbank.service.dto.TransferDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.iga.opbank.domain.Transfer}.
 */
public interface TransferService {
    /**
     * Save a transfer.
     *
     * @param transferDTO the entity to save.
     * @return the persisted entity.
     */
    TransferDTO save(TransferDTO transferDTO);

    /**
     * Updates a transfer.
     *
     * @param transferDTO the entity to update.
     * @return the persisted entity.
     */
    TransferDTO update(TransferDTO transferDTO);

    /**
     * Partially updates a transfer.
     *
     * @param transferDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TransferDTO> partialUpdate(TransferDTO transferDTO);

    /**
     * Get all the transfers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TransferDTO> findAll(Pageable pageable);
    /**
     * Get all the TransferDTO where Operation is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<TransferDTO> findAllWhereOperationIsNull();

    /**
     * Get the "id" transfer.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TransferDTO> findOne(Long id);

    /**
     * Delete the "id" transfer.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
