package com.iga.opbank.service.dto;

import com.iga.opbank.domain.enumeration.EtatOperation;
import com.iga.opbank.domain.enumeration.TypeOperation;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.iga.opbank.domain.Operation} entity.
 */
public class OperationDTO implements Serializable {

    private Long id;

    @NotNull
    private String numOperation;

    private LocalDate date;

    private TypeOperation typeOperatin;

    private EtatOperation etatOperation;

    @NotNull
    private Double montant;

    private VirementDTO virement;

    private TransferDTO transfer;

    private RechargeDTO recharge;

    private PaimentFactureDTO paimentFacture;

    private CompteDTO compte;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumOperation() {
        return numOperation;
    }

    public void setNumOperation(String numOperation) {
        this.numOperation = numOperation;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public TypeOperation getTypeOperatin() {
        return typeOperatin;
    }

    public void setTypeOperatin(TypeOperation typeOperatin) {
        this.typeOperatin = typeOperatin;
    }

    public EtatOperation getEtatOperation() {
        return etatOperation;
    }

    public void setEtatOperation(EtatOperation etatOperation) {
        this.etatOperation = etatOperation;
    }

    public Double getMontant() {
        return montant;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public VirementDTO getVirement() {
        return virement;
    }

    public void setVirement(VirementDTO virement) {
        this.virement = virement;
    }

    public TransferDTO getTransfer() {
        return transfer;
    }

    public void setTransfer(TransferDTO transfer) {
        this.transfer = transfer;
    }

    public RechargeDTO getRecharge() {
        return recharge;
    }

    public void setRecharge(RechargeDTO recharge) {
        this.recharge = recharge;
    }

    public PaimentFactureDTO getPaimentFacture() {
        return paimentFacture;
    }

    public void setPaimentFacture(PaimentFactureDTO paimentFacture) {
        this.paimentFacture = paimentFacture;
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
        if (!(o instanceof OperationDTO)) {
            return false;
        }

        OperationDTO operationDTO = (OperationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, operationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OperationDTO{" +
            "id=" + getId() +
            ", numOperation='" + getNumOperation() + "'" +
            ", date='" + getDate() + "'" +
            ", typeOperatin='" + getTypeOperatin() + "'" +
            ", etatOperation='" + getEtatOperation() + "'" +
            ", montant=" + getMontant() +
            ", virement=" + getVirement() +
            ", transfer=" + getTransfer() +
            ", recharge=" + getRecharge() +
            ", paimentFacture=" + getPaimentFacture() +
            ", compte=" + getCompte() +
            "}";
    }
}
