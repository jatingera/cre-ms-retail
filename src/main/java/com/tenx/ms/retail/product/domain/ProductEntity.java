package com.tenx.ms.retail.product.domain;

import com.tenx.ms.commons.auditing.AbstractAuditEntity;
import com.tenx.ms.retail.stock.domain.StockEntity;
import com.tenx.ms.retail.store.domain.StoreEntity;
import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Data
@Table(name = "product")
public class ProductEntity extends AbstractAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "product_id")
    private  Long productId;

    @Column(name = "store_id")
    private Long storeId;

    @Column(name = "description")
    private String description;

    @Column(name = "sku")
    private String sku;

    @Column(name = "price")
    private Double price;

    @Column(name = "name")
    private String name;

    @ManyToOne(optional = false)
    @JoinColumn(name = "store_id", insertable = false,  updatable = false)
    private StoreEntity storeEntity;

    @OneToMany(mappedBy = "productEntity", cascade = CascadeType.ALL)
    private List<StockEntity> stockEntities;
}
