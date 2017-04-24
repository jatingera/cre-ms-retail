package com.tenx.ms.retail.order.rest;

import com.tenx.ms.commons.rest.RestConstants;
import com.tenx.ms.retail.constant.OrderStatusConstant;
import com.tenx.ms.retail.order.domain.OrderEntity;
import com.tenx.ms.retail.order.rest.dto.Order;
import com.tenx.ms.retail.order.rest.dto.OrderItem;
import com.tenx.ms.retail.rest.BaseControllerTest;
import org.springframework.beans.factory.annotation.Value;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@FixMethodOrder( MethodSorters.NAME_ASCENDING)
public class OrderControllerTest extends BaseControllerTest {

    private static final String REQUEST_URI = "%s" + RestConstants.VERSION_ONE + "/orders/";

    @Value("classpath:order/create_order_with_item_in_stock")
    private File orderWithItemInStock;

    @Value("classpath:order/create_order_with_item_out_of_stock")
    private File orderWithItemOutOfStock;

    @Test
    public void test1CreateOrderWhenItemsInSock() throws Exception {


        ResponseEntity orderResponse =  getJSONResponse(orderWithItemInStock, REQUEST_URI, HttpMethod.POST,  2L);
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
                orderItem -> {assertNotNull(orderItem.getProductId());
                assertEquals(orderItem.getIsItemBackordered(), false); });  }


    @Test
    public void test2CreateOrderWhenSomeItemsNotInSock() throws Exception {

        ResponseEntity<String> orderResponse =  getJSONResponse(orderWithItemOutOfStock, REQUEST_URI, HttpMethod.POST,  2L);

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
                orderItem -> { assertNotNull(orderItem.getProductId());
                    if (orderItem.getProductId().equals(1) && orderItem.getOrderItemId().equals(1)) {
                           assertEquals(orderItem.getIsItemBackordered(), false);
                    }
                    else if (orderItem.getProductId().equals(1) && orderItem.getOrderItemId().equals(2)) {
                        assertEquals(orderItem.getIsItemBackordered(), true);
                    }else if (orderItem.getProductId().equals(2)) {
                        assertEquals(orderItem.getIsItemBackordered(), false);
                    }
                }

        );






    }


}