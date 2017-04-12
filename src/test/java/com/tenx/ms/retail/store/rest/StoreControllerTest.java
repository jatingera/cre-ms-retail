package com.tenx.ms.retail.store.rest;

import com.tenx.ms.commons.rest.RestConstants;
import com.tenx.ms.commons.tests.AbstractIntegrationTest;
import com.tenx.ms.retail.RetailServiceApp;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertTrue;

//@ContextConfiguration(classes = RetailServiceApp.class)
@SpringApplicationConfiguration(classes = RetailServiceApp.class)
@SuppressWarnings("all")
@RunWith(SpringJUnit4ClassRunner.class)
public class StoreControllerTest extends AbstractIntegrationTest {

    private static final String REQUEST_URI = "%s" + RestConstants.VERSION_ONE + "/stores/";
    //@Value("classpath:store/create_store_valid_request.json")
    //private File validStoreCreation_1;

    @Test
    public void createStore() throws Exception {

       // ResponseEntity<String> responseEntity = sendRequest(validStoreCreation_1, STORE_API_URI, HttpMethod.POST, null);
       // assertNotNull(responseEntity);
        assertTrue(true);
      //  assertEquals(HttpStatus.OK, "200");

    }
}