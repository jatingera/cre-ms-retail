package com.tenx.ms.retail.store.rest;

import com.tenx.ms.commons.rest.RestConstants;
import com.tenx.ms.retail.rest.BaseControllerTest;
import com.tenx.ms.retail.store.rest.dto.Store;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


@FixMethodOrder( MethodSorters.NAME_ASCENDING)
public class StoreControllerTest extends BaseControllerTest {

    private static final String REQUEST_URI = "%s" + RestConstants.VERSION_ONE + "/stores/";

    @Value("classpath:store/create_store_valid_request")
    private File validStoreCreation_1;

    @Value("classpath:store/create_store_valid_request_2")
    private File validStoreCreation_2;

    @Test
    public void test1CreateStore() throws Exception {
       ResponseEntity<String> storeResponse =  getJSONResponse(validStoreCreation_1, REQUEST_URI, HttpMethod.POST,  null);
       assertNotNull(storeResponse);
       assertEquals(HttpStatus.OK, storeResponse.getStatusCode());

       Store store = parseResponse(storeResponse, Store.class);
       assertNotNull(store);
       assertNotNull(store.getId());
    }


    @Test
    public void test2GetStoreByName() throws Exception {

        // get store by name
        ResponseEntity<String> storeResponseByName =  getJSONResponse(null, REQUEST_URI, HttpMethod.GET,  "AmazonTest");
        assertNotNull(storeResponseByName);
        assertEquals(HttpStatus.OK, storeResponseByName.getStatusCode());

        Store store = parseResponse(storeResponseByName, Store.class);
        assertNotNull(store);
        assertNotNull(store.getId());
        assertEquals("AmazonTest", store.getName());
    }

    @Test
    public void test3GetStoreById() throws Exception {


        // get store by Id
        ResponseEntity<String> storeResponseById =  getJSONResponse(null, REQUEST_URI, HttpMethod.GET,  1L);
        assertNotNull(storeResponseById);
        assertEquals(HttpStatus.OK, storeResponseById.getStatusCode());

        Store getStore = parseResponse(storeResponseById, Store.class);
        assertNotNull(getStore);
        assertNotNull(getStore.getId());
        assertEquals(1L, getStore.getId().longValue());
    }

    @Test
    public void test4GetAllStores() throws Exception {
           // create second store
        ResponseEntity<String> storeResponse =  getJSONResponse(validStoreCreation_2, REQUEST_URI, HttpMethod.POST,  null);
        assertNotNull(storeResponse);
        assertEquals(HttpStatus.OK, storeResponse.getStatusCode());

        ResponseEntity<String> storeList =  getJSONResponse(null, REQUEST_URI, HttpMethod.GET,  null);
        assertNotNull(storeList);
        assertEquals(HttpStatus.OK, storeList.getStatusCode());
        List<Store> storeListResponse = parseResponse(storeList, List.class);
        assertNotNull(storeListResponse);
        assertEquals(2, storeListResponse.size());

    }

    @Test
    public void test5DeleteStoreById() throws Exception {

        // get store by Id
        ResponseEntity<String> storeResponseById =  getJSONResponse(null, REQUEST_URI, HttpMethod.DELETE,  1L);
        assertNotNull(storeResponseById);
        assertEquals(HttpStatus.OK, storeResponseById.getStatusCode());



    }


}