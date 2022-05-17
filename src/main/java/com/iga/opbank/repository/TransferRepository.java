package com.iga.opbank.repository;

import com.iga.opbank.domain.Transfer;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Transfer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TransferRepository extends JpaRepository<Transfer, Long> {}
