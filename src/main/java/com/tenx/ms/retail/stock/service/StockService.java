package com.tenx.ms.retail.stock.service;

import com.tenx.ms.commons.util.converter.EntityConverter;
import com.tenx.ms.retail.product.domain.ProductEntity;
import com.tenx.ms.retail.product.repository.ProductRepository;
import com.tenx.ms.retail.stock.domain.StockEntity;
import com.tenx.ms.retail.stock.repository.StockRepository;
import com.tenx.ms.retail.stock.rest.dto.Stock;
import com.tenx.ms.retail.store.domain.StoreEntity;
import com.tenx.ms.retail.store.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class StockService {

    private static  final EntityConverter<StockEntity, Stock> STOCK_CONVERTER = new EntityConverter<>(StockEntity.class, Stock.class);

    @Autowired
    private StockRepository stockRepository;
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private ProductRepository productRepository;

    public Stock addUpdateStock(int count, Long storeId, Long productId) {

        StockEntity stockEntity = stockRepository.findOneByProductIdAndStoreId(productId, storeId).orElse(null);

        if(stockEntity != null) {
            stockEntity.setCount(count);
            stockRepository.save(stockEntity);
            //return modelMapper.map(stockEntity, Stock.class);
            return  STOCK_CONVERTER.toT2(stockEntity);
        }
        else
        {
            StoreEntity storeEntity = storeRepository.findOneById(storeId).orElseThrow(() -> new NoSuchElementException("store details not found"));
//            stockEntity.setStore(storeEntity);
//
            ProductEntity productEntity  = productRepository.findOneByProductIdAndStoreId(productId, storeId).orElseThrow(() -> new NoSuchElementException("product not exist under this store"));;
//          stockEntity.setProduct(productEntity);

            StockEntity stock = new StockEntity();
            stock.setStoreId(storeId);
            stock.setProductId(productId);
            stock.setCount(count);
            return  STOCK_CONVERTER.toT2(stockRepository.save(stock));
           // return  STOCK_CONVERTER.toT2(stock);
        }


    }
}
