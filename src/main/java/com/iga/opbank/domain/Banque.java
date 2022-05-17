package com.iga.opbank.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Banque.
 */
@Entity
@Table(name = "banque")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Banque implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "nom", nullable = false)
    private String nom;

    @Column(name = "adresse")
    private String adresse;

    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @OneToMany(mappedBy = "banque")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "carteBancaire", "operations", "virements", "client", "banque" }, allowSetters = true)
    private Set<Compte> comptes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Banque id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return this.nom;
    }

    public Banque nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getAdresse() {
        return this.adresse;
    }

    public Banque adresse(String adresse) {
        this.setAdresse(adresse);
        return this;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getEmail() {
        return this.email;
    }

    public Banque email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Compte> getComptes() {
        return this.comptes;
    }

    public void setComptes(Set<Compte> comptes) {
        if (this.comptes != null) {
            this.comptes.forEach(i -> i.setBanque(null));
        }
        if (comptes != null) {
            comptes.forEach(i -> i.setBanque(this));
        }
        this.comptes = comptes;
    }

    public Banque comptes(Set<Compte> comptes) {
        this.setComptes(comptes);
        return this;
    }

    public Banque addCompte(Compte compte) {
        this.comptes.add(compte);
        compte.setBanque(this);
        return this;
    }

    public Banque removeCompte(Compte compte) {
        this.comptes.remove(compte);
        compte.setBanque(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Banque)) {
            return false;
        }
        return id != null && id.equals(((Banque) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Banque{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", adresse='" + getAdresse() + "'" +
            ", email='" + getEmail() + "'" +
            "}";
    }
}
