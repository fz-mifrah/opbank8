package com.iga.opbank.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CarteBancaire.
 */
@Entity
@Table(name = "carte_bancaire")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CarteBancaire implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "num_carte", nullable = false)
    private String numCarte;

    @JsonIgnoreProperties(value = { "carteBancaire", "operations", "virements", "client", "banque" }, allowSetters = true)
    @OneToOne(mappedBy = "carteBancaire")
    private Compte compte;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CarteBancaire id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumCarte() {
        return this.numCarte;
    }

    public CarteBancaire numCarte(String numCarte) {
        this.setNumCarte(numCarte);
        return this;
    }

    public void setNumCarte(String numCarte) {
        this.numCarte = numCarte;
    }

    public Compte getCompte() {
        return this.compte;
    }

    public void setCompte(Compte compte) {
        if (this.compte != null) {
            this.compte.setCarteBancaire(null);
        }
        if (compte != null) {
            compte.setCarteBancaire(this);
        }
        this.compte = compte;
    }

    public CarteBancaire compte(Compte compte) {
        this.setCompte(compte);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CarteBancaire)) {
            return false;
        }
        return id != null && id.equals(((CarteBancaire) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CarteBancaire{" +
            "id=" + getId() +
            ", numCarte='" + getNumCarte() + "'" +
            "}";
    }
}
