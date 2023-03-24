package com.endava.petstore.service;

import com.endava.petstore.model.Order;
import com.endava.petstore.model.OrderStatus;
import com.endava.petstore.model.Pet;
import com.endava.petstore.repository.StoreRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;

    @Override
    public List<Order> getAllOrders() {
        return storeRepository.getAllOrders();
    }

    @Override
    public Order getOrderById(Long orderId) {
        return storeRepository.getOrderById(orderId);
    }

    @Override
    public Order saveOrder(Order order) {
        return storeRepository.saveOrder(order);
    }

    @Override
    public Order updateOrder(Order order) {
        return storeRepository.updateOrder(order);
    }

    @Override
    public void deleteOrderById(Long orderId) {
        storeRepository.deleteOrderById(orderId);
    }

    @Override
    public List<Pet> getPetsByOrderStatus(OrderStatus orderStatus) {
        return storeRepository.getPetsByOrderStatus(orderStatus);
    }
}
