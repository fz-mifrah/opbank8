package com.iga.opbank.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.iga.opbank.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TransferTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Transfer.class);
        Transfer transfer1 = new Transfer();
        transfer1.setId(1L);
        Transfer transfer2 = new Transfer();
        transfer2.setId(transfer1.getId());
        assertThat(transfer1).isEqualTo(transfer2);
        transfer2.setId(2L);
        assertThat(transfer1).isNotEqualTo(transfer2);
        transfer1.setId(null);
        assertThat(transfer1).isNotEqualTo(transfer2);
    }
}
