package com.iga.opbank.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.iga.opbank.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PaimentFactureTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaimentFacture.class);
        PaimentFacture paimentFacture1 = new PaimentFacture();
        paimentFacture1.setId(1L);
        PaimentFacture paimentFacture2 = new PaimentFacture();
        paimentFacture2.setId(paimentFacture1.getId());
        assertThat(paimentFacture1).isEqualTo(paimentFacture2);
        paimentFacture2.setId(2L);
        assertThat(paimentFacture1).isNotEqualTo(paimentFacture2);
        paimentFacture1.setId(null);
        assertThat(paimentFacture1).isNotEqualTo(paimentFacture2);
    }
}
