package com.iga.opbank.service.mapper;

import com.iga.opbank.domain.Banque;
import com.iga.opbank.service.dto.BanqueDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Banque} and its DTO {@link BanqueDTO}.
 */
@Mapper(componentModel = "spring")
public interface BanqueMapper extends EntityMapper<BanqueDTO, Banque> {}
