package com.iga.opbank.service.mapper;

import com.iga.opbank.domain.PaimentFacture;
import com.iga.opbank.domain.ServiceClass;
import com.iga.opbank.service.dto.PaimentFactureDTO;
import com.iga.opbank.service.dto.ServiceClassDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PaimentFacture} and its DTO {@link PaimentFactureDTO}.
 */
@Mapper(componentModel = "spring")
public interface PaimentFactureMapper extends EntityMapper<PaimentFactureDTO, PaimentFacture> {
    @Mapping(target = "serviceClass", source = "serviceClass", qualifiedByName = "serviceClassNomService")
    PaimentFactureDTO toDto(PaimentFacture s);

    @Named("serviceClassNomService")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nomService", source = "nomService")
    ServiceClassDTO toDtoServiceClassNomService(ServiceClass serviceClass);
}
