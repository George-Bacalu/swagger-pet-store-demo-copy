package com.endava.petstore.repository;

import com.endava.petstore.exception.InvalidResourceException;
import com.endava.petstore.exception.ResourceNotFoundException;
import com.endava.petstore.model.User;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.endava.petstore.constants.Constants.INVALID_USER_CREDENTIALS;
import static com.endava.petstore.constants.Constants.USERNAME_NOT_FOUND;
import static com.endava.petstore.constants.Constants.USER_NOT_FOUND;
import static com.endava.petstore.mock.UserMock.getMockedUser1;
import static com.endava.petstore.mock.UserMock.getMockedUser2;
import static com.endava.petstore.mock.UserMock.getMockedUser3;
import static com.endava.petstore.mock.UserMock.getMockedUsers;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class UserRepositoryIntegrationTest {

    @Autowired
    private UserRepositoryImpl userRepository;

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
    void getAllUsers_shouldReturnAllUsers() {
        System.out.println(users);
        List<User> result = userRepository.getAllUsers();
        assertThat(result).isEqualTo(users);
    }

    @Test
    void getUserById_withValidId_shouldReturnUserWithGivenId() {
        User result = userRepository.getUserById(1L);
        assertThat(result).isEqualTo(user1);
    }

    @Test
    void getUserById_withInvalidId_shouldThrowException() {
        assertThatThrownBy(() -> userRepository.getUserById(999L))
              .isInstanceOf(ResourceNotFoundException.class)
              .hasMessage(String.format(USER_NOT_FOUND, 999L));
    }

    @Test
    void saveUser_shouldAddUserToList() {
        User result = userRepository.saveUser(user1);
        assertThat(result).isEqualTo(user1);
    }

    @Test
    void updateUser_shouldModifyCurrentUser() {
        User result = userRepository.updateUser(user3);
        assertThat(result).isEqualTo(user3);
    }

    @Test
    void deleteUser_shouldRemoveUserFromList() {
        userRepository.deleteUserById(user2.getId());
        assertThatThrownBy(() -> userRepository.getUserById(999L))
              .isInstanceOf(ResourceNotFoundException.class)
              .hasMessage(String.format(USER_NOT_FOUND, 999L));
    }

    @Test
    void saveUsersArray_shouldAddMultipleUsersToList() {
        User[] users = new User[]{user1, user2};
        List<User> result = userRepository.saveUsersArray(users);
        assertThat(result).isEqualTo(List.of(user1, user2));
    }

    @Test
    void saveUsersList_shouldAddMultipleUsersToList() {
        List<User> users = List.of(user1, user2);
        List<User> result = userRepository.saveUsersList(users);
        assertThat(result).isEqualTo(users);
    }

    @Test
    void getUserByUsername_withValidUserName_shouldReturnUserWithGivenUsername() {
        String username = "test_username1";
        User result = userRepository.getUserByUsername(username);
        assertThat(result).isEqualTo(user1);
    }

    @Test
    void getUserByUsername_withInvalidUserName_shouldThrowException() {
        String username = "test_username999";
        assertThatThrownBy(() -> userRepository.getUserByUsername(username))
              .isInstanceOf(ResourceNotFoundException.class)
              .hasMessage(String.format(USERNAME_NOT_FOUND, username));
    }

    @Test
    void updateUserByUsername_shouldModifyCurrentUser() {
        String username = "test_username1";
        User resultUser = user2;
        resultUser.setId(1L);
        User result = userRepository.updateUserByUsername(user2, username);
        assertThat(result).isEqualTo(resultUser);
    }

    @Test
    void deleteUserByUsername_shouldRemoveUserFromList() {
        String invalidUsername = "invalid username";
        userRepository.deleteUserByUsername(user2.getUsername());
        assertThatThrownBy(() -> userRepository.deleteUserByUsername(invalidUsername))
              .isInstanceOf(ResourceNotFoundException.class)
              .hasMessage(String.format(USERNAME_NOT_FOUND, invalidUsername));
    }

    @Test
    void login_withValidCredentials_shouldAuthenticateTheUserIntoTheSystem() {
        String username = "test_username1", password = "#Test_password1", message = "Logged in successfully";
        String result = userRepository.login(username, password);
        assertThat(result).isEqualTo(message);
    }

    @Test
    void login_withInvalidCredentials_shouldThrowException() {
        String username = "test_username1", password = "#Test_password";
        assertThatThrownBy(() -> userRepository.login(username, password))
              .isInstanceOf(InvalidResourceException.class)
              .hasMessage(String.format(INVALID_USER_CREDENTIALS, username, password));
    }

    @Test
    void logout_shouldTerminateCurrentLoggedInUserSession() {
        String message = "Logged out successfully";
        String result = userRepository.logout();
        assertThat(result).isEqualTo(message);
    }
}
