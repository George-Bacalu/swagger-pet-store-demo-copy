package com.endava.petstore.service;

import com.endava.petstore.model.User;
import com.endava.petstore.repository.UserRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static com.endava.petstore.mock.UserMock.getMockedUser1;
import static com.endava.petstore.mock.UserMock.getMockedUser2;
import static com.endava.petstore.mock.UserMock.getMockedUsers;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class UserServiceIntegrationTest {

    @Autowired
    private UserServiceImpl userService;
    @MockBean
    private UserRepository userRepository;
    @Captor
    private ArgumentCaptor<User> userCaptor;

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
        given(userRepository.getAllUsers()).willReturn(users);
        List<User> result = userService.getAllUsers();
        assertThat(result).isEqualTo(users);
    }

    @Test
    void getUserById_shouldReturnUserWithGivenId() {
        given(userRepository.getUserById(1L)).willReturn(user1);
        User result = userService.getUserById(1L);
        assertThat(result).isEqualTo(user1);
    }

    @Test
    void saveUser_shouldAddUserToList() {
        given(userRepository.saveUser(any(User.class))).willReturn(user1);
        User result = userService.saveUser(user1);
        verify(userRepository).saveUser(userCaptor.capture());
        assertThat(result).isEqualTo(userCaptor.getValue());
    }

    @Test
    void updateUser_shouldModifyCurrentUser() {
        given(userRepository.updateUser(any(User.class))).willReturn(user2);
        User result = userService.updateUser(user1);
        assertThat(result).isEqualTo(user2);
    }

    @Test
    void deleteUser_shouldRemoveUserFromList() {
        userService.deleteUserById(user1.getId());
        verify(userRepository).deleteUserById(user1.getId());
    }

    @Test
    void saveUsersArray_shouldAddMultipleUsersToList() {
        User[] users = new User[]{user1, user2};
        given(userRepository.saveUsersArray(users)).willReturn(List.of(user1, user2));
        List<User> result = userService.saveUsersArray(users);
        assertThat(result).isEqualTo(List.of(user1, user2));
    }

    @Test
    void saveUsersList_shouldAddMultipleUsersToList() {
        List<User> users = List.of(user1, user2);
        given(userRepository.saveUsersList(users)).willReturn(List.of(user1, user2));
        List<User> result = userService.saveUsersList(users);
        assertThat(result).isEqualTo(List.of(user1, user2));
    }

    @Test
    void getUserByUsername_shouldReturnUserWithGivenUsername() {
        String username = "test_username1";
        given(userRepository.getUserByUsername(username)).willReturn(user1);
        User result = userService.getUserByUsername(username);
        assertThat(result).isEqualTo(user1);
    }

    @Test
    void updateUserByUsername_shouldModifyCurrentUser() {
        String username = "test_username1";
        User resultUser = user2;
        resultUser.setId(1L);
        given(userRepository.updateUserByUsername(user2, username)).willReturn(resultUser);
        User result = userService.updateUserByUsername(user2, username);
        assertThat(result).isEqualTo(resultUser);
    }

    @Test
    void deleteUserByUsername_shouldRemoveUserFromList() {
        userService.deleteUserByUsername(user1.getUsername());
        verify(userRepository).deleteUserByUsername(user1.getUsername());
    }

    @Test
    void login_shouldAuthenticateTheUserIntoTheSystem() {
        String username = "test_username1", password = "#Test_password1", message = "Logged in successfully";
        given(userRepository.login(username, password)).willReturn(message);
        String result = userService.login(username, password);
        assertThat(result).isEqualTo(message);
    }

    @Test
    void logout_shouldTerminateCurrentLoggedInUserSession() {
        String message = "Logged out successfully";
        given(userRepository.logout()).willReturn(message);
        String result = userService.logout();
        assertThat(result).isEqualTo(message);
    }
}
