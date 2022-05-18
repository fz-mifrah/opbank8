package com.iga.opbank.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.iga.opbank.domain.Virement} entity.
 */

public class VirementCompteACompteDTO {
    
    private Long monCompte;
    private Long compteDestinataire;
    private Long montant;

    private String label;

    public Long getMonCompte() {
        return monCompte;
    }

    public void setMonCompte(Long monCompte) {
        this.monCompte = monCompte;
    }

    public Long getCompteDestinataire() {
        return compteDestinataire;
    }

    public void setCompteDestinataire(Long compteDestinataire) {
        this.compteDestinataire = compteDestinataire;
    }

    public Long getMontant() {
        return montant;
    }

    public void setMontant(Long montant) {
        this.montant = montant;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
 
}