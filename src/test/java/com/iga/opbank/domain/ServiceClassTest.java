package com.iga.opbank.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.iga.opbank.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ServiceClassTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ServiceClass.class);
        ServiceClass serviceClass1 = new ServiceClass();
        serviceClass1.setId(1L);
        ServiceClass serviceClass2 = new ServiceClass();
        serviceClass2.setId(serviceClass1.getId());
        assertThat(serviceClass1).isEqualTo(serviceClass2);
        serviceClass2.setId(2L);
        assertThat(serviceClass1).isNotEqualTo(serviceClass2);
        serviceClass1.setId(null);
        assertThat(serviceClass1).isNotEqualTo(serviceClass2);
    }
}
