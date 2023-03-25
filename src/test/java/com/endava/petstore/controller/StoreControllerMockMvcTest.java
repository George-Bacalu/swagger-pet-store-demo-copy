package com.endava.petstore.controller;

import com.endava.petstore.exception.ResourceNotFoundException;
import com.endava.petstore.model.Order;
import com.endava.petstore.model.OrderStatus;
import com.endava.petstore.model.Pet;
import com.endava.petstore.service.StoreService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static com.endava.petstore.constants.Constants.ORDER_NOT_FOUND;
import static com.endava.petstore.constants.Constants.PET_NOT_FOUND;
import static com.endava.petstore.mock.PetMock.getMockedPet1;
import static com.endava.petstore.mock.StoreMock.getMockedOrder1;
import static com.endava.petstore.mock.StoreMock.getMockedOrder2;
import static com.endava.petstore.mock.StoreMock.getMockedOrder3;
import static com.endava.petstore.mock.StoreMock.getMockedOrders;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StoreController.class)
class StoreControllerMockMvcTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private StoreService storeService;

    private Order order1;
    private Order order2;
    private Order order3;
    private List<Order> orders;
    private Pet pet1;

    @BeforeEach
    void setUp() {
        order1 = getMockedOrder1();
        order2 = getMockedOrder2();
        order3 = getMockedOrder3();
        orders = getMockedOrders();
        pet1 = getMockedPet1();
    }

    @Test
    void getAllOrders_shouldReturnAllOrders() throws Exception {
        given(storeService.getAllOrders()).willReturn(orders);
        MvcResult result = mockMvc.perform(get("/store/order").accept(APPLICATION_JSON_VALUE))
              .andExpect(status().isOk())
              .andExpect(jsonPath("$[0].id").value(order1.getId()))
              .andExpect(jsonPath("$[0].petId").value(order1.getPetId()))
              .andExpect(jsonPath("$[0].quantity").value(order1.getQuantity()))
              .andExpect(jsonPath("$[0].shipDate").value(equalTo(order1.getShipDate().toString() + ":00")))
              .andExpect(jsonPath("$[0].orderStatus").value(order1.getOrderStatus().name()))
              .andExpect(jsonPath("$[0].complete").value(order1.getComplete()))
              .andExpect(jsonPath("$[1].id").value(order2.getId()))
              .andExpect(jsonPath("$[1].petId").value(order2.getPetId()))
              .andExpect(jsonPath("$[1].quantity").value(order2.getQuantity()))
              .andExpect(jsonPath("$[1].shipDate").value(equalTo(order2.getShipDate().toString() + ":00")))
              .andExpect(jsonPath("$[1].orderStatus").value(order2.getOrderStatus().name()))
              .andExpect(jsonPath("$[1].complete").value(order2.getComplete()))
              .andExpect(jsonPath("$[2].id").value(order3.getId()))
              .andExpect(jsonPath("$[2].petId").value(order3.getPetId()))
              .andExpect(jsonPath("$[2].quantity").value(order3.getQuantity()))
              .andExpect(jsonPath("$[2].shipDate").value(equalTo(order3.getShipDate().toString() + ":00")))
              .andExpect(jsonPath("$[2].orderStatus").value(order3.getOrderStatus().name()))
              .andExpect(jsonPath("$[2].complete").value(order3.getComplete()))
              .andReturn();
        verify(storeService).getAllOrders();
        List<Order> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
        });
        assertThat(response).isEqualTo(orders);
    }

    @Test
    void getOrderById_withValidId_shouldReturnOrderWithGivenId() throws Exception {
        given(storeService.getOrderById(1L)).willReturn(order1);
        MvcResult result = mockMvc.perform(get("/store/order/{orderId}", order1.getId()).accept(APPLICATION_JSON_VALUE))
              .andExpect(status().isOk())
              .andExpect(jsonPath("$.id").value(order1.getId()))
              .andExpect(jsonPath("$.petId").value(order1.getPetId()))
              .andExpect(jsonPath("$.quantity").value(order1.getQuantity()))
              .andExpect(jsonPath("$.shipDate").value(order1.getShipDate().toString() + ":00"))
              .andExpect(jsonPath("$.orderStatus").value(order1.getOrderStatus().name()))
              .andExpect(jsonPath("$.complete").value(order1.getComplete()))
              .andReturn();
        verify(storeService).getOrderById(1L);
        Order response = objectMapper.readValue(result.getResponse().getContentAsString(), Order.class);
        assertThat(response).isEqualTo(order1);
    }

    @Test
    void getOrderById_withInvalidId_shouldThrowException() throws Exception {
        Long orderId = 999L;
        given(storeService.getOrderById(orderId)).willThrow(new ResourceNotFoundException(String.format(ORDER_NOT_FOUND, orderId)));
        mockMvc.perform(get("/store/order/{orderId}", orderId).accept(APPLICATION_JSON_VALUE))
              .andExpect(status().isNotFound())
              .andExpect(jsonPath("$.message").value(String.format(ORDER_NOT_FOUND, orderId)))
              .andReturn();
        verify(storeService, never()).getOrderById(orderId);
    }

    @Test
    void saveOrder_withValidPetId_shouldAddOrderToList() throws Exception {
        given(storeService.saveOrder(any(Order.class))).willReturn(order1);
        MvcResult result = mockMvc.perform(post("/store/order").accept(APPLICATION_JSON_VALUE)
                    .contentType(APPLICATION_JSON_VALUE)
                    .content(objectMapper.writeValueAsString(order1)))
              .andExpect(status().isCreated())
              .andExpect(jsonPath("$.id").value(order1.getId()))
              .andExpect(jsonPath("$.petId").value(order1.getPetId()))
              .andExpect(jsonPath("$.quantity").value(order1.getQuantity()))
              .andExpect(jsonPath("$.shipDate").value(order1.getShipDate().toString() + ":00"))
              .andExpect(jsonPath("$.orderStatus").value(order1.getOrderStatus().name()))
              .andExpect(jsonPath("$.complete").value(order1.getComplete()))
              .andReturn();
        verify(storeService).saveOrder(order1);
        Order response = objectMapper.readValue(result.getResponse().getContentAsString(), Order.class);
        assertThat(response).isEqualTo(order1);
    }

    @Test
    void saveOrder_withInvalidPetId_shouldThrowException() throws Exception {
        Long petId = 999L;
        given(storeService.saveOrder(any(Order.class))).willThrow(new ResourceNotFoundException(String.format(PET_NOT_FOUND, petId)));
        mockMvc.perform(post("/store/order").accept(APPLICATION_JSON_VALUE)
              .contentType(APPLICATION_JSON_VALUE)
              .content(objectMapper.writeValueAsString(order1)))
              .andExpect(status().isNotFound())
              .andExpect(jsonPath("$.message").value(String.format(PET_NOT_FOUND, petId)))
              .andReturn();
        verify(storeService, never()).saveOrder(any(Order.class));
    }

    @Test
    void updateOrder_withValidPetId_shouldModifyCurrentOrder() throws Exception {
        given(storeService.updateOrder(any(Order.class))).willReturn(order2);
        MvcResult result = mockMvc.perform(put("/store/order").accept(APPLICATION_JSON_VALUE)
              .contentType(APPLICATION_JSON_VALUE)
              .content(objectMapper.writeValueAsString(order2)))
              .andExpect(status().isOk())
              .andExpect(jsonPath("$.id").value(order2.getId()))
              .andExpect(jsonPath("$.petId").value(order2.getPetId()))
              .andExpect(jsonPath("$.quantity").value(order2.getQuantity()))
              .andExpect(jsonPath("$.shipDate").value(order2.getShipDate().toString() + ":00"))
              .andExpect(jsonPath("$.orderStatus").value(order2.getOrderStatus().name()))
              .andExpect(jsonPath("$.complete").value(order2.getComplete()))
              .andReturn();
        verify(storeService).updateOrder(order2);
        Order response = objectMapper.readValue(result.getResponse().getContentAsString(), Order.class);
        assertThat(response).isEqualTo(order2);
    }

    @Test
    void updateOrder_withInvalidPetId_shouldThrowException() throws Exception {
        Long petId = 999L;
        given(storeService.updateOrder(any(Order.class))).willThrow(new ResourceNotFoundException(String.format(PET_NOT_FOUND, petId)));
        mockMvc.perform(put("/store/order").accept(APPLICATION_JSON_VALUE)
                    .contentType(APPLICATION_JSON_VALUE)
                    .content(objectMapper.writeValueAsString(order1)))
              .andExpect(status().isNotFound())
              .andExpect(jsonPath("$.message").value(String.format(PET_NOT_FOUND, petId)))
              .andReturn();
        verify(storeService, never()).updateOrder(any(Order.class));
    }

    @Test
    void deleteOrder_shouldRemoveOrderFromList() throws Exception {
        mockMvc.perform(delete("/store/order/{orderId}", order1.getId()).accept(APPLICATION_JSON_VALUE))
              .andExpect(status().isNoContent())
              .andReturn();
        verify(storeService).deleteOrderById(order1.getId());
    }

    @Test
    void getPetsByOrderStatus_shouldReturnPetsWithGivenOrderStatus() throws Exception {
        OrderStatus orderStatus = OrderStatus.PLACED;
        given(storeService.getPetsByOrderStatus(orderStatus)).willReturn(List.of(pet1));
        MvcResult result = mockMvc.perform(get("/store/order/inventory?orderStatus=" + orderStatus.name()))
              .andExpect(status().isOk())
              .andReturn();
        verify(storeService).getPetsByOrderStatus(orderStatus);
        List<Pet> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
        assertThat(response).isEqualTo(List.of(pet1));
    }
}
