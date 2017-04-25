package com.tenx.ms.retail.product.repository;

import com.tenx.ms.retail.product.domain.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    List<ProductEntity> findByStoreId(Long storeId);

    ProductEntity findByName(String name);

    Optional<ProductEntity> findOneByProductIdAndStoreId(Long productId, Long storeId);

    ProductEntity findOneByNameAndStoreId(String name, Long storeId);
}
