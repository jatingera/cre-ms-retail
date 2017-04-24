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

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class OrderService {

    private static  final EntityConverter<Order, OrderEntity> ORDER_CONVERTER = new EntityConverter<>(Order.class, OrderEntity.class);

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Transactional
    public Order createOrder(Long storeId, Order order) {

         // validate the store
        storeRepository.findOneById(storeId).orElseThrow(() -> new NoSuchElementException("invalid store"));

        Order orderResponse= orderProcess(storeId, order);

        OrderEntity orderEntity = addOrderDetails(orderResponse, storeId);

        if(orderEntity != null) {
            orderResponse.setOrderId(orderEntity.getOrderId());
        }

        return  ORDER_CONVERTER.toT1(orderEntity);

    }

    private Order orderProcess(Long storeId, Order order) {

             List<OrderItem> orderedItemList = order.getProductItems();

             List<StockEntity> stockEntitiesToUpdate = new ArrayList<StockEntity>();

            List<OrderItem> orderedSummary = new ArrayList<OrderItem>();



        for (OrderItem item: orderedItemList) {

            StockEntity stockEntity = stockRepository.findOneByProductIdAndStoreId(item.getProductId(), storeId)
                    .orElseThrow(null);

            int desiredQuantity = item.getQuantity();

            int availableQuantity = stockEntity.getCount();

            int diffQuantity = availableQuantity - desiredQuantity;

            OrderItem   orderedItemDetail;
//fixed - PMD error- Avoid instantiating new objects inside loops
       //     StockEntity stockEntityCopy = stockEntity;
//            StockEntity stockEntity1 = new StockEntity();
//            stockEntity1.setStoreId(stockEntity.getStoreId());
//            stockEntity1.setProductId(stockEntity.getProductId());
//            stockEntity1.setStoreEntity(stockEntity.getStoreEntity());
//            stockEntity1.setProductEntity(stockEntity.getProductEntity());
//            stockEntity1.setCount(stockEntity.getCount());

            if(diffQuantity < 0) {
                // If item out of stock then set available counts 0 and update order item with backordered quantity.
                stockEntity.setCount(0);
               //stockEntity1.setCount(0);

                // placed order for available items
                if(availableQuantity > 0) {
                    orderedItemDetail = updateOrderItemDetails(item.getProductId(), availableQuantity, false);
                    orderedSummary.add(orderedItemDetail);
                }

                //  backordered the not available items
                int backOrderItems = Math.abs(diffQuantity);
                orderedItemDetail = updateOrderItemDetails(item.getProductId(), backOrderItems, true);
                orderedSummary.add(orderedItemDetail);


            }
            else {
                stockEntity.setCount(diffQuantity);
                // update the item summary
                orderedItemDetail = updateOrderItemDetails(item.getProductId(), desiredQuantity, false);
                orderedSummary.add(orderedItemDetail);
            }

            stockEntitiesToUpdate.add(stockEntity);


        }

        // update the stock count after order is processed
        stockRepository.save(stockEntitiesToUpdate);
        order.setProductItems(orderedSummary);
        return order;


    }

    private OrderEntity addOrderDetails(Order order, Long storeId) {

        if(order.getStoreId()==null) {
            order.setStoreId(storeId);
        }
        order.setOrderStatus(OrderStatusConstant.ORDERED);

       if(order.getOrderDate() == null) {
//            ZonedDateTime utc = ZonedDateTime.now(ZoneOffset.UTC);
//            order.setOrderDate(utc.toLocalDateTime());

           Date date = new Date();
           order.setOrderDate(new Timestamp(date.getTime()));
       }

        OrderEntity orderEntitySave = ORDER_CONVERTER.toT2(order);
      //  orderEntitySave.getProductItems().stream().forEach(e -> e.setOrder(orderEntitySave));
          List<OrderItemEntity> orderItemEntityList = orderEntitySave.getProductItems();

        for (OrderItemEntity orderItemEntity: orderItemEntityList) {
            orderItemEntity.setOrder(orderEntitySave);

        }
        return  orderRepository.save(orderEntitySave);
    }

    private OrderItem updateOrderItemDetails(Long productId, Integer quantity, Boolean isItemBackordered) {

        OrderItem itemOrder = new OrderItem();
        itemOrder.setProductId(productId);
        itemOrder.setQuantity(quantity);
        itemOrder.setIsItemBackordered(isItemBackordered);
        return itemOrder;
    }

}
