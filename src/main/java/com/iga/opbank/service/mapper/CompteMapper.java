package com.iga.opbank.service.mapper;

import com.iga.opbank.domain.Banque;
import com.iga.opbank.domain.CarteBancaire;
import com.iga.opbank.domain.Compte;
import com.iga.opbank.service.dto.BanqueDTO;
import com.iga.opbank.service.dto.CarteBancaireDTO;
import com.iga.opbank.service.dto.CompteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Compte} and its DTO {@link CompteDTO}.
 */
@Mapper(componentModel = "spring")
public interface CompteMapper extends EntityMapper<CompteDTO, Compte> {
    @Mapping(target = "carteBancaire", source = "carteBancaire", qualifiedByName = "carteBancaireNumCarte")
    @Mapping(target = "banque", source = "banque", qualifiedByName = "banqueId")
    CompteDTO toDto(Compte s);

    @Named("carteBancaireNumCarte")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "numCarte", source = "numCarte")
    CarteBancaireDTO toDtoCarteBancaireNumCarte(CarteBancaire carteBancaire);

    @Named("banqueId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    BanqueDTO toDtoBanqueId(Banque banque);
}
