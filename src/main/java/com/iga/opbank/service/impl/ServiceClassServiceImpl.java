package com.iga.opbank.service.impl;

import com.iga.opbank.domain.ServiceClass;
import com.iga.opbank.repository.ServiceClassRepository;
import com.iga.opbank.service.ServiceClassService;
import com.iga.opbank.service.dto.ServiceClassDTO;
import com.iga.opbank.service.mapper.ServiceClassMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ServiceClass}.
 */
@Service
@Transactional
public class ServiceClassServiceImpl implements ServiceClassService {

    private final Logger log = LoggerFactory.getLogger(ServiceClassServiceImpl.class);

    private final ServiceClassRepository serviceClassRepository;

    private final ServiceClassMapper serviceClassMapper;

    public ServiceClassServiceImpl(ServiceClassRepository serviceClassRepository, ServiceClassMapper serviceClassMapper) {
        this.serviceClassRepository = serviceClassRepository;
        this.serviceClassMapper = serviceClassMapper;
    }

    @Override
    public ServiceClassDTO save(ServiceClassDTO serviceClassDTO) {
        log.debug("Request to save ServiceClass : {}", serviceClassDTO);
        ServiceClass serviceClass = serviceClassMapper.toEntity(serviceClassDTO);
        serviceClass = serviceClassRepository.save(serviceClass);
        return serviceClassMapper.toDto(serviceClass);
    }

    @Override
    public ServiceClassDTO update(ServiceClassDTO serviceClassDTO) {
        log.debug("Request to save ServiceClass : {}", serviceClassDTO);
        ServiceClass serviceClass = serviceClassMapper.toEntity(serviceClassDTO);
        serviceClass = serviceClassRepository.save(serviceClass);
        return serviceClassMapper.toDto(serviceClass);
    }

    @Override
    public Optional<ServiceClassDTO> partialUpdate(ServiceClassDTO serviceClassDTO) {
        log.debug("Request to partially update ServiceClass : {}", serviceClassDTO);

        return serviceClassRepository
            .findById(serviceClassDTO.getId())
            .map(existingServiceClass -> {
                serviceClassMapper.partialUpdate(existingServiceClass, serviceClassDTO);

                return existingServiceClass;
            })
            .map(serviceClassRepository::save)
            .map(serviceClassMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ServiceClassDTO> findAll() {
        log.debug("Request to get all ServiceClasses");
        return serviceClassRepository.findAll().stream().map(serviceClassMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ServiceClassDTO> findOne(Long id) {
        log.debug("Request to get ServiceClass : {}", id);
        return serviceClassRepository.findById(id).map(serviceClassMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ServiceClass : {}", id);
        serviceClassRepository.deleteById(id);
    }
}
