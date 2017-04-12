package com.tenx.ms.retail.store.service;

import com.tenx.ms.commons.util.converter.EntityConverter;
import com.tenx.ms.retail.store.domain.StoreEntity;
import com.tenx.ms.retail.store.repository.StoreRepository;
import com.tenx.ms.retail.store.rest.dto.Store;
import com.tenx.ms.retail.util.RetailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class StoreService {

    private static  final EntityConverter<StoreEntity, Store> STORE_CONVERTER = new EntityConverter<>(StoreEntity.class, Store.class);


    @Autowired
    StoreRepository storeRepository;

   // @Autowired
    //private CurrentAuthentication currentAuthentication;

    @Transactional
   public Long createStore(Store store) {

       StoreEntity storeEntity = STORE_CONVERTER.toT1(store);

       return  storeRepository.save(storeEntity).getId();
    }

    public List<Store> findAllStores() {

        List<StoreEntity>  storeEntities = storeRepository.findAll();

        return storeEntities.stream().map(STORE_CONVERTER::toT2).collect(Collectors.toList());
    }

    public Store getStore(String store) {

       StoreEntity storeEntity = null;

        if (RetailUtil.isNumeric(store)) {
            storeEntity = storeRepository.findOneById(Long.valueOf(store)).orElseThrow(() -> new NoSuchElementException("No store found with this id"));
           return STORE_CONVERTER.toT2(storeEntity);
          //  return modelMapper.map(storeEntity, Store.class);
        } else {
            storeEntity =  storeRepository.findOneByName(store).orElseThrow(() -> new NoSuchElementException("No store found with this name"));
           // return modelMapper.map(storeEntity, Store.class);
            return STORE_CONVERTER.toT2(storeEntity);
        }

    }

    public void deleteStore(Long storeId) {
        storeRepository.delete(storeId);
    }

}
