package com.endava.petstore.repository;

import com.endava.petstore.model.User;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.endava.petstore.mock.UserMock.getMockedUser1;
import static com.endava.petstore.mock.UserMock.getMockedUser2;
import static com.endava.petstore.mock.UserMock.getMockedUsers;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserRepositoryImplTest {

    @Mock
    private UserRepositoryImpl userRepository;
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
        List<User> result = userRepository.getAllUsers();
        assertThat(result).isEqualTo(users);
    }

    @Test
    void getUserById_shouldReturnUserWithGivenId() {
        given(userRepository.getUserById(1L)).willReturn(user1);
        User result = userRepository.getUserById(1L);
        assertThat(result).isEqualTo(user1);
    }

    @Test
    void saveUser_shouldAddUserToList() {
        given(userRepository.saveUser(any(User.class))).willReturn(user1);
        User result = userRepository.saveUser(user1);
        verify(userRepository).saveUser(userCaptor.capture());
        assertThat(result).isEqualTo(userCaptor.getValue());
    }

    @Test
    void updateUser_shouldModifyCurrentUser() {
        given(userRepository.updateUser(any(User.class))).willReturn(user2);
        User result = userRepository.updateUser(user1);
        assertThat(result).isEqualTo(user2);
    }

    @Test
    void deleteUser_shouldRemoveUserFromList() {
        userRepository.deleteUserById(user1.getId());
    }

    @Test
    void saveUsersArray_shouldAddMultipleUsersToList() {
        User[] users = new User[]{user1, user2};
        given(userRepository.saveUsersArray(users)).willReturn(List.of(user1, user2));
        List<User> result = userRepository.saveUsersArray(users);
        assertThat(result).isEqualTo(List.of(user1, user2));
    }

    @Test
    void saveUsersList_shouldAddMultipleUsersToList() {
        List<User> users = List.of(user1, user2);
        given(userRepository.saveUsersList(users)).willReturn(List.of(user1, user2));
        List<User> result = userRepository.saveUsersList(users);
        assertThat(result).isEqualTo(List.of(user1, user2));
    }

    @Test
    void getUserByUsername_shouldReturnUserWithGivenUsername() {
        String username = "test_username1";
        given(userRepository.getUserByUsername(username)).willReturn(user1);
        User result = userRepository.getUserByUsername(username);
        assertThat(result).isEqualTo(user1);
    }

    @Test
    void updateUserByUsername_shouldModifyCurrentUser() {
        String username = "test_username1";
        User resultUser = user2;
        resultUser.setId(1L);
        given(userRepository.updateUserByUsername(user2, username)).willReturn(resultUser);
        User result = userRepository.updateUserByUsername(user2, username);
        assertThat(result).isEqualTo(resultUser);
    }

    @Test
    void deleteUserByUsername_shouldRemoveUserFromList() {
        userRepository.deleteUserByUsername(user1.getUsername());
    }

    @Test
    void login_shouldAuthenticateTheUserIntoTheSystem() {
        String username = "test_username1", password = "#Test_password1", message = "Logged in successfully";
        given(userRepository.login(username, password)).willReturn(message);
        String result = userRepository.login(username, password);
        assertThat(result).isEqualTo(message);
    }

    @Test
    void logout_shouldTerminateCurrentLoggedInUserSession() {
        String message = "Logged out successfully";
        given(userRepository.logout()).willReturn(message);
        String result = userRepository.logout();
        assertThat(result).isEqualTo(message);
    }
}
