package com.iga.opbank.service.impl;

import com.iga.opbank.domain.Compte;
import com.iga.opbank.domain.Operation;
import com.iga.opbank.repository.CompteRepository;
import com.iga.opbank.repository.OperationRepository;
import com.iga.opbank.service.CompteService;
import com.iga.opbank.service.OperationService;
import com.iga.opbank.service.dto.CompteDTO;
import com.iga.opbank.service.dto.OperationDTO;
import com.iga.opbank.service.mapper.OperationMapper;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Operation}.
 */
@Service
@Transactional
public class OperationServiceImpl implements OperationService {

    private final Logger log = LoggerFactory.getLogger(OperationServiceImpl.class);

    private final OperationRepository operationRepository;

    private final OperationMapper operationMapper;

    private final CompteService compteService;

    private final CompteRepository compteRepository;


    public OperationServiceImpl(OperationRepository operationRepository, OperationMapper operationMapper,CompteService compteService,CompteRepository compteRepository) {
        this.operationRepository = operationRepository;
        this.operationMapper = operationMapper;
        this.compteService = compteService;
        this.compteRepository = compteRepository;
    }

    @Override
    public OperationDTO save(OperationDTO operationDTO) {
        log.debug("Request to save Operation : {}", operationDTO);
        Operation operation = operationMapper.toEntity(operationDTO);
        operation = operationRepository.save(operation);
        return operationMapper.toDto(operation);
    }

    @Override
    public OperationDTO update(OperationDTO operationDTO) {
        log.debug("Request to save Operation : {}", operationDTO);
        Operation operation = operationMapper.toEntity(operationDTO);
        operation = operationRepository.save(operation);
        return operationMapper.toDto(operation);
    }

    @Override
    public Optional<OperationDTO> partialUpdate(OperationDTO operationDTO) {
        log.debug("Request to partially update Operation : {}", operationDTO);

        return operationRepository
            .findById(operationDTO.getId())
            .map(existingOperation -> {
                operationMapper.partialUpdate(existingOperation, operationDTO);

                return existingOperation;
            })
            .map(operationRepository::save)
            .map(operationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OperationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Operations");
        return operationRepository.findAll(pageable).map(operationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OperationDTO> findAllHistory(Pageable pageable) {
        log.debug("Request to get all Operations history");
        Optional<CompteDTO> currentCompte = compteService.getCurrentCompte();
        CompteDTO myAccount  = currentCompte.get();
        Compte compte = compteRepository.findById(myAccount.getId()).get();

        List<Operation> historyCompte = operationRepository.findAllHistoryByCompte(compte);

        final int start = (int)pageable.getOffset();
        final int end = Math.min((start + pageable.getPageSize()), historyCompte.size());
        final Page<Operation> page = new PageImpl<>(historyCompte.subList(start, end), pageable, historyCompte.size());
page.stream().forEach(e -> {if(e.getCompte().getId().equals(myAccount.getId())){e.setMontant(0-e.getMontant());}});


        return page.map(operationMapper::toDto);
        //return operationRepository.findAllByCompte(compte,pageable).map(operationMapper::toDto);
    }

    public Page<OperationDTO> findAllWithEagerRelationships(Pageable pageable) {
        return operationRepository.findAllWithEagerRelationships(pageable).map(operationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OperationDTO> findOne(Long id) {
        log.debug("Request to get Operation : {}", id);
        return operationRepository.findOneWithEagerRelationships(id).map(operationMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Operation : {}", id);
        operationRepository.deleteById(id);
    }
}
