package com.endava.petstore.repository;

import com.endava.petstore.exception.ResourceNotFoundException;
import com.endava.petstore.model.Order;
import com.endava.petstore.model.OrderStatus;
import com.endava.petstore.model.Pet;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.endava.petstore.constants.Constants.ORDER_NOT_FOUND;
import static com.endava.petstore.constants.Constants.PET_NOT_FOUND;
import static com.endava.petstore.mock.PetMock.getMockedPet1;
import static com.endava.petstore.mock.StoreMock.getMockedInvalidOrder;
import static com.endava.petstore.mock.StoreMock.getMockedOrder1;
import static com.endava.petstore.mock.StoreMock.getMockedOrders;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class StoreRepositoryIntegrationTest {

    @Autowired
    private StoreRepositoryImpl storeRepository;

    private Order order1;
    private Order invalidOrder;
    private List<Order> orders;
    private Pet pet1;

    @BeforeEach
    void setUp() {
        order1 = getMockedOrder1();
        invalidOrder = getMockedInvalidOrder();
        orders = getMockedOrders();
        pet1 = getMockedPet1();
    }

    @Test
    void getAllOrders_shouldReturnAllOrders() {
        List<Order> result = storeRepository.getAllOrders();
        assertThat(result).isEqualTo(orders);
    }

    @Test
    void getOrderById_withValidId_shouldReturnOrderWithGivenId() {
        Order result = storeRepository.getOrderById(1L);
        assertThat(result).isEqualTo(order1);
    }

    @Test
    void getOrderById_withInvalidId_shouldThrowException() {
        assertThatThrownBy(() -> storeRepository.getOrderById(999L))
              .isInstanceOf(ResourceNotFoundException.class)
              .hasMessage(String.format(ORDER_NOT_FOUND, 999L));
    }

    @Test
    void saveOrder_withValidPetId_shouldAddOrderToList() {
        Order result = storeRepository.saveOrder(order1);
        assertThat(result).isEqualTo(order1);
    }

    @Test
    void saveOrder_withInvalidPetId_shouldThrowException() {
        assertThatThrownBy(() -> storeRepository.saveOrder(invalidOrder))
              .isInstanceOf(ResourceNotFoundException.class)
              .hasMessage(String.format(PET_NOT_FOUND, invalidOrder.getPetId()));
    }

    @Test
    void updateOrder_withValidPetId_shouldModifyCurrentOrder() {
        Order result = storeRepository.updateOrder(order1);
        assertThat(result).isEqualTo(order1);
    }

    @Test
    void updateOrder_withInvalidPetId_shouldThrowException() {
        assertThatThrownBy(() -> storeRepository.updateOrder(invalidOrder))
              .isInstanceOf(ResourceNotFoundException.class)
              .hasMessage(String.format(PET_NOT_FOUND, invalidOrder.getPetId()));
    }

    @Test
    void deleteOrder_shouldRemoveOrderFromList() {
        storeRepository.deleteOrderById(order1.getId());
        assertThatThrownBy(() -> storeRepository.getOrderById(999L))
              .isInstanceOf(ResourceNotFoundException.class)
              .hasMessage(ORDER_NOT_FOUND, 999L);
    }

    @Test
    void getPetsByOrderStatus_shouldReturnPetsWithGivenOrderStatus() {
        OrderStatus orderStatus = OrderStatus.PLACED;
        List<Pet> result = storeRepository.getPetsByOrderStatus(orderStatus);
        assertThat(result).isEqualTo(List.of(pet1));
    }
}
