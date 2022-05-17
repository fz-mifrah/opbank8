package com.iga.opbank.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.iga.opbank.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RechargeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Recharge.class);
        Recharge recharge1 = new Recharge();
        recharge1.setId(1L);
        Recharge recharge2 = new Recharge();
        recharge2.setId(recharge1.getId());
        assertThat(recharge1).isEqualTo(recharge2);
        recharge2.setId(2L);
        assertThat(recharge1).isNotEqualTo(recharge2);
        recharge1.setId(null);
        assertThat(recharge1).isNotEqualTo(recharge2);
    }
}
