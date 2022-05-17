package com.iga.opbank.repository;

import com.iga.opbank.domain.CarteBancaire;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CarteBancaire entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CarteBancaireRepository extends JpaRepository<CarteBancaire, Long> {}
