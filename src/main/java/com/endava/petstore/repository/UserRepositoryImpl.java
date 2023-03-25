package com.endava.petstore.repository;

import com.endava.petstore.exception.ResourceNotFoundException;
import com.endava.petstore.model.User;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import static com.endava.petstore.constants.Constants.USER_NOT_FOUND;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final Map<Long, User> users = new HashMap<>();

    @PostConstruct
    public void initializeUsers() {
        User user1 = User.builder()
              .id(1L)
              .username("test_username1")
              .firstName("test_firstname1")
              .lastName("test_lastname1")
              .email("test_email1@email.com")
              .password("#Test_password1")
              .phone("+40700 000 001")
              .userStatus(1)
              .build();
        users.put(user1.getId(), user1);
        User user2 = User.builder()
              .id(2L)
              .username("test_username2")
              .firstName("test_firstname2")
              .lastName("test_lastname2")
              .email("test_email2@email.com")
              .password("#Test_password2")
              .phone("+40700 000 002")
              .userStatus(2)
              .build();
        users.put(user2.getId(), user2);
        User user3 = User.builder()
              .id(3L)
              .username("test_username3")
              .firstName("test_firstname3")
              .lastName("test_lastname3")
              .email("test_email3@email.com")
              .password("#Test_password3")
              .phone("+40700 000 003")
              .userStatus(3)
              .build();
        users.put(user3.getId(), user3);
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUserById(Long userId) {
        return users.values().stream()
              .filter(user -> Objects.equals(user.getId(), userId)).findFirst()
              .orElseThrow(() -> new ResourceNotFoundException(String.format(USER_NOT_FOUND, userId)));
    }

    @Override
    public User saveUser(User user) {
        return users.compute(user.getId(), (key, value) -> user);
    }

    @Override
    public User updateUser(User user) {
        User updatedUser = getUserById(user.getId());
        updatedUser.setUsername(user.getUsername());
        updatedUser.setFirstName(user.getFirstName());
        updatedUser.setLastName(user.getLastName());
        updatedUser.setEmail(user.getEmail());
        updatedUser.setPassword(user.getPassword());
        updatedUser.setPhone(user.getPhone());
        updatedUser.setUserStatus(user.getUserStatus());
        return updatedUser;
    }

    @Override
    public void deleteUserById(Long userId) {
        users.remove(getUserById(userId).getId());
    }
}
