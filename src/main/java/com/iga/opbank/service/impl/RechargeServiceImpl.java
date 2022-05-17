package com.iga.opbank.service.impl;

import com.iga.opbank.domain.Recharge;
import com.iga.opbank.repository.RechargeRepository;
import com.iga.opbank.service.RechargeService;
import com.iga.opbank.service.dto.RechargeDTO;
import com.iga.opbank.service.mapper.RechargeMapper;
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
 * Service Implementation for managing {@link Recharge}.
 */
@Service
@Transactional
public class RechargeServiceImpl implements RechargeService {

    private final Logger log = LoggerFactory.getLogger(RechargeServiceImpl.class);

    private final RechargeRepository rechargeRepository;

    private final RechargeMapper rechargeMapper;

    public RechargeServiceImpl(RechargeRepository rechargeRepository, RechargeMapper rechargeMapper) {
        this.rechargeRepository = rechargeRepository;
        this.rechargeMapper = rechargeMapper;
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
}
