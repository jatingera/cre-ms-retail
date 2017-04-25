package com.tenx.ms.retail.stock.repository;

import com.tenx.ms.retail.stock.domain.StockEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockRepository extends JpaRepository<StockEntity, Long> {

    StockEntity findOneByProductIdAndStoreId(Long productId, Long storeId);
}
