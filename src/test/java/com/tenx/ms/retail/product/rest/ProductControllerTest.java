package com.tenx.ms.retail.product.rest;

import com.tenx.ms.commons.rest.RestConstants;
import com.tenx.ms.commons.rest.dto.ResourceCreated;
import com.tenx.ms.retail.product.rest.dto.Product;
import com.tenx.ms.retail.rest.BaseControllerTest;
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

public class ProductControllerTest extends BaseControllerTest {

    private static final String REQUEST_URI = "%s" + RestConstants.VERSION_ONE + "/products/";

    @Value("classpath:product/create_product_valid_request")
    private File validProductCreation_1;

    @Value("classpath:product/create_product_valid_request_2")
    private File validProductCreation_2;

    @Value("classpath:store/create_store_valid_request")
    private File createStoreValidRequest;

    @Autowired
    DataSource dataSource;

    @Before
    public void setUp() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 0;");
        jdbcTemplate.execute("TRUNCATE TABLE store");
        jdbcTemplate.execute("TRUNCATE table product");
    }
    @Test
    public void testAddProduct() throws Exception {

        //create the store
        createStore(createStoreValidRequest);

        createAndValidateProduct(validProductCreation_1, 1L);


    }

    @Test
    public void testGetProductById() throws Exception {

        //create the store
        createStore(createStoreValidRequest);

        createAndValidateProduct(validProductCreation_1, 1L);

        Long[] storeIdProductId = { 1L,1L};


        ResponseEntity<String> productResponse =  getJSONResponse(null, REQUEST_URI, HttpMethod.GET, storeIdProductId);
        assertNotNull(productResponse);
        assertEquals(HttpStatus.OK, productResponse.getStatusCode());
        Product product = parseResponse(productResponse, Product.class);
        assertNotNull(product);
        assertNotNull(product.getProductId());
        assertEquals(1L, product.getProductId().longValue());

    }

    @Test
    public void testGetProductByName() throws Exception {

        //create the store
        createStore(createStoreValidRequest);

        createAndValidateProduct(validProductCreation_1, 1L);

        Object[] storeIdProductName = { 1L, "Test Product 1" };

        ResponseEntity<String> productResponse =  getJSONResponse(null, REQUEST_URI, HttpMethod.GET,  storeIdProductName);
        assertNotNull(productResponse);
        assertEquals(HttpStatus.OK, productResponse.getStatusCode());
        Product product = parseResponse(productResponse, Product.class);
        assertNotNull(product);
        assertNotNull(product.getProductId());
        assertEquals(1L, product.getProductId().longValue());
        assertEquals("Test Product 1", product.getName());

    }


    @Test
    public void testGetProducts() throws Exception {

        //create the store
        createStore(createStoreValidRequest);

        // add 1st product for testing the list of products
        createAndValidateProduct(validProductCreation_1, 1L);

        // add second product for testing the list of products
        createAndValidateProduct(validProductCreation_2, 1L);

        ResponseEntity<String> productList =  getJSONResponse(null, REQUEST_URI, HttpMethod.GET,  1L);
        assertNotNull(productList);
        assertEquals(HttpStatus.OK, productList.getStatusCode());
        List<ResourceCreated<Long>> productListResponse = parseResponse(productList, List.class);
        assertNotNull(productListResponse);
        assertEquals(2, productListResponse.size());
      //  assertThat(2, is(productListResponse.size()));
    }



}