package com.iga.opbank.repository;

import com.iga.opbank.domain.Compte;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Compte entity.
 */
@Repository
public interface CompteRepository extends JpaRepository<Compte, Long> {
    default Optional<Compte> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Compte> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Compte> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct compte from Compte compte left join fetch compte.carteBancaire",
        countQuery = "select count(distinct compte) from Compte compte"
    )
    Page<Compte> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct compte from Compte compte left join fetch compte.carteBancaire")
    List<Compte> findAllWithToOneRelationships();

    @Query("select compte from Compte compte left join fetch compte.carteBancaire where compte.id =:id")
    Optional<Compte> findOneWithToOneRelationships(@Param("id") Long id);
}
