package com.iga.opbank.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.iga.opbank.domain.Transfer} entity.
 */
public class TransferDTO implements Serializable {

    private Long id;

    @NotNull
    private String cin;

    @NotNull
    private String nomPrenom;

    private Long tel;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCin() {
        return cin;
    }

    public void setCin(String cin) {
        this.cin = cin;
    }

    public String getNomPrenom() {
        return nomPrenom;
    }

    public void setNomPrenom(String nomPrenom) {
        this.nomPrenom = nomPrenom;
    }

    public Long getTel() {
        return tel;
    }

    public void setTel(Long tel) {
        this.tel = tel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TransferDTO)) {
            return false;
        }

        TransferDTO transferDTO = (TransferDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, transferDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TransferDTO{" +
            "id=" + getId() +
            ", cin='" + getCin() + "'" +
            ", nomPrenom='" + getNomPrenom() + "'" +
            ", tel=" + getTel() +
            "}";
    }
}
