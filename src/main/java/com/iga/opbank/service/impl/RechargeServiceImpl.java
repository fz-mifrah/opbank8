package com.iga.opbank.service.impl;

import com.iga.opbank.domain.Compte;
import com.iga.opbank.domain.Operation;
import com.iga.opbank.domain.Recharge;
import com.iga.opbank.domain.Virement;
import com.iga.opbank.domain.enumeration.EtatOperation;
import com.iga.opbank.domain.enumeration.TypeOperation;
import com.iga.opbank.repository.CompteRepository;
import com.iga.opbank.repository.OperationRepository;
import com.iga.opbank.repository.RechargeRepository;
import com.iga.opbank.service.CompteService;
import com.iga.opbank.service.OperationService;
import com.iga.opbank.service.RechargeService;
import com.iga.opbank.service.dto.CompteDTO;
import com.iga.opbank.service.dto.OperationDTO;
import com.iga.opbank.service.dto.RechargeDTO;
import com.iga.opbank.service.mapper.RechargeMapper;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Recharge}.
 */
@Service
@Transactional
public class RechargeServiceImpl implements RechargeService {

    private final Logger log = LoggerFactory.getLogger(RechargeServiceImpl.class);

    private final RechargeRepository rechargeRepository;

    private final RechargeMapper rechargeMapper;

    private final OperationRepository operationRepository;

    private final OperationService operationService;

    private final CompteRepository compteRepository;

    private final CompteService compteService;


    public RechargeServiceImpl(RechargeRepository rechargeRepository, RechargeMapper rechargeMapper, OperationRepository operationRepository, CompteRepository compteRepository, CompteService compteService,OperationService operationService) {
        this.rechargeRepository = rechargeRepository;
        this.rechargeMapper = rechargeMapper;
        this.operationRepository = operationRepository;
        this.operationService = operationService;
        this.compteRepository = compteRepository;
        this.compteService = compteService;
    }

    @Override
    public RechargeDTO save(RechargeDTO rechargeDTO) {
        log.debug("Request to save Recharge : {}", rechargeDTO);
        Recharge recharge = rechargeMapper.toEntity(rechargeDTO);
        recharge = rechargeRepository.save(recharge);
        return rechargeMapper.toDto(recharge);
    }

    @Override
    public RechargeDTO update(RechargeDTO rechargeDTO) {
        log.debug("Request to save Recharge : {}", rechargeDTO);
        Recharge recharge = rechargeMapper.toEntity(rechargeDTO);
        recharge = rechargeRepository.save(recharge);
        return rechargeMapper.toDto(recharge);
    }

    @Override
    public Optional<RechargeDTO> partialUpdate(RechargeDTO rechargeDTO) {
        log.debug("Request to partially update Recharge : {}", rechargeDTO);

        return rechargeRepository
            .findById(rechargeDTO.getId())
            .map(existingRecharge -> {
                rechargeMapper.partialUpdate(existingRecharge, rechargeDTO);

                return existingRecharge;
            })
            .map(rechargeRepository::save)
            .map(rechargeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RechargeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Recharges");
        return rechargeRepository.findAll(pageable).map(rechargeMapper::toDto);
    }

    public Page<RechargeDTO> findAllWithEagerRelationships(Pageable pageable) {
        return rechargeRepository.findAllWithEagerRelationships(pageable).map(rechargeMapper::toDto);
    }

    /**
     *  Get all the recharges where Operation is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<RechargeDTO> findAllWhereOperationIsNull() {
        log.debug("Request to get all recharges where Operation is null");
        return StreamSupport
            .stream(rechargeRepository.findAll().spliterator(), false)
            .filter(recharge -> recharge.getOperation() == null)
            .map(rechargeMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RechargeDTO> findOne(Long id) {
        log.debug("Request to get Recharge : {}", id);
        return rechargeRepository.findOneWithEagerRelationships(id).map(rechargeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Recharge : {}", id);
        rechargeRepository.deleteById(id);
    }

    @Override
    public RechargeDTO effectuerRecharge(RechargeDTO rechargeDTO, Long montantRecharge) {
        log.debug("Request to create a new Recharge my my numtel: {}", rechargeDTO.getNumTel());

        Optional<CompteDTO> currentCompte = compteService.getCurrentCompte();
        CompteDTO myAccount  = currentCompte.get();
         if (myAccount.getSolde() > montantRecharge) {
            myAccount.setSolde(myAccount.getSolde() - montantRecharge);
            OperationDTO operation = new OperationDTO();
            operation.setCompte(myAccount);
            operation.setDate(LocalDate.now());
            operation.setEtatOperation(EtatOperation.Valide);
            operation.setTypeOperatin(TypeOperation.TypeRecharge);
            operation.setMontant((double) montantRecharge);
            operation.setNumOperation("RECHARGE-" + UUID.randomUUID().toString().substring(0, 6));
            RechargeDTO recharge = save(rechargeDTO);
            operation.setRecharge(recharge);
            operation.setCompte(myAccount);
            operationService.save(operation);
            compteService.save(myAccount);
            return recharge;
         }
        return null;
    }
}
