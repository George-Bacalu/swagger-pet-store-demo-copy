package com.endava.petstore.repository;

import com.endava.petstore.model.Order;
import com.endava.petstore.exception.ResourceNotFoundException;
import com.endava.petstore.model.OrderStatus;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.endava.petstore.constants.Constants.ORDER_NOT_FOUND;

@Repository
@RequiredArgsConstructor
public class StoreRepositoryImpl implements StoreRepository {

    private final PetRepository petRepository;

    private final Map<Long, Order> orders = new HashMap<>();

    @PostConstruct
    public void initializeOrders() {
        Order order1 = Order.builder()
              .id(1L)
              .petId(1L)
              .quantity(2)
              .shipDate(LocalDateTime.of(2000, 1, 1, 0, 0, 0))
              .orderStatus(OrderStatus.PLACED)
              .complete(true)
              .build();
        orders.put(order1.getId(), order1);
        Order order2 = Order.builder()
              .id(2L)
              .petId(2L)
              .quantity(3)
              .shipDate(LocalDateTime.of(2000, 1, 2, 0, 0, 0))
              .orderStatus(OrderStatus.APPROVED)
              .complete(false)
              .build();
        orders.put(order2.getId(), order2);
        Order order3 = Order.builder()
              .id(3L)
              .petId(3L)
              .quantity(2)
              .shipDate(LocalDateTime.of(2000, 1, 3, 0, 0, 0))
              .orderStatus(OrderStatus.DELIVERED)
              .complete(true)
              .build();
        orders.put(order3.getId(), order3);
    }

    @Override
    public List<Order> getAllOrders() {
        return new ArrayList<>(orders.values());
    }

    @Override
    public Order getOrderById(Long orderId) {
        return orders.values().stream()
              .filter(order -> Objects.equals(order.getId(), orderId)).findFirst()
              .orElseThrow(() -> new ResourceNotFoundException(String.format(ORDER_NOT_FOUND, orderId)));
    }

    @Override
    public Order saveOrder(Order order) {
        petRepository.getPetById(order.getPetId());
        return orders.compute(order.getId(), (key, value) -> order);
    }

    @Override
    public Order updateOrder(Order order) {
        petRepository.getPetById(order.getPetId());
        Order updatedOrder = getOrderById(order.getId());
        updatedOrder.setPetId(order.getPetId());
        updatedOrder.setQuantity(order.getQuantity());
        updatedOrder.setShipDate(order.getShipDate());
        updatedOrder.setOrderStatus(order.getOrderStatus());
        updatedOrder.setComplete(order.getComplete());
        return updatedOrder;
    }

    @Override
    public void deleteOrderById(Long orderId) {
        orders.remove(getOrderById(orderId).getId());
    }
}
