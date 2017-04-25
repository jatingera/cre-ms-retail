package com.tenx.ms.retail.order.service;

import com.tenx.ms.commons.util.converter.EntityConverter;
import com.tenx.ms.retail.constant.OrderStatusConstant;
import com.tenx.ms.retail.order.domain.OrderEntity;
import com.tenx.ms.retail.order.domain.OrderItemEntity;
import com.tenx.ms.retail.order.repository.OrderRepository;
import com.tenx.ms.retail.order.rest.dto.Order;
import com.tenx.ms.retail.order.rest.dto.OrderItem;
import com.tenx.ms.retail.stock.domain.StockEntity;
import com.tenx.ms.retail.stock.repository.StockRepository;
import com.tenx.ms.retail.store.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class OrderService {

    private static final EntityConverter<Order, OrderEntity> ORDER_CONVERTER = new EntityConverter<>(Order.class, OrderEntity.class);

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private StoreRepository storeRepository;

    public Order createOrder(Long storeId, Order order) {

        // validate the store
        storeRepository.findOneById(storeId).orElseThrow(() -> new NoSuchElementException("invalid store"));

        Order orderResponse = orderProcess(storeId, order);

        OrderEntity orderEntity = addOrderDetails(orderResponse, storeId);

        if (orderEntity != null) {
            orderResponse.setOrderId(orderEntity.getOrderId());
        }

        return ORDER_CONVERTER.toT1(orderEntity);
    }

    private Order orderProcess(Long storeId, Order order) {

        List<OrderItem> orderedItemList = order.getProductItems();

        List<StockEntity> stockEntitiesToUpdate = new ArrayList<StockEntity>();

        List<OrderItem> orderedSummary = new ArrayList<OrderItem>();

        boolean stockFound = true;

        for (OrderItem item : orderedItemList) {

            StockEntity stockEntity = stockRepository.findOneByProductIdAndStoreId(item.getProductId(), storeId);

            if (stockEntity == null) {
                stockFound = false;
                break;
            }

            int desiredQuantity = item.getQuantity();

            int availableQuantity = stockEntity.getCount();

            int diffQuantity = availableQuantity - desiredQuantity;

            OrderItem orderedItemDetail;

            if (diffQuantity < 0) {
                // If item out of stock then set available counts 0 and update order item with backordered quantity.
                stockEntity.setCount(0);

                // placed order for available items
                if (availableQuantity > 0) {
                    orderedItemDetail = updateOrderItemDetails(item.getProductId(), availableQuantity, false);
                    orderedSummary.add(orderedItemDetail);
                }

                //  backordered the not available items
                int backOrderItems = Math.abs(diffQuantity);
                orderedItemDetail = updateOrderItemDetails(item.getProductId(), backOrderItems, true);
                orderedSummary.add(orderedItemDetail);
            } else {
                stockEntity.setCount(diffQuantity);
                // update the item summary
                orderedItemDetail = updateOrderItemDetails(item.getProductId(), desiredQuantity, false);
                orderedSummary.add(orderedItemDetail);
            }

            stockEntitiesToUpdate.add(stockEntity);
        }

        if (!stockFound) {
            throw new NoSuchElementException("Stock details not found");
        }

        // update the stock count after order is processed
        stockRepository.save(stockEntitiesToUpdate);
        order.setProductItems(orderedSummary);
        return order;
    }

    private OrderEntity addOrderDetails(Order order, Long storeId) {

        if (order.getStoreId() == null) {
            order.setStoreId(storeId);
        }
        order.setOrderStatus(OrderStatusConstant.ORDERED);

        if (order.getOrderDate() == null) {

            Date date = new Date();
            order.setOrderDate(new Timestamp(date.getTime()));
        }

        OrderEntity orderEntitySave = ORDER_CONVERTER.toT2(order);

        List<OrderItemEntity> orderItemEntityList = orderEntitySave.getProductItems();

        for (OrderItemEntity orderItemEntity : orderItemEntityList) {
            orderItemEntity.setOrder(orderEntitySave);
        }
        return orderRepository.save(orderEntitySave);
    }

    private OrderItem updateOrderItemDetails(
            Long productId,
            Integer quantity,
            Boolean isItemBackordered) {

        OrderItem itemOrder = new OrderItem();
        itemOrder.setProductId(productId);
        itemOrder.setQuantity(quantity);
        itemOrder.setIsItemBackordered(isItemBackordered);
        return itemOrder;
    }
}
