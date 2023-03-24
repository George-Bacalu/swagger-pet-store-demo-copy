package com.endava.petstore.controller;

import com.endava.petstore.model.Order;
import com.endava.petstore.model.OrderStatus;
import com.endava.petstore.model.Pet;
import com.endava.petstore.service.StoreService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static com.endava.petstore.mock.PetMock.getMockedPet1;
import static com.endava.petstore.mock.StoreMock.getMockedOrder1;
import static com.endava.petstore.mock.StoreMock.getMockedOrder2;
import static com.endava.petstore.mock.StoreMock.getMockedOrders;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class StoreControllerTest {

    @InjectMocks
    private StoreController storeController;
    @Mock
    private StoreService storeService;

    private Order order1;
    private Order order2;
    private List<Order> orders;
    private Pet pet1;

    @BeforeEach
    void setUp() {
        order1 = getMockedOrder1();
        order2 = getMockedOrder2();
        orders = getMockedOrders();
        pet1 = getMockedPet1();
    }

    @Test
    void getAllOrders_shouldReturnAllOrders() {
        given(storeService.getAllOrders()).willReturn(orders);
        ResponseEntity<List<Order>> response = storeController.getAllOrders();
        verify(storeService).getAllOrders();
        assertNotNull(response);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(orders);
    }

    @Test
    void getOrderById_shouldReturnOrderWithGivenId() {
        given(storeService.getOrderById(1L)).willReturn(order1);
        ResponseEntity<Order> response = storeController.getOrderById(1L);
        verify(storeService).getOrderById(1L);
        assertNotNull(response);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(order1);
    }

    @Test
    void saveOrder_shouldAddOrderToList() {
        given(storeService.saveOrder(any(Order.class))).willReturn(order1);
        ResponseEntity<Order> response = storeController.saveOrder(order1);
        verify(storeService).saveOrder(order1);
        assertNotNull(response);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(order1);
    }

    @Test
    void updateOrder_shouldModifyCurrentOrder() {
        given(storeService.updateOrder(any(Order.class))).willReturn(order2);
        ResponseEntity<Order> response = storeController.updateOrder(order1);
        verify(storeService).updateOrder(order1);
        assertNotNull(response);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(order2);
    }

    @Test
    void deleteOrder_shouldRemoveOrderFromList() {
        ResponseEntity<Void> response = storeController.deleteOrderById(order1.getId());
        verify(storeService).deleteOrderById(order1.getId());
        assertNotNull(response);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    void getPetsByOrderStatus_shouldReturnPetsWithGivenOrderStatus() {
        OrderStatus orderStatus = OrderStatus.PLACED;
        given(storeService.getPetsByOrderStatus(orderStatus)).willReturn(List.of(pet1));
        ResponseEntity<List<Pet>> response = storeController.getPetsByOrderStatus(orderStatus);
        assertNotNull(response);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(List.of(pet1));
    }
}
