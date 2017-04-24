package com.tenx.ms.retail.store.repository;

import com.tenx.ms.retail.store.domain.StoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<StoreEntity, Long> {

    Optional<StoreEntity> findOneById(Long id);

    Optional<StoreEntity> findOneByName(String name);

    @Query(value = "DELETE from  store" , nativeQuery = true)
    @Modifying
    @Transactional
    void truncate();

}
