package com.tenx.ms.retail.product.rest;

import com.sun.org.apache.xerces.internal.util.PropertyState;
import com.tenx.ms.commons.rest.RestConstants;
import com.tenx.ms.commons.rest.dto.ResourceCreated;
import com.tenx.ms.retail.product.rest.dto.Product;
import com.tenx.ms.retail.rest.BaseControllerTest;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.util.List;

import static com.sun.org.apache.xerces.internal.util.PropertyState.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@FixMethodOrder( MethodSorters.NAME_ASCENDING)
public class ProductControllerTest extends BaseControllerTest {

    private static final String REQUEST_URI = "%s" + RestConstants.VERSION_ONE + "/products/2/";

    @Value("classpath:product/create_product_valid_request")
    private File validProductCreation_1;

    @Value("classpath:product/create_product_valid_request_2")
    private File validProductCreation_2;

    @Value("classpath:store/create_store_valid_request")
    private File createStoreValidRequest;




    @Test
    public void test3GetProducts() throws Exception {

        //create the store
        createStore(createStoreValidRequest);

        // add second product for testing the list of products
        createAndValidateProduct(validProductCreation_2);

        ResponseEntity<String> productList =  getJSONResponse(null, REQUEST_URI, HttpMethod.GET,  null);
        assertNotNull(productList);
        assertEquals(HttpStatus.OK, productList.getStatusCode());
        List<ResourceCreated<Long>> productListResponse = parseResponse(productList, List.class);
        assertNotNull(productListResponse);
        assertEquals(2, productListResponse.size());
      //  assertThat(2, is(productListResponse.size()));
    }

    @Test
    public void test2GetProductById() throws Exception {


        ResponseEntity<String> productResponse =  getJSONResponse(null, REQUEST_URI, HttpMethod.GET,  1);
        assertNotNull(productResponse);
        assertEquals(HttpStatus.OK, productResponse.getStatusCode());
        Product product = parseResponse(productResponse, Product.class);
        assertNotNull(product);
        assertNotNull(product.getProductId());
        assertThat(1L, is(product.getProductId()));

    }

    private void assertThat(long l, PropertyState propertyState) {
    }

    @Test
    public void test2GetProductByName() throws Exception {


        ResponseEntity<String> productResponse =  getJSONResponse(null, REQUEST_URI, HttpMethod.GET,  "Test Product 1");
        assertNotNull(productResponse);
        assertEquals(HttpStatus.OK, productResponse.getStatusCode());
        Product product = parseResponse(productResponse, Product.class);
        assertNotNull(product);
        assertNotNull(product.getProductId());
        assertThat(1L, is(product.getProductId()));

    }

    private void createAndValidateProduct(File requestData) throws Exception {

        ResponseEntity<String> productResponse =  getJSONResponse(requestData, REQUEST_URI, HttpMethod.POST,  null);

        assertNotNull(productResponse);
        assertEquals(HttpStatus.OK, productResponse.getStatusCode());

        ResourceCreated<Long> product = parseResponse(productResponse, ResourceCreated.class);
        assertNotNull(product);
        assertNotNull(product.getId());


    }
}