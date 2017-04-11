package com.tenx.ms.retail.store.domain;

import com.tenx.ms.commons.auditing.AbstractAuditEntity;
import com.tenx.ms.retail.product.domain.ProductEntity;
import com.tenx.ms.retail.stock.domain.StockEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "store")
public class StoreEntity extends AbstractAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "store_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "storeEntity", cascade = CascadeType.ALL)
    private Set<ProductEntity> productEntitySet;

    @OneToMany(mappedBy = "storeEntity", cascade = CascadeType.ALL)
    private List<StockEntity> stockEntities;

}
