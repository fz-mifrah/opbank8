package com.iga.opbank.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.iga.opbank.domain.Client} entity.
 */
public class ClientDTO implements Serializable {

    private Long id;

    @NotNull
    private String cin;

    @NotNull
    private String nom;

    @NotNull
    private String prenom;

    @NotNull
    private String adress;

    @NotNull
    private Long postalCode;

    @NotNull
    private LocalDate dateNaissence;

    @NotNull
    private String email;

    private CompteDTO compte;

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

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public Long getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(Long postalCode) {
        this.postalCode = postalCode;
    }

    public LocalDate getDateNaissence() {
        return dateNaissence;
    }

    public void setDateNaissence(LocalDate dateNaissence) {
        this.dateNaissence = dateNaissence;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public CompteDTO getCompte() {
        return compte;
    }

    public void setCompte(CompteDTO compte) {
        this.compte = compte;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ClientDTO)) {
            return false;
        }

        ClientDTO clientDTO = (ClientDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, clientDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClientDTO{" +
            "id=" + getId() +
            ", cin='" + getCin() + "'" +
            ", nom='" + getNom() + "'" +
            ", prenom='" + getPrenom() + "'" +
            ", adress='" + getAdress() + "'" +
            ", postalCode=" + getPostalCode() +
            ", dateNaissence='" + getDateNaissence() + "'" +
            ", email='" + getEmail() + "'" +
            ", compte=" + getCompte() +
            "}";
    }
}
