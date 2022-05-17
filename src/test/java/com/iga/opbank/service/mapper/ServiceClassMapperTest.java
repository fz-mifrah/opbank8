package com.iga.opbank.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ServiceClassMapperTest {

    private ServiceClassMapper serviceClassMapper;

    @BeforeEach
    public void setUp() {
        serviceClassMapper = new ServiceClassMapperImpl();
    }
}
