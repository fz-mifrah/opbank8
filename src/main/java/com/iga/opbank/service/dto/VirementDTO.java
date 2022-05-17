package com.iga.opbank.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.iga.opbank.domain.Virement} entity.
 */
public class VirementDTO implements Serializable {

    private Long id;

    private String description;

    private CompteDTO compte;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
        if (!(o instanceof VirementDTO)) {
            return false;
        }

        VirementDTO virementDTO = (VirementDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, virementDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VirementDTO{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", compte=" + getCompte() +
            "}";
    }
}
