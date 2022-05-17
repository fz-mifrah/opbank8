package com.iga.opbank.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.iga.opbank.domain.ServiceClass} entity.
 */
public class ServiceClassDTO implements Serializable {

    private Long id;

    @NotNull
    private String nomService;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomService() {
        return nomService;
    }

    public void setNomService(String nomService) {
        this.nomService = nomService;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ServiceClassDTO)) {
            return false;
        }

        ServiceClassDTO serviceClassDTO = (ServiceClassDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, serviceClassDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ServiceClassDTO{" +
            "id=" + getId() +
            ", nomService='" + getNomService() + "'" +
            "}";
    }
}
