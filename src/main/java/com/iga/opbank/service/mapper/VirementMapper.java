package com.iga.opbank.service.mapper;

import com.iga.opbank.domain.Compte;
import com.iga.opbank.domain.Virement;
import com.iga.opbank.service.dto.CompteDTO;
import com.iga.opbank.service.dto.VirementDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Virement} and its DTO {@link VirementDTO}.
 */
@Mapper(componentModel = "spring")
public interface VirementMapper extends EntityMapper<VirementDTO, Virement> {
    @Mapping(target = "compte", source = "compte", qualifiedByName = "compteRib")
    VirementDTO toDto(Virement s);

    @Named("compteRib")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "rib", source = "rib")
    CompteDTO toDtoCompteRib(Compte compte);
}
