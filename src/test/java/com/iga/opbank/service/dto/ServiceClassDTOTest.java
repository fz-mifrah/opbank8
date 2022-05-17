package com.iga.opbank.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.iga.opbank.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ServiceClassDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ServiceClassDTO.class);
        ServiceClassDTO serviceClassDTO1 = new ServiceClassDTO();
        serviceClassDTO1.setId(1L);
        ServiceClassDTO serviceClassDTO2 = new ServiceClassDTO();
        assertThat(serviceClassDTO1).isNotEqualTo(serviceClassDTO2);
        serviceClassDTO2.setId(serviceClassDTO1.getId());
        assertThat(serviceClassDTO1).isEqualTo(serviceClassDTO2);
        serviceClassDTO2.setId(2L);
        assertThat(serviceClassDTO1).isNotEqualTo(serviceClassDTO2);
        serviceClassDTO1.setId(null);
        assertThat(serviceClassDTO1).isNotEqualTo(serviceClassDTO2);
    }
}
