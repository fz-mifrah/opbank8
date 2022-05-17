package com.iga.opbank.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.iga.opbank.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VirementDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(VirementDTO.class);
        VirementDTO virementDTO1 = new VirementDTO();
        virementDTO1.setId(1L);
        VirementDTO virementDTO2 = new VirementDTO();
        assertThat(virementDTO1).isNotEqualTo(virementDTO2);
        virementDTO2.setId(virementDTO1.getId());
        assertThat(virementDTO1).isEqualTo(virementDTO2);
        virementDTO2.setId(2L);
        assertThat(virementDTO1).isNotEqualTo(virementDTO2);
        virementDTO1.setId(null);
        assertThat(virementDTO1).isNotEqualTo(virementDTO2);
    }
}
