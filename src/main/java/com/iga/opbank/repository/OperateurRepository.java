package com.iga.opbank.repository;

import com.iga.opbank.domain.Operateur;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Operateur entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OperateurRepository extends JpaRepository<Operateur, Long> {}
