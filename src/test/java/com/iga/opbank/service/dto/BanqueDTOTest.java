package com.iga.opbank.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.iga.opbank.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BanqueDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BanqueDTO.class);
        BanqueDTO banqueDTO1 = new BanqueDTO();
        banqueDTO1.setId(1L);
        BanqueDTO banqueDTO2 = new BanqueDTO();
        assertThat(banqueDTO1).isNotEqualTo(banqueDTO2);
        banqueDTO2.setId(banqueDTO1.getId());
        assertThat(banqueDTO1).isEqualTo(banqueDTO2);
        banqueDTO2.setId(2L);
        assertThat(banqueDTO1).isNotEqualTo(banqueDTO2);
        banqueDTO1.setId(null);
        assertThat(banqueDTO1).isNotEqualTo(banqueDTO2);
    }
}
