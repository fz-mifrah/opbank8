package com.iga.opbank.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.iga.opbank.domain.Recharge} entity.
 */
public class RechargeDTO implements Serializable {

    private Long id;

    @NotNull
    private Long numTel;

    private OperateurDTO operateur;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNumTel() {
        return numTel;
    }

    public void setNumTel(Long numTel) {
        this.numTel = numTel;
    }

    public OperateurDTO getOperateur() {
        return operateur;
    }

    public void setOperateur(OperateurDTO operateur) {
        this.operateur = operateur;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RechargeDTO)) {
            return false;
        }

        RechargeDTO rechargeDTO = (RechargeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, rechargeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RechargeDTO{" +
            "id=" + getId() +
            ", numTel=" + getNumTel() +
            ", operateur=" + getOperateur() +
            "}";
    }
}
