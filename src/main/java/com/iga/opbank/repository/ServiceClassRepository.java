package com.iga.opbank.repository;

import com.iga.opbank.domain.ServiceClass;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ServiceClass entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ServiceClassRepository extends JpaRepository<ServiceClass, Long> {}
