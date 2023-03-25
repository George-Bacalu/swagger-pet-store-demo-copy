package com.endava.petstore.controller;

import com.endava.petstore.model.Order;
import com.endava.petstore.model.OrderStatus;
import com.endava.petstore.model.Pet;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static com.endava.petstore.constants.Constants.ORDER_NOT_FOUND;
import static com.endava.petstore.constants.Constants.PET_NOT_FOUND;
import static com.endava.petstore.mock.PetMock.getMockedPet1;
import static com.endava.petstore.mock.StoreMock.getMockedInvalidOrder;
import static com.endava.petstore.mock.StoreMock.getMockedOrder1;
import static com.endava.petstore.mock.StoreMock.getMockedOrder2;
import static com.endava.petstore.mock.StoreMock.getMockedOrders;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StoreControllerIntegrationTest {

    @Autowired
    private TestRestTemplate template;
    @Autowired
    private ObjectMapper objectMapper;

    private Order order1;
    private Order order2;
    private Order invalidOrder;
    private List<Order> orders;
    private Pet pet1;

    @BeforeEach
    void setUp() {
        order1 = getMockedOrder1();
        order2 = getMockedOrder2();
        invalidOrder = getMockedInvalidOrder();
        orders = getMockedOrders();
        pet1 = getMockedPet1();
    }

    @Test
    void getAllOrders_shouldReturnAllOrders() throws Exception  {
        ResponseEntity<String> response = template.getForEntity("/store/order", String.class);
        assertNotNull(response);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getHeaders().getContentType()).isEqualTo(APPLICATION_JSON);
        List<Order> result = objectMapper.readValue(response.getBody(), new TypeReference<>() {});
        assertThat(result).isEqualTo(orders);
    }

    @Test
    void getOrderById_withValidId_shouldReturnOrderWithGivenId() {
        ResponseEntity<Order> response = template.getForEntity("/store/order/1", Order.class);
        assertNotNull(response);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getHeaders().getContentType()).isEqualTo(APPLICATION_JSON);
        assertThat(response.getBody()).isEqualTo(order1);
    }

    @Test
    void getOrderById_withInvalidId_shouldThrowException() {
        ResponseEntity<String> response = template.getForEntity("/store/order/999", String.class);
        assertNotNull(response);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getHeaders().getContentType()).isEqualTo(APPLICATION_JSON);
        assertThat(response.getBody()).isEqualTo("Resource not found: " + String.format(ORDER_NOT_FOUND, 999L));
    }

    @Test
    void saveOrder_withValidPetId_shouldAddOrderToList() {
        ResponseEntity<Order> response = template.postForEntity("/store/order", order1, Order.class);
        assertNotNull(response);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getHeaders().getContentType()).isEqualTo(APPLICATION_JSON);
        assertThat(response.getBody()).isEqualTo(order1);
    }

    @Test
    void saveOrder_withInvalidPetId_shouldAddOrderToList() {
        ResponseEntity<String> response = template.postForEntity("/store/order", invalidOrder, String.class);
        assertNotNull(response);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getHeaders().getContentType()).isEqualTo(APPLICATION_JSON);
        assertThat(response.getBody()).isEqualTo("Resource not found: " + String.format(PET_NOT_FOUND, 999L));
    }

    @Test
    void updateOrder_shouldModifyCurrentOrder() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(APPLICATION_JSON);
        ResponseEntity<Order> response = template.exchange("/store/order", HttpMethod.PUT, new HttpEntity<>(order2, headers), Order.class);
        assertNotNull(response);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getHeaders().getContentType()).isEqualTo(APPLICATION_JSON);
        assertThat(response.getBody()).isEqualTo(order2);
    }

    @Test
    void updateOrder_withInvalidPetId_shouldAddOrderToList() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(APPLICATION_JSON);
        ResponseEntity<String> response = template.exchange("/store/order", HttpMethod.PUT, new HttpEntity<>(invalidOrder, headers), String.class);
        assertNotNull(response);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getHeaders().getContentType()).isEqualTo(APPLICATION_JSON);
        assertThat(response.getBody()).isEqualTo("Resource not found: " + String.format(PET_NOT_FOUND, 999L));
    }

    @Test
    void deleteOrder_shouldRemoveOrderFromList() {
        // send an empty body
        ResponseEntity<Void> response = template.exchange("/store/order/1", HttpMethod.DELETE, new HttpEntity<>(null), Void.class);
        assertNotNull(response);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        // make sure the order is no longer available
        ResponseEntity<Order> getResponse = template.exchange("/store/order/1", HttpMethod.GET, null, new ParameterizedTypeReference<>() {});
        assertNotNull(getResponse);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        // make sure the order is no longer in the list
        ResponseEntity<List<Order>> getAllResponse = template.exchange("/store/order", HttpMethod.GET, null, new ParameterizedTypeReference<>() {});
        assertNotNull(getAllResponse);
        assertThat(Objects.requireNonNull(getAllResponse.getBody()).stream().anyMatch(order -> order.getId() == 1L)).isFalse();
    }

    @Test
    void getPetsByOrderStatus_shouldReturnPetsWithGivenOrderStatus() throws Exception {
        OrderStatus orderStatus = OrderStatus.PLACED;
        ResponseEntity<String> response = template.getForEntity("/store/order/inventory?orderStatus=" + orderStatus.name(), String.class);
        assertNotNull(response);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getHeaders().getContentType()).isEqualTo(APPLICATION_JSON);
        List<Pet> result = objectMapper.readValue(response.getBody(), new TypeReference<>() {});
        assertThat(result).isEqualTo(List.of(pet1));
    }
}
