package com.iga.opbank.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Client.
 */
@Entity
@Table(name = "client")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Client implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "cin", nullable = false)
    private String cin;

    @NotNull
    @Column(name = "nom", nullable = false)
    private String nom;

    @NotNull
    @Column(name = "prenom", nullable = false)
    private String prenom;

    @NotNull
    @Column(name = "adress", nullable = false)
    private String adress;

    @NotNull
    @Column(name = "postal_code", nullable = false)
    private Long postalCode;

    @NotNull
    @Column(name = "date_naissence", nullable = false)
    private LocalDate dateNaissence;

    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @JsonIgnoreProperties(value = { "carteBancaire", "operations", "virements", "client", "banque" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Compte compte;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Client id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCin() {
        return this.cin;
    }

    public Client cin(String cin) {
        this.setCin(cin);
        return this;
    }

    public void setCin(String cin) {
        this.cin = cin;
    }

    public String getNom() {
        return this.nom;
    }

    public Client nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return this.prenom;
    }

    public Client prenom(String prenom) {
        this.setPrenom(prenom);
        return this;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getAdress() {
        return this.adress;
    }

    public Client adress(String adress) {
        this.setAdress(adress);
        return this;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public Long getPostalCode() {
        return this.postalCode;
    }

    public Client postalCode(Long postalCode) {
        this.setPostalCode(postalCode);
        return this;
    }

    public void setPostalCode(Long postalCode) {
        this.postalCode = postalCode;
    }

    public LocalDate getDateNaissence() {
        return this.dateNaissence;
    }

    public Client dateNaissence(LocalDate dateNaissence) {
        this.setDateNaissence(dateNaissence);
        return this;
    }

    public void setDateNaissence(LocalDate dateNaissence) {
        this.dateNaissence = dateNaissence;
    }

    public String getEmail() {
        return this.email;
    }

    public Client email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Compte getCompte() {
        return this.compte;
    }

    public void setCompte(Compte compte) {
        this.compte = compte;
    }

    public Client compte(Compte compte) {
        this.setCompte(compte);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Client)) {
            return false;
        }
        return id != null && id.equals(((Client) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Client{" +
            "id=" + getId() +
            ", cin='" + getCin() + "'" +
            ", nom='" + getNom() + "'" +
            ", prenom='" + getPrenom() + "'" +
            ", adress='" + getAdress() + "'" +
            ", postalCode=" + getPostalCode() +
            ", dateNaissence='" + getDateNaissence() + "'" +
            ", email='" + getEmail() + "'" +
            "}";
    }
}
