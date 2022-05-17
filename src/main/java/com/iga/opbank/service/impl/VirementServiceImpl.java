package com.iga.opbank.service.impl;

import com.iga.opbank.domain.Virement;
import com.iga.opbank.repository.VirementRepository;
import com.iga.opbank.service.VirementService;
import com.iga.opbank.service.dto.VirementDTO;
import com.iga.opbank.service.mapper.VirementMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Virement}.
 */
@Service
@Transactional
public class VirementServiceImpl implements VirementService {

    private final Logger log = LoggerFactory.getLogger(VirementServiceImpl.class);

    private final VirementRepository virementRepository;

    private final VirementMapper virementMapper;

    public VirementServiceImpl(VirementRepository virementRepository, VirementMapper virementMapper) {
        this.virementRepository = virementRepository;
        this.virementMapper = virementMapper;
    }

    @Override
    public VirementDTO save(VirementDTO virementDTO) {
        log.debug("Request to save Virement : {}", virementDTO);
        Virement virement = virementMapper.toEntity(virementDTO);
        virement = virementRepository.save(virement);
        return virementMapper.toDto(virement);
    }

    @Override
    public VirementDTO update(VirementDTO virementDTO) {
        log.debug("Request to save Virement : {}", virementDTO);
        Virement virement = virementMapper.toEntity(virementDTO);
        virement = virementRepository.save(virement);
        return virementMapper.toDto(virement);
    }

    @Override
    public Optional<VirementDTO> partialUpdate(VirementDTO virementDTO) {
        log.debug("Request to partially update Virement : {}", virementDTO);

        return virementRepository
            .findById(virementDTO.getId())
            .map(existingVirement -> {
                virementMapper.partialUpdate(existingVirement, virementDTO);

                return existingVirement;
            })
            .map(virementRepository::save)
            .map(virementMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<VirementDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Virements");
        return virementRepository.findAll(pageable).map(virementMapper::toDto);
    }

    public Page<VirementDTO> findAllWithEagerRelationships(Pageable pageable) {
        return virementRepository.findAllWithEagerRelationships(pageable).map(virementMapper::toDto);
    }

    /**
     *  Get all the virements where Operation is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<VirementDTO> findAllWhereOperationIsNull() {
        log.debug("Request to get all virements where Operation is null");
        return StreamSupport
            .stream(virementRepository.findAll().spliterator(), false)
            .filter(virement -> virement.getOperation() == null)
            .map(virementMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<VirementDTO> findOne(Long id) {
        log.debug("Request to get Virement : {}", id);
        return virementRepository.findOneWithEagerRelationships(id).map(virementMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Virement : {}", id);
        virementRepository.deleteById(id);
    }
}
