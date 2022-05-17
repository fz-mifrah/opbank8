package com.iga.opbank.service.impl;

import com.iga.opbank.domain.Compte;
import com.iga.opbank.repository.CompteRepository;
import com.iga.opbank.service.CompteService;
import com.iga.opbank.service.dto.CompteDTO;
import com.iga.opbank.service.mapper.CompteMapper;
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
 * Service Implementation for managing {@link Compte}.
 */
@Service
@Transactional
public class CompteServiceImpl implements CompteService {

    private final Logger log = LoggerFactory.getLogger(CompteServiceImpl.class);

    private final CompteRepository compteRepository;

    private final CompteMapper compteMapper;

    public CompteServiceImpl(CompteRepository compteRepository, CompteMapper compteMapper) {
        this.compteRepository = compteRepository;
        this.compteMapper = compteMapper;
    }

    @Override
    public CompteDTO save(CompteDTO compteDTO) {
        log.debug("Request to save Compte : {}", compteDTO);
        Compte compte = compteMapper.toEntity(compteDTO);
        compte = compteRepository.save(compte);
        return compteMapper.toDto(compte);
    }

    @Override
    public CompteDTO update(CompteDTO compteDTO) {
        log.debug("Request to save Compte : {}", compteDTO);
        Compte compte = compteMapper.toEntity(compteDTO);
        compte = compteRepository.save(compte);
        return compteMapper.toDto(compte);
    }

    @Override
    public Optional<CompteDTO> partialUpdate(CompteDTO compteDTO) {
        log.debug("Request to partially update Compte : {}", compteDTO);

        return compteRepository
            .findById(compteDTO.getId())
            .map(existingCompte -> {
                compteMapper.partialUpdate(existingCompte, compteDTO);

                return existingCompte;
            })
            .map(compteRepository::save)
            .map(compteMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CompteDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Comptes");
        return compteRepository.findAll(pageable).map(compteMapper::toDto);
    }

    public Page<CompteDTO> findAllWithEagerRelationships(Pageable pageable) {
        return compteRepository.findAllWithEagerRelationships(pageable).map(compteMapper::toDto);
    }

    /**
     *  Get all the comptes where Client is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<CompteDTO> findAllWhereClientIsNull() {
        log.debug("Request to get all comptes where Client is null");
        return StreamSupport
            .stream(compteRepository.findAll().spliterator(), false)
            .filter(compte -> compte.getClient() == null)
            .map(compteMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CompteDTO> findOne(Long id) {
        log.debug("Request to get Compte : {}", id);
        return compteRepository.findOneWithEagerRelationships(id).map(compteMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Compte : {}", id);
        compteRepository.deleteById(id);
    }
}
