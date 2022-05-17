package com.iga.opbank.repository;

import com.iga.opbank.domain.Recharge;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Recharge entity.
 */
@Repository
public interface RechargeRepository extends JpaRepository<Recharge, Long> {
    default Optional<Recharge> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Recharge> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Recharge> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct recharge from Recharge recharge left join fetch recharge.operateur",
        countQuery = "select count(distinct recharge) from Recharge recharge"
    )
    Page<Recharge> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct recharge from Recharge recharge left join fetch recharge.operateur")
    List<Recharge> findAllWithToOneRelationships();

    @Query("select recharge from Recharge recharge left join fetch recharge.operateur where recharge.id =:id")
    Optional<Recharge> findOneWithToOneRelationships(@Param("id") Long id);
}
