package com.iga.opbank.service.mapper;

import com.iga.opbank.domain.Operateur;
import com.iga.opbank.domain.Recharge;
import com.iga.opbank.service.dto.OperateurDTO;
import com.iga.opbank.service.dto.RechargeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Recharge} and its DTO {@link RechargeDTO}.
 */
@Mapper(componentModel = "spring")
public interface RechargeMapper extends EntityMapper<RechargeDTO, Recharge> {
    @Mapping(target = "operateur", source = "operateur", qualifiedByName = "operateurNomOp")
    RechargeDTO toDto(Recharge s);

    @Named("operateurNomOp")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nomOp", source = "nomOp")
    OperateurDTO toDtoOperateurNomOp(Operateur operateur);
}
