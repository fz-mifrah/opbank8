package com.iga.opbank.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.iga.opbank.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TransferDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TransferDTO.class);
        TransferDTO transferDTO1 = new TransferDTO();
        transferDTO1.setId(1L);
        TransferDTO transferDTO2 = new TransferDTO();
        assertThat(transferDTO1).isNotEqualTo(transferDTO2);
        transferDTO2.setId(transferDTO1.getId());
        assertThat(transferDTO1).isEqualTo(transferDTO2);
        transferDTO2.setId(2L);
        assertThat(transferDTO1).isNotEqualTo(transferDTO2);
        transferDTO1.setId(null);
        assertThat(transferDTO1).isNotEqualTo(transferDTO2);
    }
}
