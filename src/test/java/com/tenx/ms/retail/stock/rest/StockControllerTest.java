package com.tenx.ms.retail.stock.rest;

import com.tenx.ms.retail.rest.BaseControllerTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.io.File;

public class StockControllerTest extends BaseControllerTest {

    @Value( "classpath:product/create_product_valid_request" )
    private File validProductCreation_1;

    @Value( "classpath:store/create_store_valid_request" )
    private File createStoreValidRequest;

    @Value( "classpath:stock/add_new_stock" )
    private File addStock;

    @Value( "classpath:stock/update_stock" )
    private File updateStock;

    @Autowired
    DataSource dataSource;

    @Before
    public void setUp() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 0;");
        jdbcTemplate.execute("TRUNCATE TABLE store");
        jdbcTemplate.execute("TRUNCATE table product");
        jdbcTemplate.execute("TRUNCATE table stock");
    }

    @Test
    public void testAddStock() throws Exception {

        createStore(createStoreValidRequest);

        createAndValidateProduct(validProductCreation_1, 1L);

        Long[] storeIdAndProductId = {1L, 1L};
        addAndUpdateStock(addStock, storeIdAndProductId);
    }

    @Test
    public void testUpdateStock() throws Exception {

        createStore(createStoreValidRequest);

        createAndValidateProduct(validProductCreation_1, 1L);

        Long[] storeIdAndProductId = {1L, 1L};

        addAndUpdateStock(addStock, storeIdAndProductId);

        addAndUpdateStock(updateStock, storeIdAndProductId);
    }
}