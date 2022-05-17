package com.iga.opbank.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.iga.opbank.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OperateurDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OperateurDTO.class);
        OperateurDTO operateurDTO1 = new OperateurDTO();
        operateurDTO1.setId(1L);
        OperateurDTO operateurDTO2 = new OperateurDTO();
        assertThat(operateurDTO1).isNotEqualTo(operateurDTO2);
        operateurDTO2.setId(operateurDTO1.getId());
        assertThat(operateurDTO1).isEqualTo(operateurDTO2);
        operateurDTO2.setId(2L);
        assertThat(operateurDTO1).isNotEqualTo(operateurDTO2);
        operateurDTO1.setId(null);
        assertThat(operateurDTO1).isNotEqualTo(operateurDTO2);
    }
}
