package com.iga.opbank.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CarteBancaireMapperTest {

    private CarteBancaireMapper carteBancaireMapper;

    @BeforeEach
    public void setUp() {
        carteBancaireMapper = new CarteBancaireMapperImpl();
    }
}
