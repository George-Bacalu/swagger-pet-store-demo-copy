package com.endava.petstore.controller;

import com.endava.petstore.model.User;
import com.endava.petstore.service.UserService;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static com.endava.petstore.mock.UserMock.getMockedUser1;
import static com.endava.petstore.mock.UserMock.getMockedUser2;
import static com.endava.petstore.mock.UserMock.getMockedUsers;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    
    @InjectMocks
    private UserController userController;
    @Mock
    private UserService userService;

    private User user1;
    private User user2;
    private List<User> users;

    @BeforeEach
    void setUp() {
        user1 = getMockedUser1();
        user2 = getMockedUser2();
        users = getMockedUsers();
    }

    @Test
    void getAllUsers_shouldReturnAllUsers() {
        given(userService.getAllUsers()).willReturn(users);
        ResponseEntity<List<User>> response = userController.getAllUsers();
        verify(userService).getAllUsers();
        assertNotNull(response);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(users);
    }

    @Test
    void getUserById_shouldReturnUserWithGivenId() {
        given(userService.getUserById(1L)).willReturn(user1);
        ResponseEntity<User> response = userController.getUserById(1L);
        verify(userService).getUserById(1L);
        assertNotNull(response);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(user1);
    }

    @Test
    void saveUser_shouldAddUserToList() {
        given(userService.saveUser(any(User.class))).willReturn(user1);
        ResponseEntity<User> response = userController.saveUser(user1);
        verify(userService).saveUser(user1);
        assertNotNull(response);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(user1);
    }

    @Test
    void updateUser_shouldModifyCurrentUser() {
        given(userService.updateUser(any(User.class))).willReturn(user2);
        ResponseEntity<User> response = userController.updateUser(user1);
        verify(userService).updateUser(user1);
        assertNotNull(response);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(user2);
    }

    @Test
    void deleteUser_shouldRemoveUserFromList() {
        ResponseEntity<Void> response = userController.deleteUserById(user1.getId());
        verify(userService).deleteUserById(user1.getId());
        assertNotNull(response);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    void saveUsersArray_shouldAddMultipleUsersToList() {
        User[] users = new User[]{user1, user2};
        given(userService.saveUsersArray(users)).willReturn(List.of(user1, user2));
        ResponseEntity<List<User>> response = userController.saveUsersArray(users);
        verify(userService).saveUsersArray(users);
        assertNotNull(response);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(List.of(user1, user2));
    }

    @Test
    void saveUsersList_shouldAddMultipleUsersToList() {
        List<User> users = List.of(user1, user2);
        given(userService.saveUsersList(users)).willReturn(List.of(user1, user2));
        ResponseEntity<List<User>> response = userController.saveUsersList(users);
        verify(userService).saveUsersList(users);
        assertNotNull(response);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(List.of(user1, user2));
    }

    @Test
    void getUserByUsername_shouldReturnUserWithGivenUsername() {
        String username = "test_username1";
        given(userService.getUserByUsername(username)).willReturn(user1);
        ResponseEntity<User> response = userController.getUserByUsername(username);
        verify(userService).getUserByUsername(username);
        assertNotNull(response);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(user1);
    }

    @Test
    void updateUserByUsername_shouldModifyCurrentUser() {
        String username = "test_username1";
        User resultUser = user2;
        resultUser.setId(1L);
        given(userService.updateUserByUsername(user2, username)).willReturn(resultUser);
        ResponseEntity<User> response = userController.updateUserByUsername(user2, username);
        verify(userService).updateUserByUsername(user2, username);
        assertNotNull(response);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(resultUser);
    }

    @Test
    void deleteUserByUsername_shouldRemoveUserFromList() {
        ResponseEntity<Void> response = userController.deleteUserByUsername(user1.getUsername());
        verify(userService).deleteUserByUsername(user1.getUsername());
        assertNotNull(response);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    void login_shouldAuthenticateTheUserIntoTheSystem() {
        String username = "test_username1", password = "#Test_password1", message = "Logged in successfully";
        given(userService.login(username, password)).willReturn(message);
        ResponseEntity<Map<String, String>> response = userController.login(username, password);
        verify(userService).login(username, password);
        assertNotNull(response);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).containsEntry("message", message);
    }

    @Test
    void logout_shouldTerminateCurrentLoggedInUserSession() {
        String message = "Logged out successfully";
        given(userService.logout()).willReturn(message);
        ResponseEntity<Map<String, String>> response = userController.logout();
        verify(userService).logout();
        assertNotNull(response);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).containsEntry("message", message);
    }
}
