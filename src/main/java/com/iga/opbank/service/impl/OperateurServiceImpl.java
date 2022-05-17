package com.iga.opbank.service.impl;

import com.iga.opbank.domain.Operateur;
import com.iga.opbank.repository.OperateurRepository;
import com.iga.opbank.service.OperateurService;
import com.iga.opbank.service.dto.OperateurDTO;
import com.iga.opbank.service.mapper.OperateurMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Operateur}.
 */
@Service
@Transactional
public class OperateurServiceImpl implements OperateurService {

    private final Logger log = LoggerFactory.getLogger(OperateurServiceImpl.class);

    private final OperateurRepository operateurRepository;

    private final OperateurMapper operateurMapper;

    public OperateurServiceImpl(OperateurRepository operateurRepository, OperateurMapper operateurMapper) {
        this.operateurRepository = operateurRepository;
        this.operateurMapper = operateurMapper;
    }

    @Override
    public OperateurDTO save(OperateurDTO operateurDTO) {
        log.debug("Request to save Operateur : {}", operateurDTO);
        Operateur operateur = operateurMapper.toEntity(operateurDTO);
        operateur = operateurRepository.save(operateur);
        return operateurMapper.toDto(operateur);
    }

    @Override
    public OperateurDTO update(OperateurDTO operateurDTO) {
        log.debug("Request to save Operateur : {}", operateurDTO);
        Operateur operateur = operateurMapper.toEntity(operateurDTO);
        operateur = operateurRepository.save(operateur);
        return operateurMapper.toDto(operateur);
    }

    @Override
    public Optional<OperateurDTO> partialUpdate(OperateurDTO operateurDTO) {
        log.debug("Request to partially update Operateur : {}", operateurDTO);

        return operateurRepository
            .findById(operateurDTO.getId())
            .map(existingOperateur -> {
                operateurMapper.partialUpdate(existingOperateur, operateurDTO);

                return existingOperateur;
            })
            .map(operateurRepository::save)
            .map(operateurMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OperateurDTO> findAll() {
        log.debug("Request to get all Operateurs");
        return operateurRepository.findAll().stream().map(operateurMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OperateurDTO> findOne(Long id) {
        log.debug("Request to get Operateur : {}", id);
        return operateurRepository.findById(id).map(operateurMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Operateur : {}", id);
        operateurRepository.deleteById(id);
    }
}
