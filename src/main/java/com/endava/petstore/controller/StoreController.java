package com.endava.petstore.controller;

import com.endava.petstore.model.Order;
import com.endava.petstore.service.StoreService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;

@Api(value = "Store Rest Controller", description = "Access to Petstore orders", tags = "store")
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/store/order", produces = {APPLICATION_JSON_VALUE, APPLICATION_XML_VALUE})
public class StoreController {

    private final StoreService storeService;

    @ApiOperation(value = "Get all orders", response = List.class)
    @ApiResponses(value = {
          @ApiResponse(code = 200, message = "Successful operation"),
          @ApiResponse(code = 404, message = "No orders found"),
          @ApiResponse(code = 500, message = "Internal server error")})
    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(storeService.getAllOrders());
    }

    @ApiOperation(value = "Find purchase order by ID", notes = "For valid response try integer IDs with value >= 1 and <= 3. Other values will generate exceptions.", response = Order.class)
    @ApiResponses(value = {
          @ApiResponse(code = 200, message = "Successful operation"),
          @ApiResponse(code = 400, message = "Invalid ID supplied"),
          @ApiResponse(code = 404, message = "Order not found")})
    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderById(@ApiParam(value = "ID of order that needs to be fetched", example = "1", required = true) @PathVariable Long orderId) {
        return ResponseEntity.ok(storeService.getOrderById(orderId));
    }

    @ApiOperation(value = "Place an order for a pet", response = Order.class)
    @ApiResponses(value = {
          @ApiResponse(code = 200, message = "Successful operation"),
          @ApiResponse(code = 400, message = "Invalid order")})
    @PostMapping(consumes = {APPLICATION_JSON_VALUE, APPLICATION_XML_VALUE})
    public ResponseEntity<Order> saveOrder(@ApiParam(value = "Order placed for purchasing the pet", required = true) @RequestBody @Valid Order order) {
        return ResponseEntity.status(HttpStatus.CREATED).body(storeService.saveOrder(order));
    }

    @ApiOperation(value = "Update an existing order", response = Order.class)
    @ApiResponses(value = {
          @ApiResponse(code = 200, message = "Successful operation"),
          @ApiResponse(code = 400, message = "Invalid ID supplied"),
          @ApiResponse(code = 404, message = "Order not found"),
          @ApiResponse(code = 405, message = "Validation exception")})
    @PutMapping(consumes = {APPLICATION_JSON_VALUE, APPLICATION_XML_VALUE})
    public ResponseEntity<Order> updateOrder(@ApiParam(value = "Order placed for purchasing the pet", required = true) @RequestBody @Valid Order order) {
        return ResponseEntity.ok(storeService.updateOrder(order));
    }

    @ApiOperation(value = "Delete purchase order by ID", notes = "For valid response try integer IDs with positive integer values. Negative or non-integer values will generate API errors.")
    @ApiResponses(value = {
          @ApiResponse(code = 204, message = "Successful operation"),
          @ApiResponse(code = 400, message = "Invalid ID supplied"),
          @ApiResponse(code = 404, message = "Order not found")})
    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> deleteOrderById(@ApiParam(value = "ID of order that needs to be deleted", example = "1", required = true) @PathVariable Long orderId) {
        storeService.deleteOrderById(orderId);
        return ResponseEntity.noContent().build();
    }
}
