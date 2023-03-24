package com.endava.petstore.service;

import com.endava.petstore.model.Order;
import com.endava.petstore.model.OrderStatus;
import com.endava.petstore.model.Pet;
import com.endava.petstore.repository.StoreRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static com.endava.petstore.mock.PetMock.getMockedPet1;
import static com.endava.petstore.mock.StoreMock.getMockedOrder1;
import static com.endava.petstore.mock.StoreMock.getMockedOrder2;
import static com.endava.petstore.mock.StoreMock.getMockedOrders;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class StoreServiceIntegrationTest {

    @Autowired
    private StoreServiceImpl storeService;
    @MockBean
    private StoreRepository storeRepository;
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
    void getAllPets_shouldReturnAllPets() {
        given(storeRepository.getAllOrders()).willReturn(orders);
        List<Order> result = storeService.getAllOrders();
        assertThat(result).isEqualTo(orders);
    }

    @Test
    void getPetById_shouldReturnPetWithGivenId() {
        given(storeRepository.getOrderById(1L)).willReturn(order1);
        Order result = storeService.getOrderById(1L);
        assertThat(result).isEqualTo(order1);
    }

    @Test
    void savePet_shouldAddPetToList() {
        given(storeRepository.saveOrder(any(Order.class))).willReturn(order1);
        Order result = storeService.saveOrder(order1);
        verify(storeRepository).saveOrder(orderCaptor.capture());
        assertThat(result).isEqualTo(orderCaptor.getValue());
    }

    @Test
    void updatePet_shouldModifyCurrentPet() {
        given(storeRepository.updateOrder(any(Order.class))).willReturn(order2);
        Order result = storeService.updateOrder(order1);
        assertThat(result).isEqualTo(order2);
    }

    @Test
    void deletePet_shouldRemovePetFromList() {
        storeService.deleteOrderById(order1.getId());
        verify(storeRepository).deleteOrderById(order1.getId());
    }

    @Test
    void getPetsByOrderStatus_shouldReturnPetsWithGivenOrderStatus() {
        OrderStatus orderStatus = OrderStatus.PLACED;
        given(storeRepository.getPetsByOrderStatus(orderStatus)).willReturn(List.of(pet1));
        List<Pet> result = storeService.getPetsByOrderStatus(orderStatus);
        assertThat(result).isEqualTo(List.of(pet1));
    }
}
