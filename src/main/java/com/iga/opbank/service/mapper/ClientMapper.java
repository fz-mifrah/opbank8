package com.iga.opbank.service.mapper;

import com.iga.opbank.domain.Client;
import com.iga.opbank.domain.Compte;
import com.iga.opbank.service.dto.ClientDTO;
import com.iga.opbank.service.dto.CompteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Client} and its DTO {@link ClientDTO}.
 */
@Mapper(componentModel = "spring")
public interface ClientMapper extends EntityMapper<ClientDTO, Client> {
    @Mapping(target = "compte", source = "compte", qualifiedByName = "compteRib")
    ClientDTO toDto(Client s);

    @Named("compteRib")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "rib", source = "rib")
    CompteDTO toDtoCompteRib(Compte compte);
}
