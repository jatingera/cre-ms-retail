package com.tenx.ms.retail.store.service;

import com.tenx.ms.commons.util.converter.EntityConverter;
import com.tenx.ms.retail.store.domain.StoreEntity;
import com.tenx.ms.retail.store.repository.StoreRepository;
import com.tenx.ms.retail.store.rest.dto.Store;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static com.tenx.ms.retail.util.RetailUtil.isNumeric;

@Service
public class StoreService {

    private static  final EntityConverter<StoreEntity, Store> STORE_CONVERTER = new EntityConverter<>(StoreEntity.class, Store.class);


    @Autowired
    private StoreRepository storeRepository;

   // @Autowired
    //private CurrentAuthentication currentAuthentication;

    @Transactional
   public Store createStore(Store store) {

       StoreEntity storeEntity = STORE_CONVERTER.toT1(store);

       return  STORE_CONVERTER.toT2(storeRepository.save(storeEntity));
    }

    public List<Store> findAllStores() {

        List<StoreEntity>  storeEntities = storeRepository.findAll();

        return storeEntities.stream().map(STORE_CONVERTER::toT2).collect(Collectors.toList());


    }

    public Store getStore(String store) {

       StoreEntity storeEntity = null;

        if (isNumeric(store)) {
            storeEntity = storeRepository.findOneById(Long.valueOf(store)).orElseThrow(() -> new NoSuchElementException("No store found with this id " + store));
           return STORE_CONVERTER.toT2(storeEntity);
          //  return modelMapper.map(storeEntity, Store.class);
        } else {
            storeEntity =  storeRepository.findOneByName(store).orElseThrow(() -> new NoSuchElementException("No store found with this name "+ store));
           // return modelMapper.map(storeEntity, Store.class);
            return STORE_CONVERTER.toT2(storeEntity);
        }

    }

    public void deleteStore(Long storeId) {
        storeRepository.delete(storeId);
    }

}
