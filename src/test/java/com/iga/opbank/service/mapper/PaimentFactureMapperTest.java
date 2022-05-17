package com.iga.opbank.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PaimentFactureMapperTest {

    private PaimentFactureMapper paimentFactureMapper;

    @BeforeEach
    public void setUp() {
        paimentFactureMapper = new PaimentFactureMapperImpl();
    }
}
