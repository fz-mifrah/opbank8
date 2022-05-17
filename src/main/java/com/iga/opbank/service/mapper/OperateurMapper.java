package com.iga.opbank.service.mapper;

import com.iga.opbank.domain.Operateur;
import com.iga.opbank.service.dto.OperateurDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Operateur} and its DTO {@link OperateurDTO}.
 */
@Mapper(componentModel = "spring")
public interface OperateurMapper extends EntityMapper<OperateurDTO, Operateur> {}
