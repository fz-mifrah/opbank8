package com.iga.opbank.service.impl;

import com.iga.opbank.domain.Transfer;
import com.iga.opbank.repository.TransferRepository;
import com.iga.opbank.service.TransferService;
import com.iga.opbank.service.dto.TransferDTO;
import com.iga.opbank.service.mapper.TransferMapper;
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
 * Service Implementation for managing {@link Transfer}.
 */
@Service
@Transactional
public class TransferServiceImpl implements TransferService {

    private final Logger log = LoggerFactory.getLogger(TransferServiceImpl.class);

    private final TransferRepository transferRepository;

    private final TransferMapper transferMapper;

    public TransferServiceImpl(TransferRepository transferRepository, TransferMapper transferMapper) {
        this.transferRepository = transferRepository;
        this.transferMapper = transferMapper;
    }

    @Override
    public TransferDTO save(TransferDTO transferDTO) {
        log.debug("Request to save Transfer : {}", transferDTO);
        Transfer transfer = transferMapper.toEntity(transferDTO);
        transfer = transferRepository.save(transfer);
        return transferMapper.toDto(transfer);
    }

    @Override
    public TransferDTO update(TransferDTO transferDTO) {
        log.debug("Request to save Transfer : {}", transferDTO);
        Transfer transfer = transferMapper.toEntity(transferDTO);
        transfer = transferRepository.save(transfer);
        return transferMapper.toDto(transfer);
    }

    @Override
    public Optional<TransferDTO> partialUpdate(TransferDTO transferDTO) {
        log.debug("Request to partially update Transfer : {}", transferDTO);

        return transferRepository
            .findById(transferDTO.getId())
            .map(existingTransfer -> {
                transferMapper.partialUpdate(existingTransfer, transferDTO);

                return existingTransfer;
            })
            .map(transferRepository::save)
            .map(transferMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TransferDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Transfers");
        return transferRepository.findAll(pageable).map(transferMapper::toDto);
    }

    /**
     *  Get all the transfers where Operation is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<TransferDTO> findAllWhereOperationIsNull() {
        log.debug("Request to get all transfers where Operation is null");
        return StreamSupport
            .stream(transferRepository.findAll().spliterator(), false)
            .filter(transfer -> transfer.getOperation() == null)
            .map(transferMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TransferDTO> findOne(Long id) {
        log.debug("Request to get Transfer : {}", id);
        return transferRepository.findById(id).map(transferMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Transfer : {}", id);
        transferRepository.deleteById(id);
    }
}
