package com.iga.opbank.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BanqueMapperTest {

    private BanqueMapper banqueMapper;

    @BeforeEach
    public void setUp() {
        banqueMapper = new BanqueMapperImpl();
    }
}
