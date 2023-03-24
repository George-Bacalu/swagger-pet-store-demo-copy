package com.endava.petstore.mock;

import com.endava.petstore.model.Order;
import com.endava.petstore.model.OrderStatus;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StoreMock {

    public static List<Order> getMockedOrders() {
        return List.of(getMockedOrder1(), getMockedOrder2(), getMockedOrder3());
    }

    public static Order getMockedOrder1() {
        return Order.builder()
              .id(1L)
              .petId(1L)
              .quantity(2)
              .shipDate(LocalDateTime.of(2000, 1, 1, 0, 0, 0))
              .orderStatus(OrderStatus.PLACED)
              .complete(true)
              .build();
    }

    public static Order getMockedOrder2() {
        return Order.builder()
              .id(2L)
              .petId(2L)
              .quantity(3)
              .shipDate(LocalDateTime.of(2000, 1, 2, 0, 0, 0))
              .orderStatus(OrderStatus.APPROVED)
              .complete(false)
              .build();
    }

    public static Order getMockedOrder3() {
        return Order.builder()
              .id(3L)
              .petId(3L)
              .quantity(2)
              .shipDate(LocalDateTime.of(2000, 1, 3, 0, 0, 0))
              .orderStatus(OrderStatus.DELIVERED)
              .complete(true)
              .build();
    }

    public static Order getMockedInvalidOrder() {
        return Order.builder()
              .id(4L)
              .petId(999L)
              .quantity(2)
              .shipDate(LocalDateTime.of(2000, 1, 4, 0, 0, 0))
              .orderStatus(OrderStatus.DELIVERED)
              .complete(true)
              .build();
    }
}
