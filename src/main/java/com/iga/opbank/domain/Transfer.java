package com.iga.opbank.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Transfer.
 */
@Entity
@Table(name = "transfer")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Transfer implements Serializable {

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
    @Column(name = "nom_prenom", nullable = false)
    private String nomPrenom;

    @Column(name = "tel")
    private Long tel;

    @JsonIgnoreProperties(value = { "virement", "transfer", "recharge", "paimentFacture", "compte" }, allowSetters = true)
    @OneToOne(mappedBy = "transfer")
    private Operation operation;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Transfer id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCin() {
        return this.cin;
    }

    public Transfer cin(String cin) {
        this.setCin(cin);
        return this;
    }

    public void setCin(String cin) {
        this.cin = cin;
    }

    public String getNomPrenom() {
        return this.nomPrenom;
    }

    public Transfer nomPrenom(String nomPrenom) {
        this.setNomPrenom(nomPrenom);
        return this;
    }

    public void setNomPrenom(String nomPrenom) {
        this.nomPrenom = nomPrenom;
    }

    public Long getTel() {
        return this.tel;
    }

    public Transfer tel(Long tel) {
        this.setTel(tel);
        return this;
    }

    public void setTel(Long tel) {
        this.tel = tel;
    }

    public Operation getOperation() {
        return this.operation;
    }

    public void setOperation(Operation operation) {
        if (this.operation != null) {
            this.operation.setTransfer(null);
        }
        if (operation != null) {
            operation.setTransfer(this);
        }
        this.operation = operation;
    }

    public Transfer operation(Operation operation) {
        this.setOperation(operation);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Transfer)) {
            return false;
        }
        return id != null && id.equals(((Transfer) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Transfer{" +
            "id=" + getId() +
            ", cin='" + getCin() + "'" +
            ", nomPrenom='" + getNomPrenom() + "'" +
            ", tel=" + getTel() +
            "}";
    }
}
