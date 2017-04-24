package com.tenx.ms.retail.rest;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.tenx.ms.commons.config.Profiles;
import com.tenx.ms.commons.rest.RestConstants;
import com.tenx.ms.retail.product.repository.ProductRepository;
import com.tenx.ms.retail.store.repository.StoreRepository;
import com.tenx.ms.retail.store.rest.dto.Store;
import com.tenx.ms.test.rest.BaseIntegrationTest;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@SuppressWarnings("PMD")
@ActiveProfiles(value = {Profiles.TEST_NOAUTH, "profile-controller"})
public  abstract  class BaseControllerTest extends BaseIntegrationTest {

    protected static final String STORE_API_URI =  "%s" + RestConstants.VERSION_ONE + "/stores/";

    @Autowired
    protected StoreRepository storeRepository;

    @Autowired
    protected ProductRepository productRepository;

    @Autowired
    DataSource dataSource;

    @Before
    public void setUp() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 0;");
        jdbcTemplate.execute("TRUNCATE TABLE store");
        jdbcTemplate.execute("TRUNCATE table product");
        jdbcTemplate.execute("TRUNCATE table stock");
        jdbcTemplate.execute("TRUNCATE table orders");
        jdbcTemplate.execute("TRUNCATE table order_item");
    }

    protected ResponseEntity<String> getJSONResponse(File json, String uri, HttpMethod httpMethod, Object... l)
            throws Exception {

        String url = buildUrl(uri, l);
        String payload = null;
        if (json != null && json.exists()) {
            payload = getPayload(json);
        }
        return getResponse(url, payload, httpMethod, new ParameterizedTypeReference<String>(){});
       // TypeReference<List<String>> test= sendRequest(url, payload, httpMethod, httpStatus, new TypeReference<List<String>>(){});
    }


    private String buildUrl(String uri, Object... pathVars) throws URISyntaxException {
        String basePath = getBasePath();
        String reqUri = String.format(uri, basePath);
        if (pathVars != null && pathVars.length>0) {
            for(Object pathVar : pathVars){
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

    protected  Store createStore(File storeCreateRequest) throws Exception {

        ResponseEntity<String> storeResponse =  getJSONResponse(storeCreateRequest, STORE_API_URI, HttpMethod.POST,  null);
        assertNotNull(storeResponse);
        assertEquals(HttpStatus.OK, storeResponse.getStatusCode());
        Store store = parseResponse(storeResponse, Store.class);
        assertNotNull(store);
        assertNotNull(store.getId());
        return store;

    }

}
