package com.iga.opbank.repository;

import com.iga.opbank.domain.Banque;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Banque entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BanqueRepository extends JpaRepository<Banque, Long> {}
