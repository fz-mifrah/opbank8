package com.iga.opbank.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.iga.opbank.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CarteBancaireTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CarteBancaire.class);
        CarteBancaire carteBancaire1 = new CarteBancaire();
        carteBancaire1.setId(1L);
        CarteBancaire carteBancaire2 = new CarteBancaire();
        carteBancaire2.setId(carteBancaire1.getId());
        assertThat(carteBancaire1).isEqualTo(carteBancaire2);
        carteBancaire2.setId(2L);
        assertThat(carteBancaire1).isNotEqualTo(carteBancaire2);
        carteBancaire1.setId(null);
        assertThat(carteBancaire1).isNotEqualTo(carteBancaire2);
    }
}
