package com.iga.opbank.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Compte.
 */
@Entity
@Table(name = "compte")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Compte implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "rib", nullable = false)
    private Long rib;

    @Column(name = "date_ouverture")
    private LocalDate dateOuverture;

    @NotNull
    @Column(name = "code", nullable = false)
    private Integer code;

    @NotNull
    @Column(name = "solde", nullable = false)
    private Long solde;

    @JsonIgnoreProperties(value = { "compte" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private CarteBancaire carteBancaire;

    @OneToMany(mappedBy = "compte")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "virement", "transfer", "recharge", "paimentFacture", "compte" }, allowSetters = true)
    private Set<Operation> operations = new HashSet<>();

    @OneToMany(mappedBy = "compte")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "operation", "compte" }, allowSetters = true)
    private Set<Virement> virements = new HashSet<>();

    @JsonIgnoreProperties(value = { "compte" }, allowSetters = true)
    @OneToOne(mappedBy = "compte")
    private Client client;

    @ManyToOne
    @JsonIgnoreProperties(value = { "comptes" }, allowSetters = true)
    private Banque banque;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Compte id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRib() {
        return this.rib;
    }

    public Compte rib(Long rib) {
        this.setRib(rib);
        return this;
    }

    public void setRib(Long rib) {
        this.rib = rib;
    }

    public LocalDate getDateOuverture() {
        return this.dateOuverture;
    }

    public Compte dateOuverture(LocalDate dateOuverture) {
        this.setDateOuverture(dateOuverture);
        return this;
    }

    public void setDateOuverture(LocalDate dateOuverture) {
        this.dateOuverture = dateOuverture;
    }

    public Integer getCode() {
        return this.code;
    }

    public Compte code(Integer code) {
        this.setCode(code);
        return this;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Long getSolde() {
        return this.solde;
    }

    public Compte solde(Long solde) {
        this.setSolde(solde);
        return this;
    }

    public void setSolde(Long solde) {
        this.solde = solde;
    }

    public CarteBancaire getCarteBancaire() {
        return this.carteBancaire;
    }

    public void setCarteBancaire(CarteBancaire carteBancaire) {
        this.carteBancaire = carteBancaire;
    }

    public Compte carteBancaire(CarteBancaire carteBancaire) {
        this.setCarteBancaire(carteBancaire);
        return this;
    }

    public Set<Operation> getOperations() {
        return this.operations;
    }

    public void setOperations(Set<Operation> operations) {
        if (this.operations != null) {
            this.operations.forEach(i -> i.setCompte(null));
        }
        if (operations != null) {
            operations.forEach(i -> i.setCompte(this));
        }
        this.operations = operations;
    }

    public Compte operations(Set<Operation> operations) {
        this.setOperations(operations);
        return this;
    }

    public Compte addOperation(Operation operation) {
        this.operations.add(operation);
        operation.setCompte(this);
        return this;
    }

    public Compte removeOperation(Operation operation) {
        this.operations.remove(operation);
        operation.setCompte(null);
        return this;
    }

    public Set<Virement> getVirements() {
        return this.virements;
    }

    public void setVirements(Set<Virement> virements) {
        if (this.virements != null) {
            this.virements.forEach(i -> i.setCompte(null));
        }
        if (virements != null) {
            virements.forEach(i -> i.setCompte(this));
        }
        this.virements = virements;
    }

    public Compte virements(Set<Virement> virements) {
        this.setVirements(virements);
        return this;
    }

    public Compte addVirement(Virement virement) {
        this.virements.add(virement);
        virement.setCompte(this);
        return this;
    }

    public Compte removeVirement(Virement virement) {
        this.virements.remove(virement);
        virement.setCompte(null);
        return this;
    }

    public Client getClient() {
        return this.client;
    }

    public void setClient(Client client) {
        if (this.client != null) {
            this.client.setCompte(null);
        }
        if (client != null) {
            client.setCompte(this);
        }
        this.client = client;
    }

    public Compte client(Client client) {
        this.setClient(client);
        return this;
    }

    public Banque getBanque() {
        return this.banque;
    }

    public void setBanque(Banque banque) {
        this.banque = banque;
    }

    public Compte banque(Banque banque) {
        this.setBanque(banque);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Compte)) {
            return false;
        }
        return id != null && id.equals(((Compte) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Compte{" +
            "id=" + getId() +
            ", rib=" + getRib() +
            ", dateOuverture='" + getDateOuverture() + "'" +
            ", code=" + getCode() +
            ", solde=" + getSolde() +
            "}";
    }
}
