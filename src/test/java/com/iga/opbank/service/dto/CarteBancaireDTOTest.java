package com.iga.opbank.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.iga.opbank.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CarteBancaireDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CarteBancaireDTO.class);
        CarteBancaireDTO carteBancaireDTO1 = new CarteBancaireDTO();
        carteBancaireDTO1.setId(1L);
        CarteBancaireDTO carteBancaireDTO2 = new CarteBancaireDTO();
        assertThat(carteBancaireDTO1).isNotEqualTo(carteBancaireDTO2);
        carteBancaireDTO2.setId(carteBancaireDTO1.getId());
        assertThat(carteBancaireDTO1).isEqualTo(carteBancaireDTO2);
        carteBancaireDTO2.setId(2L);
        assertThat(carteBancaireDTO1).isNotEqualTo(carteBancaireDTO2);
        carteBancaireDTO1.setId(null);
        assertThat(carteBancaireDTO1).isNotEqualTo(carteBancaireDTO2);
    }
}
