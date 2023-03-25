package com.endava.petstore.controller;

import com.endava.petstore.model.User;
import com.endava.petstore.service.UserService;
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

@Api(value = "User Rest Controller", description = "Operations about user", tags = "user")
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/user", produces = {APPLICATION_JSON_VALUE, APPLICATION_XML_VALUE})
public class UserController {

    private final UserService userService;

    @ApiOperation(value = "Get all users", response = List.class)
    @ApiResponses(value = {
          @ApiResponse(code = 200, message = "Successful operation"),
          @ApiResponse(code = 404, message = "No users found"),
          @ApiResponse(code = 500, message = "Internal server error")})
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @ApiOperation(value = "Find user by ID", notes = "Returns a single user", response = User.class)
    @ApiResponses(value = {
          @ApiResponse(code = 200, message = "Successful operation"),
          @ApiResponse(code = 400, message = "Invalid ID supplied"),
          @ApiResponse(code = 404, message = "User not found")})
    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@ApiParam(value = "ID of user to return", example = "1", required = true) @PathVariable Long userId) {
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @ApiOperation(value = "Add a new user", response = User.class)
    @ApiResponses(value = {
          @ApiResponse(code = 201, message = "Successful operation"),
          @ApiResponse(code = 405, message = "Invalid input")})
    @PostMapping(consumes = {APPLICATION_JSON_VALUE, APPLICATION_XML_VALUE})
    public ResponseEntity<User> saveUser(@ApiParam(value = "User object that needs to be added", required = true) @RequestBody @Valid User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.saveUser(user));
    }

    @ApiOperation(value = "Update an existing user", response = User.class)
    @ApiResponses(value = {
          @ApiResponse(code = 200, message = "Successful operation"),
          @ApiResponse(code = 400, message = "Invalid ID supplied"),
          @ApiResponse(code = 404, message = "User not found"),
          @ApiResponse(code = 405, message = "Validation exception")})
    @PutMapping(consumes = {APPLICATION_JSON_VALUE, APPLICATION_XML_VALUE})
    public ResponseEntity<User> updateUser(@ApiParam(value = "User object that needs to be added", required = true) @RequestBody @Valid User user) {
        return ResponseEntity.ok(userService.updateUser(user));
    }

    @ApiOperation(value = "Deletes a user")
    @ApiResponses(value = {
          @ApiResponse(code = 204, message = "Successful operation"),
          @ApiResponse(code = 400, message = "Invalid ID supplied"),
          @ApiResponse(code = 404, message = "User not found")})
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUserById(@ApiParam(value = "User ID to delete", example = "1", required = true) @PathVariable Long userId) {
        userService.deleteUserById(userId);
        return ResponseEntity.noContent().build();
    }
}
