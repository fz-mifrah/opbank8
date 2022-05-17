package com.iga.opbank.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.iga.opbank.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OperateurTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Operateur.class);
        Operateur operateur1 = new Operateur();
        operateur1.setId(1L);
        Operateur operateur2 = new Operateur();
        operateur2.setId(operateur1.getId());
        assertThat(operateur1).isEqualTo(operateur2);
        operateur2.setId(2L);
        assertThat(operateur1).isNotEqualTo(operateur2);
        operateur1.setId(null);
        assertThat(operateur1).isNotEqualTo(operateur2);
    }
}
