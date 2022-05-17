package com.iga.opbank.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.iga.opbank.domain.Operateur} entity.
 */
public class OperateurDTO implements Serializable {

    private Long id;

    @NotNull
    private String nomOp;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomOp() {
        return nomOp;
    }

    public void setNomOp(String nomOp) {
        this.nomOp = nomOp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OperateurDTO)) {
            return false;
        }

        OperateurDTO operateurDTO = (OperateurDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, operateurDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OperateurDTO{" +
            "id=" + getId() +
            ", nomOp='" + getNomOp() + "'" +
            "}";
    }
}
