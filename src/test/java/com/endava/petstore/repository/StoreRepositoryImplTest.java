package com.endava.petstore.repository;

import com.endava.petstore.model.Order;
import com.endava.petstore.model.OrderStatus;
import com.endava.petstore.model.Pet;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.endava.petstore.mock.PetMock.getMockedPet1;
import static com.endava.petstore.mock.StoreMock.getMockedOrder1;
import static com.endava.petstore.mock.StoreMock.getMockedOrder2;
import static com.endava.petstore.mock.StoreMock.getMockedOrders;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class StoreRepositoryImplTest {

    @Mock
    private StoreRepositoryImpl storeRepository;
    @Captor
    private ArgumentCaptor<Order> orderCaptor;

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
        given(storeRepository.getAllOrders()).willReturn(orders);
        List<Order> result = storeRepository.getAllOrders();
        assertThat(result).isEqualTo(orders);
    }

    @Test
    void getOrderById_shouldReturnOrderWithGivenId() {
        given(storeRepository.getOrderById(1L)).willReturn(order1);
        Order result = storeRepository.getOrderById(1L);
        assertThat(result).isEqualTo(order1);
    }

    @Test
    void saveOrder_shouldAddOrderToList() {
        given(storeRepository.saveOrder(any(Order.class))).willReturn(order1);
        Order result = storeRepository.saveOrder(order1);
        verify(storeRepository).saveOrder(orderCaptor.capture());
        assertThat(result).isEqualTo(orderCaptor.getValue());
    }

    @Test
    void updateOrder_shouldModifyCurrentOrder() {
        given(storeRepository.updateOrder(any(Order.class))).willReturn(order2);
        Order result = storeRepository.updateOrder(order1);
        assertThat(result).isEqualTo(order2);
    }

    @Test
    void deleteOrder_shouldRemoveOrderFromList() {
        storeRepository.deleteOrderById(order1.getId());
    }

    @Test
    void getPetsByOrderStatus_shouldReturnPetsWithGivenOrderStatus() {
        OrderStatus orderStatus = OrderStatus.PLACED;
        given(storeRepository.getPetsByOrderStatus(orderStatus)).willReturn(List.of(pet1));
        List<Pet> result = storeRepository.getPetsByOrderStatus(orderStatus);
        assertThat(result).isEqualTo(List.of(pet1));
    }
}
