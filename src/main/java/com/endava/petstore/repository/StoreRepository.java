package com.endava.petstore.repository;

import com.endava.petstore.model.Order;
import java.util.List;

public interface StoreRepository {

    List<Order> getAllOrders();

    Order getOrderById(Long orderId);

    Order saveOrder(Order order);

    Order updateOrder(Order order);

    void deleteOrderById(Long orderId);
}
