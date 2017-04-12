package com.tenx.ms.retail.rest;

//import com.tenx.ms.commons.tests.AbstractIntegrationTest;

//@RunWith( SpringJUnit4ClassRunner.class)
public  abstract class BaseControllerTest  {

    /*protected static final String STORE_API_URI = "%s/retail/v1/stores/";

    protected final RestTemplate template = new TestRestTemplate();

    @Autowired
    protected StoreRepository storeRepository;

    @After
    public void deleteAll() {
        storeRepository.deleteAll();
    }

    protected ResponseEntity<String> sendRequest(File json, String uri, HttpMethod httpMethod, Object... l)
            throws IOException, URISyntaxException {

        String url = buildUrl(uri, l);
        String payload = null;
        if (json != null && json.exists()) {
            payload = getPayload(json);
        }
        return getJSONResponse(template, url, payload, httpMethod);

    }

    private String buildUrl(String uri, Object... pathVars) throws URISyntaxException {
        String basePath = basePath();
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
    } */
}
