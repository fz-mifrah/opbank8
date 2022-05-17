package com.iga.opbank.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.iga.opbank.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PaimentFactureDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaimentFactureDTO.class);
        PaimentFactureDTO paimentFactureDTO1 = new PaimentFactureDTO();
        paimentFactureDTO1.setId(1L);
        PaimentFactureDTO paimentFactureDTO2 = new PaimentFactureDTO();
        assertThat(paimentFactureDTO1).isNotEqualTo(paimentFactureDTO2);
        paimentFactureDTO2.setId(paimentFactureDTO1.getId());
        assertThat(paimentFactureDTO1).isEqualTo(paimentFactureDTO2);
        paimentFactureDTO2.setId(2L);
        assertThat(paimentFactureDTO1).isNotEqualTo(paimentFactureDTO2);
        paimentFactureDTO1.setId(null);
        assertThat(paimentFactureDTO1).isNotEqualTo(paimentFactureDTO2);
    }
}
