package com.iga.opbank.service.mapper;

import com.iga.opbank.domain.Compte;
import com.iga.opbank.domain.Operation;
import com.iga.opbank.domain.PaimentFacture;
import com.iga.opbank.domain.Recharge;
import com.iga.opbank.domain.Transfer;
import com.iga.opbank.domain.Virement;
import com.iga.opbank.service.dto.CompteDTO;
import com.iga.opbank.service.dto.OperationDTO;
import com.iga.opbank.service.dto.PaimentFactureDTO;
import com.iga.opbank.service.dto.RechargeDTO;
import com.iga.opbank.service.dto.TransferDTO;
import com.iga.opbank.service.dto.VirementDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Operation} and its DTO {@link OperationDTO}.
 */
@Mapper(componentModel = "spring")
public interface OperationMapper extends EntityMapper<OperationDTO, Operation> {
    @Mapping(target = "virement", source = "virement", qualifiedByName = "virementId")
    @Mapping(target = "transfer", source = "transfer", qualifiedByName = "transferId")
    @Mapping(target = "recharge", source = "recharge", qualifiedByName = "rechargeId")
    @Mapping(target = "paimentFacture", source = "paimentFacture", qualifiedByName = "paimentFactureId")
    @Mapping(target = "compte", source = "compte", qualifiedByName = "compteRib")
    OperationDTO toDto(Operation s);

    @Named("virementId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    VirementDTO toDtoVirementId(Virement virement);

    @Named("transferId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TransferDTO toDtoTransferId(Transfer transfer);

    @Named("rechargeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    RechargeDTO toDtoRechargeId(Recharge recharge);

    @Named("paimentFactureId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PaimentFactureDTO toDtoPaimentFactureId(PaimentFacture paimentFacture);

    @Named("compteRib")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "rib", source = "rib")
    CompteDTO toDtoCompteRib(Compte compte);
}
