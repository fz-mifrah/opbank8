package com.iga.opbank.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TransferMapperTest {

    private TransferMapper transferMapper;

    @BeforeEach
    public void setUp() {
        transferMapper = new TransferMapperImpl();
    }
}
