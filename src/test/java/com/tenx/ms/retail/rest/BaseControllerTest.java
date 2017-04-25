package com.tenx.ms.retail.rest;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.tenx.ms.commons.config.Profiles;
import com.tenx.ms.commons.rest.RestConstants;
import com.tenx.ms.commons.rest.dto.ResourceCreated;
import com.tenx.ms.retail.product.repository.ProductRepository;
import com.tenx.ms.retail.store.repository.StoreRepository;
import com.tenx.ms.retail.store.rest.dto.Store;
import com.tenx.ms.test.rest.BaseIntegrationTest;
import junit.framework.TestCase;
import org.apache.commons.io.FileUtils;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith( SpringJUnit4ClassRunner.class )
@SuppressWarnings( "PMD" )
@ActiveProfiles( value = {Profiles.TEST_NOAUTH, "profile-controller"} )
public abstract class BaseControllerTest extends BaseIntegrationTest {

    protected static final String STORE_API_URL = "%s" + RestConstants.VERSION_ONE + "/stores/";

    private static final String PRODUCT_API_URL = "%s" + RestConstants.VERSION_ONE + "/products/";

    private static final String STOCK_API_URL = "%s" + RestConstants.VERSION_ONE + "/stock/";

    @Autowired
    protected StoreRepository storeRepository;

    @Autowired
    protected ProductRepository productRepository;


    protected ResponseEntity<String> getJSONResponse(
            File json,
            String uri,
            HttpMethod httpMethod,
            Object... l)
            throws Exception {

        String url = buildUrl(uri, l);
        String payload = null;
        if (json != null && json.exists()) {
            payload = getPayload(json);
        }
        return getResponse(url, payload, httpMethod, new ParameterizedTypeReference<String>() {
        });

    }

    private String buildUrl(String uri, Object... pathVars) throws Exception {
        String basePath = getBasePath();
        String reqUri = String.format(uri, basePath);
        if (pathVars != null && pathVars.length > 0) {
            for (Object pathVar : pathVars) {
                if (pathVar != null) {
                    reqUri = String.format(reqUri + "%s/", pathVar);
                }
            }
        }
        return reqUri;
    }

    protected String getPayload(File file) throws IOException {
        return FileUtils.readFileToString(file);
    }

    protected <T> T parseResponse(ResponseEntity<String> responseEntity, Class<T> valueType)
            throws IOException, JsonParseException, JsonMappingException {
        return mapper.readValue(responseEntity.getBody(), valueType);
    }

    protected Store createStore(File storeCreateRequest) throws Exception {

        ResponseEntity<String> storeResponse = getJSONResponse(storeCreateRequest, STORE_API_URL, HttpMethod.POST, null);
        assertNotNull(storeResponse);
        assertEquals(HttpStatus.OK, storeResponse.getStatusCode());
        Store store = parseResponse(storeResponse, Store.class);
        assertNotNull(store);
        assertNotNull(store.getId());
        return store;
    }

    protected void createAndValidateProduct(File requestData, Long storeId) throws Exception {

        ResponseEntity<String> productResponse = getJSONResponse(requestData, PRODUCT_API_URL, HttpMethod.POST, storeId);

        assertNotNull(productResponse);
        assertEquals(HttpStatus.OK, productResponse.getStatusCode());

        ResourceCreated<Long> product = parseResponse(productResponse, ResourceCreated.class);
        assertNotNull(product);
        assertNotNull(product.getId());
    }

    protected void addAndUpdateStock(
            File stockRequest,
            Long[] storeIdAndProductId) throws Exception {

        ResponseEntity<String> stockResponse = getJSONResponse(stockRequest, STOCK_API_URL, HttpMethod.POST, storeIdAndProductId);

        TestCase.assertNotNull(stockResponse);
        TestCase.assertEquals(HttpStatus.OK, stockResponse.getStatusCode());
    }
}
