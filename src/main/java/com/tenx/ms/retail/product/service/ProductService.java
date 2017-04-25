package com.tenx.ms.retail.product.service;

import com.tenx.ms.commons.rest.dto.ResourceCreated;
import com.tenx.ms.commons.util.converter.EntityConverter;
import com.tenx.ms.retail.product.domain.ProductEntity;
import com.tenx.ms.retail.product.repository.ProductRepository;
import com.tenx.ms.retail.product.rest.dto.Product;
import com.tenx.ms.retail.store.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static com.tenx.ms.retail.util.RetailUtil.isNumeric;

@Service
public class ProductService {

    private static final EntityConverter<ProductEntity, Product> PRODUCT_CONVERTER = new EntityConverter<>(ProductEntity.class, Product.class);

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private StoreRepository storeRepository;

    public ResourceCreated<Long> addProduct(Product product, Long storeId) {

        storeRepository.findOneById(storeId).orElseThrow(() -> new NoSuchElementException("invalid store id " + storeId));

        ProductEntity productEntity = PRODUCT_CONVERTER.toT1(product);

        productEntity.setStoreId(storeId);
        return new ResourceCreated<Long>(productRepository.save(productEntity).getProductId());
    }

    public List<Product> getListOfProducts(Long storeId) {
        List<ProductEntity> productEntities = productRepository.findByStoreId(storeId);

        List<Product> productList = productEntities.stream().map(PRODUCT_CONVERTER::toT2).collect(Collectors.toList());

        return productList;
    }

    public Product getProductByIdOrName(String productField, Long storeId) {
        ProductEntity productEntity = new ProductEntity();
        if (isNumeric(productField)) {
            productEntity = productRepository.findOneByProductIdAndStoreId(Long.valueOf(productField), storeId).orElseThrow(() -> new NoSuchElementException("store and product id not found"));
        } else {
            productEntity = productRepository.findOneByNameAndStoreId(productField, storeId);
        }

        return PRODUCT_CONVERTER.toT2(productEntity);
    }
}
