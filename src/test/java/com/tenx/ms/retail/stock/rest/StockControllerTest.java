package com.tenx.ms.retail.stock.rest;

import com.tenx.ms.commons.rest.RestConstants;
import com.tenx.ms.retail.rest.BaseControllerTest;
import org.springframework.beans.factory.annotation.Value;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.File;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;

@FixMethodOrder( MethodSorters.NAME_ASCENDING)
public class StockControllerTest extends BaseControllerTest {


   private static final String REQUEST_URI = "%s" + RestConstants.VERSION_ONE + "/stock/2/";

    @Value("classpath:stock/add_new_stock")
    private File addStock;

    @Value("classpath:stock/update_stock")
    private File updateStock;



    @Test
    public void test1AddStock() throws Exception {

        ResponseEntity<String> stockResponse =  getJSONResponse(addStock, REQUEST_URI, HttpMethod.POST,  1L);

        assertNotNull(stockResponse);
        assertEquals(HttpStatus.OK, stockResponse.getStatusCode());

        // add second product stock
        ResponseEntity<String> stockResponse2 =  getJSONResponse(addStock, REQUEST_URI, HttpMethod.POST,  2L);

        assertNotNull(stockResponse2);
        assertEquals(HttpStatus.OK, stockResponse2.getStatusCode());

    }

    @Test
    public void test2UpdateStock() throws Exception {

        ResponseEntity<String> stockResponse =  getJSONResponse(updateStock, REQUEST_URI, HttpMethod.POST,  1L);

        assertNotNull(stockResponse);
        assertEquals(HttpStatus.OK, stockResponse.getStatusCode());

    }
}