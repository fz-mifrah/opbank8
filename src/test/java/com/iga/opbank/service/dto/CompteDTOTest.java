package com.iga.opbank.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.iga.opbank.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CompteDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CompteDTO.class);
        CompteDTO compteDTO1 = new CompteDTO();
        compteDTO1.setId(1L);
        CompteDTO compteDTO2 = new CompteDTO();
        assertThat(compteDTO1).isNotEqualTo(compteDTO2);
        compteDTO2.setId(compteDTO1.getId());
        assertThat(compteDTO1).isEqualTo(compteDTO2);
        compteDTO2.setId(2L);
        assertThat(compteDTO1).isNotEqualTo(compteDTO2);
        compteDTO1.setId(null);
        assertThat(compteDTO1).isNotEqualTo(compteDTO2);
    }
}
