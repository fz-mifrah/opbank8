package com.iga.opbank.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.iga.opbank.domain.Compte} entity.
 */
public class CompteDTO implements Serializable {

    private Long id;

    @NotNull
    private Long rib;

    private LocalDate dateOuverture;

    @NotNull
    private Integer code;

    @NotNull
    private Long solde;

    private CarteBancaireDTO carteBancaire;

    private BanqueDTO banque;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRib() {
        return rib;
    }

    public void setRib(Long rib) {
        this.rib = rib;
    }

    public LocalDate getDateOuverture() {
        return dateOuverture;
    }

    public void setDateOuverture(LocalDate dateOuverture) {
        this.dateOuverture = dateOuverture;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Long getSolde() {
        return solde;
    }

    public void setSolde(Long solde) {
        this.solde = solde;
    }

    public CarteBancaireDTO getCarteBancaire() {
        return carteBancaire;
    }

    public void setCarteBancaire(CarteBancaireDTO carteBancaire) {
        this.carteBancaire = carteBancaire;
    }

    public BanqueDTO getBanque() {
        return banque;
    }

    public void setBanque(BanqueDTO banque) {
        this.banque = banque;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CompteDTO)) {
            return false;
        }

        CompteDTO compteDTO = (CompteDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, compteDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CompteDTO{" +
            "id=" + getId() +
            ", rib=" + getRib() +
            ", dateOuverture='" + getDateOuverture() + "'" +
            ", code=" + getCode() +
            ", solde=" + getSolde() +
            ", carteBancaire=" + getCarteBancaire() +
            ", banque=" + getBanque() +
            "}";
    }
}
