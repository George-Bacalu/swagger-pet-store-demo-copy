package com.endava.petstore.service;

import com.endava.petstore.model.Order;
import java.util.List;

public interface StoreService {

    List<Order> getAllOrders();

    Order getOrderById(Long orderId);

    Order saveOrder(Order order);

    Order updateOrder(Order order);

    void deleteOrderById(Long orderId);
}
