package com.iga.opbank.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.iga.opbank.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RechargeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RechargeDTO.class);
        RechargeDTO rechargeDTO1 = new RechargeDTO();
        rechargeDTO1.setId(1L);
        RechargeDTO rechargeDTO2 = new RechargeDTO();
        assertThat(rechargeDTO1).isNotEqualTo(rechargeDTO2);
        rechargeDTO2.setId(rechargeDTO1.getId());
        assertThat(rechargeDTO1).isEqualTo(rechargeDTO2);
        rechargeDTO2.setId(2L);
        assertThat(rechargeDTO1).isNotEqualTo(rechargeDTO2);
        rechargeDTO1.setId(null);
        assertThat(rechargeDTO1).isNotEqualTo(rechargeDTO2);
    }
}
