package com.iga.opbank.repository;

import com.iga.opbank.domain.Compte;
import com.iga.opbank.domain.Virement;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Virement entity.
 */
@Repository
public interface VirementRepository extends JpaRepository<Virement, Long> {
    default Optional<Virement> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Virement> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Virement> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct virement from Virement virement left join fetch virement.compte",
        countQuery = "select count(distinct virement) from Virement virement"
    )
    Page<Virement> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct virement from Virement virement left join fetch virement.compte")
    List<Virement> findAllWithToOneRelationships();

    @Query("select virement from Virement virement left join fetch virement.compte where virement.id =:id")
    Optional<Virement> findOneWithToOneRelationships(@Param("id") Long id);


    List<Virement> findAllByCompte(Compte compte);
}
