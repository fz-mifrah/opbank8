package com.iga.opbank.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.iga.opbank.domain.PaimentFacture} entity.
 */
public class PaimentFactureDTO implements Serializable {

    private Long id;

    @NotNull
    private Long referance;

    private ServiceClassDTO serviceClass;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getReferance() {
        return referance;
    }

    public void setReferance(Long referance) {
        this.referance = referance;
    }

    public ServiceClassDTO getServiceClass() {
        return serviceClass;
    }

    public void setServiceClass(ServiceClassDTO serviceClass) {
        this.serviceClass = serviceClass;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PaimentFactureDTO)) {
            return false;
        }

        PaimentFactureDTO paimentFactureDTO = (PaimentFactureDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, paimentFactureDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaimentFactureDTO{" +
            "id=" + getId() +
            ", referance=" + getReferance() +
            ", serviceClass=" + getServiceClass() +
            "}";
    }
}
