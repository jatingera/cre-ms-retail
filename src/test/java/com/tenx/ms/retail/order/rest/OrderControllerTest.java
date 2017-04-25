package com.tenx.ms.retail.order.rest;

import com.tenx.ms.commons.rest.RestConstants;
import com.tenx.ms.retail.constant.OrderStatusConstant;
import com.tenx.ms.retail.order.domain.OrderEntity;
import com.tenx.ms.retail.order.rest.dto.Order;
import com.tenx.ms.retail.order.rest.dto.OrderItem;
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

public class OrderControllerTest extends BaseControllerTest {

    private static final String REQUEST_URI = "%s" + RestConstants.VERSION_ONE + "/orders/";

    @Value( "classpath:order/create_order_with_item_in_stock" )
    private File orderWithItemInStock;

    @Value( "classpath:order/create_order_with_item_out_of_stock" )
    private File orderWithItemOutOfStock;

    @Value( "classpath:order/create_order_when_stock_details_not_exist" )
    private File orderWithWhenStockDetailsNotExist;

    @Value( "classpath:product/create_product_valid_request" )
    private File validProductCreation_1;

    @Value( "classpath:product/create_product_valid_request_2" )
    private File validProductCreation_2;

    @Value( "classpath:store/create_store_valid_request" )
    private File createStoreValidRequest;

    @Value( "classpath:stock/add_new_stock" )
    private File addStock;

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

    @Test
    public void testCreateOrderWhenItemsInSock() throws Exception {

        createStore(createStoreValidRequest);

        createAndValidateProduct(validProductCreation_1, 1L);

        createAndValidateProduct(validProductCreation_2, 1L);

        Long[] storeIdAndProductId = {1L, 1L};
        addAndUpdateStock(addStock, storeIdAndProductId);

        Long[] storeIdAndProductId2 = {1L, 2L};
        addAndUpdateStock(addStock, storeIdAndProductId2);

        ResponseEntity orderResponse = getJSONResponse(orderWithItemInStock, REQUEST_URI, HttpMethod.POST, 1L);
        assertNotNull(orderResponse);
        assertEquals(HttpStatus.OK, orderResponse.getStatusCode());
        Order order = parseResponse(orderResponse, Order.class);

        OrderEntity orderEntity = parseResponse(orderResponse, OrderEntity.class);
        assertNotNull(order);
        assertNotNull(order.getEmail());
        assertNotNull(order.getFirstName());
        assertEquals(order.getOrderStatus(), OrderStatusConstant.ORDERED);
        List<OrderItem> orderItems = order.getProductItems();
        assertNotNull(orderItems);
        orderItems.forEach(
                orderItem -> {
                    assertNotNull(orderItem.getProductId());
                    assertEquals(orderItem.getIsItemBackordered(), false);
                });
    }

    @Test
    public void testCreateOrderWhenSomeItemsNotInSock() throws Exception {

        createStore(createStoreValidRequest);

        createAndValidateProduct(validProductCreation_1, 1L);

        createAndValidateProduct(validProductCreation_2, 1L);

        Long[] storeIdAndProductId = {1L, 1L};
        addAndUpdateStock(addStock, storeIdAndProductId);

        Long[] storeIdAndProductId2 = {1L, 2L};
        addAndUpdateStock(addStock, storeIdAndProductId2);

        ResponseEntity<String> orderResponse = getJSONResponse(orderWithItemOutOfStock, REQUEST_URI, HttpMethod.POST, 1L);

        assertNotNull(orderResponse);
        assertEquals(HttpStatus.OK, orderResponse.getStatusCode());

        Order order = parseResponse(orderResponse, Order.class);
        assertNotNull(order);
        assertEquals(order.getOrderStatus(), OrderStatusConstant.ORDERED);
        assertNotNull(order.getEmail());
        assertNotNull(order.getFirstName());

        List<OrderItem> orderItems = order.getProductItems();
        assertNotNull(orderItems);

        orderItems.forEach(
                orderItem -> {
                    assertNotNull(orderItem.getProductId());
                    if (orderItem.getProductId().equals(1) && orderItem.getOrderItemId().equals(1)) {
                        assertEquals(orderItem.getIsItemBackordered(), false);
                    } else if (orderItem.getProductId().equals(1) && orderItem.getOrderItemId().equals(2)) {
                        assertEquals(orderItem.getIsItemBackordered(), true);
                    } else if (orderItem.getProductId().equals(2)) {
                        assertEquals(orderItem.getIsItemBackordered(), false);
                    }
                }

        );
    }

    @Test
    public void testCreateOrderWhenStockNotExit() throws Exception {

        createStore(createStoreValidRequest);

        createAndValidateProduct(validProductCreation_1, 1L);


        ResponseEntity<String> orderResponse = getJSONResponse(orderWithWhenStockDetailsNotExist, REQUEST_URI, HttpMethod.POST, 1L);

        assertNotNull(orderResponse);
        assertEquals(HttpStatus.NOT_FOUND, orderResponse.getStatusCode());

    }
}