package com.iga.opbank.repository;

import com.iga.opbank.domain.PaimentFacture;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PaimentFacture entity.
 */
@Repository
public interface PaimentFactureRepository extends JpaRepository<PaimentFacture, Long> {
    default Optional<PaimentFacture> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<PaimentFacture> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<PaimentFacture> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct paimentFacture from PaimentFacture paimentFacture left join fetch paimentFacture.serviceClass",
        countQuery = "select count(distinct paimentFacture) from PaimentFacture paimentFacture"
    )
    Page<PaimentFacture> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct paimentFacture from PaimentFacture paimentFacture left join fetch paimentFacture.serviceClass")
    List<PaimentFacture> findAllWithToOneRelationships();

    @Query(
        "select paimentFacture from PaimentFacture paimentFacture left join fetch paimentFacture.serviceClass where paimentFacture.id =:id"
    )
    Optional<PaimentFacture> findOneWithToOneRelationships(@Param("id") Long id);
}
