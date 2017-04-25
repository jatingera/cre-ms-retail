package com.tenx.ms.retail.stock.domain;

import com.tenx.ms.commons.auditing.AbstractAuditEntity;
import com.tenx.ms.retail.product.domain.ProductEntity;
import com.tenx.ms.retail.store.domain.StoreEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.NumberFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table( name = "stock", uniqueConstraints = @UniqueConstraint( columnNames = {"store_id", "product_id"} ) )
@Getter
@Setter
public class StockEntity extends AbstractAuditEntity {

    @Id
    @GeneratedValue( strategy = GenerationType.AUTO )
    @Column( name = "stock_id" )
    private Long stockId;

    @Column( name = "count" )
    @NumberFormat
    private int count;

    @Column( name = "product_id" )
    @NumberFormat
    private Long productId;

    @Column( name = "store_id" )
    @NumberFormat
    private Long storeId;

    @ManyToOne( optional = false )
    @JoinColumn( name = "store_id", nullable = false, updatable = false, insertable = false )
    private StoreEntity storeEntity;

    @ManyToOne( optional = false )
    @JoinColumn( name = "product_id", nullable = false, updatable = false, insertable = false )
    private ProductEntity productEntity;
}
