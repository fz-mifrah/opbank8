package com.iga.opbank.service;

import com.iga.opbank.service.dto.ServiceClassDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.iga.opbank.domain.ServiceClass}.
 */
public interface ServiceClassService {
    /**
     * Save a serviceClass.
     *
     * @param serviceClassDTO the entity to save.
     * @return the persisted entity.
     */
    ServiceClassDTO save(ServiceClassDTO serviceClassDTO);

    /**
     * Updates a serviceClass.
     *
     * @param serviceClassDTO the entity to update.
     * @return the persisted entity.
     */
    ServiceClassDTO update(ServiceClassDTO serviceClassDTO);

    /**
     * Partially updates a serviceClass.
     *
     * @param serviceClassDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ServiceClassDTO> partialUpdate(ServiceClassDTO serviceClassDTO);

    /**
     * Get all the serviceClasses.
     *
     * @return the list of entities.
     */
    List<ServiceClassDTO> findAll();

    /**
     * Get the "id" serviceClass.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ServiceClassDTO> findOne(Long id);

    /**
     * Delete the "id" serviceClass.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
