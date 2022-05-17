package com.iga.opbank.service.mapper;

import com.iga.opbank.domain.Transfer;
import com.iga.opbank.service.dto.TransferDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Transfer} and its DTO {@link TransferDTO}.
 */
@Mapper(componentModel = "spring")
public interface TransferMapper extends EntityMapper<TransferDTO, Transfer> {}
