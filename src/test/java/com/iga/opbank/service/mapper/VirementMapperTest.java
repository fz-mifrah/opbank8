package com.iga.opbank.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class VirementMapperTest {

    private VirementMapper virementMapper;

    @BeforeEach
    public void setUp() {
        virementMapper = new VirementMapperImpl();
    }
}
