package com.tenx.ms.retail.store.rest;

import com.tenx.ms.commons.rest.RestConstants;
import com.tenx.ms.retail.rest.BaseControllerTest;
import com.tenx.ms.retail.store.rest.dto.Store;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.io.File;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class StoreControllerTest extends BaseControllerTest {

    private static final String REQUEST_URL = "%s" + RestConstants.VERSION_ONE + "/stores/";
    @Autowired
    DataSource dataSource;
    @Value( "classpath:store/create_store_valid_request" )
    private File validStoreCreation_1;
    @Value( "classpath:store/create_store_valid_request_2" )
    private File validStoreCreation_2;

    @Before
    public void setUp() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 0;");
        jdbcTemplate.execute("TRUNCATE TABLE store");
    }

    @Test
    public void testCreateStore() throws Exception {

        ResponseEntity<String> storeResponse = getJSONResponse(validStoreCreation_1, REQUEST_URL, HttpMethod.POST, null);
        assertNotNull(storeResponse);
        assertEquals(HttpStatus.OK, storeResponse.getStatusCode());

        Store store = parseResponse(storeResponse, Store.class);
        assertNotNull(store);
        assertNotNull(store.getId());
    }

    @Test
    public void testGetStoreByName() throws Exception {

        // create store
        createStore(validStoreCreation_1);

        // get store by name
        ResponseEntity<String> storeResponseByName = getJSONResponse(null, REQUEST_URL, HttpMethod.GET, "AmazonTest");
        assertNotNull(storeResponseByName);
        System.out.println(storeResponseByName);
        assertEquals(HttpStatus.OK, storeResponseByName.getStatusCode());

        Store store = parseResponse(storeResponseByName, Store.class);
        assertNotNull(store);
        assertNotNull(store.getId());
        assertEquals("AmazonTest", store.getName());
    }

    @Test
    public void testGetStoreById() throws Exception {

        createStore(validStoreCreation_1);

        // get store by Id
        ResponseEntity<String> storeResponseById = getJSONResponse(null, REQUEST_URL, HttpMethod.GET, 1L);
        assertNotNull(storeResponseById);
        assertEquals(HttpStatus.OK, storeResponseById.getStatusCode());

        Store getStore = parseResponse(storeResponseById, Store.class);
        assertNotNull(getStore);
        assertNotNull(getStore.getId());
        assertEquals(1L, getStore.getId().longValue());
    }

    @Test
    public void testGetAllStores() throws Exception {
        // create 1st store
        createStore(validStoreCreation_1);

        // create 2nd store
        createStore(validStoreCreation_2);

        ResponseEntity<String> storeList = getJSONResponse(null, REQUEST_URL, HttpMethod.GET, null);
        assertNotNull(storeList);
        assertEquals(HttpStatus.OK, storeList.getStatusCode());
        List<Store> storeListResponse = parseResponse(storeList, List.class);
        assertNotNull(storeListResponse);
        assertEquals(2, storeListResponse.size());
    }

    @Test
    public void testDeleteStoreById() throws Exception {

        // create store
        createStore(validStoreCreation_1);

        // delete store by Id
        ResponseEntity<String> storeResponseById = getJSONResponse(null, REQUEST_URL, HttpMethod.DELETE, 1L);
        assertNotNull(storeResponseById);
        assertEquals(HttpStatus.OK, storeResponseById.getStatusCode());
    }
}