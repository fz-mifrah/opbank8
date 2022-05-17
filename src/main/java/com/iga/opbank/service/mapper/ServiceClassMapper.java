package com.iga.opbank.service.mapper;

import com.iga.opbank.domain.ServiceClass;
import com.iga.opbank.service.dto.ServiceClassDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ServiceClass} and its DTO {@link ServiceClassDTO}.
 */
@Mapper(componentModel = "spring")
public interface ServiceClassMapper extends EntityMapper<ServiceClassDTO, ServiceClass> {}
