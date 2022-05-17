package com.iga.opbank.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.iga.opbank.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VirementTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Virement.class);
        Virement virement1 = new Virement();
        virement1.setId(1L);
        Virement virement2 = new Virement();
        virement2.setId(virement1.getId());
        assertThat(virement1).isEqualTo(virement2);
        virement2.setId(2L);
        assertThat(virement1).isNotEqualTo(virement2);
        virement1.setId(null);
        assertThat(virement1).isNotEqualTo(virement2);
    }
}
