package com.iga.opbank.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.iga.opbank.domain.CarteBancaire} entity.
 */
public class CarteBancaireDTO implements Serializable {

    private Long id;

    @NotNull
    private String numCarte;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumCarte() {
        return numCarte;
    }

    public void setNumCarte(String numCarte) {
        this.numCarte = numCarte;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CarteBancaireDTO)) {
            return false;
        }

        CarteBancaireDTO carteBancaireDTO = (CarteBancaireDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, carteBancaireDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CarteBancaireDTO{" +
            "id=" + getId() +
            ", numCarte='" + getNumCarte() + "'" +
            "}";
    }
}
