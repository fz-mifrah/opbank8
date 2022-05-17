package com.iga.opbank.service.mapper;

import com.iga.opbank.domain.CarteBancaire;
import com.iga.opbank.service.dto.CarteBancaireDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CarteBancaire} and its DTO {@link CarteBancaireDTO}.
 */
@Mapper(componentModel = "spring")
public interface CarteBancaireMapper extends EntityMapper<CarteBancaireDTO, CarteBancaire> {}
