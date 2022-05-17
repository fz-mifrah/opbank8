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
 * A ServiceClass.
 */
@Entity
@Table(name = "service_class")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ServiceClass implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "nom_service", nullable = false)
    private String nomService;

    @OneToMany(mappedBy = "serviceClass")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "serviceClass", "operation" }, allowSetters = true)
    private Set<PaimentFacture> paimentFactures = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ServiceClass id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomService() {
        return this.nomService;
    }

    public ServiceClass nomService(String nomService) {
        this.setNomService(nomService);
        return this;
    }

    public void setNomService(String nomService) {
        this.nomService = nomService;
    }

    public Set<PaimentFacture> getPaimentFactures() {
        return this.paimentFactures;
    }

    public void setPaimentFactures(Set<PaimentFacture> paimentFactures) {
        if (this.paimentFactures != null) {
            this.paimentFactures.forEach(i -> i.setServiceClass(null));
        }
        if (paimentFactures != null) {
            paimentFactures.forEach(i -> i.setServiceClass(this));
        }
        this.paimentFactures = paimentFactures;
    }

    public ServiceClass paimentFactures(Set<PaimentFacture> paimentFactures) {
        this.setPaimentFactures(paimentFactures);
        return this;
    }

    public ServiceClass addPaimentFacture(PaimentFacture paimentFacture) {
        this.paimentFactures.add(paimentFacture);
        paimentFacture.setServiceClass(this);
        return this;
    }

    public ServiceClass removePaimentFacture(PaimentFacture paimentFacture) {
        this.paimentFactures.remove(paimentFacture);
        paimentFacture.setServiceClass(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ServiceClass)) {
            return false;
        }
        return id != null && id.equals(((ServiceClass) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ServiceClass{" +
            "id=" + getId() +
            ", nomService='" + getNomService() + "'" +
            "}";
    }
}
