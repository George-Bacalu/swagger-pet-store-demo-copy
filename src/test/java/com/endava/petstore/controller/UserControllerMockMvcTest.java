package com.endava.petstore.controller;

import com.endava.petstore.exception.ResourceNotFoundException;
import com.endava.petstore.model.User;
import com.endava.petstore.service.UserService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static com.endava.petstore.constants.Constants.USERNAME_NOT_FOUND;
import static com.endava.petstore.constants.Constants.USER_NOT_FOUND;
import static com.endava.petstore.mock.UserMock.getMockedUser1;
import static com.endava.petstore.mock.UserMock.getMockedUser2;
import static com.endava.petstore.mock.UserMock.getMockedUser3;
import static com.endava.petstore.mock.UserMock.getMockedUsers;
import static org.assertj.core.api.Assertions.assertThat;
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

@WebMvcTest(UserController.class)
class UserControllerMockMvcTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private UserService userService;

    private User user1;
    private User user2;
    private User user3;
    private List<User> users;

    @BeforeEach
    void setUp() {
        user1 = getMockedUser1();
        user2 = getMockedUser2();
        user3 = getMockedUser3();
        users = getMockedUsers();
    }
    
    @Test
    void getAllUsers_shouldReturnAllUsers() throws Exception {
        given(userService.getAllUsers()).willReturn(users);
        MvcResult result = mockMvc.perform(get("/user").accept(MediaType.APPLICATION_JSON_VALUE))
              .andExpect(status().isOk())
              .andExpect(jsonPath("$[0].id").value(user1.getId()))
              .andExpect(jsonPath("$[0].username").value(user1.getUsername()))
              .andExpect(jsonPath("$[0].firstName").value(user1.getFirstName()))
              .andExpect(jsonPath("$[0].lastName").value(user1.getLastName()))
              .andExpect(jsonPath("$[0].email").value(user1.getEmail()))
              .andExpect(jsonPath("$[0].password").value(user1.getPassword()))
              .andExpect(jsonPath("$[0].phone").value(user1.getPhone()))
              .andExpect(jsonPath("$[0].userStatus").value(user1.getUserStatus()))
              .andExpect(jsonPath("$[1].id").value(user2.getId()))
              .andExpect(jsonPath("$[1].username").value(user2.getUsername()))
              .andExpect(jsonPath("$[1].firstName").value(user2.getFirstName()))
              .andExpect(jsonPath("$[1].lastName").value(user2.getLastName()))
              .andExpect(jsonPath("$[1].email").value(user2.getEmail()))
              .andExpect(jsonPath("$[1].password").value(user2.getPassword()))
              .andExpect(jsonPath("$[1].phone").value(user2.getPhone()))
              .andExpect(jsonPath("$[1].userStatus").value(user2.getUserStatus()))
              .andExpect(jsonPath("$[2].id").value(user3.getId()))
              .andExpect(jsonPath("$[2].username").value(user3.getUsername()))
              .andExpect(jsonPath("$[2].firstName").value(user3.getFirstName()))
              .andExpect(jsonPath("$[2].lastName").value(user3.getLastName()))
              .andExpect(jsonPath("$[2].email").value(user3.getEmail()))
              .andExpect(jsonPath("$[2].password").value(user3.getPassword()))
              .andExpect(jsonPath("$[2].phone").value(user3.getPhone()))
              .andExpect(jsonPath("$[2].userStatus").value(user3.getUserStatus()))
              .andReturn();
        verify(userService).getAllUsers();
        List<User> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
        assertThat(response).isEqualTo(users);
    }
    
    @Test
    void getUserById_withValidId_shouldReturnUserWithGivenId() throws Exception {
        given(userService.getUserById(1L)).willReturn(user1);
        MvcResult result = mockMvc.perform(get("/user/{userId}", user1.getId()).accept(APPLICATION_JSON_VALUE))
              .andExpect(status().isOk())
              .andExpect(jsonPath("$.id").value(user1.getId()))
              .andExpect(jsonPath("$.username").value(user1.getUsername()))
              .andExpect(jsonPath("$.firstName").value(user1.getFirstName()))
              .andExpect(jsonPath("$.lastName").value(user1.getLastName()))
              .andExpect(jsonPath("$.email").value(user1.getEmail()))
              .andExpect(jsonPath("$.password").value(user1.getPassword()))
              .andExpect(jsonPath("$.phone").value(user1.getPhone()))
              .andExpect(jsonPath("$.userStatus").value(user1.getUserStatus()))
              .andReturn();
        verify(userService).getUserById(1L);
        User response = objectMapper.readValue(result.getResponse().getContentAsString(), User.class);
        assertThat(response).isEqualTo(user1);
    }

    @Test
    void getUserById_withInvalidId_shouldThrowException() throws Exception {
        Long userId = 999L;
        given(userService.getUserById(userId)).willThrow(new ResourceNotFoundException(String.format(USER_NOT_FOUND, userId)));
        mockMvc.perform(get("/user/{userId}", userId).accept(APPLICATION_JSON_VALUE))
              .andExpect(status().isNotFound())
              .andExpect(jsonPath("$.message").value(String.format(USER_NOT_FOUND, userId)))
              .andReturn();
        verify(userService, never()).getUserById(userId);
    }

    @Test
    void saveUser_shouldAddUserToList() throws Exception {
        given(userService.saveUser(any(User.class))).willReturn(user1);
        MvcResult result = mockMvc.perform(post("/user").accept(APPLICATION_JSON_VALUE)
                    .contentType(APPLICATION_JSON_VALUE)
                    .content(objectMapper.writeValueAsString(user1)))
              .andExpect(status().isCreated())
              .andExpect(jsonPath("$.id").value(user1.getId()))
              .andExpect(jsonPath("$.username").value(user1.getUsername()))
              .andExpect(jsonPath("$.firstName").value(user1.getFirstName()))
              .andExpect(jsonPath("$.lastName").value(user1.getLastName()))
              .andExpect(jsonPath("$.email").value(user1.getEmail()))
              .andExpect(jsonPath("$.password").value(user1.getPassword()))
              .andExpect(jsonPath("$.phone").value(user1.getPhone()))
              .andExpect(jsonPath("$.userStatus").value(user1.getUserStatus()))
              .andReturn();
        verify(userService).saveUser(user1);
        User response = objectMapper.readValue(result.getResponse().getContentAsString(), User.class);
        assertThat(response).isEqualTo(user1);
    }

    @Test
    void updateUser_shouldModifyCurrentUser() throws Exception {
        given(userService.updateUser(any(User.class))).willReturn(user2);
        MvcResult result = mockMvc.perform(put("/user").accept(APPLICATION_JSON_VALUE)
                    .contentType(APPLICATION_JSON_VALUE)
                    .content(objectMapper.writeValueAsString(user2)))
              .andExpect(status().isOk())
              .andExpect(jsonPath("$.id").value(user2.getId()))
              .andExpect(jsonPath("$.username").value(user2.getUsername()))
              .andExpect(jsonPath("$.firstName").value(user2.getFirstName()))
              .andExpect(jsonPath("$.lastName").value(user2.getLastName()))
              .andExpect(jsonPath("$.email").value(user2.getEmail()))
              .andExpect(jsonPath("$.password").value(user2.getPassword()))
              .andExpect(jsonPath("$.phone").value(user2.getPhone()))
              .andExpect(jsonPath("$.userStatus").value(user2.getUserStatus()))
              .andReturn();
        verify(userService).updateUser(user2);
        User response = objectMapper.readValue(result.getResponse().getContentAsString(), User.class);
        assertThat(response).isEqualTo(user2);
    }

    @Test
    void deleteUser_shouldRemoveUserFromList() throws Exception {
        mockMvc.perform(delete("/user/{userId}", user1.getId()).accept(APPLICATION_JSON_VALUE))
              .andExpect(status().isNoContent())
              .andReturn();
        verify(userService).deleteUserById(user1.getId());
    }

    @Test
    void saveUsersArray_shouldAddMultipleUsersToList() throws Exception {
        User[] users = new User[]{user1, user2};
        given(userService.saveUsersArray(users)).willReturn(List.of(user1, user2));
        MvcResult result = mockMvc.perform(post("/user/createWithArray").accept(APPLICATION_JSON_VALUE)
                    .contentType(APPLICATION_JSON_VALUE)
                    .content(objectMapper.writeValueAsString(List.of(user1, user2))))
              .andExpect(status().isCreated())
              .andReturn();
        verify(userService).saveUsersArray(users);
        List<User> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
        assertThat(response).isEqualTo(List.of(user1, user2));
    }

    @Test
    void saveUsersList_shouldAddMultipleUsersToList() throws Exception {
        List<User> users = List.of(user1, user2);
        given(userService.saveUsersList(users)).willReturn(users);
        MvcResult result = mockMvc.perform(post("/user/createWithList").accept(APPLICATION_JSON_VALUE)
                    .contentType(APPLICATION_JSON_VALUE)
                    .content(objectMapper.writeValueAsString(List.of(user1, user2))))
              .andExpect(status().isCreated())
              .andReturn();
        verify(userService).saveUsersList(users);
        List<User> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
        assertThat(response).isEqualTo(users);
    }

    @Test
    void getUserByUsername_withValidUsername_shouldReturnUserWithGivenUsername() throws Exception {
        String username = "test_username1";
        given(userService.getUserByUsername(username)).willReturn(user1);
        MvcResult result =  mockMvc.perform(get("/user/username/{username}", username).accept(APPLICATION_JSON_VALUE))
              .andExpect(status().isOk())
              .andExpect(jsonPath("$.id").value(user1.getId()))
              .andExpect(jsonPath("$.username").value(user1.getUsername()))
              .andExpect(jsonPath("$.firstName").value(user1.getFirstName()))
              .andExpect(jsonPath("$.lastName").value(user1.getLastName()))
              .andExpect(jsonPath("$.email").value(user1.getEmail()))
              .andExpect(jsonPath("$.password").value(user1.getPassword()))
              .andExpect(jsonPath("$.phone").value(user1.getPhone()))
              .andExpect(jsonPath("$.userStatus").value(user1.getUserStatus()))
              .andReturn();
        verify(userService).getUserByUsername(username);
        User response = objectMapper.readValue(result.getResponse().getContentAsString(), User.class);
        assertThat(response).isEqualTo(user1);
    }

    @Test
    void getUserByUsername_withInvalidUsername_shouldThrowException() throws Exception {
        String invalidUsername = "invalid_username";
        given(userService.getUserByUsername(invalidUsername)).willThrow(new ResourceNotFoundException(String.format(USERNAME_NOT_FOUND, invalidUsername)));
        mockMvc.perform(get("/user/username/{username}", invalidUsername).accept(APPLICATION_JSON_VALUE))
              .andExpect(status().isNotFound())
              .andExpect(jsonPath("$.message").value(String.format(USERNAME_NOT_FOUND, invalidUsername)))
              .andReturn();
        verify(userService, never()).getUserByUsername(invalidUsername);
    }

    @Test
    void updateUserByUsername_shouldModifyCurrentUser() throws Exception {
        String username = "test_username1";
        User resultUser = user2;
        resultUser.setId(1L);
        given(userService.updateUserByUsername(user2, username)).willReturn(resultUser);
        MvcResult result = mockMvc.perform(put("/user/username/{username}", username).accept(APPLICATION_JSON_VALUE)
                    .contentType(APPLICATION_JSON_VALUE)
                    .content(objectMapper.writeValueAsString(user2)))
              .andExpect(status().isOk())
              .andExpect(jsonPath("$.id").value(user2.getId()))
              .andExpect(jsonPath("$.username").value(user2.getUsername()))
              .andExpect(jsonPath("$.firstName").value(user2.getFirstName()))
              .andExpect(jsonPath("$.lastName").value(user2.getLastName()))
              .andExpect(jsonPath("$.email").value(user2.getEmail()))
              .andExpect(jsonPath("$.password").value(user2.getPassword()))
              .andExpect(jsonPath("$.phone").value(user2.getPhone()))
              .andExpect(jsonPath("$.userStatus").value(user2.getUserStatus()))
              .andReturn();
        verify(userService).updateUserByUsername(user2, username);
        User response = objectMapper.readValue(result.getResponse().getContentAsString(), User.class);
        assertThat(response).isEqualTo(user2);
    }

    @Test
    void deleteUserByUsername_shouldRemoveUserFromList() throws Exception {
        mockMvc.perform(delete("/user/username/{username}", user1.getUsername()).accept(APPLICATION_JSON_VALUE))
              .andExpect(status().isNoContent())
              .andReturn();
        verify(userService).deleteUserByUsername(user1.getUsername());
    }

    @Test
    void login_shouldAuthenticateTheUserIntoTheSystem() throws Exception {
        String username = "test_username1", password = "%23Test_password1", message = "Logged in successfully";
        given(userService.login(username, password)).willReturn(message);
        MvcResult result = mockMvc.perform(get("/user/login?username=" + username + "&password=" + password))
              .andExpect(status().isOk())
              .andReturn();
        verify(userService).login(username, password);
        Map<String, String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
        assertThat(response).containsEntry("message", message);
    }

    @Test
    void logout_shouldTerminateCurrentLoggedInUserSession() throws Exception {
        String message = "Logged out successfully";
        given(userService.logout()).willReturn(message);
        MvcResult result = mockMvc.perform(get("/user/logout"))
              .andExpect(status().isOk())
              .andReturn();
        verify(userService).logout();
        Map<String, String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
        assertThat(response).containsEntry("message", message);
    }
}
