package com.iga.opbank.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RechargeMapperTest {

    private RechargeMapper rechargeMapper;

    @BeforeEach
    public void setUp() {
        rechargeMapper = new RechargeMapperImpl();
    }
}
