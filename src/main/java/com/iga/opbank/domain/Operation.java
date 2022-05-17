package com.iga.opbank.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.iga.opbank.domain.enumeration.EtatOperation;
import com.iga.opbank.domain.enumeration.TypeOperation;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Operation.
 */
@Entity
@Table(name = "operation")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Operation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "num_operation", nullable = false)
    private String numOperation;

    @Column(name = "date")
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_operatin")
    private TypeOperation typeOperatin;

    @Enumerated(EnumType.STRING)
    @Column(name = "etat_operation")
    private EtatOperation etatOperation;

    @NotNull
    @Column(name = "montant", nullable = false)
    private Double montant;

    @JsonIgnoreProperties(value = { "operation", "compte" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Virement virement;

    @JsonIgnoreProperties(value = { "operation" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Transfer transfer;

    @JsonIgnoreProperties(value = { "operateur", "operation" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Recharge recharge;

    @JsonIgnoreProperties(value = { "serviceClass", "operation" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private PaimentFacture paimentFacture;

    @ManyToOne
    @JsonIgnoreProperties(value = { "carteBancaire", "operations", "virements", "client", "banque" }, allowSetters = true)
    private Compte compte;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Operation id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumOperation() {
        return this.numOperation;
    }

    public Operation numOperation(String numOperation) {
        this.setNumOperation(numOperation);
        return this;
    }

    public void setNumOperation(String numOperation) {
        this.numOperation = numOperation;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public Operation date(LocalDate date) {
        this.setDate(date);
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public TypeOperation getTypeOperatin() {
        return this.typeOperatin;
    }

    public Operation typeOperatin(TypeOperation typeOperatin) {
        this.setTypeOperatin(typeOperatin);
        return this;
    }

    public void setTypeOperatin(TypeOperation typeOperatin) {
        this.typeOperatin = typeOperatin;
    }

    public EtatOperation getEtatOperation() {
        return this.etatOperation;
    }

    public Operation etatOperation(EtatOperation etatOperation) {
        this.setEtatOperation(etatOperation);
        return this;
    }

    public void setEtatOperation(EtatOperation etatOperation) {
        this.etatOperation = etatOperation;
    }

    public Double getMontant() {
        return this.montant;
    }

    public Operation montant(Double montant) {
        this.setMontant(montant);
        return this;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public Virement getVirement() {
        return this.virement;
    }

    public void setVirement(Virement virement) {
        this.virement = virement;
    }

    public Operation virement(Virement virement) {
        this.setVirement(virement);
        return this;
    }

    public Transfer getTransfer() {
        return this.transfer;
    }

    public void setTransfer(Transfer transfer) {
        this.transfer = transfer;
    }

    public Operation transfer(Transfer transfer) {
        this.setTransfer(transfer);
        return this;
    }

    public Recharge getRecharge() {
        return this.recharge;
    }

    public void setRecharge(Recharge recharge) {
        this.recharge = recharge;
    }

    public Operation recharge(Recharge recharge) {
        this.setRecharge(recharge);
        return this;
    }

    public PaimentFacture getPaimentFacture() {
        return this.paimentFacture;
    }

    public void setPaimentFacture(PaimentFacture paimentFacture) {
        this.paimentFacture = paimentFacture;
    }

    public Operation paimentFacture(PaimentFacture paimentFacture) {
        this.setPaimentFacture(paimentFacture);
        return this;
    }

    public Compte getCompte() {
        return this.compte;
    }

    public void setCompte(Compte compte) {
        this.compte = compte;
    }

    public Operation compte(Compte compte) {
        this.setCompte(compte);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Operation)) {
            return false;
        }
        return id != null && id.equals(((Operation) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Operation{" +
            "id=" + getId() +
            ", numOperation='" + getNumOperation() + "'" +
            ", date='" + getDate() + "'" +
            ", typeOperatin='" + getTypeOperatin() + "'" +
            ", etatOperation='" + getEtatOperation() + "'" +
            ", montant=" + getMontant() +
            "}";
    }
}
